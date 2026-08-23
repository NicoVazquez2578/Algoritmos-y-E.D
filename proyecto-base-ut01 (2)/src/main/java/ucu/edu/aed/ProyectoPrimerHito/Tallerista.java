package ucu.edu.aed.ProyectoPrimerHito;
/**
 * Representa a un tallerista (mecánico) del taller.
 *
 * Un tallerista puede estar disponible o estar ocupado atendiendo
 * un único vehículo a la vez (vehiculoActual).
 */
public class Tallerista {

    private String id;
    private String nombre;
    private String especialidad;
    private Vehiculo vehiculoActual;

    public Tallerista(String id, String nombre, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.vehiculoActual = null; // al crearse, no está atendiendo nada
    }

    /**
     * Un tallerista está disponible si no tiene un vehículo asignado.
     */
    public boolean estaDisponible() {
        return this.vehiculoActual == null;
    }

    /**
     * Asigna un vehículo a este tallerista.
     * Precondición: el tallerista debe estar disponible.
     */
    public void asignar(Vehiculo vehiculo) {
        if (!estaDisponible()) {
            throw new IllegalStateException(
                "El tallerista " + id + " ya está atendiendo un vehículo (patente: "
                + vehiculoActual.getPatente() + ")"
            );
        }
        this.vehiculoActual = vehiculo;
    }

    /**
     * Libera al tallerista, quedando disponible para un nuevo vehículo.
     */
    public void liberar() {
        this.vehiculoActual = null;
    }

    // --- Getters (útiles para consultas del Taller, ej. buscar por id) ---

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public Vehiculo getVehiculoActual() {
        return vehiculoActual;
    }

    @Override
    public String toString() {
        return "Tallerista{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", disponible=" + estaDisponible() +
                '}';
    }
}