package ProblemSet4.impl;

import ucu.edu.aed.tda.grafo.IDirectedGraphAlgorithms;
import ucu.edu.aed.tda.grafo.IDirectedIGraph;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;
import ucu.edu.aed.tda.grafo.model.result.*;

import java.util.*;

public class DirectedGraphAlgorithms implements IDirectedGraphAlgorithms {

    // ---------- Ordenamiento Topológico (Kahn) ----------

    @Override
    public <V, D> List<V> calcularClasificacionTopologica(IDirectedIGraph<V, D> g) {
        Map<V, Integer> inDegree = new HashMap<>();
        for (V v : g.vertices()) inDegree.put(v, 0);
        for (Edge<V, D> e : g.aristas())
            inDegree.put(e.target(), inDegree.get(e.target()) + 1);

        Queue<V> queue = new LinkedList<>();
        for (Map.Entry<V, Integer> entry : inDegree.entrySet())
            if (entry.getValue() == 0) queue.add(entry.getKey());

        List<V> orden = new ArrayList<>();
        while (!queue.isEmpty()) {
            V v = queue.poll();
            orden.add(v);
            for (V w : g.successors(v)) {
                int d = inDegree.get(w) - 1;
                inDegree.put(w, d);
                if (d == 0) queue.add(w);
            }
        }

        if (orden.size() != g.cantidadVertices())
            throw new IllegalStateException("El grafo tiene ciclos, no existe orden topológico.");
        return orden;
    }

    // ---------- Dijkstra ----------

    @Override
    public <V, D> IDijkstraResult<V> dijkstra(IDirectedIGraph<V, D> g, V origen) {
        Map<V, Double> dist = new HashMap<>();
        Map<V, V> pred = new HashMap<>();
        for (V v : g.vertices()) dist.put(v, Double.POSITIVE_INFINITY);
        dist.put(origen, 0.0);

        PriorityQueue<V> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(origen);
        Set<V> visitados = new HashSet<>();

        while (!pq.isEmpty()) {
            V u = pq.poll();
            if (!visitados.add(u)) continue;
            for (Edge<V, D> e : g.aristasDesde(u)) {
                double w = peso(e);
                double alt = dist.get(u) + w;
                if (alt < dist.get(e.target())) {
                    dist.put(e.target(), alt);
                    pred.put(e.target(), u);
                    pq.add(e.target());
                }
            }
        }
        return new DijkstraResult<>(origen, dist, pred);
    }

    // ---------- Floyd-Warshall ----------

    @Override
    public <V, D> IFloydWarshallResult<V> floydWarshall(IDirectedIGraph<V, D> g) {
        List<V> verts = new ArrayList<>(g.vertices());
        int n = verts.size();
        Map<V, Integer> idx = new HashMap<>();
        for (int i = 0; i < n; i++) idx.put(verts.get(i), i);

        double[][] dist = new double[n][n];
        int[][] prec = new int[n][n];
        for (double[] row : dist) Arrays.fill(row, Double.POSITIVE_INFINITY);
        for (int[] row : prec) Arrays.fill(row, -1);
        for (int i = 0; i < n; i++) dist[i][i] = 0;

        for (Edge<V, D> e : g.aristas()) {
            int s = idx.get(e.source()), t = idx.get(e.target());
            dist[s][t] = peso(e);
            prec[s][t] = s;
        }

        for (int k = 0; k < n; k++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        prec[i][j] = prec[k][j];
                    }

        return new FloydWarshallResult<>(verts, dist, prec);
    }

    private <V, D> double peso(Edge<V, D> e) {
        if (e.dato() instanceof WeightedEdge)
            return ((WeightedEdge) e.dato()).getWeight();
        if (e.dato() instanceof Number)
            return ((Number) e.dato()).doubleValue();
        return 1.0;
    }

    // ---------- Clases internas de resultado ----------

    private static class DijkstraResult<V> implements IDijkstraResult<V> {
        private final V origen;
        private final Map<V, Double> dist;
        private final Map<V, V> pred;

        DijkstraResult(V origen, Map<V, Double> dist, Map<V, V> pred) {
            this.origen = origen;
            this.dist = dist;
            this.pred = pred;
        }

        @Override
        public double getCost(V dest) { return dist.getOrDefault(dest, Double.POSITIVE_INFINITY); }

        @Override
        public Path<V> getPath(V dest) {
            LinkedList<V> path = new LinkedList<>();
            V cur = dest;
            while (cur != null && !cur.equals(origen)) {
                path.addFirst(cur);
                cur = pred.get(cur);
            }
            if (cur == null) return null;
            path.addFirst(origen);
            return new Path<>(path, getCost(dest));
        }
    }

    private static class FloydWarshallResult<V> implements IFloydWarshallResult<V> {
        private final List<V> verts;
        private final double[][] dist;
        private final int[][] prec;
        private final Map<V, Integer> idx;

        FloydWarshallResult(List<V> verts, double[][] dist, int[][] prec) {
            this.verts = verts;
            this.dist = dist;
            this.prec = prec;
            idx = new HashMap<>();
            for (int i = 0; i < verts.size(); i++) idx.put(verts.get(i), i);
        }

        @Override
        public double getCost(V from, V to) { return dist[idx.get(from)][idx.get(to)]; }

        @Override
        public boolean connected(V from, V to) { return dist[idx.get(from)][idx.get(to)] < Double.POSITIVE_INFINITY; }

        @Override
        public Path<V> getPath(V from, V to) {
            int s = idx.get(from), t = idx.get(to);
            if (prec[s][t] == -1 && s != t) return null;
            LinkedList<V> path = new LinkedList<>();
            path.addFirst(verts.get(t));
            while (t != s) {
                t = prec[s][t];
                path.addFirst(verts.get(t));
            }
            return new Path<>(new ArrayList<>(path), dist[idx.get(from)][idx.get(to)]);
        }
    }
}
