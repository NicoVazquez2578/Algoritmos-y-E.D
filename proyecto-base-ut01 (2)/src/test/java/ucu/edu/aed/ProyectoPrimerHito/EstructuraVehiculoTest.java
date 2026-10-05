package ucu.edu.aed.ProyectoPrimerHito;

import junit.framework.TestCase;
import ucu.edu.aed.tda.TDALista;

/**
 * Tests para EstructuraVehiculo — árbol n-ario de partes del vehículo.
 *
 * Estructura usada en los tests:
 *
 *   [SISTEMA]     Motor
 *     [SUBSISTEMA]  Distribución
 *       [PIEZA]       Correa
 *       [PIEZA]       Tensor
 *   [SISTEMA]     Tren delantero
 *     [PIEZA]       Amortiguador izq
 *     [PIEZA]       Amortiguador der
 */
public class EstructuraVehiculoTest extends TestCase {

    // ─── helpers ──────────────────────────────────────────────────────

    private ParteVehiculo sistema(String nombre) {
        return new ParteVehiculo(nombre, TipoParteVehiculo.SISTEMA);
    }

    private ParteVehiculo subsistema(String nombre) {
        return new ParteVehiculo(nombre, TipoParteVehiculo.SUBSISTEMA);
    }

    private ParteVehiculo pieza(String nombre) {
        return new ParteVehiculo(nombre, TipoParteVehiculo.PIEZA);
    }

    /** Arma el árbol de ejemplo descrito en el javadoc de la clase. */
    private EstructuraVehiculo crearEstructura() {
        EstructuraVehiculo ev = new EstructuraVehiculo();

        // Sistema 1: Motor
        ev.agregarSistema(sistema("Motor"));
        ev.agregarSubparte("Motor", subsistema("Distribución"));
        ev.agregarSubparte("Distribución", pieza("Correa"));
        ev.agregarSubparte("Distribución", pieza("Tensor"));

        // Sistema 2: Tren delantero (menos profundo)
        ev.agregarSistema(sistema("Tren delantero"));
        ev.agregarSubparte("Tren delantero", pieza("Amortiguador izq"));
        ev.agregarSubparte("Tren delantero", pieza("Amortiguador der"));

        return ev;
    }

    // ─── esVacio / cantidadPartes ─────────────────────────────────────

    /** Una estructura recién creada está vacía. */
    public void testEsVacioInicial() {
        EstructuraVehiculo ev = new EstructuraVehiculo();
        assertTrue(ev.esVacio());
        assertEquals(0, ev.cantidadPartes());
    }

    /** Después de agregar sistemas deja de estar vacía. */
    public void testNoEsVacioConSistemas() {
        EstructuraVehiculo ev = crearEstructura();
        assertFalse(ev.esVacio());
        // Motor: 1 sistema + 1 subsistema + 2 piezas = 4
        // Tren: 1 sistema + 2 piezas = 3
        // Total = 7
        assertEquals(7, ev.cantidadPartes());
    }

    // ─── agregarSubparte ─────────────────────────────────────────────

