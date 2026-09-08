package ucu.edu.aed.ProyectoPrimerHito;

/**
 * Estados posibles de una Tarea dentro de la OrdenTrabajo.
 *
 * La transición normal es:
 *   PENDIENTE → EN_CURSO → TERMINADA
 *
 * Una tarea puede desviarse si llega una espera de repuesto:
 *   EN_CURSO → SUSPENDIDA → PENDIENTE (cuando llega el repuesto)
 *
 * O si el cliente no autoriza esa parte:
 *   PENDIENTE → RECHAZADA
 *
 * IMPORTANTE para el cierre de la orden:
 *   Un nodo puede quedar TERMINADO solo si todos sus descendientes
 *   están TERMINADOS o RECHAZADOS. Los RECHAZADOS no bloquean el cierre
 *   porque el cliente ya tomó la decisión de no hacerlos.
 */
public enum EstadoTarea {
    PENDIENTE,   // detectada, aún no iniciada
    EN_CURSO,    // el tallerista la está ejecutando
    SUSPENDIDA,  // bloqueada esperando un repuesto
    TERMINADA,   // completada exitosamente
    RECHAZADA    // el cliente no autorizó esta parte
}
