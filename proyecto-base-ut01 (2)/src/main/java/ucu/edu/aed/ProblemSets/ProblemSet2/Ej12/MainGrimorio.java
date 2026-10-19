package ucu.edu.aed.ProblemSets.ProblemSet2.Ej12;

<<<<<<< HEAD
<<<<<<< HEAD
import ucu.edu.aed.tda.TDALista;
 
// Programa de prueba del Ejercicio 12 - El Grimorio del Archimago.
public class MainGrimorio {
 
    public static void main(String[] args) {
        Grimorio grimorio = new Grimorio();
 
        // Se insertan en el orden dado en el enunciado
        grimorio.agregarHechizo(42, "Fireball");
        grimorio.agregarHechizo(17, "Ice Lance");
        grimorio.agregarHechizo(58, "Thunder");
        grimorio.agregarHechizo(9, "Invisibility");
        grimorio.agregarHechizo(31, "Levitate");
        grimorio.agregarHechizo(73, "Summon");
        grimorio.agregarHechizo(25, "Heal");
        grimorio.agregarHechizo(50, "Teleport");
        grimorio.agregarHechizo(65, "Shield");
        grimorio.agregarHechizo(88, "Curse");
 
        // Hechizos prohibidos (ID impar). Esperado: 9, 17, 25, 31, 65, 73
        TDALista<Hechizo> prohibidos = grimorio.hechizosProhibidos();
        System.out.println("Hechizos prohibidos (" + prohibidos.tamaño() + "):");
        for (int i = 0; i < prohibidos.tamaño(); i++) { // es solo una prueba, la lista es chica
            System.out.println("  " + prohibidos.obtener(i));
        }
 
        // Cántico. Esperado: Invisibility - Ice Lance - Heal - Levitate - Shield - Summon
        System.out.println("Cántico: " + grimorio.generarCantico());
 
        // ID repetido: no se inserta
        System.out.println("Insertar ID 42 de nuevo: " + grimorio.agregarHechizo(42, "Otro"));
 
        // Grimorio vacío
        Grimorio vacio = new Grimorio();
        System.out.println("Vacío -> prohibidos: " + vacio.hechizosProhibidos().tamaño()
                + ", cántico: [" + vacio.generarCantico() + "]");
 
        // Grimorio sin hechizos prohibidos (solo IDs pares)
        Grimorio soloPares = new Grimorio();
        soloPares.agregarHechizo(2, "Spark");
        soloPares.agregarHechizo(4, "Frost");
        System.out.println("Solo pares -> prohibidos: " + soloPares.hechizosProhibidos().tamaño()
                + ", cántico: [" + soloPares.generarCantico() + "]");
 
        // Un único prohibido: el cántico no lleva separador
        Grimorio uno = new Grimorio();
        uno.agregarHechizo(7, "Shadow");
        System.out.println("Uno solo -> cántico: [" + uno.generarCantico() + "]");
=======
=======
>>>>>>> e375feff9fbf1b983dd66eb60784089a8f5ecb96
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
<<<<<<< HEAD
>>>>>>> 0a8e58794c3cd21f6f03ec0fe58df85eef8777bf
=======
>>>>>>> e375feff9fbf1b983dd66eb60784089a8f5ecb96
    }
}
