package ucu.edu.aed.ProyectoPrimerHito;

/*
  Estados posibles de un Vehiculo dentro del ciclo de vida del taller.
 */
public enum EstadoVehiculo {
    EN_ESPERA,
    ESPERANDO_REPUESTO,
    EN_REPARACION,
    LISTO_PARA_RETIRAR,
    ENTREGADO
}
