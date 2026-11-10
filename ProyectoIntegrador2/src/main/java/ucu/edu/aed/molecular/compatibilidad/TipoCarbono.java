package ucu.edu.aed.molecular.compatibilidad;

/*
  Clasificación de un carbono según cuántos OTROS carbonos tiene unidos.
    0 o 1 vecino C → primario (el metano, sin vecinos C, se cuenta como primario)
    2 → secundario, 3 → terciario, 4 → cuaternario
 */
public enum TipoCarbono {
    PRIMARIO, SECUNDARIO, TERCIARIO, CUATERNARIO;

    public static TipoCarbono segunVecinosCarbono(int vecinosCarbono) {
        if (vecinosCarbono < 0 || vecinosCarbono > 4)
            throw new IllegalArgumentException("Un carbono tiene entre 0 y 4 vecinos carbono");
        switch (vecinosCarbono) {
            case 0: case 1: return PRIMARIO;
            case 2: return SECUNDARIO;
            case 3: return TERCIARIO;
            default: return CUATERNARIO;
        }
    }
}
