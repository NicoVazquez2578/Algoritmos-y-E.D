package ucu.edu.aed.tda.Implementaciones;

import junit.framework.TestCase;
import ucu.edu.aed.tda.TDALista;

/**
 * Pruebas para {@link Lista}, la implementación de {@link TDALista}
 * basada en nodos enlazados (lista simplemente enlazada).
 */
public class ListaTest extends TestCase {

    private Lista<Integer> lista;

    @Override
    protected void setUp() {
        lista = new Lista<>();
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

    public void testRemoverPorIndiceEnListaVaciaLanzaExcepcion() {
        try {
            lista.remover(0);
            fail("Debería lanzar IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // esperado
        }
    }

    public void testRemoverPorElementoEnListaVaciaNoRompe() {
        assertFalse(lista.remover(Integer.valueOf(1)));
    }

    public void testContieneEnListaVacia() {
        assertFalse(lista.contiene(1));
    }

    public void testIndiceDeEnListaVacia() {
        assertEquals(-1, lista.indiceDe(1));
    }

    public void testBuscarEnListaVacia() {
        assertNull(lista.buscar(n -> n > 0));
    }

    // --- Un solo elemento ---

    public void testAgregarUnElemento() {
        lista.agregar(42);
        assertFalse(lista.esVacio());
        assertEquals(1, lista.tamaño());
        assertEquals(Integer.valueOf(42), lista.obtener(0));
    }

    public void testRemoverPorIndiceUnicoElementoDejaListaVacia() {
        lista.agregar(10);
        Integer removido = lista.remover(0);
        assertEquals(Integer.valueOf(10), removido);
        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
    }

    public void testRemoverPorElementoUnicoElementoDejaListaVacia() {
        lista.agregar(10);
        assertTrue(lista.remover(Integer.valueOf(10)));
        assertTrue(lista.esVacio());
    }

    // --- Varios elementos: inserciones ---

    public void testAgregarVariosAlFinalRespetaOrden() {
        lista.agregar(1);
        lista.agregar(2);
        lista.agregar(3);
        assertEquals(3, lista.tamaño());
        assertEquals(Integer.valueOf(1), lista.obtener(0));
        assertEquals(Integer.valueOf(2), lista.obtener(1));
        assertEquals(Integer.valueOf(3), lista.obtener(2));
    }

    public void testAgregarEnPosicionCeroDesplazaCabeza() {
        lista.agregar(2);
        lista.agregar(0, 1); // inserta 1 antes de la cabeza actual
        assertEquals(2, lista.tamaño());
        assertEquals(Integer.valueOf(1), lista.obtener(0));
        assertEquals(Integer.valueOf(2), lista.obtener(1));
    }

    public void testAgregarEnPosicionMedio() {
        lista.agregar(1);
        lista.agregar(3);
        lista.agregar(1, 2); // inserta 2 entre 1 y 3
        assertEquals(Integer.valueOf(1), lista.obtener(0));
        assertEquals(Integer.valueOf(2), lista.obtener(1));
        assertEquals(Integer.valueOf(3), lista.obtener(2));
    }

    public void testAgregarEnPosicionIgualATamanioEquivaleAAgregarAlFinal() {
        lista.agregar(1);
        lista.agregar(2);
        lista.agregar(lista.tamaño(), 3); // index == tamaño
        assertEquals(3, lista.tamaño());
        assertEquals(Integer.valueOf(3), lista.obtener(2));
    }

    public void testAgregarEnPosicionInvalidaLanzaExcepcion() {
        lista.agregar(1);
        try {
            lista.agregar(5, 99);
            fail("Debería lanzar IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // esperado
        }
        try {
            lista.agregar(-1, 99);
            fail("Debería lanzar IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // esperado
        }
    }

    // --- Varios elementos: eliminaciones ---

    public void testRemoverPorIndiceCabezaActualizaCabeza() {
        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);
        Integer removido = lista.remover(0);
        assertEquals(Integer.valueOf(10), removido);
        assertEquals(2, lista.tamaño());
        assertEquals(Integer.valueOf(20), lista.obtener(0));
    }

    public void testRemoverPorIndiceDelMedio() {
        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);
        Integer removido = lista.remover(1);
        assertEquals(Integer.valueOf(20), removido);
        assertEquals(2, lista.tamaño());
        assertEquals(Integer.valueOf(10), lista.obtener(0));
        assertEquals(Integer.valueOf(30), lista.obtener(1));
    }

    public void testRemoverPorIndiceDelFinal() {
        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);
        Integer removido = lista.remover(2);
        assertEquals(Integer.valueOf(30), removido);
        assertEquals(2, lista.tamaño());
    }

    public void testRemoverPorIndiceInvalidoLanzaExcepcion() {
        lista.agregar(1);
        try {
            lista.remover(5);
            fail("Debería lanzar IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // esperado
        }
    }

    public void testRemoverPorElementoPrimeraOcurrencia() {
        lista.agregar(5);
        lista.agregar(10);
        lista.agregar(10);
        boolean resultado = lista.remover(Integer.valueOf(10));
        assertTrue(resultado);
        assertEquals(2, lista.tamaño());
        // Debe quedar una sola ocurrencia de 10
        assertTrue(lista.contiene(10));
        lista.remover(Integer.valueOf(10));
        assertFalse(lista.contiene(10));
    }

    public void testRemoverElementoQueNoExiste() {
        lista.agregar(1);
        assertFalse(lista.remover(Integer.valueOf(99)));
        assertEquals(1, lista.tamaño());
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

    public void testOrdenarNoModificaLaListaOriginal() {
        lista.agregar(3);
        lista.agregar(1);
        lista.agregar(2);
        TDALista<Integer> ordenada = lista.ordenar(Integer::compareTo);

        assertEquals(Integer.valueOf(1), ordenada.obtener(0));
        assertEquals(Integer.valueOf(2), ordenada.obtener(1));
        assertEquals(Integer.valueOf(3), ordenada.obtener(2));

        // La lista original no debe modificarse
        assertEquals(Integer.valueOf(3), lista.obtener(0));
        assertEquals(Integer.valueOf(1), lista.obtener(1));
        assertEquals(Integer.valueOf(2), lista.obtener(2));
    }

    public void testOrdenarListaVacia() {
        TDALista<Integer> ordenada = lista.ordenar(Integer::compareTo);
        assertTrue(ordenada.esVacio());
    }

    // --- Vaciar ---

    public void testVaciar() {
        lista.agregar(1);
        lista.agregar(2);
        lista.vaciar();
        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
        // Después de vaciar, debe comportarse como una lista recién creada
        try {
            lista.obtener(0);
            fail("Debería lanzar IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // esperado
        }
    }

    // --- Caso borde propio de listas enlazadas: reconstrucción tras vaciar ---

    public void testAgregarDespuesDeVaciarReconstruyeCorrectamente() {
        lista.agregar(1);
        lista.agregar(2);
        lista.vaciar();
        lista.agregar(99);
        assertEquals(1, lista.tamaño());
        assertEquals(Integer.valueOf(99), lista.obtener(0));
    }
}
