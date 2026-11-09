package GrafosNoDirigidos;

import java.util.*;

public class UndirectedGraphAlgorithms {

    // ── BEA (Breadth-First Search) ────────────────────────────────────────────
    // Precondición: grafo no nulo; origen pertenece al grafo.
    // Recorre nivel por nivel usando cola FIFO; garantiza visitar cada vértice
    // exactamente una vez.  Retorna el orden de visita y el árbol abarcador como
    // mapa padre[v] = quién descubrió a v.

    public static <V> BeaResultado<V> bea(UndirectedGraph<V> grafo, V origen) {
        List<V> orden = new ArrayList<>();
        Map<V, V> padre = new LinkedHashMap<>();
        Set<V> visitados = new LinkedHashSet<>();
        Queue<V> cola = new LinkedList<>();

        visitados.add(origen);
        padre.put(origen, null);
        cola.add(origen);

        while (!cola.isEmpty()) {
            V actual = cola.poll();
            orden.add(actual);
            for (UndirectedGraph.Arista<V> arista : grafo.adyacencias(actual)) {
                V vecino = arista.destino;
                if (visitados.add(vecino)) {
                    padre.put(vecino, actual);
                    cola.add(vecino);
                }
            }
        }
        return new BeaResultado<>(orden, padre);
    }

    // BEA sobre grafo posiblemente no conexo
    public static <V> List<V> beaCompleto(UndirectedGraph<V> grafo) {
        List<V> total = new ArrayList<>();
        Set<V> visitados = new HashSet<>();
        for (V v : grafo.vertices()) {
            if (!visitados.contains(v)) {
                BeaResultado<V> r = bea(grafo, v);
                total.addAll(r.orden);
                visitados.addAll(r.orden);
            }
        }
        return total;
    }

    // ── Puntos de articulación (Tarjan DFS) ──────────────────────────────────
    // Precondición: grafo conexo no dirigido.
    // Un vértice v es punto de articulación si su eliminación desconecta el grafo.
    // disc[v]: tiempo de descubrimiento; low[v]: menor disc alcanzable desde el
    // subárbol de v. v es AP si:
    //   - es raíz del DFS con >1 hijo, O
    //   - no es raíz y existe un hijo w tal que low[w] >= disc[v].

    public static <V> Set<V> puntosDeArticulacion(UndirectedGraph<V> grafo) {
        Set<V> ap = new LinkedHashSet<>();
        Map<V, Integer> disc = new HashMap<>();
        Map<V, Integer> low = new HashMap<>();
        Map<V, V> padre = new HashMap<>();
        int[] timer = {0};

        for (V v : grafo.vertices())
            if (!disc.containsKey(v))
                dfsAP(v, grafo, disc, low, padre, ap, timer);

        return ap;
    }

    private static <V> void dfsAP(V v, UndirectedGraph<V> grafo,
                                   Map<V, Integer> disc, Map<V, Integer> low,
                                   Map<V, V> padre, Set<V> ap, int[] timer) {
        disc.put(v, timer[0]);
        low.put(v, timer[0]);
        timer[0]++;
        int hijos = 0;

        for (UndirectedGraph.Arista<V> e : grafo.adyacencias(v)) {
            V w = e.destino;
            if (!disc.containsKey(w)) {
                hijos++;
                padre.put(w, v);
                dfsAP(w, grafo, disc, low, padre, ap, timer);
                low.put(v, Math.min(low.get(v), low.get(w)));
                // raíz con más de un hijo en el árbol DFS
                if (padre.get(v) == null && hijos > 1) ap.add(v);
                // no-raíz: ningún descendiente llega "por encima" de v
                if (padre.get(v) != null && low.get(w) >= disc.get(v)) ap.add(v);
            } else if (!w.equals(padre.get(v))) {
                // arco de retroceso: actualiza low
                low.put(v, Math.min(low.get(v), disc.get(w)));
            }
        }
    }

    // ── Resultado de BEA ─────────────────────────────────────────────────────

    public static class BeaResultado<V> {
        public final List<V> orden;
        public final Map<V, V> padre; // padre[v] = vértice que lo descubrió

        public BeaResultado(List<V> orden, Map<V, V> padre) {
            this.orden = orden;
            this.padre = padre;
        }

        public void imprimirArbolAbarcador() {
            System.out.println("Arbol abarcador BFS:");
            for (Map.Entry<V, V> e : padre.entrySet()) {
                if (e.getValue() != null)
                    System.out.println("  " + e.getValue() + " -> " + e.getKey());
            }
        }
    }
}
