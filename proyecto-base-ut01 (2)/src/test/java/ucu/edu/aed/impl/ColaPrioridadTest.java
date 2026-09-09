package ucu.edu.aed.impl;

import junit.framework.TestCase;
import java.util.NoSuchElementException;

public class ColaPrioridadTest extends TestCase {

    private ColaPrioridad<String> cola;

    @Override
    protected void setUp() {
        cola = new ColaPrioridad<>();
    }

    // --- Estructura vacía ---

    public void testEsVacioAlCrear() {
        assertTrue(cola.esVacio());
        assertEquals(0, cola.tamaño());
    }

    public void testDesencolarVaciaLanzaExcepcion() {
        try {
            cola.desencolarMasPrioritario();
            fail("Debería lanzar NoSuchElementException");
        } catch (NoSuchElementException e) {
            // esperado
        }
    }

    public void testVerMasPrioritarioVaciaLanzaExcepcion() {
        try {
            cola.verMasPrioritario();
            fail("Debería lanzar NoSuchElementException");
        } catch (NoSuchElementException e) {
            // esperado
        }
    }

    // --- Un solo elemento ---

    public void testEncolarYDesencolarUnElemento() {
        cola.encolar("unico", 1);
        assertEquals("unico", cola.desencolarMasPrioritario());
        assertTrue(cola.esVacio());
    }

    public void testVerMasPrioritarioNoRemueve() {
        cola.encolar("X", 1);
        assertEquals("X", cola.verMasPrioritario());
        assertEquals(1, cola.tamaño()); // sigue estando
    }

    // --- Varios elementos — verificación de orden por prioridad ---

    public void testSalePrimeroElDeMenorNumeroDePrioridad() {
        cola.encolar("baja",    10);
        cola.encolar("alta",    1);
        cola.encolar("media",   5);

        // Debe salir en orden: alta(1), media(5), baja(10)
        assertEquals("alta",   cola.desencolarMasPrioritario());
        assertEquals("media",  cola.desencolarMasPrioritario());
        assertEquals("baja",   cola.desencolarMasPrioritario());
    }

    public void testEncolarEnOrdenInversoDebeRespetarPrioridad() {
        cola.encolar("C", 3);
        cola.encolar("B", 2);
        cola.encolar("A", 1);

        assertEquals("A", cola.desencolarMasPrioritario());
        assertEquals("B", cola.desencolarMasPrioritario());
        assertEquals("C", cola.desencolarMasPrioritario());
    }

    // --- Empate en prioridad ---

    public void testEmpateDePrioridadRespetaOrdenDeIngreso() {
        // Con igual prioridad, el primero en entrar debe salir primero
        cola.encolar("primero", 5);
        cola.encolar("segundo", 5);

        assertEquals("primero", cola.desencolarMasPrioritario());
        assertEquals("segundo", cola.desencolarMasPrioritario());
    }

    // --- verMasPrioritario ---

    public void testVerMasPrioritarioDevuelveElCorrecto() {
        cola.encolar("media", 5);
        cola.encolar("alta",  1);
        assertEquals("alta", cola.verMasPrioritario());
    }

    // --- poneEnCola heredado de TDACola (sin prioridad explícita) ---

    public void testPoneEnColaVaDetrasDeLosPrioritarios() {
        cola.encolar("prioritario", 1);
        cola.poneEnCola("sin prioridad"); // usa Integer.MAX_VALUE internamente
        assertEquals("prioritario", cola.desencolarMasPrioritario());
        assertEquals("sin prioridad", cola.desencolarMasPrioritario());
    }

    // --- Operaciones heredadas de TDALista ---

    public void testContieneElementoPresente() {
        cola.encolar("vehiculo", 2);
        assertTrue(cola.contiene("vehiculo"));
    }

    public void testTamañoCorrectoTrasVareasOperaciones() {
        cola.encolar("A", 1);
        cola.encolar("B", 2);
        cola.encolar("C", 3);
        assertEquals(3, cola.tamaño());
        cola.desencolarMasPrioritario();
        assertEquals(2, cola.tamaño());
    }

    public void testVaciar() {
        cola.encolar("A", 1);
        cola.encolar("B", 2);
        cola.vaciar();
        assertTrue(cola.esVacio());
    }
}
