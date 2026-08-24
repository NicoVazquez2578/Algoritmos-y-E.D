package ucu.edu.aed.ProyectoPrimerHito;

import ucu.edu.aed.tda.TDACola;
import ucu.edu.aed.tda.TDAColaPrioridad;
import ucu.edu.aed.tda.TDALista;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Gestiona el flujo del taller mecánico: ingreso de vehículos, asignación
 * a talleristas, espera de repuestos y entrega final.
 */
public class Taller {

    private static final int UMBRAL_URGENCIA = 8;

    private final TDACola<Vehiculo> colaEspera;
    private final IGestorEsperaRepuestos gestorEsperaRepuestos;
    private final TDALista<Tallerista> talleristas;
    private final TDALista<Vehiculo> historialEntregados;
    private final TDAColaPrioridad<Vehiculo> colaUrgencias;

    public Taller(TDACola<Vehiculo> colaEspera,
                  IGestorEsperaRepuestos gestorEsperaRepuestos,
                  TDALista<Tallerista> talleristas,
                  TDALista<Vehiculo> historialEntregados,
                  TDAColaPrioridad<Vehiculo> colaUrgencias) {
        this.colaEspera = Objects.requireNonNull(colaEspera);
        this.gestorEsperaRepuestos = Objects.requireNonNull(gestorEsperaRepuestos);
        this.talleristas = Objects.requireNonNull(talleristas);
        this.historialEntregados = Objects.requireNonNull(historialEntregados);
        this.colaUrgencias = Objects.requireNonNull(colaUrgencias);
    }

    /**
     * Registra el ingreso de un vehículo al taller.
     */
    public void registrarVehiculo(Vehiculo vehiculo) {
        Objects.requireNonNull(vehiculo, "El vehículo no puede ser nulo.");
        colaEspera.poneEnCola(vehiculo);
        if (vehiculo.getNivelUrgencia() >= UMBRAL_URGENCIA) {
            colaUrgencias.encolar(vehiculo, -vehiculo.getNivelUrgencia());
        }
    }

    /**
     * Asigna el próximo vehículo a un tallerista libre, priorizando urgencias.
     *
     * @throws IllegalStateException si no hay talleristas disponibles
     * @throws NoSuchElementException si no hay vehículos en espera
     */
    public Tallerista atenderProximoVehiculo() {
        Tallerista libre = talleristas.buscar(Tallerista::estaDisponible);
        if (libre == null) {
            throw new IllegalStateException("No hay talleristas disponibles en este momento.");
        }

        Vehiculo siguiente;
        if (!colaUrgencias.esVacio()) {
            siguiente = colaUrgencias.desencolarMasPrioritario();
            colaEspera.remover(siguiente);
        } else {
            if (colaEspera.esVacio()) {
                throw new NoSuchElementException("No hay vehículos esperando ser atendidos.");
            }
            siguiente = colaEspera.quitaDeCola();
        }

        libre.asignar(siguiente);
        siguiente.cambiarEstado(EstadoVehiculo.EN_REPARACION);
        return libre;
    }

    /**
     * Registra una nueva falla en un vehículo que ya está ingresado.
     */
    public void registrarFallaAdicional(String patente, Tarea tarea) {
        Vehiculo vehiculo = buscarVehiculoActivo(patente);
        if (vehiculo == null) {
            throw new NoSuchElementException("No se encontró un vehículo activo con patente " + patente);
        }
        vehiculo.agregarTareaPendiente(tarea);
    }

    /**
     * Pausa la reparación por falta de repuesto y libera al tallerista.
     */
    public void marcarEsperaRepuesto(String patente) {
        Tallerista tallerista = buscarTalleristaQueAtiende(patente);
        if (tallerista == null) {
            throw new NoSuchElementException("Ningún tallerista está atendiendo el vehículo " + patente);
        }
        Vehiculo vehiculo = tallerista.getVehiculoActual();
        tallerista.liberar();
        vehiculo.cambiarEstado(EstadoVehiculo.ESPERANDO_REPUESTO);
        gestorEsperaRepuestos.agregar(vehiculo);
    }

    /**
     * Reincorpora el vehículo a la cola de espera tras recibir el repuesto.
     */
    public void repuestoDisponible(String patente) {
        Vehiculo vehiculo = gestorEsperaRepuestos.quitarPorPatente(patente);
        vehiculo.cambiarEstado(EstadoVehiculo.EN_ESPERA);
        if (vehiculo.getNivelUrgencia() >= UMBRAL_URGENCIA) {
            colaUrgencias.encolar(vehiculo, -vehiculo.getNivelUrgencia());
        } else {
            colaEspera.poneEnCola(vehiculo);
        }
    }

    /**
     * Finaliza el trabajo: libera al tallerista y mueve el vehículo al historial.
     */
    public void finalizarVehiculo(String patente) {
        Tallerista tallerista = buscarTalleristaQueAtiende(patente);
        if (tallerista == null) {
            throw new NoSuchElementException("Ningún tallerista está atendiendo el vehículo " + patente);
        }
        Vehiculo vehiculo = tallerista.getVehiculoActual();
        tallerista.liberar();
        vehiculo.cambiarEstado(EstadoVehiculo.ENTREGADO);
        historialEntregados.agregar(vehiculo);
    }

    /**
     * Calcula el promedio de días en el taller para los vehículos entregados.
     */
    public double tiempoPromedioEspera() {
        if (historialEntregados.esVacio()) {
            return 0.0;
        }
        long sumaDias = 0;
        int cantidad = historialEntregados.tamaño();
        for (int i = 0; i < cantidad; i++) {
            Vehiculo v = historialEntregados.obtener(i);
            sumaDias += ChronoUnit.DAYS.between(v.getFechaIngreso(), LocalDate.now());
        }
        return (double) sumaDias / cantidad;
    }

    /**
     * Retorna los 'n' vehículos más urgentes entre los que están esperando reparación o repuestos.
     */
    public TDALista<Vehiculo> vehiculosMasUrgentes(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n no puede ser negativo.");
        }

        Comparator<Vehiculo> porUrgenciaDesc =
                Comparator.comparingInt(Vehiculo::getNivelUrgencia).reversed();

        TDALista<Vehiculo> resultado = colaEspera.ordenar(porUrgenciaDesc);

        TDALista<Vehiculo> enEsperaRepuesto = gestorEsperaRepuestos.listar();
        for (int i = 0; i < enEsperaRepuesto.tamaño(); i++) {
            resultado.agregar(enEsperaRepuesto.obtener(i));
        }
        resultado = resultado.ordenar(porUrgenciaDesc);

        while (resultado.tamaño() > n) {
            resultado.remover(resultado.tamaño() - 1);
        }
        return resultado;
    }

    // Auxiliares de búsqueda

    private Vehiculo buscarVehiculoActivo(String patente) {
        Vehiculo enCola = colaEspera.buscar(v -> v.getPatente().equals(patente));
        if (enCola != null) {
            return enCola;
        }

        Vehiculo enEsperaRepuesto = gestorEsperaRepuestos.listar()
                .buscar(v -> v.getPatente().equals(patente));
        if (enEsperaRepuesto != null) {
            return enEsperaRepuesto;
        }

        Tallerista tallerista = buscarTalleristaQueAtiende(patente);
        return (tallerista != null) ? tallerista.getVehiculoActual() : null;
    }

    private Tallerista buscarTalleristaQueAtiende(String patente) {
        return talleristas.buscar(t -> t.getVehiculoActual() != null
                && t.getVehiculoActual().getPatente().equals(patente));
    }
}