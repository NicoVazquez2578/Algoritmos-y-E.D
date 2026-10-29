package ucu.edu.aed.molecular.sintesis;

import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;

/*
  Dato de cada arista del grafo de reacciones.
 
  Para que el costo de una reacción se cuente UNA sola vez en un camino:
   - arista reactivo → reacción: lleva el peso de la reacción según el criterio elegido;
   - arista reacción → producto: peso 0.
  Un camino Compuesto → Reaccion → Compuesto suma entonces exactamente el peso de la reacción.
 */
public class PasoSintesis extends WeightedEdge {

    private final Reaccion reaccion;
    private final boolean entrada; // true: reactivo → reacción; false: reacción → producto

    private PasoSintesis(Reaccion reaccion, double peso, boolean entrada) {
        super(peso);
        this.reaccion = reaccion;
        this.entrada = entrada;
    }

    public static PasoSintesis entrada(Reaccion r, Criterio criterio) {
        return new PasoSintesis(r, criterio.peso(r), true);
    }

    public static PasoSintesis salida(Reaccion r) {
        return new PasoSintesis(r, 0.0, false);
    }

    public Reaccion getReaccion() { return reaccion; }
    public boolean esEntrada() { return entrada; }

    @Override
    public String toString() { return (entrada ? "in:" : "out:") + reaccion.getId() + "(" + getWeight() + ")"; }
}
