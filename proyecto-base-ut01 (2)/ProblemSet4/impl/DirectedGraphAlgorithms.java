package ProblemSet4.impl;

import ucu.edu.aed.tda.grafo.IDirectedGraphAlgorithms;
import ucu.edu.aed.tda.grafo.IDirectedIGraph;
import ucu.edu.aed.tda.grafo.model.IGraph;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;
import ucu.edu.aed.tda.grafo.model.result.*;

import java.util.*;
import java.util.function.Consumer;

public class DirectedGraphAlgorithms implements IDirectedGraphAlgorithms {

    // ── Topológico (Kahn) ────────────────────────────────────────────────────

    @Override
    public <V, D> List<V> calcularClasificacionTopologica(IDirectedIGraph<V, D> g) {
        Map<V, Integer> inDegree = new HashMap<>();
        for (V v : g.vertices()) inDegree.put(v, 0);
        for (Edge<V, D> e : g.aristas())
            inDegree.merge(e.target(), 1, Integer::sum);

        Queue<V> queue = new LinkedList<>();
        for (Map.Entry<V, Integer> entry : inDegree.entrySet())
            if (entry.getValue() == 0) queue.add(entry.getKey());

        List<V> orden = new ArrayList<>();
        while (!queue.isEmpty()) {
            V v = queue.poll();
            orden.add(v);
            for (V w : g.successors(g.construirComparable(v))) {
                int d = inDegree.get(w) - 1;
                inDegree.put(w, d);
                if (d == 0) queue.add(w);
            }
        }

        if (orden.size() != g.cantidadDeVertices())
            throw new IllegalStateException("El grafo tiene ciclos, no existe orden topológico.");
        return orden;
    }

    // ── Dijkstra ─────────────────────────────────────────────────────────────

    @Override
    public <V, D extends WeightedEdge> IDijkstraResult<V> dijkstra(Comparable<V> source, IDirectedIGraph<V, D> grafo) {
        V origen = grafo.buscarVertice(source);
        Map<V, Double> dist = new HashMap<>();
        Map<V, V> pred = new HashMap<>();
        for (V v : grafo.vertices()) dist.put(v, Double.POSITIVE_INFINITY);
        dist.put(origen, 0.0);

        PriorityQueue<V> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(origen);
        Set<V> visitados = new HashSet<>();

        while (!pq.isEmpty()) {
            V u = pq.poll();
            if (!visitados.add(u)) continue;
            for (Edge<V, D> e : grafo.adyacencias(grafo.construirComparable(u))) {
                double w = e.dato().getWeight();
                double alt = dist.get(u) + w;
                if (alt < dist.get(e.target())) {
                    dist.put(e.target(), alt);
                    pred.put(e.target(), u);
                    pq.add(e.target());
                }
            }
        }
        return new ResultadoDijkstra<>(origen, dist, pred);
    }

    // ── Floyd-Warshall ───────────────────────────────────────────────────────

    @Override
    public <V, D extends WeightedEdge> IFloydWarshallResult<V> floyd(IDirectedIGraph<V, D> grafo) {
        List<V> verts = new ArrayList<>(grafo.vertices());
        int n = verts.size();
        Map<V, Integer> idx = new HashMap<>();
        for (int i = 0; i < n; i++) idx.put(verts.get(i), i);

        double[][] dist = new double[n][n];
        int[][] prec = new int[n][n];
        for (double[] row : dist) Arrays.fill(row, Double.POSITIVE_INFINITY);
        for (int[] row : prec) Arrays.fill(row, -1);
        for (int i = 0; i < n; i++) dist[i][i] = 0;

        for (Edge<V, D> e : grafo.aristas()) {
            int s = idx.get(e.source()), t = idx.get(e.target());
            dist[s][t] = e.dato().getWeight();
            prec[s][t] = s;
        }

        for (int k = 0; k < n; k++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        prec[i][j] = prec[k][j];
                    }

        return new ResultadoFloyd<>(verts, dist, prec);
    }

    @Override
    public <V, D extends WeightedEdge> IFloydWarshallResult<V> warshall(IDirectedIGraph<V, D> grafo) {
        return floyd(grafo);
    }

    // ── Centro y excentricidad ────────────────────────────────────────────────

    @Override
    public <V, D extends WeightedEdge> V obtenerCentroGrafo(IDirectedIGraph<V, D> grafo) {
        IFloydWarshallResult<V> fw = floyd(grafo);
        V centro = null;
        double minEcc = Double.POSITIVE_INFINITY;
        for (V v : grafo.vertices()) {
            double ecc = obtenerExcentricidad(fw, grafo.vertices(), v);
            if (ecc < minEcc) { minEcc = ecc; centro = v; }
        }
        return centro;
    }

    @Override
    public <V, D extends WeightedEdge> double obtenerExcentricidad(IDirectedIGraph<V, D> grafo, Comparable<V> vertexCriteria) {
        V v = grafo.buscarVertice(vertexCriteria);
        IFloydWarshallResult<V> fw = floyd(grafo);
        return obtenerExcentricidad(fw, grafo.vertices(), v);
    }

