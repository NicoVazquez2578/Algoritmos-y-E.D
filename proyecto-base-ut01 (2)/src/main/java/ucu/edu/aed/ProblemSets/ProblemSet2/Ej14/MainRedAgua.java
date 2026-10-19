package ucu.edu.aed.ProblemSets.ProblemSet2.Ej14;

/**
 * EJERCICIO 14 — Red de distribución de agua
 *
 * Demuestra la simulación usando la configuración del enunciado:
 *
 *           Este (200L cap, 250L inicial → empieza llena y se vacía en t=1)
 *          /    \
 *       Norte   Sur (150L cap cada uno, vacíos)
 *       /   \   / \
 *    Planta  A  C   D (100L cap cada uno, vacíos)
 */
public class MainRedAgua {

    public static void main(String[] args) {
        System.out.println("=== EJERCICIO 14: Red de distribución de agua ===\n");

        RedDistribucion red = new RedDistribucion();

        // Crear nodos (nombre, capacidad, agua inicial)
        red.agregarNodo("Este",   200, 200); // comienza llena → en t=1 se vacía
        red.agregarNodo("Norte",  150,   0);
        red.agregarNodo("Sur",    150,   0);
        red.agregarNodo("Planta", 100,   0);
        red.agregarNodo("A",      100,   0);
        red.agregarNodo("C",      100,   0);
        red.agregarNodo("D",      100,   0);

        // Conectar la red
        red.conectar("Este",   "Norte", "Sur");
        red.conectar("Norte",  "Planta", "A");
        red.conectar("Sur",    "C",      "D");
        // Planta, A, C, D son hojas

        // Mostrar estado inicial
        red.mostrarEstado();

        // Simular 6 pasos
        for (int paso = 1; paso <= 6; paso++) {
            red.avanzarTiempo();
            System.out.println();
            red.mostrarEstado();
        }

        // Consultas finales
        System.out.println("\n=== Resumen final ===");
        for (String n : new String[]{"Este", "Norte", "Sur", "Planta", "A", "C", "D"}) {
            System.out.printf("  %-8s  agua=%.1f  vaciados=%d  enviados=%.1f%n",
                n,
                red.getAguaEnNodo(n),
                red.getVecesVaciado(n),
                red.getLitrosEnviados(n));
        }
    }
}
