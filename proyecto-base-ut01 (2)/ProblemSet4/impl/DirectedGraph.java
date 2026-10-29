package ProblemSet4.impl;

import ucu.edu.aed.tda.grafo.IDirectedIGraph;
import ucu.edu.aed.tda.grafo.model.edge.DirectedEdge;
import ucu.edu.aed.tda.grafo.model.edge.Edge;

import java.util.*;

public class DirectedGraph<V, D> implements IDirectedIGraph<V, D> {

    private final Map<V, Set<Edge<V, D>>> adj = new LinkedHashMap<>();

    // ── IGraph ──────────────────────────────────────────────────────────────

    @Override
    public boolean agregarVertice(V vertex) {
        if (adj.containsKey(vertex)) return false;
        adj.put(vertex, new LinkedHashSet<>());
        return true;
    }

    @Override
    public V buscarVertice(Comparable<V> criterio) {
        for (V v : adj.keySet())
            if (criterio.compareTo(v) == 0) return v;
        return null;
    }

    @Override
    public boolean agregarArista(V source, V target, D dato) {
        agregarVertice(source);
        agregarVertice(target);
        return adj.get(source).add(new DirectedEdge<>(source, target, dato));
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
        for (Set<Edge<V, D>> edges : adj.values())
            edges.removeIf(e -> e.target().equals(v));
        return true;
    }

    @Override
    public Set<V> vertices() {
        return Collections.unmodifiableSet(adj.keySet());
    }

    @Override
    public Set<Edge<V, D>> aristas() {
        Set<Edge<V, D>> all = new LinkedHashSet<>();
        for (Set<Edge<V, D>> s : adj.values()) all.addAll(s);
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
        return new ArrayList<>(adj.get(v));
    }

    @Override
    public void vaciar() {
        adj.clear();
    }

    // ── IDirectedIGraph ──────────────────────────────────────────────────────

    @Override
    public Set<V> successors(Comparable<V> criteria) {
        V v = buscarVertice(criteria);
        if (v == null) return Collections.emptySet();
        Set<V> succ = new LinkedHashSet<>();
        for (Edge<V, D> e : adj.get(v)) succ.add(e.target());
        return succ;
    }

    @Override
    public Set<V> predecessors(Comparable<V> criteria) {
        V target = buscarVertice(criteria);
        if (target == null) return Collections.emptySet();
        Set<V> pred = new LinkedHashSet<>();
        for (Map.Entry<V, Set<Edge<V, D>>> entry : adj.entrySet())
            for (Edge<V, D> e : entry.getValue())
                if (e.target().equals(target)) pred.add(entry.getKey());
        return pred;
    }

    // ── IGraph: ciclos y conectividad ────────────────────────────────────────

    @Override
    public boolean tieneCiclos() {
        Map<V, Integer> color = new HashMap<>();
        for (V v : adj.keySet()) color.put(v, 0);
        for (V v : adj.keySet())
            if (color.get(v) == 0 && dfsCiclo(v, color)) return true;
        return false;
    }

    private boolean dfsCiclo(V v, Map<V, Integer> color) {
        color.put(v, 1);
        for (V w : successors(construirComparable(v))) {
            if (color.get(w) == 1) return true;
            if (color.get(w) == 0 && dfsCiclo(w, color)) return true;
        }
        color.put(v, 2);
        return false;
    }

    @Override
    public boolean esConexo() {
        if (adj.isEmpty()) return true;
        Set<V> visitado = new HashSet<>();
        Queue<V> queue = new LinkedList<>();
        V inicio = adj.keySet().iterator().next();
        queue.add(inicio);
        visitado.add(inicio);
        while (!queue.isEmpty()) {
            V v = queue.poll();
            for (V w : successors(construirComparable(v)))
                if (visitado.add(w)) queue.add(w);
            for (V w : predecessors(construirComparable(v)))
                if (visitado.add(w)) queue.add(w);
        }
        return visitado.size() == adj.size();
    }
}
