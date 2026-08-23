package ucu.edu.aed.ProyectoPrimerHito;

import java.time.LocalDate;
import java.util.Objects;

/*
  Unidad de trabajo sobre un {@link Vehiculo}: un mantenimiento planificado,
  la reparacion original por la que ingreso, o una falla adicional
  detectada durante la inspeccion.
 */
public class Tarea {

    private final String descripcion;
    private final TipoTarea tipo;
    private final LocalDate fechaDeteccion;
    private boolean resuelta;

    public Tarea(String descripcion, TipoTarea tipo, LocalDate fechaDeteccion) {
        this.descripcion = Objects.requireNonNull(descripcion);
        this.tipo = Objects.requireNonNull(tipo);
        this.fechaDeteccion = fechaDeteccion;
        this.resuelta = false;
    }

    public void marcarResuelta() {
        this.resuelta = true;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public TipoTarea getTipo() {
        return tipo;
    }

    public LocalDate getFechaDeteccion() {
        return fechaDeteccion;
    }

    public boolean isResuelta() {
        return resuelta;
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "descripcion='" + descripcion + '\'' +
                ", tipo=" + tipo +
                ", resuelta=" + resuelta +
                '}';
    }
}
