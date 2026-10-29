package ucu.edu.aed.molecular.grafo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucu.edu.aed.molecular.grafo.algoritmos.UndirectedGraphAlgorithms;
import ucu.edu.aed.tda.grafo.IUndirectedGraph;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UndirectedGraphAlgorithmsTest {

    private UndirectedGraph<String, WeightedEdge> g;
    private UndirectedGraphAlgorithms alg;

    @BeforeEach
    void setUp() {
        alg = new UndirectedGraphAlgorithms();
        g = new UndirectedGraph<>();
        g.agregarArista("A", "B", new WeightedEdge(4));
        g.agregarArista("A", "C", new WeightedEdge(2));
        g.agregarArista("B", "C", new WeightedEdge(1));
        g.agregarArista("B", "D", new WeightedEdge(5));
        g.agregarArista("C", "D", new WeightedEdge(8));
    }

    @Test
    void kruskalDevuelveMSTConN1Aristas() {
        IUndirectedGraph<String, WeightedEdge> mst = alg.kruskal(g);
        // MST de n vertices tiene n-1 aristas
        assertEquals(g.cantidadDeVertices() - 1, mst.aristas().size());
    }

    @Test
    void kruskalEsConexo() {
        IUndirectedGraph<String, WeightedEdge> mst = alg.kruskal(g);
        assertTrue(mst.esConexo());
    }

    @Test
    void primDevuelveMSTConN1Aristas() {
        IUndirectedGraph<String, WeightedEdge> mst = alg.prim(g, g.construirComparable("A"));
        assertEquals(g.cantidadDeVertices() - 1, mst.aristas().size());
    }

    @Test
    void primEsConexo() {
        IUndirectedGraph<String, WeightedEdge> mst = alg.prim(g, g.construirComparable("A"));
        assertTrue(mst.esConexo());
    }

    @Test
    void beaVisitaTodosLosVertices() {
        List<String> visitados = new ArrayList<>();
        alg.bea(g, visitados::add);
        assertEquals(g.cantidadDeVertices(), visitados.size());
    }

    @Test
    void puntosDeArticulacion() {
        // grafo lineal A-B-C: B es punto de articulacion
        UndirectedGraph<String, WeightedEdge> lineal = new UndirectedGraph<>();
        lineal.agregarArista("A", "B", new WeightedEdge(1));
        lineal.agregarArista("B", "C", new WeightedEdge(1));
        Set<String> ap = alg.puntosDeArticulacion(lineal);
        assertTrue(ap.contains("B"));
        assertFalse(ap.contains("A"));
        assertFalse(ap.contains("C"));
    }

    @Test
    void puntosDeArticulacionGrafoSinAP() {
        // triangulo: ningun vertice es AP
        UndirectedGraph<String, WeightedEdge> triangulo = new UndirectedGraph<>();
        triangulo.agregarArista("A", "B", new WeightedEdge(1));
        triangulo.agregarArista("B", "C", new WeightedEdge(1));
        triangulo.agregarArista("C", "A", new WeightedEdge(1));
        Set<String> ap = alg.puntosDeArticulacion(triangulo);
        assertTrue(ap.isEmpty());
    }
}
