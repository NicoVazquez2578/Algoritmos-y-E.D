package ucu.edu.aed.ProyectoPrimerHito;

import ucu.edu.aed.tda.TDALista;
import ucu.edu.aed.tda.TDAPila;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa un vehiculo registrado en el taller mecanico.
 *
 * <p>Cada vehiculo mantiene dos estructuras propias, ambas recibidas por
 * inyeccion en el constructor (no se instancian aca adentro a proposito:
 * a esta clase le alcanza con conocer el TDA, no la implementacion concreta
 * que el grupo elija usar por detras — array, enlazada, etc.):</p>
 *
 * <ul>
 *   <li>{@code tareasPendientes} (TDAPila): modela que las fallas
 *       adicionales detectadas durante la inspeccion deben resolverse
 *       antes que la tarea que estaba en curso (LIFO).</li>
 *   <li>{@code historialTrabajos} (TDALista): registro cronologico de
 *       las tareas ya resueltas sobre este vehiculo.</li>
 * </ul>
 */
public class Vehiculo {

    private final String patente;
    private String marca;
    private String modelo;
    private int anio;
    private String propietario;
    private final TipoIngreso tipoIngreso;
    private EstadoVehiculo estado;
    private int nivelUrgencia;
    private final LocalDate fechaIngreso;

    private final TDAPila<Tarea> tareasPendientes;
    private final TDALista<Tarea> historialTrabajos;

    public Vehiculo(String patente, String marca, String modelo, int anio, String propietario,
                     TipoIngreso tipoIngreso, int nivelUrgencia, LocalDate fechaIngreso,
                     TDAPila<Tarea> tareasPendientes, TDALista<Tarea> historialTrabajos) {
        if (patente == null || patente.isBlank()) {
            throw new IllegalArgumentException("La patente no puede ser nula ni vacia.");
        }
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.propietario = propietario;
        this.tipoIngreso = tipoIngreso;
        this.nivelUrgencia = nivelUrgencia;
        this.fechaIngreso = fechaIngreso;
        this.estado = EstadoVehiculo.EN_ESPERA;
        this.tareasPendientes = tareasPendientes;
        this.historialTrabajos = historialTrabajos;
    }

    /**
     * Agrega una nueva tarea (falla adicional, reparacion o mantenimiento)
     * al tope de la pila de pendientes.
     *
     * <p>Complejidad: O(1), ya que {@code mete} inserta directamente en el
     * tope de la pila, sin recorrer la estructura.</p>
     */
    public void agregarTareaPendiente(Tarea tarea) {
        Objects.requireNonNull(tarea, "La tarea no puede ser nula.");
        tareasPendientes.mete(tarea);
    }

    /**
     * Resuelve la tarea que esta actualmente en el tope de la pila
     * (la ultima detectada, no necesariamente la primera) y la mueve
     * al historial.
     *
     * <p>Complejidad: O(1) para sacar de la pila; el costo de
     * {@code agregar} sobre el historial depende de la implementacion
     * de {@code TDALista} que el grupo elija (O(1) si se agrega al final
     * manteniendo referencia a la cola, O(n) si hay que recorrer).</p>
     *
     * @throws IllegalStateException si no hay tareas pendientes
     */
    public Tarea resolverTareaActual() {
        if (tareasPendientes.esVacio()) {
            throw new IllegalStateException(
                    "El vehiculo " + patente + " no tiene tareas pendientes para resolver.");
        }
        Tarea resuelta = tareasPendientes.saca();
        resuelta.marcarResuelta();
        historialTrabajos.agregar(resuelta);
        return resuelta;
    }

    /** Complejidad: O(1). */
    public boolean hayTareasPendientes() {
        return !tareasPendientes.esVacio();
    }

    /**
     * Consulta cual es la proxima tarea a resolver sin removerla.
     * Complejidad: O(1).
     */
    public Tarea proximaTareaAResolver() {
        return tareasPendientes.esVacio() ? null : tareasPendientes.tope();
    }

    public void cambiarEstado(EstadoVehiculo nuevoEstado) {
        this.estado = Objects.requireNonNull(nuevoEstado);
    }

    // ---- Getters / setters ----

    public String getPatente() {
        return patente;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAnio() {
        return anio;
    }

    public String getPropietario() {
        return propietario;
    }

    public TipoIngreso getTipoIngreso() {
        return tipoIngreso;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }

    public int getNivelUrgencia() {
        return nivelUrgencia;
    }

    public void setNivelUrgencia(int nivelUrgencia) {
        this.nivelUrgencia = nivelUrgencia;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    /**
     * Expone el historial para consultas de solo lectura. Ojo: como
     * {@code TDALista} no ofrece una vista inmutable, quien reciba esta
     * referencia podria modificar el historial "por afuera". Es una
     * decision de diseno a discutir con el grupo (por ejemplo, agregar
     * un metodo {@code copiarHistorial()} si se quiere blindar del todo
     * el encapsulamiento).
     */
    public TDALista<Tarea> getHistorialTrabajos() {
        return historialTrabajos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehiculo)) return false;
        Vehiculo vehiculo = (Vehiculo) o;
        return patente.equals(vehiculo.patente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patente);
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "patente='" + patente + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", estado=" + estado +
                ", nivelUrgencia=" + nivelUrgencia +
                '}';
    }
}
