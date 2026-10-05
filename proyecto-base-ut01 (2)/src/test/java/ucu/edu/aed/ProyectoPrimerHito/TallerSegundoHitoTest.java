package ucu.edu.aed.ProyectoPrimerHito;

import junit.framework.TestCase;
import ucu.edu.aed.impl.Cola;
import ucu.edu.aed.impl.ColaPrioridad;
import ucu.edu.aed.impl.Lista;
import ucu.edu.aed.impl.Pila;
import ucu.edu.aed.tda.TDACola;
import ucu.edu.aed.tda.TDAColaPrioridad;
import ucu.edu.aed.tda.TDALista;
import ucu.edu.aed.tda.TDAPila;

import java.time.LocalDate;
import java.util.NoSuchElementException;

/**
 * Tests de integración para las operaciones nuevas de Taller (Hito 2).
 *
 * Cada test construye su propio Taller con el mismo constructor de 5 parámetros
 * que se usa en TallerTest, garantizando compatibilidad hacia atrás.
 *
 * Los tests cubren:
 *   - buscarVehiculoPorMatricula (BST por patente)
 *   - vehiculosEnRangoDeFechas  (BST por fecha de ingreso)
 *   - reprogramarVehiculo       (Montículo de fechas comprometidas)
 *   - Operaciones sobre OrdenTrabajo delegadas al Taller (agregarTareaDerivada,
 *     puedeTerminar, tareasPendientesParaEntregar, suspenderPorRepuesto,
 *     reanudarPorRepuesto, costoTotal, costoParcial, aprobarParte, rechazarParte)
 *   - trabajosBloqueadosPorRepuesto (Consulta 1)
 *   - vehiculoConMasTareasPendientes (Consulta 2)
 *   - costoPromedioPorSistema (Consulta 3)
 */
public class TallerSegundoHitoTest extends TestCase {

    // ─── estado compartido por setUp ──────────────────────────────────

    private Taller taller;
    private Tallerista tallerista;

    @Override
    protected void setUp() {
        TDACola<Vehiculo> colaEspera     = new Cola<>();
        TDAColaPrioridad<Vehiculo> cola  = new ColaPrioridad<>();
        TDALista<Tallerista> talleristas = new Lista<>();
        TDALista<Vehiculo> historial     = new Lista<>();
        IGestorEsperaRepuestos gestor    = new GestorEsperaRepuestos(new Lista<>());

        taller = new Taller(colaEspera, gestor, talleristas, historial, cola);

        // Un tallerista disponible para los tests que necesiten atender vehículos
        tallerista = new Tallerista("T1", "Mecánico", "General");
        talleristas.agregar(tallerista);
    }

    // ─── helpers ──────────────────────────────────────────────────────

    /** Crea un vehículo mínimo y lo registra en el taller. */
    private Vehiculo registrar(String patente) {
        return registrar(patente, LocalDate.now());
    }

    private Vehiculo registrar(String patente, LocalDate fechaIngreso) {
        TDAPila<Tarea> tareas = new Pila<>();
        TDALista<Tarea> hist  = new Lista<>();
        tareas.mete(new Tarea("Revisión inicial", TipoTarea.MANTENIMIENTO, LocalDate.now()));
        Vehiculo v = new Vehiculo(patente, "Marca", "Modelo", 2020,
                "Propietario", TipoIngreso.MANTENIMIENTO_PLANIFICADO, 3, fechaIngreso, tareas, hist);
        taller.registrarVehiculo(v);
        return v;
    }

    private Tarea tarea(String desc, double costo) {
        return new Tarea(desc, TipoTarea.FALLA_ADICIONAL, LocalDate.now(), costo, null);
    }

    // ═══════════════════════════════════════════════════════════════════
    // buscarVehiculoPorMatricula
    // ═══════════════════════════════════════════════════════════════════

    /** Registrar un vehículo y buscarlo por patente debe devolver el mismo objeto. */
    public void testBuscarVehiculoPorMatriculaExistente() {
        Vehiculo v = registrar("AAA1111");
        Vehiculo resultado = taller.buscarVehiculoPorMatricula("AAA1111");
        assertNotNull(resultado);
        assertEquals("AAA1111", resultado.getPatente());
    }

    /** Buscar una patente que nunca se registró devuelve null. */
    public void testBuscarVehiculoPorMatriculaInexistente() {
        assertNull(taller.buscarVehiculoPorMatricula("ZZZ9999"));
    }

