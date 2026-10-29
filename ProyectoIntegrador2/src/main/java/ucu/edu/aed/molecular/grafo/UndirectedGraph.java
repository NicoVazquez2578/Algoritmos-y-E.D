package ucu.edu.aed.molecular.grafo;

import ucu.edu.aed.tda.grafo.IUndirectedGraph;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.edge.UndirectedEdge;

import java.util.*;

public class UndirectedGraph<V, D> extends AbstractGraph<V, D> implements IUndirectedGraph<V, D> {

    @Override
    public boolean agregarArista(V source, V target, D dato) {
        agregarVertice(source);
        agregarVertice(target);
        // Check using symmetric equality
        UndirectedEdge<V, D> check = new UndirectedEdge<>(source, target, dato);
        if (adj.get(source).contains(check)) return false;
        adj.get(source).add(new UndirectedEdge<>(source, target, dato));
        adj.get(target).add(new UndirectedEdge<>(target, source, dato));
        return true;
    }

    @Override
    public boolean eliminarArista(Comparable<V> source, Comparable<V> target) {
        V s = buscarVertice(source);
        V t = buscarVertice(target);
        if (s == null || t == null) return false;
        boolean removed = adj.get(s).removeIf(e -> e.target().equals(t));
        adj.get(t).removeIf(e -> e.target().equals(s));
        return removed;
    }

    // aristas() deduplicates via UndirectedEdge.equals (symmetric hashCode/equals)
    @Override
    public Set<Edge<V, D>> aristas() {
        Set<Edge<V, D>> all = new LinkedHashSet<>();
        for (List<Edge<V, D>> list : adj.values()) all.addAll(list);
        return all;
    }

    @Override
    public boolean tieneCiclos() {
        Set<V> visitado = new HashSet<>();
        for (V v : adj.keySet())
            if (!visitado.contains(v) && dfsCiclo(v, null, visitado)) return true;
        return false;
    }

    private boolean dfsCiclo(V v, V padre, Set<V> visitado) {
        visitado.add(v);
        for (Edge<V, D> e : adj.get(v)) {
            V w = e.target();
            if (!visitado.contains(w)) {
                if (dfsCiclo(w, v, visitado)) return true;
            } else if (!w.equals(padre)) {
                return true;
            }
        }
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
            for (Edge<V, D> e : adj.get(v))
                if (visitado.add(e.target())) queue.add(e.target());
        }
        return visitado.size() == adj.size();
    }
}