    private <V> double obtenerExcentricidad(IFloydWarshallResult<V> fw, Set<V> vertices, V v) {
        double ecc = 0;
        for (V u : vertices) {
            double d = fw.getCost(v, u);
            if (d > ecc && d < Double.POSITIVE_INFINITY) ecc = d;
        }
        return ecc;
    }

    // ── Ejercicio 5: Obtener todos los caminos posibles (DFS con Backtracking) ──

    /**
     * Busca y retorna una lista con todos los caminos posibles entre una ciudad origen
     * y una ciudad destino, calculando el costo total de cada viaje.
     */
    @Override
    public <V, D extends WeightedEdge> List<Path<V>> obtenerTodosLosCaminos(
            Comparable<V> source, Comparable<V> target, IGraph<V, D> grafo) {

        V ciudadOrigen = grafo.buscarVertice(source);
        V ciudadDestino = grafo.buscarVertice(target);
        List<Path<V>> todosLosCaminos = new ArrayList<>();

        // Si alguna de las dos ciudades no existe en el mapa, no hay caminos
        if (ciudadOrigen == null || ciudadDestino == null) {
            return todosLosCaminos;
        }

        // Conjunto auxiliar para saber qué ciudades ya visitamos en la ruta actual y no repetir
        Set<V> visitadosEnRutaActual = new HashSet<>();
        List<V> rutaActual = new ArrayList<>();

        // Iniciamos la búsqueda recursiva desde el origen con costo acumulado 0
        buscarCaminosRecursivo(ciudadOrigen, ciudadDestino, grafo, rutaActual, visitadosEnRutaActual, 0.0, todosLosCaminos);

        return todosLosCaminos;
    }

    /**
     * Método auxiliar que avanza paso a paso explorando los vuelos disponibles.
     * Utiliza la técnica de "Backtracking" (retroceso): avanza por una ruta y cuando llega al final,
     * retrocede un paso para probar otra alternativa.
     */
    private <V, D extends WeightedEdge> void buscarCaminosRecursivo(
            V ciudadActual, V ciudadDestino, IGraph<V, D> grafo,
            List<V> rutaActual, Set<V> visitadosEnRutaActual, double costoAcumulado, List<Path<V>> resultadoFinal) {

        // 1. Agregamos la ciudad actual a nuestra ruta en curso
        rutaActual.add(ciudadActual);
        visitadosEnRutaActual.add(ciudadActual);

        // 2. CASO BASE: ¿Llegamos al destino?
        if (ciudadActual.equals(ciudadDestino)) {
            // Guardamos una copia de la ruta completa encontrada y su costo total
            resultadoFinal.add(new Path<>(new ArrayList<>(rutaActual), costoAcumulado));
        } else {
            // 3. CASO RECURSIVO: Si todavía no llegamos, vemos a qué ciudades podemos volar desde acá
            for (Edge<V, D> conexion : grafo.adyacencias(grafo.construirComparable(ciudadActual))) {
                V ciudadSiguiente = conexion.target();
                double precioVuelo = conexion.dato().getWeight();

                // Si la ciudad siguiente NO está en la ruta actual, volamos hacia ella
                if (!visitadosEnRutaActual.contains(ciudadSiguiente)) {
                    buscarCaminosRecursivo(
                            ciudadSiguiente, ciudadDestino, grafo,
                            rutaActual, visitadosEnRutaActual,
                            costoAcumulado + precioVuelo, resultadoFinal);
                }
                // Si ya estaba en la ruta actual, la ignoramos para no dar vueltas en círculo (ciclos)
            }
        }

        // 4. BACKTRACKING (Retroceso):
        // Sacamos esta ciudad de la ruta actual para poder probar otros caminos que también puedan pasar por ella
        rutaActual.remove(rutaActual.size() - 1);
        visitadosEnRutaActual.remove(ciudadActual);
    }

    // ── Recorridos ────────────────────────────────────────────────────────────

    @Override
    public <V, D> void recorridoEnProfundidad(IGraph<V, D> grafo, Comparable<V> sourceCriteria, Consumer<V> consumer) {
        V inicio = grafo.buscarVertice(sourceCriteria);
        if (inicio == null) return;
        Set<V> visitado = new LinkedHashSet<>();
        dfsVisit(inicio, grafo, visitado, consumer);
    }

    private <V, D> void dfsVisit(V v, IGraph<V, D> grafo, Set<V> visitado, Consumer<V> consumer) {
        if (!visitado.add(v)) return;
        consumer.accept(v);
        for (Edge<V, D> e : grafo.adyacencias(grafo.construirComparable(v)))
            dfsVisit(e.target(), grafo, visitado, consumer);
    }

    @Override
    public <V, D> void recorridoEnAmplitud(IGraph<V, D> grafo, Comparable<V> sourceCriteria, Consumer<V> consumer) {
        V inicio = grafo.buscarVertice(sourceCriteria);
        if (inicio == null) return;
        Set<V> visitado = new LinkedHashSet<>();
        Queue<V> queue = new LinkedList<>();
        queue.add(inicio);
        visitado.add(inicio);
        while (!queue.isEmpty()) {
            V v = queue.poll();
            consumer.accept(v);
            for (Edge<V, D> e : grafo.adyacencias(grafo.construirComparable(v)))
                if (visitado.add(e.target())) queue.add(e.target());
        }
    }

}
