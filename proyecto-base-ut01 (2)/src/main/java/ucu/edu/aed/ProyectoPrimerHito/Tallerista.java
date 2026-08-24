package ucu.edu.aed.ProyectoPrimerHito;

/**
 * Representa a un tallerista y el vehículo que tiene asignado.
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
        this.vehiculoActual = null;
    }

    public boolean estaDisponible() {
        return this.vehiculoActual == null;
    }

    /**
     * Asigna un vehículo al tallerista.
     * @throws IllegalStateException si el tallerista ya está ocupado.
     */
    public void asignar(Vehiculo vehiculo) {
        if (!estaDisponible()) {
            throw new IllegalStateException(
                "El tallerista " + id + " ya está atendiendo el vehículo con patente "
                + vehiculoActual.getPatente()
            );
        }
        this.vehiculoActual = vehiculo;
    }

    public void liberar() {
        this.vehiculoActual = null;
    }

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