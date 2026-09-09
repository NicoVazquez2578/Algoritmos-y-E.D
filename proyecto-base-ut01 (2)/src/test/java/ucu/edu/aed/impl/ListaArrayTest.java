package ucu.edu.aed.impl;

import junit.framework.TestCase;
import ucu.edu.aed.tda.TDALista;

public class ListaArrayTest extends TestCase {

    private ListaArray<Integer> lista;

    @Override
    protected void setUp() {
        lista = new ListaArray<>();
    }

    // --- Estructura vacía ---

    public void testEsVacioAlCrear() {
        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
    }

    public void testObtenerEnListaVaciaLanzaExcepcion() {
        try {
            lista.obtener(0);
            fail("Debería lanzar IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // esperado
        }
    }

    public void testRemoverEnListaVaciaLanzaExcepcion() {
        try {
            lista.remover(0);
            fail("Debería lanzar IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // esperado
        }
    }

    // --- Un solo elemento ---

    public void testAgregarUnElemento() {
        lista.agregar(42);
        assertFalse(lista.esVacio());
        assertEquals(1, lista.tamaño());
        assertEquals(Integer.valueOf(42), lista.obtener(0));
    }

    public void testRemoverUnicoElemento() {
        lista.agregar(10);
        Integer removido = lista.remover(0);
        assertEquals(Integer.valueOf(10), removido);
        assertTrue(lista.esVacio());
    }

    // --- Varios elementos ---

    public void testAgregarVariosAlFinal() {
        lista.agregar(1);
        lista.agregar(2);
        lista.agregar(3);
        assertEquals(3, lista.tamaño());
        assertEquals(Integer.valueOf(1), lista.obtener(0));
        assertEquals(Integer.valueOf(2), lista.obtener(1));
        assertEquals(Integer.valueOf(3), lista.obtener(2));
    }

    public void testAgregarEnPosicionMedio() {
        lista.agregar(1);
        lista.agregar(3);
        lista.agregar(1, 2); // inserta 2 entre 1 y 3
        assertEquals(Integer.valueOf(1), lista.obtener(0));
        assertEquals(Integer.valueOf(2), lista.obtener(1));
        assertEquals(Integer.valueOf(3), lista.obtener(2));
    }

    public void testAgregarEnPosicionCero() {
        lista.agregar(2);
        lista.agregar(0, 1); // inserta 1 al inicio
        assertEquals(Integer.valueOf(1), lista.obtener(0));
        assertEquals(Integer.valueOf(2), lista.obtener(1));
    }

    public void testRemoverPorIndiceDelMedio() {
        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);
        Integer removido = lista.remover(1);
        assertEquals(Integer.valueOf(20), removido);
        assertEquals(2, lista.tamaño());
        assertEquals(Integer.valueOf(30), lista.obtener(1));
    }

    public void testRemoverPorElemento() {
        lista.agregar(5);
        lista.agregar(10);
        lista.agregar(15);
        boolean resultado = lista.remover(Integer.valueOf(10));
        assertTrue(resultado);
        assertEquals(2, lista.tamaño());
        assertFalse(lista.contiene(10));
    }

    public void testRemoverElementoQueNoExiste() {
        lista.agregar(1);
        assertFalse(lista.remover(Integer.valueOf(99)));
    }

    // --- Búsquedas ---

    public void testContieneElementoPresente() {
        lista.agregar(7);
        assertTrue(lista.contiene(7));
    }

    public void testContieneElementoAusente() {
        lista.agregar(7);
        assertFalse(lista.contiene(99));
    }

    public void testIndiceDeElementoPresente() {
        lista.agregar(10);
        lista.agregar(20);
        assertEquals(1, lista.indiceDe(20));
    }

    public void testIndiceDeElementoAusente() {
        lista.agregar(10);
        assertEquals(-1, lista.indiceDe(99));
    }

    public void testBuscarConCriterio() {
        lista.agregar(3);
        lista.agregar(8);
        lista.agregar(15);
        Integer resultado = lista.buscar(n -> n > 5);
        assertEquals(Integer.valueOf(8), resultado);
    }

    public void testBuscarSinResultado() {
        lista.agregar(1);
        lista.agregar(2);
        assertNull(lista.buscar(n -> n > 100));
    }

    // --- Ordenar ---

    public void testOrdenar() {
        lista.agregar(3);
        lista.agregar(1);
        lista.agregar(2);
        TDALista<Integer> ordenada = lista.ordenar(Integer::compareTo);
        assertEquals(Integer.valueOf(1), ordenada.obtener(0));
        assertEquals(Integer.valueOf(2), ordenada.obtener(1));
        assertEquals(Integer.valueOf(3), ordenada.obtener(2));
        // La lista original no debe modificarse
        assertEquals(Integer.valueOf(3), lista.obtener(0));
    }

    // --- Vaciar ---

    public void testVaciar() {
        lista.agregar(1);
        lista.agregar(2);
        lista.vaciar();
        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
    }

    // --- Expansión del arreglo ---

    public void testExpansionAutomatica() {
        // Agrega más de 10 elementos para forzar la expansión del arreglo interno
        for (int i = 0; i < 20; i++) lista.agregar(i);
        assertEquals(20, lista.tamaño());
        assertEquals(Integer.valueOf(19), lista.obtener(19));
    }
}
