import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Parte 1: hashCode de algunos tipos
        Integer n = 42;
        System.out.println("Integer 42 -> " + n.hashCode());
        String[] palabras = {"Hola", "HolaMundo", "HashMap", "Colecciones"};

        // Parte 2: en qué posición cae cada String en un HashMap de 16 posiciones
        // HashMap usa h ^ (h >>> 16) y luego (n - 1) & hash
        for (String s : palabras) {
            int h = s.hashCode();
            int hash = h ^ (h >>> 16);
            int indice = (16 - 1) & hash;
            System.out.println(s + " -> hashCode " + h + ", posicion " + indice);
        }

        // Parte 3: prueba de Alumno
        Alumno a = new Alumno(1, "Ana Perez", "ana@ucu.edu.uy");
        Alumno b = new Alumno(1, "Ana M. Perez", "ana@gmail.com");
        Alumno c = new Alumno(2, "Luis Gomez", "luis@ucu.edu.uy");

        System.out.println();
        System.out.println("a.equals(b): " + a.equals(b));
        System.out.println("mismo hashCode: " + (a.hashCode() == b.hashCode()));
        System.out.println("a.equals(c): " + a.equals(c));

        Map<Alumno, String> notas = new HashMap<>();
        notas.put(a, "10");
        notas.put(b, "11");
        notas.put(c, "8");
        System.out.println("Tamaño del mapa: " + notas.size());
        System.out.println("Nota de a: " + notas.get(a));
    }
}