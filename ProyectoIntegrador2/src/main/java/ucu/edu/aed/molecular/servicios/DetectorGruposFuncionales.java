package ucu.edu.aed.molecular.servicios;

import ucu.edu.aed.molecular.grafo.algoritmos.DirectedGraphAlgorithms;
import ucu.edu.aed.molecular.grafo.algoritmos.UndirectedGraphAlgorithms;
import ucu.edu.aed.molecular.molecula.*;
import ucu.edu.aed.tda.grafo.model.edge.Edge;

import java.util.*;

/*
  Detecta grupos funcionales en una molécula usando la biblioteca de grafos.

  Cada grupo se describe como un PATRÓN LOCAL de vértices y aristas:
   - Alcohol:           C(saturado) - O - H        (O con 1 vecino C y 1 hidrógeno)
   - Ácido carboxílico: C(=O) - O - H              (un C con un O doble y un O-H simple)
   - Amina:             N con vecinos C, solo enlaces simples, y sin C=O al lado (eso sería amida)
   - Carbonilo:         C=O cuyo C solo tiene vecinos C/H (aldehído o cetona)
   - Anillo:            ciclo en el grafo (tieneCiclos de la biblioteca)

  Para buscar los patrones se recorre la molécula con BEA (recorridoEnAmplitud de la biblioteca)
  y en cada átomo se miran sus vecinos. Como cada átomo tiene a lo sumo 4 enlaces,
  revisar un patrón local cuesta O(1) por átomo.
  Costo total: O(V·(V + E)) por las búsquedas de vértice de AbstractGraph (lista, no hash).
*/
public class DetectorGruposFuncionales {

    public static final String ALCOHOL = "Alcohol";
    public static final String ACIDO = "Acido carboxilico";
    public static final String AMINA = "Amina";
    public static final String CARBONILO = "Carbonilo (aldehido/cetona)";
    public static final String ANILLO = "Anillo";

    private final DirectedGraphAlgorithms algDirigidos = new DirectedGraphAlgorithms();
    private final UndirectedGraphAlgorithms algNoDirigidos = new UndirectedGraphAlgorithms();

    /** Todas las coincidencias de todos los grupos. */
    public List<Coincidencia> detectar(Molecula m) {
        List<Coincidencia> res = new ArrayList<>();
        for (Atomo a : recorrerEnAmplitud(m)) {
            if (a.es(Elemento.O)) {
                Coincidencia c = alcohol(m, a);
                if (c != null) res.add(c);
            } else if (a.es(Elemento.C)) {
                Coincidencia c = acidoCarboxilico(m, a);
                if (c != null) res.add(c);
                Coincidencia k = carbonilo(m, a);
                if (k != null) res.add(k);
            } else if (a.es(Elemento.N)) {
                Coincidencia c = amina(m, a);
                if (c != null) res.add(c);
            }
        }
        Coincidencia anillo = anillos(m);
        if (anillo != null) res.add(anillo);
        return res;
    }

    /** Nombres de los grupos presentes, sin repetir. */
    public Set<String> gruposPresentes(Molecula m) {
        Set<String> nombres = new LinkedHashSet<>();
        for (Coincidencia c : detectar(m)) nombres.add(c.getGrupo().startsWith(ANILLO) ? ANILLO : c.getGrupo());
        return nombres;
    }

    public boolean contiene(Molecula m, String grupo) { return gruposPresentes(m).contains(grupo); }

    // ─────────────── Patrones ───────────────

    /** O con exactamente 1 vecino C (por enlace simple) y 1 H; el C no es carbonilo ni aromático. */
    private Coincidencia alcohol(Molecula m, Atomo o) {
        List<Atomo> carbonos = m.vecinosDe(o, Elemento.C);
        if (carbonos.size() != 1 || m.grado(o) - m.vecinosDe(o, Elemento.H).size() != 1) return null;
        if (m.hidrogenosTotales(o) != 1) return null;
        Atomo c = carbonos.get(0);
        if (tieneEnlaceMultiple(m, c)) return null; // C=O (ácido) o C=C (fenol/enol): no es alcohol
        return new Coincidencia(ALCOHOL, List.of(c, o));
    }

