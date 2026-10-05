package ucu.edu.aed.ProyectoPrimerHito;

import java.util.Objects;

/**
 * Dato almacenado en cada nodo de la EstructuraVehiculo.
 * Representa una parte concreta del vehículo (un sistema, subsistema o pieza)
 * sobre la cual se puede registrar una Tarea.
 *
 * Por ejemplo, si el tallerista trabaja sobre la "correa de distribución",
 * la tarea queda asociada a esa parte y el cliente puede ver exactamente
 * sobre qué componente se realizó el trabajo.
 */
public class ParteVehiculo {

    private final String nombre;
    private final TipoParteVehiculo tipo;

    public ParteVehiculo(String nombre, TipoParteVehiculo tipo) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
        this.tipo = Objects.requireNonNull(tipo, "El tipo no puede ser nulo");
    }

    public String getNombre() { return nombre; }

    public TipoParteVehiculo getTipo() { return tipo; }

    @Override
    public String toString() {
        return "[" + tipo + "] " + nombre;
    }

    // Dos partes son iguales si tienen el mismo nombre (asumimos nombres únicos por vehículo).
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParteVehiculo)) return false;
        ParteVehiculo that = (ParteVehiculo) o;
        return nombre.equals(that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}
