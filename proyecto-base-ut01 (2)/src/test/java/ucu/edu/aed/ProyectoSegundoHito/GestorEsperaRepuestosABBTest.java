package ucu.edu.aed.ProyectoSegundoHito;

import java.time.LocalDate;
import junit.framework.TestCase;
import ucu.edu.aed.ProyectoPrimerHito.TipoIngreso;
import ucu.edu.aed.ProyectoPrimerHito.Vehiculo;
import ucu.edu.aed.impl.Lista;
import ucu.edu.aed.impl.Pila;
import ucu.edu.aed.tda.TDALista;

public class GestorEsperaRepuestosABBTest extends TestCase {

    private GestorEsperaRepuestosABB gestor;

    @Override
    protected void setUp() {
        gestor = new GestorEsperaRepuestosABB();
    }

    private Vehiculo crearVehiculo(String patente) {
        return new Vehiculo(patente, "Toyota", "Corolla", 2020, "Juan",
                TipoIngreso.MANTENIMIENTO_PLANIFICADO, 5, LocalDate.now(),
                new Pila<>(), new Lista<>());
    }

    public void testAgregarYBuscar() {
        Vehiculo v1 = crearVehiculo("DEF456");
        Vehiculo v2 = crearVehiculo("ABC123");
        Vehiculo v3 = crearVehiculo("GHI789");

        gestor.agregar(v1);
        gestor.agregar(v2);
        gestor.agregar(v3);

        assertEquals(3, gestor.cantidadVehiculos());
        assertEquals(v1, gestor.buscarPorPatente("DEF456"));
        assertEquals(v2, gestor.buscarPorPatente("ABC123"));
        assertEquals(v3, gestor.buscarPorPatente("GHI789"));
        assertNull(gestor.buscarPorPatente("ZZZ999"));
    }

    public void testQuitarPorPatente() {
        Vehiculo v1 = crearVehiculo("DEF456");
        Vehiculo v2 = crearVehiculo("ABC123");
        Vehiculo v3 = crearVehiculo("GHI789");

        gestor.agregar(v1);
        gestor.agregar(v2);
        gestor.agregar(v3);

        Vehiculo quitado = gestor.quitarPorPatente("ABC123");
        assertEquals(v2, quitado);
        assertEquals(2, gestor.cantidadVehiculos());
        assertNull(gestor.buscarPorPatente("ABC123"));
    }

    public void testListarOrdenado() {
        gestor.agregar(crearVehiculo("CCC333"));
        gestor.agregar(crearVehiculo("AAA111"));
        gestor.agregar(crearVehiculo("BBB222"));

        TDALista<Vehiculo> lista = gestor.listar();
        assertEquals(3, lista.tamaño());
        assertEquals("AAA111", lista.obtener(0).getPatente());
        assertEquals("BBB222", lista.obtener(1).getPatente());
        assertEquals("CCC333", lista.obtener(2).getPatente());
    }
}
