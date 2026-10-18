package ucu.edu.aed.tda.grafo;

import ucu.edu.aed.tda.grafo.model.result.IDijkstraResult;
import ucu.edu.aed.tda.grafo.model.result.IFloydWarshallResult;
import java.util.List;

public interface IDirectedGraphAlgorithms {
    <V, D> IDijkstraResult<V> dijkstra(IDirectedIGraph<V, D> grafo, V origen);
    <V, D> IFloydWarshallResult<V> floydWarshall(IDirectedIGraph<V, D> grafo);
    <V, D> List<V> calcularClasificacionTopologica(IDirectedIGraph<V, D> grafo);
}
