package GrafosNoDirigidos;

import java.util.List;

/**
 * Ejercicio 10 — Empresa láctea: recorrido BFS en grafo no dirigido.
 *
 * Grafo del enunciado (vértices A-H, aristas con peso):
 *   A-B:2  A-C:3  B-D:2  C-D:1  C-F:4
 *   D-E:2  D-F:4  E-F:1  E-G:2  F-H:1  G-H:3
 *
 * Parte 3 — implementación de BEA usando UndirectedGraphAlgorithms.bea()
 */
public class Ej10Main {

    public static void main(String[] args) {
        UndirectedGraph<String> grafo = construirGrafo();

        System.out.println("=== Ejercicio 10: BEA (Búsqueda en Amplitud) ===");
        System.out.println("\nLista de adyacencias:");
        System.out.print(grafo);

        System.out.println("BEA desde vertice 'E':");
        UndirectedGraphAlgorithms.BeaResultado<String> resultado =
                UndirectedGraphAlgorithms.bea(grafo, "E");

        System.out.println("Orden de visita: " + resultado.orden);
        resultado.imprimirArbolAbarcador();

        System.out.println("\nBEA completo (grafo conexo, mismo resultado):");
        List<String> completo = UndirectedGraphAlgorithms.beaCompleto(grafo);
        System.out.println("Orden: " + completo);
    }

    static UndirectedGraph<String> construirGrafo() {
        UndirectedGraph<String> g = new UndirectedGraph<>();
        g.agregarArista("A", "B", 2);
        g.agregarArista("A", "C", 3);
        g.agregarArista("B", "D", 2);
        g.agregarArista("C", "D", 1);
        g.agregarArista("C", "F", 4);
        g.agregarArista("D", "E", 2);
        g.agregarArista("D", "F", 4);
        g.agregarArista("E", "F", 1);
        g.agregarArista("E", "G", 2);
        g.agregarArista("F", "H", 1);
        g.agregarArista("G", "H", 3);
        return g;
    }
}
