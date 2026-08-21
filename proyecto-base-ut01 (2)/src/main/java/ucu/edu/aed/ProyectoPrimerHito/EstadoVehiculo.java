package ucu.edu.aed.ProyectoPrimerHito;

/**
 * Estados posibles de un {@link Vehiculo} dentro del ciclo de vida del taller.
 */
public enum EstadoVehiculo {
    EN_ESPERA,
    ESPERANDO_REPUESTO,
    EN_REPARACION,
    LISTO_PARA_RETIRAR,
    ENTREGADO
}
