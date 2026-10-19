package ProblemSet4.impl;

import ucu.edu.aed.tda.grafo.model.result.IDijkstraResult;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ResultadoDijkstra<V> implements IDijkstraResult<V> {

    // La ciudad desde donde arrancó el viaje
    private final V ciudadOrigen;

    // Guarda el costo mínimo para llegar a cada ciudad
    private final Map<V, Double> distancias;

    // Guarda para cada ciudad cuál fue la ciudad anterior (predecesor) en el camino óptimo
    private final Map<V, V> ciudadesAnteriores;

    public ResultadoDijkstra(V ciudadOrigen, Map<V, Double> distancias, Map<V, V> ciudadesAnteriores) {
        this.ciudadOrigen = ciudadOrigen;
        this.distancias = distancias;
        this.ciudadesAnteriores = ciudadesAnteriores;
    }

    /**
     * Devuelve el costo mínimo para ir desde el origen hasta la ciudad indicada.
     * Si no se puede llegar, devuelve infinito.
     */
    @Override
    public double getCost(V ciudadDestino) {
        return distancias.getOrDefault(ciudadDestino, Double.POSITIVE_INFINITY);
    }

    /**
     * Reconstruye y devuelve la lista de ciudades del viaje paso a paso (ejemplo: [Montevideo, Buenos Aires, Santiago]).
     * Si no hay forma de llegar, devuelve null.
     */
    @Override
    public List<V> getPath(V ciudadDestino) {
        // Si el costo es infinito, significa que no hay conexión posible
        if (getCost(ciudadDestino) == Double.POSITIVE_INFINITY) {
            return null;
        }

        // Reconstruimos el camino hacia atrás, empezando desde el destino hacia el origen
        LinkedList<V> camino = new LinkedList<>();
        V ciudadActual = ciudadDestino;

        while (ciudadActual != null && !ciudadActual.equals(ciudadOrigen)) {
            camino.addFirst(ciudadActual); // Agregamos al principio
            ciudadActual = ciudadesAnteriores.get(ciudadActual); // Retrocedemos al paso anterior
        }

        if (ciudadActual == null) {
            return null;
        }

        // Finalmente agregamos el origen al principio del todo
        camino.addFirst(ciudadOrigen);
        return new ArrayList<>(camino);
    }
}

