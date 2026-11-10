package ucu.edu.aed.molecular.servicios;

import ucu.edu.aed.molecular.compatibilidad.PerfilEstructural;
import ucu.edu.aed.molecular.compatibilidad.TipoCarbono;
import ucu.edu.aed.molecular.desafio3.OptimizacionWiener;
import ucu.edu.aed.molecular.grafo.DirectedGraph;
import ucu.edu.aed.molecular.grafo.UndirectedGraph;
import ucu.edu.aed.molecular.grafo.algoritmos.DirectedGraphAlgorithms;
import ucu.edu.aed.molecular.molecula.*;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.result.IDijkstraResult;

import java.util.*;

/*
  Calcula las propiedades estructurales que pide el análisis farmacológico.

  - Cadena carbonada principal: camino simple más largo dentro del esqueleto de carbonos.
      * Si el esqueleto es un árbol (sin anillos): "doble barrido". Dijkstra desde cualquier
        carbono -> el más lejano u; Dijkstra desde u -> el más lejano v. El camino u..v es el
        más largo. Dos Dijkstra: O((V + E) log V).
      * Si tiene anillos: búsqueda exhaustiva con DFS (exponencial en teoría, pero las moléculas
        del sistema tienen pocos carbonos).
  - Tipos de carbono: para cada C se cuentan sus vecinos C. O(V + E).
  - Índice de Wiener: se reutiliza la versión optimizada del Desafío 3 (BFS desde cada átomo + caché).
*/
public class AnalizadorEstructural {

    private final DirectedGraphAlgorithms alg = new DirectedGraphAlgorithms();

    public PerfilEstructural perfil(Molecula m) {
        return new PerfilEstructural(m.getNombre(), longitudCadenaPrincipal(m), clasificarCarbonos(m), indiceWiener(m));
    }

    /** Cantidad de carbonos de la cadena más larga. */
    public int longitudCadenaPrincipal(Molecula m) {
        UndirectedGraph<Atomo, Enlace> esqueleto = m.subgrafoCarbonos();
        if (esqueleto.cantidadDeVertices() == 0) return 0;
        if (esqueleto.tieneCiclos()) return caminoMasLargoDFS(esqueleto);

        int mejor = 0;
        Set<Atomo> visitados = new HashSet<>();
        DirectedGraph<Atomo, Enlace> dirigido = aDirigido(esqueleto);
        for (Atomo inicio : esqueleto.vertices()) {          // un doble barrido por componente
            if (visitados.contains(inicio)) continue;
            IDijkstraResult<Atomo> d1 = alg.dijkstra(dirigido.construirComparable(inicio), dirigido);
            Atomo u = masLejano(d1, esqueleto.vertices(), visitados);
            IDijkstraResult<Atomo> d2 = alg.dijkstra(dirigido.construirComparable(u), dirigido);
            Atomo v = masLejano(d2, esqueleto.vertices(), null);
            mejor = Math.max(mejor, (int) d2.getCost(v) + 1);
        }
        return mejor;
    }

    public Map<TipoCarbono, Integer> clasificarCarbonos(Molecula m) {
        Map<TipoCarbono, Integer> cuenta = new EnumMap<>(TipoCarbono.class);
        for (TipoCarbono t : TipoCarbono.values()) cuenta.put(t, 0);
        for (Atomo c : m.atomosDe(Elemento.C)) {
            TipoCarbono tipo = TipoCarbono.segunVecinosCarbono(m.vecinosDe(c, Elemento.C).size());
            cuenta.merge(tipo, 1, Integer::sum);
        }
        return cuenta;
    }

    public long indiceWiener(Molecula m) { return OptimizacionWiener.calcularConCache(m); }

    // ─────────────── Auxiliares ───────────────

    private Atomo masLejano(IDijkstraResult<Atomo> r, Set<Atomo> candidatos, Set<Atomo> marcarAlcanzados) {
        Atomo lejano = null;
        double max = -1;
        for (Atomo a : candidatos) {
            double d = r.getCost(a);
            if (d == Double.POSITIVE_INFINITY) continue;
            if (marcarAlcanzados != null) marcarAlcanzados.add(a);
            if (d > max) { max = d; lejano = a; }
        }
        return lejano;
    }

    private DirectedGraph<Atomo, Enlace> aDirigido(UndirectedGraph<Atomo, Enlace> g) {
        DirectedGraph<Atomo, Enlace> d = new DirectedGraph<>();
        for (Atomo a : g.vertices()) d.agregarVertice(a);
        for (Edge<Atomo, Enlace> e : g.aristas()) {
            d.agregarArista(e.source(), e.target(), e.dato());
            d.agregarArista(e.target(), e.source(), e.dato());
        }
        return d;
    }

    private int caminoMasLargoDFS(UndirectedGraph<Atomo, Enlace> g) {
        int[] mejor = {0};
        for (Atomo inicio : g.vertices()) dfs(g, inicio, new HashSet<>(), 1, mejor);
        return mejor[0];
    }

    private void dfs(UndirectedGraph<Atomo, Enlace> g, Atomo actual, Set<Atomo> enCamino, int largo, int[] mejor) {
        enCamino.add(actual);
        mejor[0] = Math.max(mejor[0], largo);
        for (Edge<Atomo, Enlace> e : g.adyacencias(g.construirComparable(actual)))
            if (!enCamino.contains(e.target())) dfs(g, e.target(), enCamino, largo + 1, mejor);
        enCamino.remove(actual);
    }
}
