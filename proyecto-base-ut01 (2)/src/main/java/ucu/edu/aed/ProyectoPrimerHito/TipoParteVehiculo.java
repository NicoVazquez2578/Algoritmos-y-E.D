package ucu.edu.aed.ProyectoPrimerHito;

/**
 * Nivel jerárquico de una parte dentro de la EstructuraVehiculo.
 *
 * El árbol n-ario puede tener profundidad variable según el sistema:
 *   - Tren delantero: SISTEMA → PIEZA (2 niveles)
 *   - Motor:          SISTEMA → SUBSISTEMA → SUBSISTEMA → PIEZA (4 niveles)
 *
 * No fijamos de antemano cuántos niveles hay; el árbol n-ario
 * permite que cada rama tenga la profundidad que necesite.
 */
public enum TipoParteVehiculo {
    SISTEMA,     // nivel más general: motor, tren delantero, transmisión
    SUBSISTEMA,  // nivel intermedio: árbol de levas, sistema de distribución
    PIEZA        // componente concreto: correa, válvula, tensor
}
