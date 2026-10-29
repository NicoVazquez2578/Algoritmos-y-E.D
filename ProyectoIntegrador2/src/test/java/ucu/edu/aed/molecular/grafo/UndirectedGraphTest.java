package ucu.edu.aed.molecular.grafo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UndirectedGraphTest {

    private UndirectedGraph<String, Integer> g;

    @BeforeEach
    void setUp() {
        g = new UndirectedGraph<>();
    }

    @Test
    void agregarAristaSimetrica() {
        g.agregarArista("A", "B", 5);
        // ambas direcciones existen en adyacencias
        assertFalse(g.adyacencias(g.construirComparable("A")).isEmpty());
        assertFalse(g.adyacencias(g.construirComparable("B")).isEmpty());
    }

    @Test
    void aristasNoDuplica() {
        g.agregarArista("A", "B", 5);
        // aristas() debe devolver 1 sola arista aunque se guarden 2 en adj
        assertEquals(1, g.aristas().size());
    }

    @Test
    void tieneCiclosSinCiclo() {
        g.agregarArista("A", "B", 1);
        g.agregarArista("B", "C", 1);
        assertFalse(g.tieneCiclos());
    }

    @Test
    void tieneCiclosConCiclo() {
        g.agregarArista("A", "B", 1);
        g.agregarArista("B", "C", 1);
        g.agregarArista("C", "A", 1);
        assertTrue(g.tieneCiclos());
    }

    @Test
    void esConexo() {
        g.agregarArista("A", "B", 1);
        g.agregarArista("B", "C", 1);
        assertTrue(g.esConexo());
    }

    @Test
    void noEsConexo() {
        g.agregarVertice("A");
        g.agregarVertice("B");
        assertFalse(g.esConexo());
    }

    @Test
    void eliminarAristaAmbosLados() {
        g.agregarArista("A", "B", 1);
        g.eliminarArista(g.construirComparable("A"), g.construirComparable("B"));
        assertTrue(g.adyacencias(g.construirComparable("A")).isEmpty());
        assertTrue(g.adyacencias(g.construirComparable("B")).isEmpty());
    }
}
