package ucu.edu.aed.molecular.grafo.resultado;

import ucu.edu.aed.tda.grafo.model.result.IFloydWarshallResult;

import java.util.*;

public class ResultadoFloyd<V> implements IFloydWarshallResult<V> {

    private final List<V> vertices;
    private final double[][] matrizCostos;
    private final int[][] matrizP;
    private final Map<V, Integer> indice;

    public ResultadoFloyd(List<V> vertices, double[][] matrizCostos, int[][] matrizP) {
        this.vertices     = vertices;
        this.matrizCostos = matrizCostos;
        this.matrizP      = matrizP;
        this.indice       = new HashMap<>();
        for (int i = 0; i < vertices.size(); i++) indice.put(vertices.get(i), i);
    }

    @Override
    public double getCost(V from, V to) {
        return matrizCostos[indice.get(from)][indice.get(to)];
    }

    @Override
    public boolean connected(V from, V to) {
        return matrizCostos[indice.get(from)][indice.get(to)] < Double.POSITIVE_INFINITY;
    }

    @Override
    public List<V> getPath(V from, V to) {
        int s = indice.get(from), t = indice.get(to);
        if (matrizP[s][t] == -1 && s != t) return null;
        LinkedList<V> path = new LinkedList<>();
        int cur = t;
        path.addFirst(vertices.get(cur));
        while (cur != s) {
            cur = matrizP[s][cur];
            path.addFirst(vertices.get(cur));
        }
        return new ArrayList<>(path);
    }
}
