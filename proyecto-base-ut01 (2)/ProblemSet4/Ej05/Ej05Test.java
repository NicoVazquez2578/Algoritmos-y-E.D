package ProblemSet4.Ej05;

import ProblemSet4.impl.DirectedGraph;
import ProblemSet4.impl.DirectedGraphAlgorithms;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;
import ucu.edu.aed.tda.grafo.model.result.Path;

import java.util.List;

public class Ej05Test {

    public static void main(String[] args) {
        testCuadro3MontevideoPortoAlegre();
        testOrigenSinSalidas();
        testDestinoInalcanzable();
        testGrafoConCiclos();
        testOrigenEsDestino();
        System.out.println("Todos los tests de Ejercicio 5 pasaron exitosamente!");
    }

    private static void testCuadro3MontevideoPortoAlegre() {
        DirectedGraph<String, WeightedEdge> g = MainEj05.crearGrafoCuadro3();
        DirectedGraphAlgorithms alg = new DirectedGraphAlgorithms();

        List<Path<String>> caminos = alg.obtenerTodosLosCaminos(
                g.construirComparable("Montevideo"),
                g.construirComparable("Porto Alegre"),
                g
        );

        assert caminos.size() == 3 : "Debe haber exactamente 3 caminos en Cuadro 3";

        // Verificar que los costos coincidan con los calculados
        boolean tieneDirecto = false;
        boolean tienePorBA = false;
        boolean tienePorBAyASU = false;

        for (Path<String> p : caminos) {
            if (p.getPath().size() == 2 && p.getCost() == 1000.0) {
                tieneDirecto = true;
            } else if (p.getPath().size() == 3 && p.getCost() == 1450.0) {
                tienePorBA = true;
            } else if (p.getPath().size() == 4 && p.getCost() == 2500.0) {
                tienePorBAyASU = true;
            }
        }

        assert tieneDirecto : "Debe incluir el vuelo directo Montevideo -> Porto Alegre (1000)";
        assert tienePorBA : "Debe incluir Montevideo -> Buenos Aires -> Porto Alegre (1450)";
        assert tienePorBAyASU : "Debe incluir Montevideo -> Buenos Aires -> Asuncion -> Porto Alegre (2500)";
        System.out.println("[PASS] testCuadro3MontevideoPortoAlegre");
    }

    private static void testOrigenSinSalidas() {
        DirectedGraph<String, WeightedEdge> g = new DirectedGraph<>();
        g.agregarVertice("A");
        g.agregarVertice("B");
        DirectedGraphAlgorithms alg = new DirectedGraphAlgorithms();

        List<Path<String>> caminos = alg.obtenerTodosLosCaminos(
                g.construirComparable("A"),
                g.construirComparable("B"),
                g
        );
        assert caminos.isEmpty() : "No debe encontrar caminos si no hay aristas";
        System.out.println("[PASS] testOrigenSinSalidas");
    }

    private static void testDestinoInalcanzable() {
        DirectedGraph<String, WeightedEdge> g = MainEj05.crearGrafoCuadro3();
        DirectedGraphAlgorithms alg = new DirectedGraphAlgorithms();

        // En Cuadro 3, Rio de Janeiro solo va a Curitiba y Curitiba solo va a Rio. Nadie llega a Rio salvo Curitiba.
        // POA -> MVD -> (BA -> ASU -> CUR -> RIO)
        List<Path<String>> caminos = alg.obtenerTodosLosCaminos(
                g.construirComparable("Rio de Janeiro"),
                g.construirComparable("Montevideo"),
                g
        );
        assert caminos.isEmpty() : "Desde Rio de Janeiro no hay forma de salir del ciclo Curitiba <-> Rio en Cuadro 3";
        System.out.println("[PASS] testDestinoInalcanzable");
    }

    private static void testGrafoConCiclos() {
        // Grafo con ciclo fuerte A -> B -> C -> A, y B -> D
        DirectedGraph<String, WeightedEdge> g = new DirectedGraph<>();
        g.agregarArista("A", "B", new WeightedEdge(10));
        g.agregarArista("B", "C", new WeightedEdge(20));
        g.agregarArista("C", "A", new WeightedEdge(30));
        g.agregarArista("B", "D", new WeightedEdge(15));
        g.agregarArista("C", "D", new WeightedEdge(5));

        DirectedGraphAlgorithms alg = new DirectedGraphAlgorithms();
        List<Path<String>> caminos = alg.obtenerTodosLosCaminos(
                g.construirComparable("A"),
                g.construirComparable("D"),
                g
        );

        // Caminos simples posibles:
        // A -> B -> D (10 + 15 = 25)
        // A -> B -> C -> D (10 + 20 + 5 = 35)
        assert caminos.size() == 2 : "Debe encontrar exactamente 2 caminos simples sin entrar en bucle infinito por el ciclo";
        System.out.println("[PASS] testGrafoConCiclos");
    }

    private static void testOrigenEsDestino() {
        DirectedGraph<String, WeightedEdge> g = new DirectedGraph<>();
        g.agregarVertice("A");
        DirectedGraphAlgorithms alg = new DirectedGraphAlgorithms();

        List<Path<String>> caminos = alg.obtenerTodosLosCaminos(
                g.construirComparable("A"),
                g.construirComparable("A"),
                g
        );
        assert caminos.size() == 1 : "Origen igual a destino debe devolver un camino trivial";
        assert caminos.get(0).getCost() == 0.0 : "El costo del camino trivial debe ser 0";
        System.out.println("[PASS] testOrigenEsDestino");
    }
}

