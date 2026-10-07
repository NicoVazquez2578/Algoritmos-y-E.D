package ucu.edu.aed.ProyectoPrimerHito;

import junit.framework.TestCase;
import ucu.edu.aed.impl.Lista;
import ucu.edu.aed.impl.Pila;
import ucu.edu.aed.tda.TDALista;
import ucu.edu.aed.tda.TDAPila;

import java.time.LocalDate;

public class TalleristaTest extends TestCase {

    private Tallerista tallerista;
    private Vehiculo vehiculo;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        tallerista = new Tallerista("T01", "Ana Pérez", "Motor");
        vehiculo = crearVehiculo("AAA1234", 3);
    }

    private Vehiculo crearVehiculo(String patente, int nivelUrgencia) {
        TDAPila<Tarea> tareasPendientes = new Pila<>();
        TDALista<Tarea> historial = new Lista<>();
        return new Vehiculo(patente, "Chevrolet", "Onix", 2020, "Juan Dueño",
                TipoIngreso.FALLA_INFORMADA, nivelUrgencia, LocalDate.now(),
                tareasPendientes, historial);
    }

    /**
     * JUnit 3 no trae assertThrows (es de JUnit 4.13+/5). Este helper hace
     * lo mismo a mano: ejecuta la acción y falla si no lanza el tipo esperado.
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

    // --- estaDisponible / getVehiculoActual ---

    public void testAlCrearse_estaDisponible() {
        assertTrue(tallerista.estaDisponible());
        assertNull(tallerista.getVehiculoActual());
    }

    // --- asignar ---

    public void testAsignar_ocupaAlTalleristaConElVehiculo() {
        tallerista.asignar(vehiculo);

        assertFalse(tallerista.estaDisponible());
        assertEquals(vehiculo, tallerista.getVehiculoActual());
    }

    public void testAsignar_siYaEstaOcupado_lanzaExcepcion() {
        tallerista.asignar(vehiculo);
        final Vehiculo otroVehiculo = crearVehiculo("BBB5678", 1);

        try {
            tallerista.asignar(otroVehiculo);
            fail("Debería lanzar IllegalStateException");
        } catch (IllegalStateException e) {
            // esperado
        }
        // El vehículo original no debe haberse perdido/reemplazado
        assertEquals(vehiculo, tallerista.getVehiculoActual());
    }

    // --- liberar ---

    public void testLiberar_dejaAlTalleristaDisponibleDeNuevo() {
        tallerista.asignar(vehiculo);

        tallerista.liberar();

        assertTrue(tallerista.estaDisponible());
        assertNull(tallerista.getVehiculoActual());
    }

    public void testLiberar_sinTenerVehiculoAsignado_noRompeNada() {
        tallerista.liberar(); // si esto tira una excepción, JUnit marca el test como error
        assertTrue(tallerista.estaDisponible());
    }

    public void testDespuesDeLiberar_puedeAsignarseOtroVehiculo() {
        tallerista.asignar(vehiculo);
        tallerista.liberar();
        Vehiculo otroVehiculo = crearVehiculo("BBB5678", 1);

        tallerista.asignar(otroVehiculo);
        assertEquals(otroVehiculo, tallerista.getVehiculoActual());
    }

    // --- getters ---

    public void testGetters_devuelvenLosValoresDelConstructor() {
        assertEquals("T01", tallerista.getId());
        assertEquals("Ana Pérez", tallerista.getNombre());
        assertEquals("Motor", tallerista.getEspecialidad());
    }
}
