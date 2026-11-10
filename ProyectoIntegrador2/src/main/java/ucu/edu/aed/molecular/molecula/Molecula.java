package ucu.edu.aed.molecular.molecula;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ucu.edu.aed.molecular.grafo.DirectedGraph;
import ucu.edu.aed.molecular.grafo.UndirectedGraph;
import ucu.edu.aed.tda.grafo.IUndirectedGraph;
import ucu.edu.aed.tda.grafo.model.edge.Edge;

/*
  Molécula modelada como GRAFO NO DIRIGIDO:
   - vértice = Atomo
   - arista  = Enlace (simple, doble o triple)
  Es no dirigido porque un enlace químico es simétrico: si A está unido a B, B está unido a A.
 
  Representación: UndirectedGraph de la biblioteca (lista de adyacencia en un LinkedHashMap).
  Cada enlace se guarda dos veces, una en la lista de cada átomo, y en cada copia
  source() es el átomo dueño de la lista. Por eso el vecino siempre es e.target().
 */
public class Molecula {

    private final String nombre;
    private final IUndirectedGraph<Atomo, Enlace> grafo;
    private int siguienteId = 1;

    /** Crea una molécula vacía usando el UndirectedGraph de la biblioteca. */
    public Molecula(String nombre) {
        this(nombre, new UndirectedGraph<>());
    }

