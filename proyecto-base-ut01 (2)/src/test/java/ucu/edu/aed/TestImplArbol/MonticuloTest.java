package ucu.edu.aed.TestImplArbol;

package ucu.edu.aed.Arboles.Impl.Monticulo;

import junit.framework.TestCase;

import java.util.NoSuchElementException;

public class MonticuloTest extends TestCase {

    private Monticulo<Integer> monticulo;

    protected void setUp() {
        monticulo = new Monticulo<>();
    }

    public void testMonticuloVacio_esVacioDevuelveTrue() {
        assertTrue(monticulo.esVacio());
        assertEquals(0, monticulo.cantidadElementos());
    }

    public void testMonticuloVacio_extraerMinimoLanzaExcepcion() {
        try {
            monticulo.extraerMinimo();
            fail("Se esperaba NoSuchElementException");
        } catch (NoSuchElementException e) {
            // esperado
        }
    }

    public void testMonticuloVacio_verMinimoLanzaExcepcion() {
        try {
            monticulo.verMinimo();
            fail("Se esperaba NoSuchElementException");
        } catch (NoSuchElementException e) {
            // esperado
        }
    }

    public void testUnElemento() {
        monticulo.insertar(10);
        assertFalse(monticulo.esVacio());
        assertEquals(1, monticulo.cantidadElementos());
        assertEquals(Integer.valueOf(10), monticulo.verMinimo());
    }

    public void testUnElemento_extraerloDejaElMonticuloVacio() {
        monticulo.insertar(10);
        assertEquals(Integer.valueOf(10), monticulo.extraerMinimo());
        assertTrue(monticulo.esVacio());
    }

    public void testInsertar_rechazaNull() {
        assertFalse(monticulo.insertar(null));
        assertTrue(monticulo.esVacio());
    }

    public void testExtraerMinimo_siempreDevuelveElMenorPrimero() {
        int[] valores = {50, 20, 80, 10, 40, 70, 30};
        for (int v : valores) {
            monticulo.insertar(v);
        }

        int anterior = Integer.MIN_VALUE;
        int cantidad = 0;
        while (!monticulo.esVacio()) {
            int actual = monticulo.extraerMinimo();
            assertTrue(actual >= anterior);
            anterior = actual;
            cantidad++;
        }
        assertEquals(valores.length, cantidad);
    }

    public void testVerMinimo_noQuitaElElemento() {
        monticulo.insertar(30);
        monticulo.insertar(10);
        monticulo.insertar(20);

        assertEquals(Integer.valueOf(10), monticulo.verMinimo());
        assertEquals(3, monticulo.cantidadElementos());
        assertEquals(Integer.valueOf(10), monticulo.verMinimo());
    }

    public void testInsertar_permiteDuplicados() {
        monticulo.insertar(10);
        monticulo.insertar(10);
        assertEquals(2, monticulo.cantidadElementos());
        assertEquals(Integer.valueOf(10), monticulo.extraerMinimo());
        assertEquals(Integer.valueOf(10), monticulo.extraerMinimo());
        assertTrue(monticulo.esVacio());
    }

    public void testInsertar_muchosElementos_mantieneLaPropiedadDeHeap() {
        for (int i = 100; i >= 1; i--) {
            monticulo.insertar(i);
        }
        assertEquals(100, monticulo.cantidadElementos());
        for (int i = 1; i <= 100; i++) {
            assertEquals(Integer.valueOf(i), monticulo.extraerMinimo());
        }
        assertTrue(monticulo.esVacio());
    }
}