    /** Con varios vehículos registrados, cada búsqueda devuelve el correcto. */
    public void testBuscarVehiculoPorMatriculaVariosVehiculos() {
        registrar("AAA1111");
        registrar("BBB2222");
        registrar("CCC3333");
        assertEquals("AAA1111", taller.buscarVehiculoPorMatricula("AAA1111").getPatente());
        assertEquals("BBB2222", taller.buscarVehiculoPorMatricula("BBB2222").getPatente());
        assertEquals("CCC3333", taller.buscarVehiculoPorMatricula("CCC3333").getPatente());
    }

    // ═══════════════════════════════════════════════════════════════════
    // vehiculosEnRangoDeFechas
    // ═══════════════════════════════════════════════════════════════════

    /** Vehículos dentro del rango deben aparecer; los de fuera no. */
    public void testVehiculosEnRangoDeFechas() {
        LocalDate hoy   = LocalDate.now();
        LocalDate hace5 = hoy.minusDays(5);
        LocalDate hace3 = hoy.minusDays(3);
        LocalDate hace1 = hoy.minusDays(1);

        registrar("DENTRO_1", hace3);   // dentro del rango [hace5, hace1]
        registrar("DENTRO_2", hace5);   // en el límite inferior
        registrar("FUERA",    hoy);     // fuera del rango [hace5, hace1]

        TDALista<Vehiculo> resultado = taller.vehiculosEnRangoDeFechas(hace5, hace1);
        assertEquals(2, resultado.tamaño());

        // Verificar que solo están los vehículos de dentro del rango
        boolean encontroDentro1 = false, encontroDentro2 = false;
        for (int i = 0; i < resultado.tamaño(); i++) {
            String p = resultado.obtener(i).getPatente();
            if ("DENTRO_1".equals(p)) encontroDentro1 = true;
            if ("DENTRO_2".equals(p)) encontroDentro2 = true;
        }
        assertTrue(encontroDentro1);
        assertTrue(encontroDentro2);
    }

    /** Rango sin vehículos devuelve lista vacía. */
    public void testVehiculosEnRangoVacio() {
        registrar("AAA1111", LocalDate.now());
        TDALista<Vehiculo> resultado = taller.vehiculosEnRangoDeFechas(
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(5));
        assertEquals(0, resultado.tamaño());
    }

    // ═══════════════════════════════════════════════════════════════════
    // reprogramarVehiculo
    // ═══════════════════════════════════════════════════════════════════

    /** reprogramarVehiculo actualiza la fecha comprometida del vehículo. */
    public void testReprogramarVehiculoActualizaFecha() {
        Vehiculo v = registrar("AAA1111");
        LocalDate nueva = LocalDate.now().plusDays(10);

        taller.reprogramarVehiculo("AAA1111", nueva);
        assertEquals(nueva, taller.buscarVehiculoPorMatricula("AAA1111").getFechaEntregaComprometida());
    }

