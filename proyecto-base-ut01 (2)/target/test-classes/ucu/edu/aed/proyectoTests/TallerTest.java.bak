package ucu.edu.aed.ProyectoPrimerHito;

import junit.framework.TestCase;
import ucu.edu.aed.tda.TDACola;
import ucu.edu.aed.tda.TDAColaPrioridad;
import ucu.edu.aed.tda.TDALista;
import ucu.edu.aed.tda.TDAPila;
import ucu.edu.aed.impl.Cola;
import ucu.edu.aed.impl.ColaPrioridad;
import ucu.edu.aed.impl.Lista;
import ucu.edu.aed.impl.Pila;

import java.time.LocalDate;

/**
 * Tests de Taller, escritos para JUnit 3 (junit:junit:3.8.1):
 * clase que extiende TestCase, métodos public void testXxx(), y setUp()
 * en vez de @BeforeEach (JUnit 3 no tiene anotaciones ni assertThrows).
 */
public class TallerTest extends TestCase {

    private Taller taller;
    private TDACola<Vehiculo> colaEspera;
    private TDAColaPrioridad<Vehiculo> colaUrgencias;
    private TDALista<Tallerista> talleristas;
    private TDALista<Vehiculo> historialEntregados;
    private IGestorEsperaRepuestos gestorEsperaRepuestos;

    // Igual al UMBRAL_URGENCIA privado de Taller (8). Si lo cambian allá,
    // hay que actualizarlo acá también.
    private static final int NIVEL_URGENTE = 9;
    private static final int NIVEL_NORMAL = 3;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        colaEspera = new Cola<>();
        colaUrgencias = new ColaPrioridad<>();
        talleristas = new Lista<>();
        historialEntregados = new Lista<>();
        gestorEsperaRepuestos = new GestorEsperaRepuestosV1(new Lista<>());

