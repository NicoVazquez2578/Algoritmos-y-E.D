package ucu.edu.aed.molecular.grafo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucu.edu.aed.molecular.grafo.algoritmos.DirectedGraphAlgorithms;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;
import ucu.edu.aed.tda.grafo.model.result.IDijkstraResult;
import ucu.edu.aed.tda.grafo.model.result.IFloydWarshallResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DirectedGraphAlgorithmsTest {

    private DirectedGraph<String, WeightedEdge> g;
    private DirectedGraphAlgorithms alg;

    @BeforeEach
    void setUp() {
        alg = new DirectedGraphAlgorithms();
        g = new DirectedGraph<>();
        g.agregarArista("A", "B", new WeightedEdge(1));
        g.agregarArista("A", "C", new WeightedEdge(4));
        g.agregarArista("B", "C", new WeightedEdge(2));
        g.agregarArista("B", "D", new WeightedEdge(5));
        g.agregarArista("C", "D", new WeightedEdge(1));
    }

    @Test
    void dijkstraCostos() {
        IDijkstraResult<String> r = alg.dijkstra(g.construirComparable("A"), g);
        assertEquals(0.0, r.getCost("A"));
        assertEquals(1.0, r.getCost("B"));
        assertEquals(3.0, r.getCost("C"));
        assertEquals(4.0, r.getCost("D"));
    }

    @Test
    void dijkstraCamino() {
        IDijkstraResult<String> r = alg.dijkstra(g.construirComparable("A"), g);
        assertEquals(List.of("A", "B", "C", "D"), r.getPath("D"));
    }

    @Test
    void floydCostoYCamino() {
        IFloydWarshallResult<String> fw = alg.floyd(g);
        assertEquals(4.0, fw.getCost("A", "D"));
        assertTrue(fw.connected("A", "D"));
        assertFalse(fw.connected("D", "A"));
    }

    @Test
    void ordenTopologico() {
        DirectedGraph<String, WeightedEdge> dag = new DirectedGraph<>();
        dag.agregarArista("A", "B", new WeightedEdge(1));
        dag.agregarArista("B", "C", new WeightedEdge(1));
        List<String> orden = alg.calcularClasificacionTopologica(dag);
        assertTrue(orden.indexOf("A") < orden.indexOf("B"));
        assertTrue(orden.indexOf("B") < orden.indexOf("C"));
    }

    @Test
    void ordenTopologicoConCicloLanzaExcepcion() {
        DirectedGraph<String, WeightedEdge> conCiclo = new DirectedGraph<>();
        conCiclo.agregarArista("A", "B", new WeightedEdge(1));
        conCiclo.agregarArista("B", "A", new WeightedEdge(1));
        assertThrows(IllegalStateException.class, () -> alg.calcularClasificacionTopologica(conCiclo));
    }

    @Test
    void recorridoEnProfundidad() {
        List<String> visitados = new ArrayList<>();
        alg.recorridoEnProfundidad(g, g.construirComparable("A"), visitados::add);
        assertEquals("A", visitados.get(0));
        assertEquals(4, visitados.size());
    }

    @Test
    void recorridoEnAmplitud() {
        List<String> visitados = new ArrayList<>();
        alg.recorridoEnAmplitud(g, g.construirComparable("A"), visitados::add);
        assertEquals("A", visitados.get(0));
        assertEquals(4, visitados.size());
    }

    @Test
    void centroYExcentricidad() {
        // grafo pequeño y simétrico para verificar
        DirectedGraph<String, WeightedEdge> c = new DirectedGraph<>();
        c.agregarArista("A", "B", new WeightedEdge(1));
        c.agregarArista("B", "C", new WeightedEdge(1));
        c.agregarArista("C", "A", new WeightedEdge(1));
        String centro = alg.obtenerCentroGrafo(c);
        assertNotNull(centro);
        double ecc = alg.obtenerExcentricidad(c, c.construirComparable(centro));
        assertTrue(ecc > 0);
    }
}
