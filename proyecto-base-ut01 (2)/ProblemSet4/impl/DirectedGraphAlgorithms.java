package ProblemSet4.impl;

import ucu.edu.aed.tda.grafo.IDirectedGraphAlgorithms;
import ucu.edu.aed.tda.grafo.IDirectedIGraph;
import ucu.edu.aed.tda.grafo.model.IGraph;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;
import ucu.edu.aed.tda.grafo.model.result.*;

import java.util.*;
import java.util.function.Consumer;

public class DirectedGraphAlgorithms implements IDirectedGraphAlgorithms {

    // ── Topológico (Kahn) ────────────────────────────────────────────────────

    @Override
    public <V, D> List<V> calcularClasificacionTopologica(IDirectedIGraph<V, D> g) {
        Map<V, Integer> inDegree = new HashMap<>();
        for (V v : g.vertices()) inDegree.put(v, 0);
        for (Edge<V, D> e : g.aristas())
            inDegree.merge(e.target(), 1, Integer::sum);

        Queue<V> queue = new LinkedList<>();
        for (Map.Entry<V, Integer> entry : inDegree.entrySet())
            if (entry.getValue() == 0) queue.add(entry.getKey());

        List<V> orden = new ArrayList<>();
        while (!queue.isEmpty()) {
            V v = queue.poll();
            orden.add(v);
            for (V w : g.successors(g.construirComparable(v))) {
                int d = inDegree.get(w) - 1;
                inDegree.put(w, d);
                if (d == 0) queue.add(w);
            }
        }

        if (orden.size() != g.cantidadDeVertices())
            throw new IllegalStateException("El grafo tiene ciclos, no existe orden topológico.");
        return orden;
    }

    // ── Dijkstra ─────────────────────────────────────────────────────────────

