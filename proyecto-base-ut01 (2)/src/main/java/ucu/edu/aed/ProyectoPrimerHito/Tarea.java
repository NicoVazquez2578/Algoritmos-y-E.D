package ucu.edu.aed.ProyectoPrimerHito;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Unidad de trabajo sobre un Vehiculo.
 *
 * HITO 1: mantenimiento planificado, reparación original o falla adicional.
 *
 * HITO 2 (extensión):
 *   - EstadoTarea: estado más fino que el boolean 'resuelta' original.
 *     'resuelta' se mantiene por compatibilidad con los tests del Hito 1.
 *   - costo: precio estimado de ejecutar este trabajo.
 *   - parte: la ParteVehiculo sobre la que se realiza (puede ser null si
 *     no se especifica la parte exacta al crear la tarea).
 *
 * El constructor original de 3 parámetros sigue funcionando; los campos
 * nuevos toman valores por defecto (PENDIENTE, 0.0, null).
 */
public class Tarea {

    private final String descripcion;
    private final TipoTarea tipo;
    private final LocalDate fechaDeteccion;

    // ── Hito 1: campo original ──────────────────────────────────────────
    private boolean resuelta;

    // ── Hito 2: campos nuevos ──────────────────────────────────────────
    private EstadoTarea estado;
    private double costo;
    private ParteVehiculo parte; // parte del vehículo sobre la que se hace el trabajo

    // ── Constructor original (Hito 1) ──────────────────────────────────
    // Se mantiene para que el código existente siga compilando sin cambios.
    public Tarea(String descripcion, TipoTarea tipo, LocalDate fechaDeteccion) {
        this(descripcion, tipo, fechaDeteccion, 0.0, null);
    }

    // ── Constructor completo (Hito 2) ──────────────────────────────────
    public Tarea(String descripcion, TipoTarea tipo, LocalDate fechaDeteccion,
                 double costo, ParteVehiculo parte) {
        this.descripcion = Objects.requireNonNull(descripcion);
        this.tipo = Objects.requireNonNull(tipo);
        this.fechaDeteccion = fechaDeteccion;
        this.resuelta = false;
        this.estado = EstadoTarea.PENDIENTE;
        this.costo = costo;
        this.parte = parte;
    }

    // ── Métodos Hito 1 (sin cambios de comportamiento) ─────────────────

    /**
     * Marca la tarea como resuelta (Hito 1).
     * También actualiza el estado a TERMINADA para que sea consistente con Hito 2.
     */
    public void marcarResuelta() {
        this.resuelta = true;
        this.estado = EstadoTarea.TERMINADA;
    }

    public boolean isResuelta() {
        // Una tarea está resuelta si el flag original está activo
        // O si el estado del Hito 2 es TERMINADA
        return resuelta || estado == EstadoTarea.TERMINADA;
    }

    // ── Getters Hito 1 ─────────────────────────────────────────────────

    public String getDescripcion()    { return descripcion; }
    public TipoTarea getTipo()        { return tipo; }
    public LocalDate getFechaDeteccion() { return fechaDeteccion; }

    // ── Getters y setters Hito 2 ───────────────────────────────────────

    public EstadoTarea getEstado()    { return estado; }

    /**
     * Cambia el estado de la tarea. Usado por OrdenTrabajo para suspender,
     * reanudar, aprobar y rechazar tareas.
     */
    public void setEstado(EstadoTarea estado) {
        this.estado = Objects.requireNonNull(estado);
        // Mantener sincronizado el flag del Hito 1
        if (estado == EstadoTarea.TERMINADA) this.resuelta = true;
    }

    public double getCosto()          { return costo; }
    public void setCosto(double costo) {
        if (costo < 0) throw new IllegalArgumentException("El costo no puede ser negativo.");
        this.costo = costo;
    }

    public ParteVehiculo getParte()   { return parte; }
    public void setParte(ParteVehiculo parte) { this.parte = parte; }

    @Override
    public String toString() {
        return "Tarea{" +
                "descripcion='" + descripcion + '\'' +
                ", tipo=" + tipo +
                ", estado=" + estado +
                ", costo=" + costo +
                '}';
    }
}

