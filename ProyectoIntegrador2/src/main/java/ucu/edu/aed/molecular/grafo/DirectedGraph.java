package ucu.edu.aed.molecular.grafo;

import ucu.edu.aed.tda.grafo.IDirectedIGraph;
import ucu.edu.aed.tda.grafo.model.edge.DirectedEdge;
import ucu.edu.aed.tda.grafo.model.edge.Edge;

import java.util.*;

public class DirectedGraph<V, D> extends AbstractGraph<V, D> implements IDirectedIGraph<V, D> {

    @Override
    public boolean agregarArista(V source, V target, D dato) {
        agregarVertice(source);
        agregarVertice(target);
        Edge<V, D> edge = new DirectedEdge<>(source, target, dato);
        if (adj.get(source).contains(edge)) return false;
        return adj.get(source).add(edge);
    }

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
        for (Map.Entry<V, List<Edge<V, D>>> entry : adj.entrySet())
            for (Edge<V, D> e : entry.getValue())
                if (e.target().equals(target)) pred.add(entry.getKey());
        return pred;
    }

    @Override
    public boolean tieneCiclos() {
        // WHITE=0, GRAY=1, BLACK=2
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
