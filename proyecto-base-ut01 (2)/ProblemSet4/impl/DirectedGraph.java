package ProblemSet4.impl;

import ucu.edu.aed.tda.grafo.IDirectedIGraph;
import ucu.edu.aed.tda.grafo.model.edge.DirectedEdge;
import ucu.edu.aed.tda.grafo.model.edge.Edge;

import java.util.*;

public class DirectedGraph<V, D> implements IDirectedIGraph<V, D> {

    private final Map<V, Set<Edge<V, D>>> adj = new LinkedHashMap<>();

    @Override
    public void agregarVertice(V v) {
        adj.putIfAbsent(v, new LinkedHashSet<>());
    }

    @Override
    public void agregarArista(V source, V target, D dato) {
        agregarVertice(source);
        agregarVertice(target);
        adj.get(source).add(new DirectedEdge<>(source, target, dato));
    }

    @Override
    public boolean contieneVertice(V v) { return adj.containsKey(v); }

    @Override
    public boolean contieneArista(V source, V target) {
        if (!adj.containsKey(source)) return false;
        for (Edge<V, D> e : adj.get(source))
            if (e.target().equals(target)) return true;
        return false;
    }

    @Override
    public Set<V> vertices() { return Collections.unmodifiableSet(adj.keySet()); }

    @Override
    public Set<Edge<V, D>> aristas() {
        Set<Edge<V, D>> all = new LinkedHashSet<>();
        for (Set<Edge<V, D>> s : adj.values()) all.addAll(s);
        return all;
    }

    @Override
    public Set<Edge<V, D>> aristasDesde(V v) {
        return adj.getOrDefault(v, Collections.emptySet());
    }

    @Override
    public int cantidadVertices() { return adj.size(); }

    @Override
    public int cantidadAristas() {
        int n = 0;
        for (Set<Edge<V, D>> s : adj.values()) n += s.size();
        return n;
    }

    @Override
    public Set<V> successors(V v) {
        Set<V> succ = new LinkedHashSet<>();
        for (Edge<V, D> e : aristasDesde(v)) succ.add(e.target());
        return succ;
    }

    @Override
    public Set<V> predecessors(V v) {
        Set<V> pred = new LinkedHashSet<>();
        for (Map.Entry<V, Set<Edge<V, D>>> entry : adj.entrySet())
            for (Edge<V, D> e : entry.getValue())
                if (e.target().equals(v)) pred.add(entry.getKey());
        return pred;
    }

    // DFS con coloreo: WHITE=0, GRAY=1, BLACK=2
    // Un ciclo existe si encontramos un vecino GRAY (back-edge)
    @Override
    public boolean tieneCiclos() {
        Map<V, Integer> color = new HashMap<>();
        for (V v : adj.keySet()) color.put(v, 0);
        for (V v : adj.keySet())
            if (color.get(v) == 0 && dfsCiclo(v, color)) return true;
        return false;
    }

    private boolean dfsCiclo(V v, Map<V, Integer> color) {
        color.put(v, 1); // GRAY
        for (V w : successors(v)) {
            if (color.get(w) == 1) return true;    // back-edge → ciclo
            if (color.get(w) == 0 && dfsCiclo(w, color)) return true;
        }
        color.put(v, 2); // BLACK
        return false;
    }

    // BFS ignorando dirección para verificar conectividad
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
            for (V w : successors(v))
                if (visitado.add(w)) queue.add(w);
            for (V w : predecessors(v))
                if (visitado.add(w)) queue.add(w);
        }
        return visitado.size() == adj.size();
    }
}
