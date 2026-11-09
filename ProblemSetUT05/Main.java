package ProblemSetUT05;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        // datos del enunciado
        int[] datos = {256, 458, 655, 298, 43, 648, 778, 621, 655, 19, 124, 847};

        demostrarMetodo(datos, "insercion", "Insercion Directa");
        demostrarMetodo(datos, "shell",     "Shell Sort (5,3,1)");
        demostrarMetodo(datos, "burbuja",   "Burbuja");

        System.out.println("\n--- Verificacion con tipos de vectores (n=10) ---");
        for (String metodo : new String[]{"insercion", "shell", "burbuja"}) {
            int[] asc  = GeneradorDatosGenerico.generarAscendente(10);
            int[] desc = GeneradorDatosGenerico.generarDescendente(10);
            int[] rand = GeneradorDatosGenerico.generarAleatorio(10);
            TClasificador.clasificar(asc,  metodo);
            TClasificador.clasificar(desc, metodo);
            TClasificador.clasificar(rand, metodo);
            System.out.printf("[%-10s] ascendente=%b  descendente=%b  aleatorio=%b%n",
                metodo,
                TClasificador.estaOrdenado(asc),
                TClasificador.estaOrdenado(desc),
                TClasificador.estaOrdenado(rand));
        }
    }

    private static void demostrarMetodo(int[] datos, String metodo, String nombre) {
        int[] v = Arrays.copyOf(datos, datos.length);
        TClasificador.clasificar(v, metodo);
        System.out.printf("%n--- %s ---%n%s%nordenado: %b%n",
            nombre, Arrays.toString(v), TClasificador.estaOrdenado(v));
    }
}
