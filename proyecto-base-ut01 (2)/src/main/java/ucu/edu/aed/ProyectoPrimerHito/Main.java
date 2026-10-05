package ucu.edu.aed.ProyectoPrimerHito;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import ucu.edu.aed.impl.Cola;
import ucu.edu.aed.impl.ColaPrioridad;
import ucu.edu.aed.impl.Lista;
import ucu.edu.aed.impl.Pila;
import ucu.edu.aed.tda.TDALista;
import ucu.edu.aed.tda.TDAPila;

/*
  Programa para probar el Taller a mano.
  Simula: ingreso de vehiculos, atencion, falla adicional, 
  espera de repuesto, entrega. Va imprimiendo cada paso en consola.
 */
public class Main {

    public static void main(String[] args) {
        Taller taller = armarTaller();

        System.out.println("--- 1. Ingreso de vehiculos ---");
        Vehiculo v1 = crearVehiculo("AAA1111", "Toyota", "Corolla", "Ana", 3, 6);
        Vehiculo v2 = crearVehiculo("BBB2222", "Chevrolet", "Onix", "Beto", 9, 2); // urgente
        Vehiculo v3 = crearVehiculo("CCC3333", "Ford", "Fiesta", "Cami", 5, 4);

        taller.registrarVehiculo(v1);
        taller.registrarVehiculo(v2);
        taller.registrarVehiculo(v3);
        System.out.println("Registrados: " + v1.getPatente() + " (urgencia " + v1.getNivelUrgencia() + "), "
                + v2.getPatente() + " (urgencia " + v2.getNivelUrgencia() + ", URGENTE), "
                + v3.getPatente() + " (urgencia " + v3.getNivelUrgencia() + ")");

        System.out.println("\n--- 2. Atencion: el urgente pasa primero, aunque llego despues ---");
        Tallerista t1 = taller.atenderProximoVehiculo();
        System.out.println(t1.getNombre() + " atiende a " + t1.getVehiculoActual().getPatente()
                + " (esperado: BBB2222, por ser el urgente)");

        Tallerista t2 = taller.atenderProximoVehiculo();
        System.out.println(t2.getNombre() + " atiende a " + t2.getVehiculoActual().getPatente()
                + " (esperado: AAA1111, el mas viejo de los que quedaban)");

        System.out.println("\n--- 3. Se detecta una falla nueva en BBB2222 ---");
        Tarea fallaFrenos = new Tarea("Pastillas de freno gastadas", TipoTarea.FALLA_ADICIONAL, LocalDate.now());
        taller.registrarFallaAdicional("BBB2222", fallaFrenos);
        System.out.println("Proxima tarea de BBB2222: " + v2.proximaTareaAResolver().getDescripcion());

        System.out.println("\n--- 4. BBB2222 se queda sin repuesto ---");
        taller.marcarEsperaRepuesto("BBB2222");
        System.out.println(t1.getNombre() + " queda libre: " + t1.estaDisponible());
        System.out.println("Estado BBB2222: " + v2.getEstado());

        System.out.println("\n--- 5. El taller sigue atendiendo mientras tanto ---");
        Tallerista t3 = taller.atenderProximoVehiculo();
        System.out.println(t3.getNombre() + " atiende a " + t3.getVehiculoActual().getPatente()
                + " (esperado: CCC3333, el unico que quedaba)");

        System.out.println("\n--- 6. Llega el repuesto de BBB2222 ---");
        taller.repuestoDisponible("BBB2222");
        System.out.println("Estado BBB2222: " + v2.getEstado());

        Tallerista t4 = taller.atenderProximoVehiculo();
        System.out.println(t4.getNombre() + " retoma a " + t4.getVehiculoActual().getPatente()
                + " (esperado: BBB2222, sigue siendo el mas urgente)");

        System.out.println("\n-- 7. Se entregan los tres vehiculos ---");
        taller.finalizarVehiculo(v2.getPatente());
        taller.finalizarVehiculo(v1.getPatente());
        taller.finalizarVehiculo(v3.getPatente());
        System.out.println("Estados finales: " + v1.getPatente() + "=" + v1.getEstado()
                + ", " + v2.getPatente() + "=" + v2.getEstado()
                + ", " + v3.getPatente() + "=" + v3.getEstado());

        System.out.println("\n--- 8. Metricas ---");
        System.out.printf("Tiempo promedio de espera: %.2f dias%n", taller.tiempoPromedioEspera());

        TDALista<Vehiculo> masUrgentes = taller.vehiculosMasUrgentes(5);
        System.out.println("vehiculosMasUrgentes(5) -> " + masUrgentes.tamaño()
                + " vehiculo(s) (esperado: 0, ya se entregaron todos)");

        System.out.println("\n-- 9. Casos de error --");
        try {
            taller.atenderProximoVehiculo();
            System.out.println("ERROR: esto no deberia pasar, no quedan vehiculos.");
        } catch (NoSuchElementException e) {
            System.out.println("OK, error esperado (no hay mas vehiculos): " + e.getMessage());
        }

        try {
            taller.registrarFallaAdicional("PATENTE_INEXISTENTE", fallaFrenos);
            System.out.println("ERROR: esto no deberia pasar, la patente no existe.");
        } catch (NoSuchElementException e) {
            System.out.println("OK, error esperado (patente inexistente): " + e.getMessage());
        }
    }

    // Arma el Taller con una implementacion concreta para cada TDA.
    private static Taller armarTaller() {
        TDALista<Tallerista> talleristas = new Lista<>();
        talleristas.agregar(new Tallerista("T1", "Marta", "Mecanica general"));
        talleristas.agregar(new Tallerista("T2", "Pedro", "Electricidad"));
        talleristas.agregar(new Tallerista("T3", "Carla", "Frenos"));

        return new Taller(
                new Cola<>(),                                   // orden de llegada
                new GestorEsperaRepuestos(new Lista<>()),      // espera de repuestos
                talleristas,
                new Lista<>(),                                   // historial de entregados
                new ColaPrioridad<>()                            // vehiculos urgentes
        );
    }

    // Crea un vehiculo con su pila de tareas y su historial, ambos vacios.
    private static Vehiculo crearVehiculo(String patente, String marca, String modelo,
                                           String propietario, int nivelUrgencia, int diasIngresoAtras) {
        TDAPila<Tarea> tareasPendientes = new Pila<>();
        TDALista<Tarea> historialTrabajos = new Lista<>();
        return new Vehiculo(patente, marca, modelo, 2020, propietario,
                TipoIngreso.MANTENIMIENTO_PLANIFICADO, nivelUrgencia,
                LocalDate.now().minusDays(diasIngresoAtras),
                tareasPendientes, historialTrabajos);
    }
}