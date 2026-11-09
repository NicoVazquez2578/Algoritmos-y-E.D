package GrafosNoDirigidos;

import java.util.*;

public class UndirectedGraph<V> {

    private final Map<V, List<Arista<V>>> adj = new LinkedHashMap<>();

    public static class Arista<V> {
        public final V origen;
        public final V destino;
        public final double peso;

        public Arista(V origen, V destino, double peso) {
            this.origen = origen;
            this.destino = destino;
            this.peso = peso;
        }

        @Override
        public String toString() {
            return origen + " --" + peso + "--> " + destino;
        }
    }

    public void agregarVertice(V v) {
        adj.putIfAbsent(v, new ArrayList<>());
    }

    public void agregarArista(V u, V v, double peso) {
        agregarVertice(u);
        agregarVertice(v);
        adj.get(u).add(new Arista<>(u, v, peso));
        adj.get(v).add(new Arista<>(v, u, peso));
    }

    public void agregarArista(V u, V v) {
        agregarArista(u, v, 1.0);
    }

    public Set<V> vertices() {
        return Collections.unmodifiableSet(adj.keySet());
    }

    public List<Arista<V>> adyacencias(V v) {
        return adj.getOrDefault(v, Collections.emptyList());
    }

    public int cantidadDeVertices() {
        return adj.size();
    }

    public boolean esConexo() {
        if (adj.isEmpty()) return true;
        V inicio = adj.keySet().iterator().next();
        Set<V> visitados = new HashSet<>();
        Queue<V> cola = new LinkedList<>();
        cola.add(inicio);
        visitados.add(inicio);
        while (!cola.isEmpty()) {
            V curr = cola.poll();
            for (Arista<V> a : adj.get(curr))
                if (visitados.add(a.destino)) cola.add(a.destino);
        }
        return visitados.size() == adj.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (V v : adj.keySet()) {
            sb.append(v).append(" -> ").append(adj.get(v)).append("\n");
        }
        return sb.toString();
    }
}
