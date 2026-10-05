package ucu.edu.aed.ProblemSets.ProblemSet2.Ej12;

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
    }
}
