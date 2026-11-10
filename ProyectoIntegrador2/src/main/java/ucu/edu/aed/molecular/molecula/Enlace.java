package ucu.edu.aed.molecular.molecula;

import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;

/*
  Dato asociado a cada arista del grafo de una molécula.
 */
public class Enlace extends WeightedEdge {

    private static final double PESO_TOPOLOGICO = 1.0;

    private final TipoEnlace tipo;

    public Enlace(TipoEnlace tipo) {
        super(PESO_TOPOLOGICO);
        if (tipo == null) throw new IllegalArgumentException("El tipo de enlace no puede ser null");
        this.tipo = tipo;
    }

    public TipoEnlace getTipo() { return tipo; }
    public int getOrden() { return tipo.getOrden(); }

    @Override
    public String toString() { return tipo.name(); }
}
