package ucu.edu.aed.molecular.molecula;

/*
  Tipo de enlace covalente. El orden indica cuántas unidades de valencia
  consume en cada uno de los dos átomos que une.
 */
public enum TipoEnlace {
    SIMPLE(1),
    DOBLE(2),
    TRIPLE(3);

    private final int orden;

    TipoEnlace(int orden) { this.orden = orden; }

    public int getOrden() { return orden; }
}
