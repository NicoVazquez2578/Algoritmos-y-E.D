package ucu.edu.aed.molecular.grafo.resultado;

import ucu.edu.aed.tda.grafo.model.result.IDijkstraResult;

import java.util.*;

public class ResultadoDijkstra<V> implements IDijkstraResult<V> {

    private final V origen;
    private final Map<V, Double> distancias;
    private final Map<V, V> previos;

    public ResultadoDijkstra(V origen, Map<V, Double> distancias, Map<V, V> previos) {
        this.origen    = origen;
        this.distancias = distancias;
        this.previos   = previos;
    }

    @Override
    public double getCost(V dest) {
        return distancias.getOrDefault(dest, Double.POSITIVE_INFINITY);
    }

    @Override
    public List<V> getPath(V dest) {
        if (getCost(dest) == Double.POSITIVE_INFINITY) return null;
        LinkedList<V> path = new LinkedList<>();
        V cur = dest;
        while (cur != null && !cur.equals(origen)) {
            path.addFirst(cur);
            cur = previos.get(cur);
        }
        if (cur == null) return null;
        path.addFirst(origen);
        return new ArrayList<>(path);
    }
}
