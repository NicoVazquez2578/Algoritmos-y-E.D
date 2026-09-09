package ucu.edu.aed.ProyectoPrimerHito;

import ucu.edu.aed.tda.TDALista;
import ucu.edu.aed.tda.TDAPila;

import java.time.LocalDate;
import java.util.Objects;

/*
 Representa un vehiculo registrado en el taller mecanico.
 */
public class Vehiculo implements Comparable<Vehiculo> {

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

    // ── Hito 2: campos nuevos ──────────────────────────────────────────
    // La OrdenTrabajo es el árbol jerárquico de tareas del vehículo.
    // Es null hasta que se registra el primer trabajo (se inicializa llamando a iniciarOrden).
    private OrdenTrabajo ordenTrabajo;

    // Fecha en que el cliente necesita el vehículo. Null si no hay compromiso.
    // Si está presente, el vehículo se atiende por este criterio (en el Montículo)
    // en lugar de por orden de llegada o urgencia.
    private LocalDate fechaEntregaComprometida;

    // Constructor original del Hito 1 — no se modifica para no romper los tests existentes.
    public Vehiculo(String patente, String marca, String modelo, int anio, String propietario,
                     TipoIngreso tipoIngreso, int nivelUrgencia, LocalDate fechaIngreso,
                     TDAPila<Tarea> tareasPendientes, TDALista<Tarea> historialTrabajos) {
        if (patente == null || patente.trim().isEmpty()) {
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
        this.ordenTrabajo = null;
        this.fechaEntregaComprometida = null;
    }

    // ── Métodos Hito 2 ─────────────────────────────────────────────────

    /**
     * Crea la OrdenTrabajo del vehículo con la tarea principal como raíz.
     * Debe llamarse cuando el tallerista empieza a trabajar en el vehículo
     * y ya sabe cuál es el problema principal.
     */
    public void iniciarOrden(Tarea tareaRaiz) {
        this.ordenTrabajo = new OrdenTrabajo(tareaRaiz);
    }

    public OrdenTrabajo getOrdenTrabajo() { return ordenTrabajo; }

    public LocalDate getFechaEntregaComprometida() { return fechaEntregaComprometida; }

    public void setFechaEntregaComprometida(LocalDate fecha) {
        this.fechaEntregaComprometida = fecha;
    }

    public void agregarTareaPendiente(Tarea tarea) {
        Objects.requireNonNull(tarea, "La tarea no puede ser nula.");
        tareasPendientes.mete(tarea);
    }
     
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

    public boolean hayTareasPendientes() {
        return !tareasPendientes.esVacio();
    }

    public Tarea proximaTareaAResolver() {
        return tareasPendientes.esVacio() ? null : tareasPendientes.tope();
    }

    public void cambiarEstado(EstadoVehiculo nuevoEstado) {
        this.estado = Objects.requireNonNull(nuevoEstado);
    }

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
    public int compareTo(Vehiculo otro) {
        return this.patente.compareTo(otro.patente);
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
