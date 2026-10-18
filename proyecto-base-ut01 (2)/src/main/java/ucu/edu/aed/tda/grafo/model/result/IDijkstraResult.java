package ucu.edu.aed.tda.grafo.model.result;

public interface IDijkstraResult<V> {
    double getCost(V destination);
    Path<V> getPath(V destination);
}
