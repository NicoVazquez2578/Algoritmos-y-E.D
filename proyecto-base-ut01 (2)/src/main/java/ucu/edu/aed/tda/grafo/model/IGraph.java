package ucu.edu.aed.tda.grafo.model;

import ucu.edu.aed.tda.grafo.model.edge.Edge;
import java.util.Set;

public interface IGraph<V, D> {
    void agregarVertice(V vertice);
    void agregarArista(V source, V target, D dato);
    boolean contieneVertice(V vertice);
    boolean contieneArista(V source, V target);
    Set<V> vertices();
    Set<Edge<V, D>> aristas();
    Set<Edge<V, D>> aristasDesde(V vertice);
    int cantidadVertices();
    int cantidadAristas();
    boolean esConexo();
    boolean tieneCiclos();
}
