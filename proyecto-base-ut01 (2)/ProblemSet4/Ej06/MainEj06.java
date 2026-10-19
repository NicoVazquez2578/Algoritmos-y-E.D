package ProblemSet4.Ej06;

import ProblemSet4.impl.DirectedGraph;
import ProblemSet4.impl.DirectedGraphAlgorithms;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;
import ucu.edu.aed.tda.grafo.model.result.IDijkstraResult;
import ucu.edu.aed.tda.grafo.model.result.IFloydWarshallResult;

import java.io.*;
import java.util.List;

public class MainEj06 {

    public static void main(String[] args) throws IOException {

        // ── Parte 1.1: Demostrar tieneCiclos ────────────────────────────────
        System.out.println("=== Parte 1.1 - tieneCiclos ===");

        DirectedGraph<String, WeightedEdge> sinCiclo = new DirectedGraph<>();
        sinCiclo.agregarArista("A", "B", new WeightedEdge(1.0));
        sinCiclo.agregarArista("B", "C", new WeightedEdge(2.0));
        sinCiclo.agregarArista("A", "C", new WeightedEdge(5.0));
        System.out.println("Grafo sin ciclo → tieneCiclos: " + sinCiclo.tieneCiclos());

        DirectedGraph<String, WeightedEdge> conCiclo = new DirectedGraph<>();
        conCiclo.agregarArista("A", "B", new WeightedEdge(1.0));
        conCiclo.agregarArista("B", "C", new WeightedEdge(2.0));
        conCiclo.agregarArista("C", "A", new WeightedEdge(3.0));
        System.out.println("Grafo con ciclo → tieneCiclos: " + conCiclo.tieneCiclos());

        // ── Parte 1.2: Ordenamiento topológico (Kahn) ───────────────────────
        System.out.println("\n=== Parte 1.2 - Orden Topológico ===");

        DirectedGraph<String, WeightedEdge> dag = new DirectedGraph<>();
        dag.agregarArista("compilar",   "testear",    new WeightedEdge(1.0));
        dag.agregarArista("compilar",   "empaquetar", new WeightedEdge(1.0));
        dag.agregarArista("testear",    "empaquetar", new WeightedEdge(1.0));
        dag.agregarArista("empaquetar", "desplegar",  new WeightedEdge(1.0));
        dag.agregarArista("configurar", "compilar",   new WeightedEdge(1.0));

        DirectedGraphAlgorithms alg = new DirectedGraphAlgorithms();
        List<String> orden = alg.calcularClasificacionTopologica(dag);
        System.out.println("Orden topológico: " + orden);

        // ── Parte 1.3: Ejecutar con archivos conexiones_2 / aeropuertos_2 ───
        System.out.println("\n=== Parte 1.3 - Grafo desde archivos ===");
        String baseDir = new File("ProblemSet4/Ej06/aeropuertos_2.txt").exists()
                ? "ProblemSet4/Ej06/"
                : "proyecto-base-ut01 (2)/ProblemSet4/Ej06/";
        DirectedGraph<String, WeightedEdge> grafoDatos = cargarGrafo(
                baseDir + "aeropuertos_2.txt", baseDir + "conexiones_2.txt");
        System.out.println("Vértices: " + grafoDatos.vertices());
        System.out.println("¿Tiene ciclos? → " + grafoDatos.tieneCiclos());

        // ── Parte 1.4: Dijkstra ──────────────────────────────────────────────
        System.out.println("\n=== Parte 1.4 - Dijkstra desde B ===");

        DirectedGraph<String, WeightedEdge> g = new DirectedGraph<>();
        g.agregarArista("A", "B", new WeightedEdge(1.0));
        g.agregarArista("A", "C", new WeightedEdge(2.0));
        g.agregarArista("A", "D", new WeightedEdge(7.0));
        g.agregarArista("B", "A", new WeightedEdge(7.0));
        g.agregarArista("B", "C", new WeightedEdge(1.0));
        g.agregarArista("B", "D", new WeightedEdge(2.0));
        g.agregarArista("C", "D", new WeightedEdge(3.0));
        g.agregarArista("D", "A", new WeightedEdge(6.0));
        g.agregarArista("D", "C", new WeightedEdge(4.0));
        g.agregarArista("D", "E", new WeightedEdge(4.0));
        g.agregarArista("E", "B", new WeightedEdge(2.0));
        g.agregarArista("E", "D", new WeightedEdge(8.0));

        IDijkstraResult<String> dijk = alg.dijkstra(g.construirComparable("B"), g);
        for (String v : new String[]{"A", "B", "C", "D", "E"}) {
            System.out.printf("  B → %-2s  costo=%.0f  camino=%s%n",
                    v, dijk.getCost(v), dijk.getPath(v));
        }

        // ── Parte 1.5: Floyd-Warshall (Figura 1) ────────────────────────────
        System.out.println("\n=== Parte 1.5 - Floyd-Warshall (Figura 1) ===");

        DirectedGraph<String, WeightedEdge> fig1 = new DirectedGraph<>();
        fig1.agregarArista("B", "A", new WeightedEdge(6.0));
        fig1.agregarArista("A", "D", new WeightedEdge(4.0));
        fig1.agregarArista("D", "B", new WeightedEdge(3.0));
        fig1.agregarArista("A", "C", new WeightedEdge(1.0));
        fig1.agregarArista("C", "D", new WeightedEdge(5.0));
        fig1.agregarArista("D", "E", new WeightedEdge(2.0));
        fig1.agregarArista("E", "B", new WeightedEdge(3.0));
        fig1.agregarArista("C", "E", new WeightedEdge(1.0));

        IFloydWarshallResult<String> fw = alg.floyd(fig1);
        System.out.println("Camino A → E: " + fw.getPath("A", "E") + "  costo=" + fw.getCost("A", "E"));

        System.out.println("\nExcentricidades:");
        String[] nodos = {"A", "B", "C", "D", "E"};
        String centro = null;
        double minEcc = Double.POSITIVE_INFINITY;
        for (String v : nodos) {
            double ecc = 0;
            for (String u : nodos) {
                double d = fw.getCost(v, u);
                if (d > ecc && d < Double.POSITIVE_INFINITY) ecc = d;
            }
            System.out.printf("  ecc(%s) = %.0f%n", v, ecc);
            if (ecc < minEcc) { minEcc = ecc; centro = v; }
        }
        System.out.println("Centro del grafo: " + centro + " (excentricidad=" + minEcc + ")");

        // ── Parte 2: Camino Crítico ─────────────────────────────────────────
        System.out.println("\n=== Parte 2 - Camino Crítico ===");

        DirectedGraph<String, WeightedEdge> proyecto = new DirectedGraph<>();
        proyecto.agregarArista("Inicio",     "DiseñoDB",    new WeightedEdge(3.0));
        proyecto.agregarArista("Inicio",     "DiseñoUI",    new WeightedEdge(5.0));
        proyecto.agregarArista("DiseñoDB",   "BackEnd",     new WeightedEdge(8.0));
        proyecto.agregarArista("DiseñoUI",   "FrontEnd",    new WeightedEdge(6.0));
        proyecto.agregarArista("BackEnd",    "Integración", new WeightedEdge(4.0));
        proyecto.agregarArista("FrontEnd",   "Integración", new WeightedEdge(4.0));
        proyecto.agregarArista("DiseñoDB",   "Pruebas",     new WeightedEdge(12.0));
        proyecto.agregarArista("Integración","Pruebas",     new WeightedEdge(3.0));
        proyecto.agregarArista("Pruebas",    "Fin",         new WeightedEdge(2.0));

        CaminoCritico.Resultado<String> res = CaminoCritico.calcular(proyecto);
        System.out.println("Camino crítico: " + res.caminoCritico);
        System.out.printf("Costo total: %.0f días%n", res.costoCritico);
        System.out.println("\nTodas las secuencias:");
        for (int i = 0; i < res.otrosCaminos.size(); i++) {
            System.out.printf("  %s  costo=%.0f  holgura=%.0f%n",
                    res.otrosCaminos.get(i), res.costos.get(i), res.holguras.get(i));
        }
    }

    private static DirectedGraph<String, WeightedEdge> cargarGrafo(
            String archivoAeropuertos, String archivoConexiones) throws IOException {
        DirectedGraph<String, WeightedEdge> g = new DirectedGraph<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivoAeropuertos))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) g.agregarVertice(line);
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(archivoConexiones))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                g.agregarArista(parts[0].trim(), parts[1].trim(),
                        new WeightedEdge(Double.parseDouble(parts[2].trim())));
            }
        }
        return g;
    }
}
