package GrafosNoDirigidos;

import java.util.Set;

/**
 * Ejercicio 11 — Red de telecomunicaciones en África: puntos de articulación.
 *
 * Ciudades: Nairobi, Cairo, Monrovia, Garoua, Mekele, Praia
 * Conexiones:
 *   NAIROBI-CAIRO, NAIROBI-MONROVIA, NAIROBI-GAROUA
 *   MONROVIA-GAROUA, MONROVIA-MEKELE, GAROUA-MEKELE
 *   MEKELE-PRAIA
 *
 * Parte 3 — implementación de puntosDeArticulacion() usando Tarjan DFS.
 */
public class Ej11Main {

    public static void main(String[] args) {
        UndirectedGraph<String> grafo = construirRed();

        System.out.println("=== Ejercicio 11: Puntos de Articulación ===");
        System.out.println("\nRed de telecomunicaciones:");
        System.out.print(grafo);
        System.out.println("Conexo: " + grafo.esConexo());

        System.out.println("\nBEA desde 'NAIROBI':");
        UndirectedGraphAlgorithms.BeaResultado<String> bfs =
                UndirectedGraphAlgorithms.bea(grafo, "NAIROBI");
        System.out.println("Orden: " + bfs.orden);

        Set<String> ap = UndirectedGraphAlgorithms.puntosDeArticulacion(grafo);
        System.out.println("\nPuntos de articulacion encontrados: " + ap);
        System.out.println();
        for (String ciudad : ap) {
            System.out.println("-> " + ciudad
                + ": su falla aisla partes de la red.");
        }
    }

    static UndirectedGraph<String> construirRed() {
        UndirectedGraph<String> g = new UndirectedGraph<>();
        g.agregarArista("NAIROBI",  "CAIRO");
        g.agregarArista("NAIROBI",  "MONROVIA");
        g.agregarArista("NAIROBI",  "GAROUA");
        g.agregarArista("MONROVIA", "GAROUA");
        g.agregarArista("MONROVIA", "MEKELE");
        g.agregarArista("GAROUA",   "MEKELE");
        g.agregarArista("MEKELE",   "PRAIA");
        return g;
    }
}
