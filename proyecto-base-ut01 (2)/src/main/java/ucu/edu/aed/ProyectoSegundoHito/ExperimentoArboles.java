package ucu.edu.aed.ProyectoSegundoHito;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ucu.edu.aed.ProyectoPrimerHito.GestorEsperaRepuestos;
import ucu.edu.aed.ProyectoPrimerHito.TipoIngreso;
import ucu.edu.aed.ProyectoPrimerHito.Vehiculo;
import ucu.edu.aed.impl.Lista;
import ucu.edu.aed.impl.Pila;

/**
 * Experimento del Desafío 3:
 * Mide el tiempo de buscar por patente en la Lista (O(n)) vs en el ABB (O(log n))
 * con n = 100, 1.000, 10.000 y 100.000 vehículos.
 */
public class ExperimentoArboles {

    private static final int[] TAMANIOS = {100, 1000, 10000, 100000};
    private static final int REPETICIONES = 30;
    private static final String ARCHIVO_SALIDA = "resultados_experimento_arboles.txt";

    public static void main(String[] args) {
        System.out.println("=== Experimento Desafio 3: Lista O(n) vs ABB O(log n) ===");
        System.out.println("Midiendo tiempo promedio con " + REPETICIONES + " repeticiones...\n");

        StringBuilder salida = new StringBuilder();
        salida.append("n\ttiempo_Lista_ns\ttiempo_ABB_ns\ttiempo_Lista_us\tspeedup\n");
        System.out.printf("%-10s %-18s %-18s %-16s %-10s%n",
                "n", "T_Lista (ns)", "T_ABB (ns)", "T_Lista (us)", "Speedup");
        System.out.println("-----------------------------------------------------------------------");

        for (int n : TAMANIOS) {
            // Generamos patentes mezcladas para que el árbol no quede degenerado
            List<String> patentes = generarPatentes(n);
            String patenteBuscada = patentes.get(patentes.size() - 1); // peor caso: buscar la última

            long tiempoLista = medirLista(patentes, patenteBuscada);
            long tiempoABB = medirABB(patentes, patenteBuscada);

            double tiempoListaUs = tiempoLista / 1000.0;
            double speedup = tiempoABB > 0 ? (double) tiempoLista / tiempoABB : 0;

            String fila = String.format("%-10d %-18d %-18d %-16.2f %.1fx%n",
                    n, tiempoLista, tiempoABB, tiempoListaUs, speedup);

            System.out.print(fila);
            salida.append(String.format("%d\t%d\t%d\t%.2f\t%.1fx%n",
                    n, tiempoLista, tiempoABB, tiempoListaUs, speedup));
        }

        // Guardamos los resultados en el archivo de texto
        guardarArchivo(salida.toString());
    }

    // Mide el tiempo promedio de búsqueda en la lista secuencial
    private static long medirLista(List<String> patentes, String buscada) {
        GestorEsperaRepuestos gestor = new GestorEsperaRepuestos(new Lista<>());
        for (String p : patentes) {
            gestor.agregar(crearVehiculo(p));
        }

        long total = 0;
        for (int i = 0; i < REPETICIONES; i++) {
            long inicio = System.nanoTime();
            gestor.buscarPorPatente(buscada);
            long fin = System.nanoTime();
            total += (fin - inicio);
        }
        return total / REPETICIONES;
    }

    // Mide el tiempo promedio de búsqueda en el árbol binario
    private static long medirABB(List<String> patentes, String buscada) {
        GestorEsperaRepuestosABB gestor = new GestorEsperaRepuestosABB();
        for (String p : patentes) {
            gestor.agregar(crearVehiculo(p));
        }

        long total = 0;
        for (int i = 0; i < REPETICIONES; i++) {
            long inicio = System.nanoTime();
            gestor.buscarPorPatente(buscada);
            long fin = System.nanoTime();
            total += (fin - inicio);
        }
        return total / REPETICIONES;
    }

    // Crea patentes variadas y mezcladas
    private static List<String> generarPatentes(int n) {
        List<String> lista = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            lista.add(String.format("ABC%07d", i));
        }
        Collections.shuffle(lista, new Random(12345));
        return lista;
    }

    private static Vehiculo crearVehiculo(String patente) {
        return new Vehiculo(patente, "Marca", "Modelo", 2022, "Dueño",
                TipoIngreso.MANTENIMIENTO_PLANIFICADO, 1, LocalDate.now(),
                new Pila<>(), new Lista<>());
    }

    private static void guardarArchivo(String texto) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_SALIDA))) {
            pw.print(texto);
            System.out.println("\nTabla de resultados guardada en: " + ARCHIVO_SALIDA);
        } catch (IOException e) {
            System.err.println("Error al guardar archivo: " + e.getMessage());
        }
    }
}
