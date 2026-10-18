package ProblemSet4.Ej06;

import ucu.edu.aed.tda.grafo.IDirectedIGraph;
import ucu.edu.aed.tda.grafo.model.edge.Edge;

import java.util.*;

public class CaminoCritico {

    public static class Resultado<V> {
        public final List<V> caminoCritico;
        public final double costoCritico;
        public final List<List<V>> otrosCaminos;
        public final List<Double> costos;
        public final List<Double> holguras;

        Resultado(List<V> cc, double costo, List<List<V>> otros, List<Double> costos, List<Double> holguras) {
            this.caminoCritico = cc;
            this.costoCritico  = costo;
            this.otrosCaminos  = otros;
            this.costos        = costos;
            this.holguras      = holguras;
        }
    }

    public static <V, D> Resultado<V> calcular(IDirectedIGraph<V, D> g) {
        List<V> orden = kahnSort(g);

        V fuente = null, sumidero = null;
        for (V v : orden) {
            if (g.predecessors(g.construirComparable(v)).isEmpty()) { fuente = v; break; }
        }
        for (int i = orden.size() - 1; i >= 0; i--) {
            if (g.successors(g.construirComparable(orden.get(i))).isEmpty()) {
                sumidero = orden.get(i); break;
            }
        }

        Map<V, Double> dist = new LinkedHashMap<>();
        Map<V, V> predMap = new LinkedHashMap<>();
        for (V v : orden) dist.put(v, 0.0);

        for (V v : orden) {
            for (Edge<V, D> e : g.adyacencias(g.construirComparable(v))) {
                double w = peso(e);
                double alt = dist.get(v) + w;
                if (alt > dist.get(e.target())) {
                    dist.put(e.target(), alt);
                    predMap.put(e.target(), v);
                }
            }
        }

        double costoCritico = sumidero != null ? dist.get(sumidero) : 0;

        List<V> cc = new LinkedList<>();
        V cur = sumidero;
        while (cur != null) {
            ((LinkedList<V>) cc).addFirst(cur);
            cur = predMap.get(cur);
        }

        List<List<V>> todosCaminos = new ArrayList<>();
        List<Double> costosCaminos = new ArrayList<>();
        if (fuente != null && sumidero != null) {
            dfsAllPaths(fuente, sumidero, g, new ArrayList<>(), 0.0, todosCaminos, costosCaminos);
        }

        List<Double> holguras = new ArrayList<>();
        for (double c : costosCaminos) holguras.add(costoCritico - c);

        return new Resultado<>(cc, costoCritico, todosCaminos, costosCaminos, holguras);
    }

    private static <V, D> void dfsAllPaths(V actual, V destino, IDirectedIGraph<V, D> g,
                                            List<V> camino, double costo,
                                            List<List<V>> todos, List<Double> costos) {
        camino.add(actual);
        if (actual.equals(destino)) {
            todos.add(new ArrayList<>(camino));
            costos.add(costo);
        } else {
            for (Edge<V, D> e : g.adyacencias(g.construirComparable(actual))) {
                if (!camino.contains(e.target()))
                    dfsAllPaths(e.target(), destino, g, camino, costo + peso(e), todos, costos);
            }
        }
        camino.remove(camino.size() - 1);
    }

    private static <V, D> List<V> kahnSort(IDirectedIGraph<V, D> g) {
        Map<V, Integer> inDeg = new HashMap<>();
        for (V v : g.vertices()) inDeg.put(v, 0);
        for (Edge<V, D> e : g.aristas()) inDeg.merge(e.target(), 1, Integer::sum);
        Queue<V> q = new LinkedList<>();
        for (Map.Entry<V, Integer> en : inDeg.entrySet())
            if (en.getValue() == 0) q.add(en.getKey());
        List<V> orden = new ArrayList<>();
        while (!q.isEmpty()) {
            V v = q.poll();
            orden.add(v);
            for (V w : g.successors(g.construirComparable(v))) {
                int d = inDeg.get(w) - 1;
                inDeg.put(w, d);
                if (d == 0) q.add(w);
            }
        }
        return orden;
    }

    @SuppressWarnings("unchecked")
    private static <V, D> double peso(Edge<V, D> e) {
        if (e.dato() instanceof ucu.edu.aed.tda.grafo.model.edge.WeightedEdge we)
            return we.getWeight();
        if (e.dato() instanceof Number n) return n.doubleValue();
        return 1.0;
    }
}
