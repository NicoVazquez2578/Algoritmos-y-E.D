package ucu.edu.aed.molecular.grafo.algoritmos;

import ucu.edu.aed.molecular.grafo.UndirectedGraph;
import ucu.edu.aed.tda.grafo.IUndirectedGraph;
import ucu.edu.aed.tda.grafo.IUndirectedGraphAlgorithm;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;

import java.util.*;
import java.util.function.Consumer;

public class UndirectedGraphAlgorithms implements IUndirectedGraphAlgorithm {

    // ── Kruskal ──────────────────────────────────────────────────────────────

    @Override
    public <V, D extends WeightedEdge> IUndirectedGraph<V, D> kruskal(IUndirectedGraph<V, D> graph) {
        List<Edge<V, D>> edges = new ArrayList<>(graph.aristas());
        edges.sort(Comparator.comparingDouble(e -> e.dato().getWeight()));

        UndirectedGraph<V, D> mst = new UndirectedGraph<>();
        graph.vertices().forEach(mst::agregarVertice);

        Map<V, V> parent = new HashMap<>();
        for (V v : graph.vertices()) parent.put(v, v);

        for (Edge<V, D> e : edges) {
            V rootS = find(parent, e.source());
            V rootT = find(parent, e.target());
            if (!rootS.equals(rootT)) {
                mst.agregarArista(e.source(), e.target(), e.dato());
                parent.put(rootS, rootT);
            }
        }
        return mst;
    }

    private <V> V find(Map<V, V> parent, V v) {
        while (!parent.get(v).equals(v)) v = parent.get(v);
        return v;
    }

    // ── Prim ─────────────────────────────────────────────────────────────────

    @Override
    public <V, D extends WeightedEdge> IUndirectedGraph<V, D> prim(IUndirectedGraph<V, D> graph, Comparable<V> source) {
        V inicio = graph.buscarVertice(source);
        UndirectedGraph<V, D> mst = new UndirectedGraph<>();
        graph.vertices().forEach(mst::agregarVertice);

        Set<V> enMST = new HashSet<>();
        enMST.add(inicio);

        PriorityQueue<Edge<V, D>> pq = new PriorityQueue<>(
                Comparator.comparingDouble(e -> e.dato().getWeight()));

        for (Edge<V, D> e : graph.adyacencias(graph.construirComparable(inicio)))
            pq.add(e);

        while (!pq.isEmpty()) {
            Edge<V, D> minEdge = pq.poll();
            V dest = minEdge.target();
            if (enMST.contains(dest)) continue;

            enMST.add(dest);
            mst.agregarArista(minEdge.source(), dest, minEdge.dato());

            for (Edge<V, D> e : graph.adyacencias(graph.construirComparable(dest)))
                if (!enMST.contains(e.target())) pq.add(e);
        }
        return mst;
    }

    // ── searchMinEdge ────────────────────────────────────────────────────────

    @Override
    public <V, D extends WeightedEdge> Edge<V, D> searchMinEdge(
            IUndirectedGraph<V, D> graph, Collection<V> U, Collection<V> V_set) {
        Edge<V, D> minEdge = null;
        double minWeight = Double.POSITIVE_INFINITY;

        for (V u : U) {
            for (Edge<V, D> e : graph.adyacencias(graph.construirComparable(u))) {
                if (V_set.contains(e.target()) && e.dato().getWeight() < minWeight) {
                    minWeight = e.dato().getWeight();
                    minEdge = e;
                }
            }
        }
        return minEdge;
    }

    // ── BEA (Breadth-First Search) ───────────────────────────────────────────

    @Override
    public <V, D> void bea(IUndirectedGraph<V, D> graph, Consumer<V> consumer) {
        Set<V> visitado = new LinkedHashSet<>();
        for (V v : graph.vertices()) {
            if (visitado.contains(v)) continue;
            Queue<V> queue = new LinkedList<>();
            queue.add(v);
            visitado.add(v);
            while (!queue.isEmpty()) {
                V cur = queue.poll();
                consumer.accept(cur);
                for (Edge<V, D> e : graph.adyacencias(graph.construirComparable(cur)))
                    if (visitado.add(e.target())) queue.add(e.target());
            }
        }
    }

    // ── Puntos de articulación (Tarjan) ──────────────────────────────────────

    public <V, D> Set<V> puntosDeArticulacion(IUndirectedGraph<V, D> graph) {
        Set<V> ap = new LinkedHashSet<>();
        Map<V, Integer> disc = new HashMap<>();
        Map<V, Integer> low = new HashMap<>();
        Map<V, V> parent = new HashMap<>();
        int[] timer = {0};

        for (V v : graph.vertices())
            if (!disc.containsKey(v))
                dfsAP(v, graph, disc, low, parent, ap, timer);

        return ap;
    }

    private <V, D> void dfsAP(V v, IUndirectedGraph<V, D> graph,
                               Map<V, Integer> disc, Map<V, Integer> low,
                               Map<V, V> parent, Set<V> ap, int[] timer) {
        disc.put(v, timer[0]);
        low.put(v, timer[0]);
        timer[0]++;
        int childCount = 0;

        for (Edge<V, D> e : graph.adyacencias(graph.construirComparable(v))) {
            V w = e.target();
            if (!disc.containsKey(w)) {
                childCount++;
                parent.put(w, v);
                dfsAP(w, graph, disc, low, parent, ap, timer);
                low.put(v, Math.min(low.get(v), low.get(w)));

                if (parent.get(v) == null && childCount > 1) ap.add(v);
                if (parent.get(v) != null && low.get(w) >= disc.get(v)) ap.add(v);
            } else if (!w.equals(parent.get(v))) {
                low.put(v, Math.min(low.get(v), disc.get(w)));
            }
        }
    }
}
