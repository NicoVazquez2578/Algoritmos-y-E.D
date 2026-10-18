package ProblemSet4.Ej05;

import ProblemSet4.impl.DirectedGraph;
import ProblemSet4.impl.DirectedGraphAlgorithms;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;
import ucu.edu.aed.tda.grafo.model.result.Path;

import java.util.List;

public class MainEj05 {

    public static void main(String[] args) {

        System.out.println("  EJERCICIO 5 - TODOS LOS CAMINOS: MONTEVIDEO -> PORTO ALEGRE   ");

        DirectedGraphAlgorithms algorithms = new DirectedGraphAlgorithms();

        // -------------------------------------------------------------
        // Escenario A: Grafo de 6 ciudades (Cuadro 3 del Ejercicio 4)
        // -------------------------------------------------------------
        System.out.println("Escenario A: Grafo Cuadro 3 (6 Ciudades - TA4 Ejercicio 4)");
        DirectedGraph<String, WeightedEdge> grafo6 = crearGrafoCuadro3();
        List<Path<String>> caminos6 = algorithms.obtenerTodosLosCaminos(
                grafo6.construirComparable("Montevideo"),
                grafo6.construirComparable("Porto Alegre"),
                grafo6
        );

        System.out.printf("Se encontraron %d caminos posibles de Montevideo a Porto Alegre:%n", caminos6.size());
        for (int i = 0; i < caminos6.size(); i++) {
            Path<String> p = caminos6.get(i);
            System.out.printf("  [%d] Itinerario: %-50s Costo Total: %.0f%n",
                    (i + 1), String.join(" -> ", p.getPath()), p.getCost());
        }

        // -------------------------------------------------------------
        // Escenario B: Grafo de 8 ciudades (Cuadro 2 del Ejercicio 3)
        // -------------------------------------------------------------
        System.out.println("Escenario B: Grafo Cuadro 2 (8 Ciudades expandido - Ejercicio 3)");
        DirectedGraph<String, WeightedEdge> grafo8 = crearGrafoCuadro2();
        List<Path<String>> caminos8 = algorithms.obtenerTodosLosCaminos(
                grafo8.construirComparable("Montevideo"),
                grafo8.construirComparable("Porto Alegre"),
                grafo8
        );

        System.out.printf("Se encontraron %d caminos posibles de Montevideo a Porto Alegre:%n", caminos8.size());
        for (int i = 0; i < caminos8.size(); i++) {
            Path<String> p = caminos8.get(i);
            System.out.printf("  [%d] Itinerario: %-50s Costo Total: %.0f%n",
                    (i + 1), String.join(" -> ", p.getPath()), p.getCost());
        }
    }

    /**
     * Construye el grafo de acuerdo al Cuadro 3 (Ejercicio 4)
     */
    public static DirectedGraph<String, WeightedEdge> crearGrafoCuadro3() {
        DirectedGraph<String, WeightedEdge> g = new DirectedGraph<>();

        // Vértices
        g.agregarVertice("Asunción");
        g.agregarVertice("Buenos Aires");
        g.agregarVertice("Curitiba");
        g.agregarVertice("Montevideo");
        g.agregarVertice("Porto Alegre");
        g.agregarVertice("Rio de Janeiro");

        // Conexiones desde Asunción
        g.agregarArista("Asunción", "Buenos Aires", new WeightedEdge(1600));
        g.agregarArista("Asunción", "Curitiba",     new WeightedEdge(800));
        g.agregarArista("Asunción", "Porto Alegre", new WeightedEdge(700));

        // Conexiones desde Buenos Aires
        g.agregarArista("Buenos Aires", "Asunción",     new WeightedEdge(1600));
        g.agregarArista("Buenos Aires", "Montevideo",   new WeightedEdge(200));
        g.agregarArista("Buenos Aires", "Porto Alegre", new WeightedEdge(1250));

        // Conexiones desde Curitiba
        g.agregarArista("Curitiba", "Rio de Janeiro", new WeightedEdge(1500));

        // Conexiones desde Montevideo
        g.agregarArista("Montevideo", "Buenos Aires", new WeightedEdge(200));
        g.agregarArista("Montevideo", "Porto Alegre", new WeightedEdge(1000));

        // Conexiones desde Porto Alegre
        g.agregarArista("Porto Alegre", "Montevideo", new WeightedEdge(1000));

        // Conexiones desde Rio de Janeiro
        g.agregarArista("Rio de Janeiro", "Curitiba", new WeightedEdge(1500));

        return g;
    }

    /**
     * Construye el grafo de acuerdo al Cuadro 2 (Ejercicio 3)
     */
    public static DirectedGraph<String, WeightedEdge> crearGrafoCuadro2() {
        DirectedGraph<String, WeightedEdge> g = new DirectedGraph<>();

        // Conexiones desde Asunción
        g.agregarArista("Asunción", "Buenos Aires", new WeightedEdge(1600));
        g.agregarArista("Asunción", "Curitiba",     new WeightedEdge(800));
        g.agregarArista("Asunción", "Porto Alegre", new WeightedEdge(700));

        // Conexiones desde Buenos Aires
        g.agregarArista("Buenos Aires", "Asunción",     new WeightedEdge(1600));
        g.agregarArista("Buenos Aires", "Montevideo",   new WeightedEdge(200));
        g.agregarArista("Buenos Aires", "Porto Alegre", new WeightedEdge(1250));

        // Conexiones desde Curitiba
        g.agregarArista("Curitiba", "Rio de Janeiro", new WeightedEdge(1500));

        // Conexiones desde Montevideo
        g.agregarArista("Montevideo", "Buenos Aires", new WeightedEdge(200));
        g.agregarArista("Montevideo", "Porto Alegre", new WeightedEdge(1000));
        g.agregarArista("Montevideo", "San Pablo",    new WeightedEdge(980));
        g.agregarArista("Montevideo", "Santos",       new WeightedEdge(1200));

        // Conexiones desde Rio de Janeiro
        g.agregarArista("Rio de Janeiro", "Curitiba",  new WeightedEdge(1500));
        g.agregarArista("Rio de Janeiro", "San Pablo", new WeightedEdge(1800));
        g.agregarArista("Rio de Janeiro", "Santos",    new WeightedEdge(1900));

        // Conexiones desde San Pablo
        g.agregarArista("San Pablo", "Asunción",     new WeightedEdge(1200));
        g.agregarArista("San Pablo", "Curitiba",     new WeightedEdge(600));
        g.agregarArista("San Pablo", "Montevideo",   new WeightedEdge(2000));
        g.agregarArista("San Pablo", "Porto Alegre", new WeightedEdge(980));
        g.agregarArista("San Pablo", "Rio de Janeiro", new WeightedEdge(1800));
        g.agregarArista("San Pablo", "Santos",       new WeightedEdge(220));

        // Conexiones desde Santos
        g.agregarArista("Santos", "Porto Alegre", new WeightedEdge(1200));
        g.agregarArista("Santos", "San Pablo",    new WeightedEdge(220));

        return g;
    }
}

