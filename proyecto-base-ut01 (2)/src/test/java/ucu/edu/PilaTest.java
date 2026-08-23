package ucu.edu;

import junit.framework.TestCase;
import ucu.edu.aed.impl.Pila;
import java.util.NoSuchElementException;

public class PilaTest extends TestCase {

    public void testPilaVacia() {
        Pila<Integer> pila = new Pila<>();
        assertTrue(pila.esVacio());
        assertEquals(0, pila.tamaño());
    }

    public void testMeteYTope() {
        Pila<String> pila = new Pila<>();
        pila.mete("Primer Elemento");
        pila.mete("Segundo Elemento");

        assertEquals("Segundo Elemento", pila.tope());
        assertEquals(2, pila.tamaño());
        assertFalse(pila.esVacio());
    }

    public void testSaca() {
        Pila<Integer> pila = new Pila<>();
        pila.mete(10);
        pila.mete(20);
        pila.mete(30);

        assertEquals(Integer.valueOf(30), pila.saca());
        assertEquals(Integer.valueOf(20), pila.saca());
        assertEquals(Integer.valueOf(10), pila.saca());
        assertTrue(pila.esVacio());
    }

    public void testExcepcionTopePilaVacia() {
        Pila<Integer> pila = new Pila<>();
        try {
            pila.tope();
            fail("Debería haber lanzado NoSuchElementException");
        } catch (NoSuchElementException e) {
            // Excepción esperada
        }
    }

    public void testExcepcionSacaPilaVacia() {
        Pila<Integer> pila = new Pila<>();
        try {
            pila.saca();
            fail("Debería haber lanzado NoSuchElementException");
        } catch (NoSuchElementException e) {
            // Excepción esperada
        }
    }
}