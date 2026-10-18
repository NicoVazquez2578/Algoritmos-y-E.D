package ucu.edu.aed.tda.grafo;

import ucu.edu.aed.tda.grafo.model.IGraph;
import java.util.Set;

public interface IDirectedIGraph<V, D> extends IGraph<V, D> {
    Set<V> successors(V vertice);
    Set<V> predecessors(V vertice);
}
