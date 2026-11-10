import ucu.edu.aed.tda.grafo.IUndirectedGraph;

public class UndirectedGraphAlgorithm implements IUndirectedGraphAlgorithm {
    @Override
    public <V, D extends WeightedEdge> IUndirectedGraph<V, D> kruskal(IUndirectedGraph<V, D> graph) {
        return null;
    }

    @Override
    public <V, D extends WeightedEdge> IUndirectedGraph<V, D> prim(IUndirectedGraph<V, D> graph, Comparable<V> source) {
        Set<V> U = new HashSet<>(graph.vertices());
        Set<V> V = new HashSet<>();

        U.add((V) source);
        V.remove(source);

        IUndirectedGraphAlgorithm<V, D> m = new UndirectedGraphAlgorithm<>();
        double costo = 0; 

        for(V vertice : graph.vertices()) {
            m.agregarVertice(vertice);
        }

        while(!v.isEmpty()){
            Edge<V, D> arista = searchMinEdge(graph, u, v);
            
        }
        
        return null;
    }

    @Override
    public <V, D extends WeightedEdge> Edge<V, D> searchMinEdge(IUndirectedGraph<V, D> graph, Collection<V> U, Collection<V> V) {
        return null;
    }

    @Override
    public <V, D> void bea(IUndirectedGraph<V, D> graph, Consumer<V> consumer) {

    }
    

}
