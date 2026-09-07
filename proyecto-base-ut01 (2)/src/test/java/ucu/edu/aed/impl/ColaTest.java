package ucu.edu.aed.impl;

import junit.framework.TestCase;
import java.util.NoSuchElementException;

public class ColaTest extends TestCase {

    private Cola<String> cola;

    @Override
    protected void setUp() {
        cola = new Cola<>();
    }

    // --- Estructura vacía ---

    public void testEsVacioAlCrear() {
        assertTrue(cola.esVacio());
        assertEquals(0, cola.tamaño());
    }

    public void testFrenteEnColaVaciaLanzaExcepcion() {
        try {
            cola.frente();
            fail("Debería lanzar NoSuchElementException");
        } catch (NoSuchElementException e) {
            // esperado
        }
    }

    public void testQuitaDeColaVaciaLanzaExcepcion() {
        try {
            cola.quitaDeCola();
            fail("Debería lanzar NoSuchElementException");
        } catch (NoSuchElementException e) {
            // esperado
        }
    }

    // --- Un solo elemento ---

    public void testPoneYQuitaUnElemento() {
        cola.poneEnCola("A");
        assertFalse(cola.esVacio());
        assertEquals("A", cola.quitaDeCola());
        assertTrue(cola.esVacio());
    }

    public void testFrenteNoRemoveElElemento() {
        cola.poneEnCola("X");
        assertEquals("X", cola.frente());
        assertEquals(1, cola.tamaño()); // sigue estando
    }

    // --- Varios elementos — verificación FIFO ---

    public void testOrdenFIFO() {
        cola.poneEnCola("primero");
        cola.poneEnCola("segundo");
        cola.poneEnCola("tercero");

        assertEquals("primero", cola.quitaDeCola());
        assertEquals("segundo", cola.quitaDeCola());
        assertEquals("tercero", cola.quitaDeCola());
        assertTrue(cola.esVacio());
    }

    public void testFrenteEsSiempreElPrimeroEnEntrar() {
        cola.poneEnCola("A");
        cola.poneEnCola("B");
        assertEquals("A", cola.frente()); // B entró después, no debe ser el frente
    }

    // --- Operaciones heredadas de TDALista ---

    public void testContiene() {
        cola.poneEnCola("hola");
        assertTrue(cola.contiene("hola"));
        assertFalse(cola.contiene("adios"));
    }

    public void testTamaño() {
        cola.poneEnCola("a");
        cola.poneEnCola("b");
        assertEquals(2, cola.tamaño());
    }

    public void testVaciar() {
        cola.poneEnCola("a");
        cola.poneEnCola("b");
        cola.vaciar();
        assertTrue(cola.esVacio());
    }

    // --- Caso borde: encolar y desencolar intercalados ---

    public void testEncolarDesencolarIntercalados() {
        cola.poneEnCola("1");
        cola.poneEnCola("2");
        assertEquals("1", cola.quitaDeCola());
        cola.poneEnCola("3");
        assertEquals("2", cola.quitaDeCola());
        assertEquals("3", cola.quitaDeCola());
        assertTrue(cola.esVacio());
    }
}
