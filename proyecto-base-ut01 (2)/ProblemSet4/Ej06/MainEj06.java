package ProblemSet4.Ej06;

import ProblemSet4.impl.DirectedGraph;
import ProblemSet4.impl.DirectedGraphAlgorithms;
import ucu.edu.aed.tda.grafo.model.result.IDijkstraResult;
import ucu.edu.aed.tda.grafo.model.result.IFloydWarshallResult;

import java.io.*;
import java.util.List;

public class MainEj06 {

    public static void main(String[] args) throws IOException {

        // ── Parte 1.1: Demostrar tieneCiclos ────────────────────────────────
        System.out.println("=== Parte 1.1 - tieneCiclos ===");

        DirectedGraph<String, Double> sinCiclo = new DirectedGraph<>();
        sinCiclo.agregarArista("A", "B", 1.0);
        sinCiclo.agregarArista("B", "C", 2.0);
        sinCiclo.agregarArista("A", "C", 5.0);
        System.out.println("Grafo sin ciclo → tieneCiclos: " + sinCiclo.tieneCiclos()); // false

        DirectedGraph<String, Double> conCiclo = new DirectedGraph<>();
        conCiclo.agregarArista("A", "B", 1.0);
        conCiclo.agregarArista("B", "C", 2.0);
        conCiclo.agregarArista("C", "A", 3.0); // ciclo A→B→C→A
        System.out.println("Grafo con ciclo → tieneCiclos: " + conCiclo.tieneCiclos()); // true

        // ── Parte 1.2: Ordenamiento topológico (Kahn) ───────────────────────
        System.out.println("\n=== Parte 1.2 - Orden Topológico ===");

        DirectedGraph<String, Double> dag = new DirectedGraph<>();
        dag.agregarArista("compilar",   "testear",    1.0);
        dag.agregarArista("compilar",   "empaquetar", 1.0);
        dag.agregarArista("testear",    "empaquetar", 1.0);
        dag.agregarArista("empaquetar", "desplegar",  1.0);
        dag.agregarArista("configurar", "compilar",   1.0);

        DirectedGraphAlgorithms alg = new DirectedGraphAlgorithms();
        List<String> orden = alg.calcularClasificacionTopologica(dag);
        System.out.println("Orden topológico: " + orden);

        // ── Parte 1.3: Ejecutar con archivos conexiones_2 / aeropuertos_2 ───
        System.out.println("\n=== Parte 1.3 - Grafo desde archivos ===");
        String baseDir = "proyecto-base-ut01 (2)/ProblemSet4/Ej06/";
        DirectedGraph<String, Double> grafoDatos = cargarGrafo(baseDir + "aeropuertos_2.txt",
                                                                baseDir + "conexiones_2.txt");
        System.out.println("Vértices: " + grafoDatos.vertices());
        System.out.println("¿Tiene ciclos? → " + grafoDatos.tieneCiclos());

        // ── Parte 1.4: Dijkstra con la matriz del Ej07 ──────────────────────
        System.out.println("\n=== Parte 1.4 - Dijkstra desde B ===");

        DirectedGraph<String, Double> g = new DirectedGraph<>();
        g.agregarArista("A", "B", 1.0); g.agregarArista("A", "C", 2.0); g.agregarArista("A", "D", 7.0);
        g.agregarArista("B", "A", 7.0); g.agregarArista("B", "C", 1.0); g.agregarArista("B", "D", 2.0);
        g.agregarArista("C", "D", 3.0);
        g.agregarArista("D", "A", 6.0); g.agregarArista("D", "C", 4.0); g.agregarArista("D", "E", 4.0);
        g.agregarArista("E", "B", 2.0); g.agregarArista("E", "D", 8.0);

        IDijkstraResult<String> dijk = alg.dijkstra(g, "B");
        for (String v : new String[]{"A", "B", "C", "D", "E"}) {
            System.out.printf("  B → %-2s  costo=%.0f  camino=%s%n",
                    v, dijk.getCost(v), dijk.getPath(v));
        }

        // ── Parte 1.5: Floyd-Warshall con el grafo de Figura 1 ──────────────
        System.out.println("\n=== Parte 1.5 - Floyd-Warshall (Figura 1) ===");

        DirectedGraph<String, Double> fig1 = new DirectedGraph<>();
        fig1.agregarArista("B", "A", 6.0);
        fig1.agregarArista("A", "D", 4.0);
        fig1.agregarArista("D", "B", 3.0);
        fig1.agregarArista("A", "C", 1.0);
        fig1.agregarArista("C", "D", 5.0);
        fig1.agregarArista("D", "E", 2.0);
        fig1.agregarArista("E", "B", 3.0);
        fig1.agregarArista("C", "E", 1.0);

        IFloydWarshallResult<String> fw = alg.floydWarshall(fig1);
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

        DirectedGraph<String, Double> proyecto = new DirectedGraph<>();
        proyecto.agregarArista("Inicio",    "DiseñoDB",   3.0);
        proyecto.agregarArista("Inicio",    "DiseñoUI",   5.0);
        proyecto.agregarArista("DiseñoDB",  "BackEnd",    8.0);
        proyecto.agregarArista("DiseñoUI",  "FrontEnd",   6.0);
        proyecto.agregarArista("BackEnd",   "Integración",4.0);
        proyecto.agregarArista("FrontEnd",  "Integración",4.0);
        proyecto.agregarArista("DiseñoDB",  "Pruebas",    12.0);
        proyecto.agregarArista("Integración","Pruebas",   3.0);
        proyecto.agregarArista("Pruebas",   "Fin",        2.0);

        CaminoCritico.Resultado<String> res = CaminoCritico.calcular(proyecto);
        System.out.println("Camino crítico: " + res.caminoCritico);
        System.out.printf("Costo total: %.0f días%n", res.costoCritico);
        System.out.println("\nTodas las secuencias:");
        for (int i = 0; i < res.otrosCaminos.size(); i++) {
            System.out.printf("  %s  costo=%.0f  holgura=%.0f%n",
                    res.otrosCaminos.get(i), res.costos.get(i), res.holguras.get(i));
        }
    }

    private static DirectedGraph<String, Double> cargarGrafo(String archivoAeropuertos,
                                                               String archivoConexiones) throws IOException {
        DirectedGraph<String, Double> g = new DirectedGraph<>();
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
                g.agregarArista(parts[0].trim(), parts[1].trim(), Double.parseDouble(parts[2].trim()));
            }
        }
        return g;
    }
}
