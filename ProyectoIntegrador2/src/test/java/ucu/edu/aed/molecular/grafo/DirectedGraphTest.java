package ucu.edu.aed.molecular.grafo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectedGraphTest {

    private DirectedGraph<String, Integer> g;

    @BeforeEach
    void setUp() {
        g = new DirectedGraph<>();
    }

    @Test
    void agregarVerticeReturnsTrueFirstTime() {
        assertTrue(g.agregarVertice("A"));
        assertFalse(g.agregarVertice("A"));
    }

    @Test
    void agregarAristaCreaVerticesImplicitamente() {
        g.agregarArista("A", "B", 1);
        assertTrue(g.vertices().contains("A"));
        assertTrue(g.vertices().contains("B"));
    }

    @Test
    void successorsYPredecessors() {
        g.agregarArista("A", "B", 1);
        g.agregarArista("A", "C", 2);
        assertEquals(2, g.successors(g.construirComparable("A")).size());
        assertTrue(g.predecessors(g.construirComparable("B")).contains("A"));
        assertTrue(g.predecessors(g.construirComparable("A")).isEmpty());
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
    void buscarVertice() {
        g.agregarVertice("X");
        assertEquals("X", g.buscarVertice(v -> v.equals("X") ? 0 : 1));
        assertNull(g.buscarVertice(v -> v.equals("Z") ? 0 : 1));
    }

    @Test
    void eliminarArista() {
        g.agregarArista("A", "B", 1);
        assertTrue(g.eliminarArista(g.construirComparable("A"), g.construirComparable("B")));
        assertFalse(g.existeArista(g.construirComparable("A"), g.construirComparable("B")));
    }

    @Test
    void removerVertice() {
        g.agregarArista("A", "B", 1);
        g.agregarArista("C", "A", 1);
        g.removerVertice(g.construirComparable("A"));
        assertFalse(g.vertices().contains("A"));
        assertTrue(g.aristas().isEmpty());
    }
}