    /** Agregar subparte con padre inexistente debe lanzar excepción. */
    public void testAgregarSubpartePadreInexistente() {
        EstructuraVehiculo ev = new EstructuraVehiculo();
        ev.agregarSistema(sistema("Motor"));
        try {
            ev.agregarSubparte("Transmision", pieza("Embrague")); // "Transmision" no existe
            fail("Debería lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // correcto
        }
    }

    // ─── buscarParte ──────────────────────────────────────────────────

    /** buscarParte encuentra un sistema (primer nivel). */
    public void testBuscarParteSistema() {
        EstructuraVehiculo ev = crearEstructura();
        ParteVehiculo motor = ev.buscarParte("Motor");
        assertNotNull(motor);
        assertEquals("Motor", motor.getNombre());
        assertEquals(TipoParteVehiculo.SISTEMA, motor.getTipo());
    }

    /** buscarParte encuentra una pieza en lo profundo del árbol. */
    public void testBuscarPartePiezaProfunda() {
        EstructuraVehiculo ev = crearEstructura();
        ParteVehiculo correa = ev.buscarParte("Correa");
        assertNotNull(correa);
        assertEquals(TipoParteVehiculo.PIEZA, correa.getTipo());
    }

    /** buscarParte devuelve null si la parte no existe. */
    public void testBuscarParteInexistente() {
        EstructuraVehiculo ev = crearEstructura();
        assertNull(ev.buscarParte("Carburador"));
    }

    // ─── listarPorNiveles (BFS) ───────────────────────────────────────

    /**
     * El BFS debe devolver los nodos nivel por nivel.
     * Nivel 0: Motor, Tren delantero (los dos sistemas)
     * Nivel 1: Distribución, Amortiguador izq, Amortiguador der
     * Nivel 2: Correa, Tensor
     *
     * Verificamos que Motor y Tren estén antes que Distribución,
     * y que Distribución esté antes que Correa/Tensor.
     */
    public void testListarPorNivelesBFS() {
        EstructuraVehiculo ev = crearEstructura();
        TDALista<ParteVehiculo> lista = ev.listarPorNiveles();

        assertEquals(7, lista.tamaño());

        // Los dos sistemas van al inicio (nivel 0)
        assertEquals("Motor",          lista.obtener(0).getNombre());
        assertEquals("Tren delantero", lista.obtener(1).getNombre());

        // Distribución es hijo de Motor (nivel 1), antes que las piezas de Tren
        assertEquals("Distribución",       lista.obtener(2).getNombre());
        assertEquals("Amortiguador izq",   lista.obtener(3).getNombre());
        assertEquals("Amortiguador der",   lista.obtener(4).getNombre());

        // Correa y Tensor son hijos de Distribución (nivel 2)
        assertEquals("Correa",  lista.obtener(5).getNombre());
        assertEquals("Tensor",  lista.obtener(6).getNombre());
    }

    /** listarPorNiveles en una estructura vacía devuelve lista vacía. */
    public void testListarPorNivelesVacio() {
        EstructuraVehiculo ev = new EstructuraVehiculo();
        TDALista<ParteVehiculo> lista = ev.listarPorNiveles();
        assertEquals(0, lista.tamaño());
    }

    // ─── solo un sistema ─────────────────────────────────────────────

    /** Con un único sistema y sin hijos, BFS devuelve solo ese sistema. */
    public void testListarPorNivelesUnSistema() {
        EstructuraVehiculo ev = new EstructuraVehiculo();
        ev.agregarSistema(sistema("Motor"));
        TDALista<ParteVehiculo> lista = ev.listarPorNiveles();
        assertEquals(1, lista.tamaño());
        assertEquals("Motor", lista.obtener(0).getNombre());
    }

    // ─── profundidad variable ─────────────────────────────────────────

    /**
     * El árbol no impone profundidad uniforme: una rama puede tener 4 niveles
     * mientras otra tiene 2. Verificamos que cantidadPartes sea correcto.
     */
    public void testProfundidadVariableEntreSistemas() {
        EstructuraVehiculo ev = new EstructuraVehiculo();

        // Rama profunda: Motor → Culata → Válvula → Muelle
        ev.agregarSistema(sistema("Motor"));
        ev.agregarSubparte("Motor", subsistema("Culata"));
        ev.agregarSubparte("Culata", subsistema("Válvula"));
        ev.agregarSubparte("Válvula", pieza("Muelle"));

        // Rama corta: Tren → Amortiguador
        ev.agregarSistema(sistema("Tren"));
        ev.agregarSubparte("Tren", pieza("Amortiguador"));

        // Motor(1) + Culata(1) + Válvula(1) + Muelle(1) + Tren(1) + Amortiguador(1) = 6
        assertEquals(6, ev.cantidadPartes());
    }
}