        taller = new Taller(colaEspera, gestorEsperaRepuestos, talleristas,
                historialEntregados, colaUrgencias);
    }

    private Vehiculo crearVehiculo(String patente, int nivelUrgencia) {
        TDAPila<Tarea> tareasPendientes = new Pila<>();
        TDALista<Tarea> historial = new Lista<>();
        return new Vehiculo(patente, "Chevrolet", "Onix", 2020, "Juan Dueño",
                TipoIngreso.FALLA_INFORMADA, nivelUrgencia, LocalDate.now(),
                tareasPendientes, historial);
    }

    private Vehiculo crearVehiculoConIngreso(String patente, LocalDate fechaIngreso) {
        TDAPila<Tarea> tareasPendientes = new Pila<>();
        TDALista<Tarea> historial = new Lista<>();
        return new Vehiculo(patente, "Chevrolet", "Onix", 2020, "Juan Dueño",
                TipoIngreso.FALLA_INFORMADA, NIVEL_NORMAL, fechaIngreso,
                tareasPendientes, historial);
    }

    private Tallerista agregarTallerista(String id) {
        Tallerista t = new Tallerista(id, "Nombre " + id, "General");
        talleristas.agregar(t);
        return t;
    }

    /**
     * JUnit 3 no trae assertThrows. Este helper hace lo mismo a mano:
     * ejecuta la acción y falla si no lanza el tipo esperado.
     */
    private void assertThrows(Class<? extends Throwable> tipoEsperado, Runnable accion) {
        try {
            accion.run();
            fail("Se esperaba que se lanzara " + tipoEsperado.getSimpleName());
        } catch (Throwable t) {
            if (!tipoEsperado.isInstance(t)) {
                fail("Se esperaba " + tipoEsperado.getSimpleName() + " pero se lanzó " + t.getClass().getSimpleName());
            }
        }
    }

    // ---------- registrarVehiculo / atenderProximoVehiculo ----------

    public void testAtenderProximoVehiculo_sinTalleristasDisponibles_lanzaExcepcion() {
        taller.registrarVehiculo(crearVehiculo("AAA1111", NIVEL_NORMAL));

        assertThrows(IllegalStateException.class, () -> taller.atenderProximoVehiculo());
    }

    public void testAtenderProximoVehiculo_sinVehiculosEnEspera_lanzaExcepcion() {
        agregarTallerista("T01");

        assertThrows(java.util.NoSuchElementException.class, () -> taller.atenderProximoVehiculo());
    }

    public void testAtenderProximoVehiculo_respetaOrdenDeLlegadaEntreVehiculosNormales() {
        agregarTallerista("T01");
        Vehiculo primero = crearVehiculo("AAA1111", NIVEL_NORMAL);
        Vehiculo segundo = crearVehiculo("BBB2222", NIVEL_NORMAL);
        taller.registrarVehiculo(primero);
        taller.registrarVehiculo(segundo);

        Tallerista asignado = taller.atenderProximoVehiculo();

        assertEquals(primero, asignado.getVehiculoActual());
        assertEquals(EstadoVehiculo.EN_REPARACION, primero.getEstado());
    }

    public void testAtenderProximoVehiculo_priorizaVehiculoUrgentePorSobreElOrdenDeLlegada() {
        agregarTallerista("T01");
        Vehiculo llegoPrimeroPeroNormal = crearVehiculo("AAA1111", NIVEL_NORMAL);
        Vehiculo llegoSegundoPeroUrgente = crearVehiculo("BBB2222", NIVEL_URGENTE);
        taller.registrarVehiculo(llegoPrimeroPeroNormal);
        taller.registrarVehiculo(llegoSegundoPeroUrgente);

        Tallerista asignado = taller.atenderProximoVehiculo();

        assertEquals(llegoSegundoPeroUrgente, asignado.getVehiculoActual());
    }

    public void testAtenderProximoVehiculo_asignaAUnTalleristaLibreYNoAUnoOcupado() {
        Tallerista ocupado = agregarTallerista("T01");
        ocupado.asignar(crearVehiculo("YYY0000", NIVEL_NORMAL));
        Tallerista libre = agregarTallerista("T02");
        taller.registrarVehiculo(crearVehiculo("AAA1111", NIVEL_NORMAL));

        Tallerista asignado = taller.atenderProximoVehiculo();

        assertEquals(libre, asignado);
    }

    // ---------- registrarFallaAdicional ----------

    public void testRegistrarFallaAdicional_agregaLaTareaAlVehiculoEnCola() {
        Vehiculo vehiculo = crearVehiculo("AAA1111", NIVEL_NORMAL);
        taller.registrarVehiculo(vehiculo);
        Tarea falla = new Tarea("Ruido en freno delantero", TipoTarea.FALLA_ADICIONAL, LocalDate.now());

        taller.registrarFallaAdicional("AAA1111", falla);

        assertTrue(vehiculo.hayTareasPendientes());
        assertEquals(falla, vehiculo.proximaTareaAResolver());
    }

    public void testRegistrarFallaAdicional_patenteInexistente_lanzaExcepcion() {
        final Tarea falla = new Tarea("Ruido", TipoTarea.FALLA_ADICIONAL, LocalDate.now());

        assertThrows(java.util.NoSuchElementException.class,
                () -> taller.registrarFallaAdicional("NOEXISTE", falla));
    }

    // ---------- marcarEsperaRepuesto / repuestoDisponible ----------

    public void testMarcarEsperaRepuesto_liberaAlTalleristaYCambiaElEstadoDelVehiculo() {
        Tallerista tallerista = agregarTallerista("T01");
        Vehiculo vehiculo = crearVehiculo("AAA1111", NIVEL_NORMAL);
        taller.registrarVehiculo(vehiculo);
        taller.atenderProximoVehiculo(); // lo asigna a T01

        taller.marcarEsperaRepuesto("AAA1111");

        assertTrue(tallerista.estaDisponible());
        assertEquals(EstadoVehiculo.ESPERANDO_REPUESTO, vehiculo.getEstado());
    }

    public void testMarcarEsperaRepuesto_siNingunTalleristaLoAtiende_lanzaExcepcion() {
        assertThrows(java.util.NoSuchElementException.class,
                () -> taller.marcarEsperaRepuesto("NOEXISTE"));
    }

    public void testRepuestoDisponible_reincorporaElVehiculoALaColaDeEspera() {
        agregarTallerista("T01");
        Vehiculo vehiculo = crearVehiculo("AAA1111", NIVEL_NORMAL);
        taller.registrarVehiculo(vehiculo);
        taller.atenderProximoVehiculo();
        taller.marcarEsperaRepuesto("AAA1111");

        taller.repuestoDisponible("AAA1111");

        assertEquals(EstadoVehiculo.EN_ESPERA, vehiculo.getEstado());
        // Debe poder volver a atenderse: ya no está "esperando repuesto"
        agregarTallerista("T02");
        Tallerista asignado = taller.atenderProximoVehiculo();
        assertEquals(vehiculo, asignado.getVehiculoActual());
    }

    public void testRepuestoDisponible_patenteInexistente_lanzaExcepcion() {
        assertThrows(java.util.NoSuchElementException.class,
                () -> taller.repuestoDisponible("NOEXISTE"));
    }

    // ---------- finalizarVehiculo ----------

    public void testFinalizarVehiculo_marcaEntregadoYLoMueveAlHistorial() {
        Tallerista tallerista = agregarTallerista("T01");
        Vehiculo vehiculo = crearVehiculo("AAA1111", NIVEL_NORMAL);
        taller.registrarVehiculo(vehiculo);
        taller.atenderProximoVehiculo();

        taller.finalizarVehiculo("AAA1111");

        assertTrue(tallerista.estaDisponible());
        assertEquals(EstadoVehiculo.ENTREGADO, vehiculo.getEstado());
        assertEquals(1, historialEntregados.tamaño());
        assertEquals(vehiculo, historialEntregados.obtener(0));
    }

    public void testFinalizarVehiculo_siNadieLoAtiende_lanzaExcepcion() {
        assertThrows(java.util.NoSuchElementException.class,
                () -> taller.finalizarVehiculo("NOEXISTE"));
    }

    // ---------- tiempoPromedioEspera ----------

    public void testTiempoPromedioEspera_sinEntregados_esCero() {
        assertEquals(0.0, taller.tiempoPromedioEspera(), 0.0001);
    }

    public void testTiempoPromedioEspera_calculaElPromedioEnDias() {
        agregarTallerista("T01");
        agregarTallerista("T02");

        Vehiculo hace10Dias = crearVehiculoConIngreso("AAA1111", LocalDate.now().minusDays(10));
        Vehiculo hace4Dias = crearVehiculoConIngreso("BBB2222", LocalDate.now().minusDays(4));
        taller.registrarVehiculo(hace10Dias);
        taller.registrarVehiculo(hace4Dias);

        taller.atenderProximoVehiculo();
        taller.finalizarVehiculo("AAA1111");
        taller.atenderProximoVehiculo();
        taller.finalizarVehiculo("BBB2222");

        // (10 + 4) / 2 = 7.0
        assertEquals(7.0, taller.tiempoPromedioEspera(), 0.001);
    }

    // ---------- vehiculosMasUrgentes ----------

    public void testVehiculosMasUrgentes_devuelveOrdenadosDeMayorAMenorUrgencia() {
        taller.registrarVehiculo(crearVehiculo("AAA1111", 2));
        taller.registrarVehiculo(crearVehiculo("BBB2222", 9));
        taller.registrarVehiculo(crearVehiculo("CCC3333", 5));

        TDALista<Vehiculo> masUrgentes = taller.vehiculosMasUrgentes(3);

        assertEquals(3, masUrgentes.tamaño());
        assertEquals("BBB2222", masUrgentes.obtener(0).getPatente());
        assertEquals("CCC3333", masUrgentes.obtener(1).getPatente());
        assertEquals("AAA1111", masUrgentes.obtener(2).getPatente());
    }

    public void testVehiculosMasUrgentes_respetaElLimiteN() {
        taller.registrarVehiculo(crearVehiculo("AAA1111", 1));
        taller.registrarVehiculo(crearVehiculo("BBB2222", 9));
        taller.registrarVehiculo(crearVehiculo("CCC3333", 5));

        TDALista<Vehiculo> top1 = taller.vehiculosMasUrgentes(1);

        assertEquals(1, top1.tamaño());
        assertEquals("BBB2222", top1.obtener(0).getPatente());
    }

    public void testVehiculosMasUrgentes_incluyeALosQueEsperanRepuesto() {
        agregarTallerista("T01");
        Vehiculo enEspera = crearVehiculo("AAA1111", 2);
        Vehiculo esperandoRepuestoUrgente = crearVehiculo("BBB2222", 9);
        taller.registrarVehiculo(enEspera);
        taller.registrarVehiculo(esperandoRepuestoUrgente);

        taller.atenderProximoVehiculo(); // toma al urgente (BBB2222)
        taller.marcarEsperaRepuesto("BBB2222");

        TDALista<Vehiculo> masUrgentes = taller.vehiculosMasUrgentes(2);

        assertEquals(2, masUrgentes.tamaño());
        assertEquals("BBB2222", masUrgentes.obtener(0).getPatente());
    }

    public void testVehiculosMasUrgentes_conNNegativo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> taller.vehiculosMasUrgentes(-1));
    }
}