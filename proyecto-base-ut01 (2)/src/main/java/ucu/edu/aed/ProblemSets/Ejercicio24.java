package ucu.edu.aed.ProblemSets;

import ucu.edu.aed.impl.Conjunto;
import ucu.edu.aed.tda.TDAConjunto;
import ucu.edu.aed.tda.TDALista;

/**
 * Ejercicio 24 - Conjuntos.
 *
 * <p>Esta clase corresponde a la Parte 3 del ejercicio: un ejemplo
 * concreto de uso del TDA Conjunto ya implementado, para mostrar que
 * funciona con un caso real (dos cursos de la facultad y sus alumnos).</p>
 *
 * <p>Si no se entiende algo del "por qué" de Union/Intersección acá
 * abajo, conviene mirar primero Ejercicio24_Parte1.txt (tiene un
 * ejemplo paso a paso con números antes del seudocódigo formal) y
 * después ucu.edu.aed.impl.Conjunto (la implementación, con los
 * comentarios de cómo funciona cada método). Los casos de prueba en
 * JUnit están en Ejercicio24Test.java.</p>
 */
public class Ejercicio24 {

    public static void main(String[] args) {
        // Se crea un Conjunto<TAlumno> por curso. El comparador
        // POR_CEDULA le dice al Conjunto cómo ordenar los alumnos
        // internamente (lo necesita para que union/interseccion
        // funcionen, ver Conjunto.java).
        TDAConjunto<TAlumno> aed1 = new Conjunto<>(TAlumno.POR_CEDULA);
        TDAConjunto<TAlumno> pf = new Conjunto<>(TAlumno.POR_CEDULA);

        TAlumno ana = new TAlumno(1001, "Ana", "Pérez");
        TAlumno bruno = new TAlumno(1002, "Bruno", "Gómez");
        TAlumno carla = new TAlumno(1003, "Carla", "Díaz");
        TAlumno diego = new TAlumno(1004, "Diego", "Suárez");
        TAlumno elena = new TAlumno(1005, "Elena", "Fernández");

        // AED1: Ana, Bruno, Carla
        aed1.agregar(ana);
        aed1.agregar(bruno);
        aed1.agregar(carla);

        // PF: Carla, Diego, Elena.
        // Carla se matricula en los dos cursos a propósito, para poder
        // mostrar después qué diferencia hay entre unión e intersección.
        pf.agregar(carla);
        pf.agregar(diego);
        pf.agregar(elena);

        System.out.println("=== AED1 ===");
        imprimir(aed1);
        System.out.println("=== PF ===");
        imprimir(pf);

        System.out.println();
        System.out.println("¿Carla está en AED1? " + aed1.contiene(carla));
        System.out.println("¿Carla está en PF?   " + pf.contiene(carla));
        System.out.println("¿Ana está en PF?     " + pf.contiene(ana));

        System.out.println();
        System.out.println("=== Alumnos matriculados en AED1 o PF (unión) ===");
        // La unión trae a todos: los que están en un curso, en el otro,
        // o en ambos (a Carla la vamos a ver una sola vez, no dos).
        imprimir(aed1.union(pf));

        System.out.println();
        System.out.println("=== Alumnos matriculados en AED1 y PF a la vez (intersección) ===");
        // La intersección solo trae a quienes están anotados en los DOS
        // cursos simultáneamente: en este ejemplo, solo Carla.
        imprimir(aed1.interseccion(pf));
    }

    private static void imprimir(TDALista<TAlumno> lista) {
        for (int i = 0; i < lista.tamaño(); i++) {
            System.out.println("  " + lista.obtener(i));
        }
    }
}
