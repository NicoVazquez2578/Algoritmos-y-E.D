package ucu.edu.aed.ProyectoPrimerHito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucu.edu.aed.tda.TDALista;
import ucu.edu.aed.tda.TDAPila;

// TODO: reemplazar por los nombres reales de tus implementaciones concretas
// de TDAPila y TDALista hechas en el Desafío 1.
import ucu.edu.aed.tda.impl.PilaEnlazada;
import ucu.edu.aed.tda.impl.ListaEnlazada;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TalleristaTest {

    private Tallerista tallerista;
    private Vehiculo vehiculo;

    @BeforeEach
    void setUp() {
        tallerista = new Tallerista("T01", "Ana Pérez", "Motor");
        vehiculo = crearVehiculo("AAA1234", 3);
    }

    private Vehiculo crearVehiculo(String patente, int nivelUrgencia) {
        TDAPila<Tarea> tareasPendientes = new PilaEnlazada<>();
        TDALista<Tarea> historial = new ListaEnlazada<>();
        return new Vehiculo(patente, "Chevrolet", "Onix", 2020, "Juan Dueño",
                TipoIngreso.FALLA_INFORMADA, nivelUrgencia, LocalDate.now(),
                tareasPendientes, historial);
    }

    @Test
    void alCrearse_estaDisponible() {
        assertTrue(tallerista.estaDisponible());
        assertNull(tallerista.getVehiculoActual());
    }

    @Test
    void asignar_ocupaAlTalleristaConElVehiculo() {
        tallerista.asignar(vehiculo);

        assertFalse(tallerista.estaDisponible());
        assertEquals(vehiculo, tallerista.getVehiculoActual());
    }

    @Test
    void asignar_siYaEstaOcupado_lanzaExcepcion() {
        tallerista.asignar(vehiculo);
        Vehiculo otroVehiculo = crearVehiculo("BBB5678", 1);

        assertThrows(IllegalStateException.class, () -> tallerista.asignar(otroVehiculo));
        // El vehículo original no debe haberse perdido/reemplazado
        assertEquals(vehiculo, tallerista.getVehiculoActual());
    }

    @Test
    void liberar_dejaAlTalleristaDisponibleDeNuevo() {
        tallerista.asignar(vehiculo);

        tallerista.liberar();

        assertTrue(tallerista.estaDisponible());
        assertNull(tallerista.getVehiculoActual());
    }

    @Test
    void liberar_sinTenerVehiculoAsignado_noRompeNada() {
        assertDoesNotThrow(() -> tallerista.liberar());
        assertTrue(tallerista.estaDisponible());
    }

    @Test
    void despuesDeLiberar_puedeAsignarseOtroVehiculo() {
        tallerista.asignar(vehiculo);
        tallerista.liberar();
        Vehiculo otroVehiculo = crearVehiculo("BBB5678", 1);

        assertDoesNotThrow(() -> tallerista.asignar(otroVehiculo));
        assertEquals(otroVehiculo, tallerista.getVehiculoActual());
    }

    @Test
    void getters_devuelvenLosValoresDelConstructor() {
        assertEquals("T01", tallerista.getId());
        assertEquals("Ana Pérez", tallerista.getNombre());
        assertEquals("Motor", tallerista.getEspecialidad());
    }
}