    /**
     * Constructor con inyección del grafo: permite probar la molécula con otra
     * implementación de IUndirectedGraph (por ejemplo, una con matriz de adyacencia en el Desafío 3).
     */
    public Molecula(String nombre, IUndirectedGraph<Atomo, Enlace> grafoVacio) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("La molécula necesita un nombre");
        if (grafoVacio == null) throw new IllegalArgumentException("El grafo no puede ser null");
        if (grafoVacio.cantidadDeVertices() > 0) throw new IllegalArgumentException("El grafo debe estar vacío");
        this.nombre = nombre;
        this.grafo = grafoVacio;
    }

    // ───────────────────────── Construcción ─────────────────────────

    /** Crea un átomo nuevo con id automático y lo agrega como vértice. O(1) (put en LinkedHashMap). */
    public Atomo agregarAtomo(Elemento elemento) {
        Atomo atomo = new Atomo(siguienteId++, elemento);
        grafo.agregarVertice(atomo);
        return atomo;
    }

    /** Agrega varios átomos del mismo elemento. Útil para cadenas: agregarAtomos(Elemento.C, 6). */
    public List<Atomo> agregarAtomos(Elemento elemento, int cantidad) {
        List<Atomo> nuevos = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) nuevos.add(agregarAtomo(elemento));
        return nuevos;
    }

    /**
     * Une dos átomos con un enlace. Valida antes de agregar la arista:
     *  - que ambos átomos pertenezcan a la molécula,
     *  - que no sea un auto-enlace ni un enlace repetido,
     *  - que ninguno de los dos supere su valencia.
     * Si algo falla, lanza IllegalArgumentException y la molécula queda sin cambios.
     *
     * Costo: O(V + grado(a) + grado(b)). El O(V) viene de buscarVertice en AbstractGraph,
     * que recorre las claves con el Comparable (no usa el hash).
     */
    public void enlazar(Atomo a, Atomo b, TipoEnlace tipo) {
        if (tipo == null) throw new IllegalArgumentException("El tipo de enlace no puede ser null");
        if (!contiene(a) || !contiene(b)) throw new IllegalArgumentException("Ambos átomos deben pertenecer a " + nombre);
        if (a.equals(b)) throw new IllegalArgumentException("Un átomo no puede enlazarse consigo mismo: " + a);
        if (estanEnlazados(a, b)) throw new IllegalArgumentException("Ya existe un enlace entre " + a + " y " + b);
        int orden = tipo.getOrden();
        if (valenciaUsada(a) + orden > a.getElemento().getValencia())
            throw new IllegalArgumentException(a + " superaría su valencia (" + a.getElemento().getValencia() + ")");
        if (valenciaUsada(b) + orden > b.getElemento().getValencia())
            throw new IllegalArgumentException(b + " superaría su valencia (" + b.getElemento().getValencia() + ")");
        grafo.agregarArista(a, b, new Enlace(tipo));
    }

    /** Atajo para enlaces simples. */
    public void enlazar(Atomo a, Atomo b) { enlazar(a, b, TipoEnlace.SIMPLE); }

    /** Une una lista de átomos en cadena con enlaces simples: a0-a1-a2-... */
    public void enlazarEnCadena(List<Atomo> atomos) {
        for (int i = 0; i + 1 < atomos.size(); i++) enlazar(atomos.get(i), atomos.get(i + 1));
    }

    // ───────────────────────── Consultas sobre átomos ─────────────────────────

    public boolean contiene(Atomo a) { return a != null && grafo.vertices().contains(a); }

    public boolean estanEnlazados(Atomo a, Atomo b) { return obtenerEnlace(a, b) != null; }

    /** Devuelve el enlace entre a y b, o null si no están unidos. Usa obtenerArista de la biblioteca. */
    public Enlace obtenerEnlace(Atomo a, Atomo b) {
        Edge<Atomo, Enlace> e = grafo.obtenerArista(grafo.construirComparable(a), grafo.construirComparable(b));
        return e == null ? null : e.dato();
    }

    /** Átomos unidos directamente a 'a'. O(V + grado(a)). */
    public List<Atomo> vecinos(Atomo a) {
        List<Atomo> vecinos = new ArrayList<>();
        for (Edge<Atomo, Enlace> e : adyacencias(a)) vecinos.add(e.target());
        return vecinos;
    }

    /** Vecinos de 'a' que son del elemento indicado. Útil para grupos funcionales. */
    public List<Atomo> vecinosDe(Atomo a, Elemento elemento) {
        List<Atomo> res = new ArrayList<>();
        for (Atomo v : vecinos(a)) if (v.es(elemento)) res.add(v);
        return res;
    }

    /** Aristas del átomo; en cada una source() == a y target() == vecino. */
    public List<Edge<Atomo, Enlace>> adyacencias(Atomo a) {
        return grafo.adyacencias(grafo.construirComparable(a));
    }

    /** Cantidad de enlaces (aristas) del átomo, sin importar su orden. */
    public int grado(Atomo a) { return adyacencias(a).size(); }

    /** Suma de los órdenes de los enlaces del átomo. Ej: en C=O el carbono usa 2. */
    public int valenciaUsada(Atomo a) {
        int suma = 0;
        for (Edge<Atomo, Enlace> e : adyacencias(a)) suma += e.dato().getOrden();
        return suma;
    }

    /** Hidrógenos que completan la valencia del átomo y no fueron cargados como vértices. */
    public int hidrogenosImplicitos(Atomo a) {
        if (a.es(Elemento.H)) return 0;
        return Math.max(0, a.getElemento().getValencia() - valenciaUsada(a));
    }

    /** Hidrógenos totales unidos a 'a' (explícitos + implícitos). Útil para detectar -OH, -NH2. */
    public int hidrogenosTotales(Atomo a) {
        return vecinosDe(a, Elemento.H).size() + hidrogenosImplicitos(a);
    }

    public Atomo buscarAtomo(int id) {
        return grafo.buscarVertice(x -> x.getId() == id ? 0 : 1);
    }

    public List<Atomo> atomosDe(Elemento elemento) {
        List<Atomo> res = new ArrayList<>();
        for (Atomo a : grafo.vertices()) if (a.es(elemento)) res.add(a);
        return res;
    }

    // ───────────────────────── Validación ─────────────────────────

    /**
     * Revisa toda la molécula y devuelve la lista de problemas (vacía si es válida):
     *  - que tenga al menos un átomo,
     *  - que ningún átomo supere su valencia,
     *  - que sea conexa (usa esConexo de la biblioteca: un BEA desde un vértice).
     * Costo: O(V·(V + grado)) por las búsquedas de vértice + O(V + E) de esConexo.
     */
    public List<String> validar() {
        List<String> errores = new ArrayList<>();
        if (grafo.cantidadDeVertices() == 0) errores.add("La molécula no tiene átomos");
        for (Atomo a : grafo.vertices()) {
            int usada = valenciaUsada(a);
            int max = a.getElemento().getValencia();
            if (usada > max) errores.add(a + " usa valencia " + usada + " y su máximo es " + max);
        }
        if (!grafo.esConexo()) errores.add("La molécula no es conexa: tiene fragmentos sueltos");
        return errores;
    }

    public boolean esValida() { return validar().isEmpty(); }

    /** true si la molécula tiene al menos un anillo (ciclo en el grafo). Usa tieneCiclos de la biblioteca. */
    public boolean tieneAnillos() { return grafo.tieneCiclos(); }

    // ───────────────────────── Datos derivados ─────────────────────────

    /** Cuenta cuántos átomos hay de cada elemento, sumando los hidrógenos implícitos. */
    public Map<Elemento, Integer> contarElementos() {
        Map<Elemento, Integer> cuenta = new EnumMap<>(Elemento.class);
        int hImplicitos = 0;
        for (Atomo a : grafo.vertices()) {
            cuenta.merge(a.getElemento(), 1, Integer::sum);
            hImplicitos += hidrogenosImplicitos(a);
        }
        if (hImplicitos > 0) cuenta.merge(Elemento.H, hImplicitos, Integer::sum);
        return cuenta;
    }

    /**
     * Fórmula molecular en notación de Hill:
     *  - si hay carbono: primero C, luego H, luego el resto en orden alfabético;
     *  - si no hay carbono: todos en orden alfabético.
     * Ejemplos: etanol → C2H6O, agua → H2O, amoníaco → H3N.
     */
    public String formulaMolecular() {
        Map<Elemento, Integer> cuenta = contarElementos();
        List<Elemento> orden = new ArrayList<>(cuenta.keySet());
        boolean hayCarbono = cuenta.containsKey(Elemento.C);
        orden.sort((x, y) -> {
            if (hayCarbono) {
                int px = prioridadHill(x), py = prioridadHill(y);
                if (px != py) return Integer.compare(px, py);
            }
            return x.getSimbolo().compareTo(y.getSimbolo());
        });
        StringBuilder sb = new StringBuilder();
        for (Elemento e : orden) {
            sb.append(e.getSimbolo());
            int n = cuenta.get(e);
            if (n > 1) sb.append(n);
        }
        return sb.toString();
    }

    private static int prioridadHill(Elemento e) {
        if (e == Elemento.C) return 0;
        if (e == Elemento.H) return 1;
        return 2;
    }

    /** Masa molar en g/mol = Σ (cantidad × masa atómica). */
    public double masaMolar() {
        double masa = 0;
        for (Map.Entry<Elemento, Integer> e : contarElementos().entrySet()) {
            masa += e.getKey().getMasaAtomica() * e.getValue();
        }
        return masa;
    }

    // ───────────────────────── Vistas para los algoritmos ─────────────────────────

    /**
     * Copia de la molécula como grafo DIRIGIDO, con un arco en cada sentido por enlace.
     * Hace falta porque Dijkstra, Floyd, centro y excentricidad de la biblioteca solo
     * aceptan IDirectedIGraph. Un enlace no dirigido A-B equivale a los arcos A→B y B→A,
     * así que las distancias no cambian. El peso de cada arco es 1 (ver Enlace).
     * Costo: O(V + E).
     */
    public DirectedGraph<Atomo, Enlace> comoGrafoDirigido() {
        DirectedGraph<Atomo, Enlace> dirigido = new DirectedGraph<>();
        for (Atomo a : grafo.vertices()) dirigido.agregarVertice(a);
        for (Edge<Atomo, Enlace> e : grafo.aristas()) {
            dirigido.agregarArista(e.source(), e.target(), e.dato());
            dirigido.agregarArista(e.target(), e.source(), e.dato());
        }
        return dirigido;
    }

    /**
     * Subgrafo con solo los carbonos y los enlaces C-C (el "esqueleto carbonado").
     * Sobre él se calcula la cadena principal y la clasificación de carbonos.
     * Costo: O(V + E).
     */
    public UndirectedGraph<Atomo, Enlace> subgrafoCarbonos() {
        UndirectedGraph<Atomo, Enlace> esqueleto = new UndirectedGraph<>();
        for (Atomo a : grafo.vertices()) if (a.es(Elemento.C)) esqueleto.agregarVertice(a);
        for (Edge<Atomo, Enlace> e : grafo.aristas()) {
            if (e.source().es(Elemento.C) && e.target().es(Elemento.C))
                esqueleto.agregarArista(e.source(), e.target(), e.dato());
        }
        return esqueleto;
    }

    // ───────────────────────── Acceso ─────────────────────────

    public String getNombre() { return nombre; }

    public Set<Atomo> atomos() { return grafo.vertices(); }

    public int cantidadAtomos() { return grafo.cantidadDeVertices(); }

    public int cantidadEnlaces() { return grafo.cantidadDeAristas(); }

    /**
     * Grafo subyacente, para aplicarle los algoritmos no dirigidos (BEA, Prim, Kruskal,
     * puntos de articulación). No debe modificarse desde afuera: para agregar átomos
     * o enlaces usar agregarAtomo() y enlazar(), que validan.
     */
    public IUndirectedGraph<Atomo, Enlace> getGrafo() { return grafo; }

    @Override
    public String toString() { return nombre + " (" + formulaMolecular() + ")"; }
}
