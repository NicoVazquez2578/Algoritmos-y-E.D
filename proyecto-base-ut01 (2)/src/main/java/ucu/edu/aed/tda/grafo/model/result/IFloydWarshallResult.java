package ucu.edu.aed.tda.grafo.model.result;

public interface IFloydWarshallResult<V> {
    double getCost(V from, V to);
    Path<V> getPath(V from, V to);
    boolean connected(V from, V to);
}
