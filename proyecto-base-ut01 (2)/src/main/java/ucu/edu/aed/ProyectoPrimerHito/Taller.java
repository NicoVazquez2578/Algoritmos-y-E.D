package ucu.edu.aed.ProyectoPrimerHito;

import ucu.edu.aed.tda.TDACola;
import ucu.edu.aed.tda.TDAColaPrioridad;
import ucu.edu.aed.tda.TDALista;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;

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

    public void registrarVehiculo(Vehiculo vehiculo) {
        Objects.requireNonNull(vehiculo, "El vehículo no puede ser nulo.");
        colaEspera.poneEnCola(vehiculo);
        if (vehiculo.getNivelUrgencia() >= UMBRAL_URGENCIA) {
            colaUrgencias.encolar(vehiculo, -vehiculo.getNivelUrgencia());
        }
    }

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

    public void registrarFallaAdicional(String patente, Tarea tarea) {
        Vehiculo vehiculo = buscarVehiculoActivo(patente);
        if (vehiculo == null) {
            throw new NoSuchElementException("No se encontró un vehículo activo con patente " + patente);
        }
        vehiculo.agregarTareaPendiente(tarea);
    }

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

    public void repuestoDisponible(String patente) {
        Vehiculo vehiculo = gestorEsperaRepuestos.quitarPorPatente(patente);
        if (vehiculo == null) {
            throw new NoSuchElementException("Ningún vehículo con patente " + patente + " está esperando repuesto.");
        }
        vehiculo.cambiarEstado(EstadoVehiculo.EN_ESPERA);
        if (vehiculo.getNivelUrgencia() >= UMBRAL_URGENCIA) {
            colaUrgencias.encolar(vehiculo, -vehiculo.getNivelUrgencia());
        } else {
            colaEspera.poneEnCola(vehiculo);
        }
    }

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
