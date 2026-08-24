package ucu.edu.aed.impl;

import junit.framework.TestCase;
import ucu.edu.aed.tda.TDALista;

/**
 * Pruebas para {@link ListaCircularDoble}, la implementación de
 * {@link TDALista} basada en nodos doblemente enlazados donde el
 * último nodo se conecta de vuelta a la cabeza.
 */
public class ListaCircularDobleTest extends TestCase {

    private ListaCircularDoble<Integer> lista;

    @Override
    protected void setUp() {
        lista = new ListaCircularDoble<>();
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

    public void testAgregarUnElementoFormaAutobucleEnAmbosSentidos() {
        lista.agregar(42);
        assertFalse(lista.esVacio());
        assertEquals(1, lista.tamaño());
        assertEquals(Integer.valueOf(42), lista.obtener(0));
        // Si "anterior" no quedó también apuntando al propio nodo,
        // insertar antes de la cabeza (índice 0) rompería el círculo.
        lista.agregar(0, 41);
        assertEquals(Integer.valueOf(41), lista.obtener(0));
        assertEquals(Integer.valueOf(42), lista.obtener(1));
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

    public void testAgregarDespuesDeVaciarConUnElementoFormaNuevoAutobucle() {
        lista.agregar(1);
        lista.remover(0);
        lista.agregar(99);
        lista.agregar(100);
        assertEquals(2, lista.tamaño());
        assertEquals(Integer.valueOf(99), lista.obtener(0));
        assertEquals(Integer.valueOf(100), lista.obtener(1));
        // Confirma que "anterior" también se reconstruyó bien:
        // insertar antes de la cabeza debe seguir funcionando.
        lista.agregar(0, 98);
        assertEquals(Integer.valueOf(98), lista.obtener(0));
    }

    // --- Varios elementos: inserciones ---

    public void testAgregarVariosAlFinalRespetaOrden() {
        for (int i = 1; i <= 5; i++) {
            lista.agregar(i);
        }
        assertEquals(5, lista.tamaño());
        for (int i = 0; i < 5; i++) {
            assertEquals(Integer.valueOf(i + 1), lista.obtener(i));
        }
    }

    public void testAgregarEnPosicionCeroActualizaCabezaYMantieneElCirculo() {
        lista.agregar(2);
        lista.agregar(3);
        lista.agregar(0, 1); // 1, 2, 3
        assertEquals(3, lista.tamaño());
        assertEquals(Integer.valueOf(1), lista.obtener(0));
        assertEquals(Integer.valueOf(2), lista.obtener(1));
        assertEquals(Integer.valueOf(3), lista.obtener(2));
        // Si cabeza.anterior no quedó apuntando al último nodo real,
        // este agregar (que usa cabeza.anterior como "cola") falla.
        lista.agregar(4);
        assertEquals(4, lista.tamaño());
        assertEquals(Integer.valueOf(4), lista.obtener(3));
    }

    public void testAgregarEnPosicionMedio() {
        lista.agregar(1);
        lista.agregar(3);
        lista.agregar(1, 2);
        assertEquals(Integer.valueOf(1), lista.obtener(0));
        assertEquals(Integer.valueOf(2), lista.obtener(1));
        assertEquals(Integer.valueOf(3), lista.obtener(2));
    }

    public void testAgregarEnPosicionIgualATamanioEquivaleAAgregarAlFinal() {
        lista.agregar(1);
        lista.agregar(2);
        lista.agregar(lista.tamaño(), 3);
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

    public void testObtenerEnIndicesCercanosAlPrincipioYAlFinal() {
        // Lista larga: ejercita las dos ramas de nodoEn (desde
        // cabeza hacia adelante, y desde cabeza.anterior hacia atrás).
        for (int i = 0; i < 10; i++) {
            lista.agregar(i);
        }
        assertEquals(Integer.valueOf(1), lista.obtener(1));
        assertEquals(Integer.valueOf(8), lista.obtener(8));
        assertEquals(Integer.valueOf(9), lista.obtener(9));
    }

    // --- Varios elementos: eliminaciones ---

    public void testRemoverPorIndiceCabezaActualizaCabezaYMantieneElCirculo() {
        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);
        Integer removido = lista.remover(0);
        assertEquals(Integer.valueOf(10), removido);
        assertEquals(2, lista.tamaño());
        assertEquals(Integer.valueOf(20), lista.obtener(0));
        lista.agregar(40);
        assertEquals(Integer.valueOf(40), lista.obtener(2));
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

    public void testRemoverUltimoElementoPermiteSeguirAgregandoCorrectamente() {
        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);
        Integer removido = lista.remover(2); // remueve cabeza.anterior (la "cola")
        assertEquals(Integer.valueOf(30), removido);
        assertEquals(2, lista.tamaño());
        lista.agregar(40);
        assertEquals(3, lista.tamaño());
        assertEquals(Integer.valueOf(40), lista.obtener(2));
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
        assertTrue(lista.remover(Integer.valueOf(10)));
        assertEquals(2, lista.tamaño());
        assertTrue(lista.contiene(10));
        lista.remover(Integer.valueOf(10));
        assertFalse(lista.contiene(10));
    }

    public void testRemoverPorElementoQueEsLaCabezaActualizaCabeza() {
        lista.agregar(1);
        lista.agregar(2);
        lista.agregar(3);
        assertTrue(lista.remover(Integer.valueOf(1)));
        assertEquals(Integer.valueOf(2), lista.obtener(0));
        assertEquals(Integer.valueOf(3), lista.obtener(1));
    }

    public void testRemoverElementoQueNoExiste() {
        lista.agregar(1);
        assertFalse(lista.remover(Integer.valueOf(99)));
        assertEquals(1, lista.tamaño());
    }

    // --- Búsquedas (también validan que el recorrido esté acotado
    //     por tamaño y no entre en ciclo infinito) ---

    public void testContieneElementoPresenteYAusente() {
        for (int i = 0; i < 6; i++) {
            lista.agregar(i);
        }
        assertTrue(lista.contiene(5));
        assertFalse(lista.contiene(99));
    }

    public void testIndiceDeElementoPresenteYAusente() {
        lista.agregar(10);
        lista.agregar(20);
        assertEquals(1, lista.indiceDe(20));
        assertEquals(-1, lista.indiceDe(99));
    }

    public void testBuscarConCriterioYSinResultado() {
        lista.agregar(3);
        lista.agregar(8);
        lista.agregar(15);
        assertEquals(Integer.valueOf(8), lista.buscar(n -> n > 5));
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

        assertEquals(Integer.valueOf(3), lista.obtener(0));
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
        try {
            lista.obtener(0);
            fail("Debería lanzar IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // esperado
        }
    }
}
