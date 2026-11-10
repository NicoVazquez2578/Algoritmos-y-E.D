package ucu.edu.aed.molecular.grafo;

import ucu.edu.aed.tda.grafo.model.IGraph;
import ucu.edu.aed.tda.grafo.model.edge.Edge;

import java.util.*;

public abstract class AbstractGraph<V, D> implements IGraph<V, D> {

    protected final Map<V, List<Edge<V, D>>> adj = new LinkedHashMap<>();

    @Override
    public boolean agregarVertice(V vertex) {
        if (adj.containsKey(vertex)) return false;
        adj.put(vertex, new ArrayList<>());
        return true;
    }

    @Override
    public V buscarVertice(Comparable<V> criterio) {
        for (V v : adj.keySet())
            if (criterio.compareTo(v) == 0) return v;
        return null;
    }

    @Override
    public Set<V> vertices() {
        return Collections.unmodifiableSet(adj.keySet());
    }

    @Override
    public Set<Edge<V, D>> aristas() {
        Set<Edge<V, D>> all = new LinkedHashSet<>();
        for (List<Edge<V, D>> list : adj.values()) all.addAll(list);
        return all;
    }

    @Override
    public boolean existeArista(Comparable<V> sourceCriteria, Comparable<V> targetCriteria) {
        return obtenerArista(sourceCriteria, targetCriteria) != null;
    }

    @Override
    public Edge<V, D> obtenerArista(Comparable<V> sourceCriteria, Comparable<V> targetCriteria) {
        V s = buscarVertice(sourceCriteria);
        if (s == null) return null;
        for (Edge<V, D> e : adj.get(s))
            if (targetCriteria.compareTo(e.target()) == 0) return e;
        return null;
    }

    @Override
    public List<Edge<V, D>> adyacencias(Comparable<V> verticeCriteria) {
        V v = buscarVertice(verticeCriteria);
        if (v == null) return Collections.emptyList();
        return Collections.unmodifiableList(adj.get(v));
    }

    @Override
    public boolean eliminarArista(Comparable<V> source, Comparable<V> target) {
        V s = buscarVertice(source);
        V t = buscarVertice(target);
        if (s == null || t == null) return false;
        return adj.get(s).removeIf(e -> e.target().equals(t));
    }

    @Override
    public boolean removerVertice(Comparable<V> criteria) {
        V v = buscarVertice(criteria);
        if (v == null) return false;
        adj.remove(v);
        for (List<Edge<V, D>> edges : adj.values())
            edges.removeIf(e -> e.source().equals(v) || e.target().equals(v));
        return true;
    }

    @Override
    public void vaciar() {
        adj.clear();
    }
}
