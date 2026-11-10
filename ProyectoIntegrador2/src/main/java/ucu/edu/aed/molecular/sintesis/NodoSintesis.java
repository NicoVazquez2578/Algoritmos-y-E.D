package ucu.edu.aed.molecular.sintesis;

/*
  Vértice del grafo de reacciones.
 
  El grafo de reacciones es DIRIGIDO y BIPARTITO, con dos tipos de vértice:
    Compuesto  ──►  Reaccion  ──►  Compuesto
    (reactivo)                    (producto)
  Así una reacción con varios reactivos (A + B → C) se representa sin inventar
  aristas falsas A → B: ambos reactivos apuntan al mismo vértice Reaccion.
 */
public interface NodoSintesis {

    // Identificador único dentro de la red (nombre del compuesto o id de la reacción). 
    String getId();

    boolean esReaccion();

    default boolean esCompuesto() { return !esReaccion(); }
}
