package ucu.edu.aed.ProblemSets.ej17;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import ucu.edu.aed.tda.TDALista;

/**
 * Integra el procesamiento de adquisiciones y préstamos (Ejercicio 17 - Paso 3).
 * Los archivos deben estar en src/main/resources.
 */
public class AppBiblioteca {

    public static void main(String[] args) throws Exception {
        Biblioteca biblioteca = new Biblioteca();
        double valorTotalAgregado = 0;
        int variacionPrestamos = 0;

        // Sub-equipo A — Adquisiciones
        InputStream adqStream = AppBiblioteca.class.getClassLoader()
                .getResourceAsStream("adquisiciones.txt");
        if (adqStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(adqStream));
            String linea;
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] p = linea.split(",");
                String codigo   = p[0].trim();
                String titulo   = p[1].trim();
                double precio   = Double.parseDouble(p[2].trim());
                int cantidad    = Integer.parseInt(p[3].trim());
                biblioteca.incorporarLibro(new Libro(codigo, titulo, precio, cantidad));
                valorTotalAgregado += precio * cantidad;
            }
            reader.close();
        } else {
            System.out.println("Advertencia: adquisiciones.txt no encontrado.");
        }

        // Sub-equipo B — Préstamos
        InputStream prestStream = AppBiblioteca.class.getClassLoader()
                .getResourceAsStream("prestamos.txt");
        if (prestStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(prestStream));
            String linea;
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] p = linea.split(",");
                String codigo   = p[0].trim();
                String tipo     = p[1].trim();
                int cantidad    = Integer.parseInt(p[2].trim());
                int mov = biblioteca.registrarMovimiento(codigo, tipo, cantidad);
                variacionPrestamos += "PRESTAMO".equalsIgnoreCase(tipo) ? mov : -mov;
            }
            reader.close();
        } else {
            System.out.println("Advertencia: prestamos.txt no encontrado.");
        }

        // Resultados
        System.out.println("=== CATÁLOGO (ordenado por título) ===");
        TDALista<Libro> ordenada = biblioteca.listarPorTitulo();
        for (int i = 0; i < ordenada.tamaño(); i++) {
            System.out.println(ordenada.obtener(i));
        }
        System.out.println("\nTotal libros en catálogo : " + biblioteca.totalLibros());
        System.out.printf("Valor total agregado     : $%.2f%n", valorTotalAgregado);
        System.out.println("Variación de préstamos   : " + variacionPrestamos);
    }
}