    @Override
    public <V, D extends WeightedEdge> IDijkstraResult<V> dijkstra(Comparable<V> source, IDirectedIGraph<V, D> grafo) {
        V origen = grafo.buscarVertice(source);
        Map<V, Double> dist = new HashMap<>();
        Map<V, V> pred = new HashMap<>();
        for (V v : grafo.vertices()) dist.put(v, Double.POSITIVE_INFINITY);
        dist.put(origen, 0.0);

        PriorityQueue<V> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(origen);
        Set<V> visitados = new HashSet<>();

        while (!pq.isEmpty()) {
            V u = pq.poll();
            if (!visitados.add(u)) continue;
            for (Edge<V, D> e : grafo.adyacencias(grafo.construirComparable(u))) {
                double w = e.dato().getWeight();
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

    // ── Floyd-Warshall ───────────────────────────────────────────────────────

    @Override
    public <V, D extends WeightedEdge> IFloydWarshallResult<V> floyd(IDirectedIGraph<V, D> grafo) {
        List<V> verts = new ArrayList<>(grafo.vertices());
        int n = verts.size();
        Map<V, Integer> idx = new HashMap<>();
        for (int i = 0; i < n; i++) idx.put(verts.get(i), i);

        double[][] dist = new double[n][n];
        int[][] prec = new int[n][n];
        for (double[] row : dist) Arrays.fill(row, Double.POSITIVE_INFINITY);
        for (int[] row : prec) Arrays.fill(row, -1);
        for (int i = 0; i < n; i++) dist[i][i] = 0;

        for (Edge<V, D> e : grafo.aristas()) {
            int s = idx.get(e.source()), t = idx.get(e.target());
            dist[s][t] = e.dato().getWeight();
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

    @Override
    public <V, D extends WeightedEdge> IFloydWarshallResult<V> warshall(IDirectedIGraph<V, D> grafo) {
        return floyd(grafo);
    }

    // ── Centro y excentricidad ────────────────────────────────────────────────

    @Override
    public <V, D extends WeightedEdge> V obtenerCentroGrafo(IDirectedIGraph<V, D> grafo) {
        IFloydWarshallResult<V> fw = floyd(grafo);
        V centro = null;
        double minEcc = Double.POSITIVE_INFINITY;
        for (V v : grafo.vertices()) {
            double ecc = obtenerExcentricidad(fw, grafo.vertices(), v);
            if (ecc < minEcc) { minEcc = ecc; centro = v; }
        }
        return centro;
    }

    @Override
    public <V, D extends WeightedEdge> double obtenerExcentricidad(IDirectedIGraph<V, D> grafo, Comparable<V> vertexCriteria) {
        V v = grafo.buscarVertice(vertexCriteria);
        IFloydWarshallResult<V> fw = floyd(grafo);
        return obtenerExcentricidad(fw, grafo.vertices(), v);
    }

    private <V> double obtenerExcentricidad(IFloydWarshallResult<V> fw, Set<V> vertices, V v) {
        double ecc = 0;
        for (V u : vertices) {
            double d = fw.getCost(v, u);
            if (d > ecc && d < Double.POSITIVE_INFINITY) ecc = d;
        }
        return ecc;
    }

    // ── Todos los caminos ─────────────────────────────────────────────────────

    @Override
    public <V, D extends WeightedEdge> List<Path<V>> obtenerTodosLosCaminos(
            Comparable<V> source, Comparable<V> target, IGraph<V, D> grafo) {
        V s = grafo.buscarVertice(source);
        V t = grafo.buscarVertice(target);
        List<Path<V>> resultado = new ArrayList<>();
        if (s == null || t == null) return resultado;
        dfsAllPaths(s, t, grafo, new ArrayList<>(), 0.0, resultado);
        return resultado;
    }

    private <V, D extends WeightedEdge> void dfsAllPaths(
            V actual, V destino, IGraph<V, D> grafo,
            List<V> camino, double costo, List<Path<V>> todos) {
        camino.add(actual);
        if (actual.equals(destino)) {
            todos.add(new Path<>(new ArrayList<>(camino), costo));
        } else {
            for (Edge<V, D> e : grafo.adyacencias(grafo.construirComparable(actual))) {
                if (!camino.contains(e.target()))
                    dfsAllPaths(e.target(), destino, grafo, camino, costo + e.dato().getWeight(), todos);
            }
        }
        camino.remove(camino.size() - 1);
    }

    // ── Recorridos ────────────────────────────────────────────────────────────

    @Override
    public <V, D> void recorridoEnProfundidad(IGraph<V, D> grafo, Comparable<V> sourceCriteria, Consumer<V> consumer) {
        V inicio = grafo.buscarVertice(sourceCriteria);
        if (inicio == null) return;
        Set<V> visitado = new LinkedHashSet<>();
        dfsVisit(inicio, grafo, visitado, consumer);
    }

    private <V, D> void dfsVisit(V v, IGraph<V, D> grafo, Set<V> visitado, Consumer<V> consumer) {
        if (!visitado.add(v)) return;
        consumer.accept(v);
        for (Edge<V, D> e : grafo.adyacencias(grafo.construirComparable(v)))
            dfsVisit(e.target(), grafo, visitado, consumer);
    }

    @Override
    public <V, D> void recorridoEnAmplitud(IGraph<V, D> grafo, Comparable<V> sourceCriteria, Consumer<V> consumer) {
        V inicio = grafo.buscarVertice(sourceCriteria);
        if (inicio == null) return;
        Set<V> visitado = new LinkedHashSet<>();
        Queue<V> queue = new LinkedList<>();
        queue.add(inicio);
        visitado.add(inicio);
        while (!queue.isEmpty()) {
            V v = queue.poll();
            consumer.accept(v);
            for (Edge<V, D> e : grafo.adyacencias(grafo.construirComparable(v)))
                if (visitado.add(e.target())) queue.add(e.target());
        }
    }

    // ── Clases internas de resultado ─────────────────────────────────────────

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
        public List<V> getPath(V dest) {
            if (getCost(dest) == Double.POSITIVE_INFINITY) return null;
            LinkedList<V> path = new LinkedList<>();
            V cur = dest;
            while (cur != null && !cur.equals(origen)) {
                path.addFirst(cur);
                cur = pred.get(cur);
            }
            if (cur == null) return null;
            path.addFirst(origen);
            return new ArrayList<>(path);
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
        public List<V> getPath(V from, V to) {
            int s = idx.get(from), t = idx.get(to);
            if (prec[s][t] == -1 && s != t) return null;
            LinkedList<V> path = new LinkedList<>();
            int cur = t;
            path.addFirst(verts.get(cur));
            while (cur != s) {
                cur = prec[s][cur];
                path.addFirst(verts.get(cur));
            }
            return new ArrayList<>(path);
        }
    }
}
