package ProblemSetUT05;

import java.util.Random;

public class GeneradorDatosGenerico {

    private static final int TAMANIO = 100;
    private static final Random random = new Random();

    // vector monótonamente ascendente: [0, 1, 2, ..., n-1]
    public static int[] generarAscendente() {
        return generarAscendente(TAMANIO);
    }

    public static int[] generarAscendente(int n) {
        int[] v = new int[n];
        for (int i = 0; i < n; i++) v[i] = i;
        return v;
    }

    // vector monótonamente descendente: [n-1, n-2, ..., 0]
    public static int[] generarDescendente() {
        return generarDescendente(TAMANIO);
    }

    public static int[] generarDescendente(int n) {
        int[] v = new int[n];
        for (int i = 0; i < n; i++) v[i] = n - 1 - i;
        return v;
    }

    // vector con valores aleatorios; pueden existir claves repetidas; O(n)
    public static int[] generarAleatorio() {
        return generarAleatorio(TAMANIO);
    }

    public static int[] generarAleatorio(int n) {
        int[] v = new int[n];
        for (int i = 0; i < n; i++) v[i] = random.nextInt(n * 2);
        return v;
    }

    public static boolean estaOrdenado(int[] v) {
        for (int i = 0; i < v.length - 1; i++) {
            if (v[i] > v[i + 1]) return false;
        }
        return true;
    }
}
