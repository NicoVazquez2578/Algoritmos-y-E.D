package ucu.edu.aed.ProblemSets.ProblemSet2.Ej12;

import java.util.List;

/**
 * EJERCICIO 12 — El Grimorio del Archimago
 *
 * Construye el grimorio con los 10 hechizos de Aldric y demuestra las
 * dos operaciones principales.
 */
public class MainGrimorio {

    public static void main(String[] args) {
        System.out.println("=== EJERCICIO 12: El Grimorio del Archimago ===\n");

        GrimorioBST grimorio = new GrimorioBST();

        // Insertar los hechizos en el orden dado por el enunciado
        Hechizo[] hechizos = {
            new Hechizo(42, "Fireball"),
            new Hechizo(17, "Ice Lance"),
            new Hechizo(58, "Thunder"),
            new Hechizo( 9, "Invisibility"),
            new Hechizo(31, "Levitate"),
            new Hechizo(73, "Summon"),
            new Hechizo(25, "Heal"),
            new Hechizo(50, "Teleport"),
            new Hechizo(65, "Shield"),
            new Hechizo(88, "Curse")
        };

        System.out.println("Insertando hechizos en el grimorio:");
        for (Hechizo h : hechizos) {
            grimorio.insertar(h);
            System.out.println("  " + h);
        }

        System.out.println("\nGrimorio en inorden (orden por ID):");
        for (Hechizo h : grimorio.inorden()) {
            System.out.println("  " + h + (h.esProhibido() ? "  ← PROHIBIDO" : ""));
        }

        System.out.println("\n--- Hechizos prohibidos (ID impar) ---");
        List<Hechizo> prohibidos = grimorio.consultarHechizosProhibidos();
        for (Hechizo h : prohibidos) {
            System.out.println("  " + h);
        }

        System.out.println("\n--- Cántico secreto de Aldric ---");
        System.out.println("  \"" + grimorio.generarCantico() + "\"");
    }
}
