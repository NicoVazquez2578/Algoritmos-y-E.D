package ProblemSet4.impl;

import ucu.edu.aed.tda.grafo.model.result.IFloydWarshallResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Esta clase guarda la respuesta del algoritmo de Floyd-Warshall.
 * Como Floyd calcula las distancias más cortas entre TODOS los pares de ciudades posibles,
 * esta clase guarda la matriz completa de costos y la matriz de caminos para poder consultar
 * cualquier viaje (origen -> destino).
 *
 * @param <V> Tipo de dato de los vértices (por ejemplo, String para nombres de ciudades).
 */
public class ResultadoFloyd<V> implements IFloydWarshallResult<V> {

    // Lista ordenada de todas las ciudades del grafo
    private final List<V> listaCiudades;

    // Matriz de distancias mínimas: matrizDistancias[i][j] es el menor costo de la ciudad i a la ciudad j
    private final double[][] matrizDistancias;

    // Matriz de predecesores: sirve para reconstruir la ruta paso a paso
    private final int[][] matrizPredecesores;

    // Diccionario auxiliar para saber en qué fila/columna está cada ciudad en la matriz
    private final Map<V, Integer> posicionCiudad;

    public ResultadoFloyd(List<V> listaCiudades, double[][] matrizDistancias, int[][] matrizPredecesores) {
        this.listaCiudades = listaCiudades;
        this.matrizDistancias = matrizDistancias;
        this.matrizPredecesores = matrizPredecesores;

        this.posicionCiudad = new HashMap<>();
        for (int i = 0; i < listaCiudades.size(); i++) {
            this.posicionCiudad.put(listaCiudades.get(i), i);
        }
    }

    /**
     * Devuelve el costo del viaje más corto entre la ciudad origen y la ciudad destino.
     */
    @Override
    public double getCost(V origen, V destino) {
        Integer fila = posicionCiudad.get(origen);
        Integer columna = posicionCiudad.get(destino);
        if (fila == null || columna == null) {
            return Double.POSITIVE_INFINITY;
        }
        return matrizDistancias[fila][columna];
    }

    /**
     * Devuelve true si existe algún camino (directo o con escalas) entre el origen y el destino.
     */
    @Override
    public boolean connected(V origen, V destino) {
        return getCost(origen, destino) < Double.POSITIVE_INFINITY;
    }

    /**
     * Devuelve la lista con las ciudades a recorrer en orden desde el origen hasta el destino.
     */
    @Override
    public List<V> getPath(V origen, V destino) {
        Integer filaOrigen = posicionCiudad.get(origen);
        Integer columnaDestino = posicionCiudad.get(destino);

        if (filaOrigen == null || columnaDestino == null) {
            return null;
        }

        // Si no hay predecesor y no es la misma ciudad, no hay camino
        if (matrizPredecesores[filaOrigen][columnaDestino] == -1 && !origen.equals(destino)) {
            return null;
        }

        // Reconstruimos el camino desde el destino hacia atrás
        LinkedList<V> ruta = new LinkedList<>();
        int pasoActual = columnaDestino;
        ruta.addFirst(listaCiudades.get(pasoActual));

        while (pasoActual != filaOrigen) {
            pasoActual = matrizPredecesores[filaOrigen][pasoActual];
            ruta.addFirst(listaCiudades.get(pasoActual));
        }

        return new ArrayList<>(ruta);
    }
}