    /** C con un O por enlace doble y otro O por enlace simple que tiene 1 H. */
    private Coincidencia acidoCarboxilico(Molecula m, Atomo c) {
        Atomo oDoble = null, oHidroxilo = null;
        for (Atomo o : m.vecinosDe(c, Elemento.O)) {
            int orden = m.obtenerEnlace(c, o).getOrden();
            if (orden == 2) oDoble = o;
            else if (orden == 1 && m.hidrogenosTotales(o) == 1 && m.grado(o) - m.vecinosDe(o, Elemento.H).size() == 1) oHidroxilo = o;
        }
        if (oDoble == null || oHidroxilo == null) return null;
        return new Coincidencia(ACIDO, List.of(c, oDoble, oHidroxilo));
    }

    /** C=O donde el C no tiene otros heteroátomos (si tuviera O u N sería ácido, éster o amida). */
    private Coincidencia carbonilo(Molecula m, Atomo c) {
        Atomo oDoble = null;
        for (Atomo o : m.vecinosDe(c, Elemento.O)) if (m.obtenerEnlace(c, o).getOrden() == 2) oDoble = o;
        if (oDoble == null) return null;
        for (Atomo v : m.vecinos(c)) if (!v.equals(oDoble) && !v.es(Elemento.C) && !v.es(Elemento.H)) return null;
        return new Coincidencia(CARBONILO, List.of(c, oDoble));
    }

    /** N con al menos un C vecino, solo enlaces simples y ningún C vecino con C=O (amida). */
    private Coincidencia amina(Molecula m, Atomo n) {
        List<Atomo> carbonos = m.vecinosDe(n, Elemento.C);
        if (carbonos.isEmpty() || tieneEnlaceMultiple(m, n)) return null;
        for (Atomo c : carbonos) {
            for (Atomo o : m.vecinosDe(c, Elemento.O)) if (m.obtenerEnlace(c, o).getOrden() == 2) return null;
        }
        List<Atomo> atomos = new ArrayList<>();
        atomos.add(n);
        atomos.addAll(carbonos);
        return new Coincidencia(AMINA, atomos);
    }

    /**
     * Anillos: primero tieneCiclos() de la biblioteca (DFS). Si hay ciclo:
     *  - cantidad de anillos = E - (aristas del árbol abarcador de Kruskal)
     *    (cada arista que no entra al árbol cierra exactamente un ciclo);
     *  - átomos del anillo = los que quedan al "podar" repetidamente las hojas (grado 1).
     */
    private Coincidencia anillos(Molecula m) {
        if (!m.tieneAnillos()) return null;
        int cantidad = contarAnillos(m);
        Map<Atomo, Integer> grado = new HashMap<>();
        Deque<Atomo> hojas = new ArrayDeque<>();
        for (Atomo a : m.atomos()) {
            grado.put(a, m.grado(a));
            if (m.grado(a) <= 1) hojas.add(a);
        }
        Set<Atomo> podados = new HashSet<>();
        while (!hojas.isEmpty()) {
            Atomo h = hojas.poll();
            if (!podados.add(h)) continue;
            for (Atomo v : m.vecinos(h)) {
                if (podados.contains(v)) continue;
                grado.put(v, grado.get(v) - 1);
                if (grado.get(v) <= 1) hojas.add(v);
            }
        }
        List<Atomo> enAnillo = new ArrayList<>();
        for (Atomo a : m.atomos()) if (!podados.contains(a)) enAnillo.add(a);
        return new Coincidencia(ANILLO + " (" + cantidad + ")", enAnillo);
    }

    /** Número de anillos independientes = E - aristas del árbol abarcador mínimo (Kruskal). */
    public int contarAnillos(Molecula m) {
        if (m.cantidadAtomos() == 0) return 0;
        return m.cantidadEnlaces() - algNoDirigidos.kruskal(m.getGrafo()).cantidadDeAristas();
    }

    // ─────────────── Auxiliares ───────────────

    private boolean tieneEnlaceMultiple(Molecula m, Atomo a) {
        for (Edge<Atomo, Enlace> e : m.adyacencias(a)) if (e.dato().getOrden() > 1) return true;
        return false;
    }

    /** Orden de visita BEA desde cada componente (la molécula válida es conexa: un solo recorrido). */
    private List<Atomo> recorrerEnAmplitud(Molecula m) {
        List<Atomo> orden = new ArrayList<>();
        Set<Atomo> vistos = new HashSet<>();
        for (Atomo inicio : m.atomos()) {
            if (vistos.contains(inicio)) continue;
            algDirigidos.recorridoEnAmplitud(m.getGrafo(), m.getGrafo().construirComparable(inicio), a -> {
                if (vistos.add(a)) orden.add(a);
            });
        }
        return orden;
    }
}
