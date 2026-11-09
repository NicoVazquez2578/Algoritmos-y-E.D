package ProblemSetUT05;

public class TClasificador {

    // Inserción Directa — O(n^2) peor/promedio, O(n) mejor (ya ordenado)
    public static void ordenarPorInsercion(int[] v) {
        for (int i = 1; i < v.length; i++) {
            int clave = v[i];
            int j = i - 1;
            while (j >= 0 && v[j] > clave) {
                v[j + 1] = v[j];
                j--;
            }
            v[j + 1] = clave;
        }
    }

    // Shell Sort con secuencia (5, 3, 1) — O(n log^2 n) promedio
    public static void shellSort(int[] v) {
        int[] incrementos = {5, 3, 1};
        for (int h : incrementos) {
            for (int i = h; i < v.length; i++) {
                int clave = v[i];
                int j = i - h;
                while (j >= 0 && v[j] > clave) {
                    v[j + h] = v[j];
                    j -= h;
                }
                v[j + h] = clave;
            }
        }
    }

    // Burbuja optimizada — corte anticipado si no hubo intercambios
    public static void burbuja(int[] v) {
        boolean intercambio;
        for (int i = 0; i < v.length - 1; i++) {
            intercambio = false;
            for (int j = 0; j < v.length - 1 - i; j++) {
                if (v[j] > v[j + 1]) {
                    int tmp = v[j];
                    v[j] = v[j + 1];
                    v[j + 1] = tmp;
                    intercambio = true;
                }
            }
            if (!intercambio) break;
        }
    }

    public static void clasificar(int[] v, String metodo) {
        switch (metodo.toLowerCase()) {
            case "insercion" -> ordenarPorInsercion(v);
            case "shell"     -> shellSort(v);
            case "burbuja"   -> burbuja(v);
            default          -> throw new IllegalArgumentException("Metodo desconocido: " + metodo);
        }
    }

    public static boolean estaOrdenado(int[] v) {
        for (int i = 0; i < v.length - 1; i++) {
            if (v[i] > v[i + 1]) return false;
        }
        return true;
    }
}