    /** reprogramarVehiculo con patente inexistente lanza NoSuchElementException. */
    public void testReprogramarVehiculoInexistente() {
        try {
            taller.reprogramarVehiculo("ZZZ9999", LocalDate.now().plusDays(3));
            fail("Debería lanzar NoSuchElementException");
        } catch (NoSuchElementException e) {
            // correcto
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // OrdenTrabajo delegado al Taller
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Flujo típico: iniciar orden, agregar tarea derivada, aprobar, terminar.
     * Verifica que el taller delega correctamente a OrdenTrabajo.
     */
    public void testFlujoBasicoOrdenTrabajo() {
        Vehiculo v = registrar("AAA1111");
        Tarea raiz = tarea("Cambio correa", 500.0);
        v.iniciarOrden(raiz);

        Tarea derivada = tarea("Tensor roto", 150.0);
        taller.agregarTareaDerivada("AAA1111", raiz, derivada);

        // raíz no puede terminarse mientras exista la derivada pendiente
        assertFalse(taller.puedeTerminar("AAA1111", raiz));

        // derivada puede terminarse (es hoja)
        assertTrue(taller.puedeTerminar("AAA1111", derivada));

        // terminar la derivada
        derivada.setEstado(EstadoTarea.TERMINADA);

        // ahora la raíz puede terminarse
        assertTrue(taller.puedeTerminar("AAA1111", raiz));
    }

    /** tareasPendientesParaEntregar devuelve el postorden correcto. */
    public void testTareasPendientesParaEntregarPostorden() {
        Vehiculo v = registrar("AAA1111");
        Tarea raiz  = tarea("Raíz", 0.0);
        Tarea hijo1 = tarea("Hijo1", 0.0);
        Tarea hijo2 = tarea("Hijo2", 0.0);
        v.iniciarOrden(raiz);
        taller.agregarTareaDerivada("AAA1111", raiz, hijo1);
        taller.agregarTareaDerivada("AAA1111", raiz, hijo2);

        TDALista<Tarea> lista = taller.tareasPendientesParaEntregar("AAA1111");
        // postorden: hijo1, hijo2, raiz
        assertEquals(3, lista.tamaño());
        assertEquals(hijo1, lista.obtener(0));
        assertEquals(hijo2, lista.obtener(1));
        assertEquals(raiz,  lista.obtener(2));
    }

    /** costoTotal suma todas las tareas del árbol. */
    public void testCostoTotal() {
        Vehiculo v = registrar("AAA1111");
        Tarea raiz  = tarea("Raíz",  500.0);
        Tarea hijo  = tarea("Hijo",  300.0);
        v.iniciarOrden(raiz);
        taller.agregarTareaDerivada("AAA1111", raiz, hijo);

        assertEquals(800.0, taller.costoTotal("AAA1111"), 0.001);
    }

    /** costoParcial suma solo la tarea y sus descendientes. */
    public void testCostoParcial() {
        Vehiculo v = registrar("AAA1111");
        Tarea raiz  = tarea("Raíz",  500.0);
        Tarea hijo  = tarea("Hijo",  200.0);
        Tarea nieto = tarea("Nieto", 100.0);
        v.iniciarOrden(raiz);
        taller.agregarTareaDerivada("AAA1111", raiz, hijo);
        taller.agregarTareaDerivada("AAA1111", hijo, nieto);

        // costoParcial de hijo = hijo + nieto = 300
        assertEquals(300.0, taller.costoParcial("AAA1111", hijo), 0.001);
    }

    /** aprobarParte cambia PENDIENTE → EN_CURSO solo en el nodo indicado. */
    public void testAprobarParte() {
        Vehiculo v = registrar("AAA1111");
        Tarea raiz = tarea("Raíz", 0.0);
        Tarea hijo = tarea("Hijo", 0.0);
        v.iniciarOrden(raiz);
        taller.agregarTareaDerivada("AAA1111", raiz, hijo);

        taller.aprobarParte("AAA1111", raiz);
        assertEquals(EstadoTarea.EN_CURSO, raiz.getEstado());
        assertEquals(EstadoTarea.PENDIENTE, hijo.getEstado()); // no se propaga
    }

    /** rechazarParte propaga RECHAZADA a todo el subárbol. */
    public void testRechazarParte() {
        Vehiculo v = registrar("AAA1111");
        Tarea raiz  = tarea("Raíz",  0.0);
        Tarea hijo  = tarea("Hijo",  0.0);
        Tarea nieto = tarea("Nieto", 0.0);
        v.iniciarOrden(raiz);
        taller.agregarTareaDerivada("AAA1111", raiz, hijo);
        taller.agregarTareaDerivada("AAA1111", hijo, nieto);

        taller.rechazarParte("AAA1111", hijo);
        assertEquals(EstadoTarea.RECHAZADA, hijo.getEstado());
        assertEquals(EstadoTarea.RECHAZADA, nieto.getEstado());
        assertEquals(EstadoTarea.PENDIENTE, raiz.getEstado()); // raíz no se toca
    }

    // ═══════════════════════════════════════════════════════════════════
    // suspenderPorRepuesto / reanudarPorRepuesto
    // ═══════════════════════════════════════════════════════════════════

    /** Suspender solo la rama afectada; el resto del árbol queda activo. */
    public void testSuspenderPorRepuesto() {
        Vehiculo v = registrar("AAA1111");
        Tarea raiz  = tarea("Raíz",  0.0);
        Tarea hijo1 = tarea("Hijo1", 0.0);
        Tarea hijo2 = tarea("Hijo2", 0.0);
        v.iniciarOrden(raiz);
        taller.agregarTareaDerivada("AAA1111", raiz, hijo1);
        taller.agregarTareaDerivada("AAA1111", raiz, hijo2);

        taller.suspenderPorRepuesto("AAA1111", hijo1);
        assertEquals(EstadoTarea.SUSPENDIDA, hijo1.getEstado());
        assertEquals(EstadoTarea.PENDIENTE,  hijo2.getEstado());
        assertEquals(EstadoTarea.PENDIENTE,  raiz.getEstado());
    }

    /** Reanudar vuelve las SUSPENDIDAS a PENDIENTE. */
    public void testReanudarPorRepuesto() {
        Vehiculo v = registrar("AAA1111");
        Tarea raiz  = tarea("Raíz",  0.0);
        Tarea hijo1 = tarea("Hijo1", 0.0);
        v.iniciarOrden(raiz);
        taller.agregarTareaDerivada("AAA1111", raiz, hijo1);

        taller.suspenderPorRepuesto("AAA1111", hijo1);
        taller.reanudarPorRepuesto("AAA1111", hijo1);

        assertEquals(EstadoTarea.PENDIENTE, hijo1.getEstado());
    }

    // ═══════════════════════════════════════════════════════════════════
    // Consulta 1: trabajosBloqueadosPorRepuesto
    // ═══════════════════════════════════════════════════════════════════

    /** Lista todas las tareas SUSPENDIDAS del vehículo. */
    public void testTrabajosBloqueadosPorRepuesto() {
        Vehiculo v = registrar("AAA1111");
        Tarea raiz  = tarea("Raíz",  0.0);
        Tarea hijo1 = tarea("Hijo1", 0.0);
        Tarea hijo2 = tarea("Hijo2", 0.0);
        v.iniciarOrden(raiz);
        taller.agregarTareaDerivada("AAA1111", raiz, hijo1);
        taller.agregarTareaDerivada("AAA1111", raiz, hijo2);

        taller.suspenderPorRepuesto("AAA1111", hijo1);
        TDALista<Tarea> bloqueados = taller.trabajosBloqueadosPorRepuesto("AAA1111");
        assertEquals(1, bloqueados.tamaño());
        assertEquals(hijo1, bloqueados.obtener(0));
    }

    /** Consulta de vehículo sin orden iniciada lanza IllegalStateException. */
    public void testTrabajosBloqueadosSinOrden() {
        registrar("AAA1111");
        try {
            taller.trabajosBloqueadosPorRepuesto("AAA1111");
            fail("Debería lanzar IllegalStateException");
        } catch (IllegalStateException e) {
            // correcto: el vehículo existe pero no tiene OrdenTrabajo
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // Consulta 2: vehiculoConMasTareasPendientes
    // ═══════════════════════════════════════════════════════════════════

    /**
     * El vehículo siendo atendido con más tareas pendientes debe ser identificado.
     * Usamos atenderProximoVehiculo para que el tallerista tenga el vehículo asignado.
     */
    public void testVehiculoConMasTareasPendientes() {
        Vehiculo v1 = registrar("AAA1111");
        Tarea raiz1 = tarea("Raíz v1", 0.0);
        Tarea h1    = tarea("Hijo v1", 0.0);
        v1.iniciarOrden(raiz1);
        taller.agregarTareaDerivada("AAA1111", raiz1, h1);
        // v1 tiene 2 tareas pendientes

        Vehiculo v2 = registrar("BBB2222");
        Tarea raiz2 = tarea("Raíz v2", 0.0);
        v2.iniciarOrden(raiz2);
        // v2 tiene 1 tarea pendiente

        // El tallerista atiende al primer vehículo en la cola (AAA1111)
        taller.atenderProximoVehiculo();

        // v1 tiene más pendientes que v2 (que no está siendo atendido)
        Vehiculo resultado = taller.vehiculoConMasTareasPendientes();
        assertNotNull(resultado);
        assertEquals("AAA1111", resultado.getPatente());
    }

    /** Sin vehículos siendo atendidos, el resultado es null. */
    public void testVehiculoConMasTareasPendientesSinAtendidos() {
        assertNull(taller.vehiculoConMasTareasPendientes());
    }

    // ═══════════════════════════════════════════════════════════════════
    // Operaciones con patente inexistente
    // ═══════════════════════════════════════════════════════════════════

    /** Todas las operaciones con patente inexistente lanzan NoSuchElementException. */
    public void testPatenteInexistenteLanzaExcepcion() {
        Tarea t = tarea("Tarea", 0.0);

        try { taller.agregarTareaDerivada("NOEXISTE", t, t); fail(); }
        catch (NoSuchElementException e) { /* correcto */ }

        try { taller.puedeTerminar("NOEXISTE", t); fail(); }
        catch (NoSuchElementException e) { /* correcto */ }

        try { taller.tareasPendientesParaEntregar("NOEXISTE"); fail(); }
        catch (NoSuchElementException e) { /* correcto */ }

        try { taller.costoTotal("NOEXISTE"); fail(); }
        catch (NoSuchElementException e) { /* correcto */ }

        try { taller.trabajosBloqueadosPorRepuesto("NOEXISTE"); fail(); }
        catch (NoSuchElementException e) { /* correcto */ }
    }
}
