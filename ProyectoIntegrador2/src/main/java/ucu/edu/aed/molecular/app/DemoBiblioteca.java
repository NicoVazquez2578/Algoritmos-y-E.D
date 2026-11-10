package ucu.edu.aed.molecular.app;

import ucu.edu.aed.molecular.grafo.DirectedGraph;
import ucu.edu.aed.molecular.grafo.UndirectedGraph;
import ucu.edu.aed.molecular.grafo.algoritmos.DirectedGraphAlgorithms;
import ucu.edu.aed.molecular.grafo.algoritmos.UndirectedGraphAlgorithms;
import ucu.edu.aed.tda.grafo.IUndirectedGraph;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;
import ucu.edu.aed.tda.grafo.model.result.IDijkstraResult;
import ucu.edu.aed.tda.grafo.model.result.IFloydWarshallResult;
import ucu.edu.aed.tda.grafo.model.result.Path;

import java.util.*;

/* Demostración de la biblioteca de grafos (Desafío 1) sobre grafos chicos fáciles de seguir a mano. */
public final class DemoBiblioteca {

    private DemoBiblioteca() { }

    public static void ejecutar() {
        DirectedGraphAlgorithms dga = new DirectedGraphAlgorithms();
        UndirectedGraphAlgorithms uga = new UndirectedGraphAlgorithms();

        System.out.println("\n--- GRAFO DIRIGIDO ---");
        System.out.println("A->B(4)  A->C(2)  C->B(1)  B->D(5)  C->D(8)  D->E(3)");
        DirectedGraph<String, WeightedEdge> g = new DirectedGraph<>();
        arco(g, "A", "B", 4); arco(g, "A", "C", 2); arco(g, "C", "B", 1);
        arco(g, "B", "D", 5); arco(g, "C", "D", 8); arco(g, "D", "E", 3);

        System.out.println("Vertices: " + g.vertices() + " | aristas: " + g.cantidadDeAristas());
        System.out.println("Sucesores de C: " + g.successors(g.construirComparable("C"))
                + " | predecesores de B: " + g.predecessors(g.construirComparable("B")));
        System.out.println("Es conexo: " + g.esConexo() + " | tiene ciclos: " + g.tieneCiclos());

        List<String> bpf = new ArrayList<>();
        dga.recorridoEnProfundidad(g, g.construirComparable("A"), bpf::add);
        List<String> bea = new ArrayList<>();
        dga.recorridoEnAmplitud(g, g.construirComparable("A"), bea::add);
        System.out.println("BPF desde A: " + bpf + " | BEA desde A: " + bea);

        IDijkstraResult<String> dj = dga.dijkstra(g.construirComparable("A"), g);
        System.out.println("Dijkstra A->E: " + dj.getPath("E") + " costo " + dj.getCost("E") + "  (A-C-B-D-E = 2+1+5+3 = 11)");

        IFloydWarshallResult<String> fl = dga.floyd(g);
        System.out.println("Floyd C->E: " + fl.getPath("C", "E") + " costo " + fl.getCost("C", "E")
                + " | E llega a A? " + fl.connected("E", "A"));

        List<Path<String>> caminos = dga.obtenerTodosLosCaminos(g.construirComparable("A"), g.construirComparable("D"), g);
        System.out.println("Todos los caminos A->D: " + caminos);
        System.out.println("Clasificacion topologica: " + dga.calcularClasificacionTopologica(g));

        System.out.println("\n--- GRAFO NO DIRIGIDO ---");
        System.out.println("A-B(1)  B-C(2)  A-C(4)  C-D(3)  D-E(5)");
        UndirectedGraph<String, WeightedEdge> u = new UndirectedGraph<>();
        arista(u, "A", "B", 1); arista(u, "B", "C", 2); arista(u, "A", "C", 4);
        arista(u, "C", "D", 3); arista(u, "D", "E", 5);
        System.out.println("Aristas: " + u.cantidadDeAristas() + " | conexo: " + u.esConexo() + " | ciclos: " + u.tieneCiclos());
        List<String> beaU = new ArrayList<>();
        uga.bea(u, beaU::add);
        System.out.println("BEA: " + beaU);
        System.out.println("Prim desde A: " + aristas(uga.prim(u, u.construirComparable("A"))) + "  costo " + costo(uga.prim(u, u.construirComparable("A"))));
        System.out.println("Kruskal:      " + aristas(uga.kruskal(u)) + "  costo " + costo(uga.kruskal(u)) + "  (1+2+3+5 = 11)");
        System.out.println("Puntos de articulacion: " + uga.puntosDeArticulacion(u) + "  (C y D separan el grafo)");

        System.out.println("\n--- CASOS BORDE ---");
        DirectedGraph<String, WeightedEdge> vacio = new DirectedGraph<>();
        System.out.println("Grafo vacio: conexo=" + vacio.esConexo() + ", ciclos=" + vacio.tieneCiclos()
                + ", topologico=" + dga.calcularClasificacionTopologica(vacio));
        System.out.println("Vertice inexistente Z: buscar=" + g.buscarVertice(g.construirComparable("Z"))
                + ", adyacencias=" + g.adyacencias(g.construirComparable("Z"))
                + ", caminos A->Z=" + dga.obtenerTodosLosCaminos(g.construirComparable("A"), g.construirComparable("Z"), g));
        DirectedGraph<String, WeightedEdge> ciclo = new DirectedGraph<>();
        arco(ciclo, "X", "Y", 1); arco(ciclo, "Y", "X", 1);
        try {
            dga.calcularClasificacionTopologica(ciclo);
        } catch (IllegalStateException e) {
            System.out.println("Topologico con ciclo X<->Y: " + e.getMessage());
        }
    }

    private static void arco(DirectedGraph<String, WeightedEdge> g, String a, String b, double w) {
        g.agregarArista(a, b, new WeightedEdge(w));
    }

    private static void arista(UndirectedGraph<String, WeightedEdge> g, String a, String b, double w) {
        g.agregarArista(a, b, new WeightedEdge(w));
    }

    private static String aristas(IUndirectedGraph<String, WeightedEdge> g) {
        List<String> res = new ArrayList<>();
        for (Edge<String, WeightedEdge> e : g.aristas()) res.add(e.source() + "-" + e.target());
        return res.toString();
    }

    private static double costo(IUndirectedGraph<String, WeightedEdge> g) {
        double total = 0;
        for (Edge<String, WeightedEdge> e : g.aristas()) total += e.dato().getWeight();
        return total;
    }
}
