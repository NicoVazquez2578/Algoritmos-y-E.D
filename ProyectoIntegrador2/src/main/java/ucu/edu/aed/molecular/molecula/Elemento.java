package ucu.edu.aed.molecular.molecula;

/*
  Elementos químicos soportados por el sistema.
  Cada elemento conoce su símbolo, su masa atómica (g/mol) y su valencia más común.
 */
public enum Elemento {
    H("H", 1.008, 1),
    C("C", 12.011, 4),
    N("N", 14.007, 3),
    O("O", 15.999, 2),
    F("F", 18.998, 1),
    P("P", 30.974, 3),
    S("S", 32.06, 2),
    CL("Cl", 35.45, 1),
    BR("Br", 79.904, 1),
    I("I", 126.904, 1);

    private final String simbolo;
    private final double masaAtomica;
    private final int valencia;

    Elemento(String simbolo, double masaAtomica, int valencia) {
        this.simbolo = simbolo;
        this.masaAtomica = masaAtomica;
        this.valencia = valencia;
    }

    public String getSimbolo() { return simbolo; }
    public double getMasaAtomica() { return masaAtomica; }
    public int getValencia() { return valencia; }

    /** Busca un elemento por su símbolo ("C", "Cl", ...). O(k), k = cantidad de elementos. */
    public static Elemento desdeSimbolo(String simbolo) {
        for (Elemento e : values()) {
            if (e.simbolo.equalsIgnoreCase(simbolo)) return e;
        }
        throw new IllegalArgumentException("Elemento desconocido: " + simbolo);
    }
}
