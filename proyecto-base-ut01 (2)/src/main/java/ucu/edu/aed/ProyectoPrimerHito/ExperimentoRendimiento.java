package ucu.edu.aed.ProyectoPrimerHito;

import java.time.LocalDate;

import ucu.edu.aed.impl.Lista;
import ucu.edu.aed.impl.Pila;

/*
 Experimento del Desafio 3: compara GestorEsperaRepuestos (busqueda
 secuencial) contra GestorEsperaRepuestosV2 (indice + lista propia)
 midiendo cuanto tarda quitarPorPatente() en el peor caso, para
 distintos tamaños de la lista de espera.
 
  Peor caso elegido: buscar el ULTIMO vehiculo agregado. Para V1 eso
 significa recorrer los n-1 anteriores antes de encontrarlo.
 */
public class ExperimentoRendimiento {

    private static final int[] TAMANIOS = {200, 500, 1000, 2000, 4000, 8000};
    private static final int REPETICIONES = 15;

    public static void main(String[] args) {
        
        for (int n : TAMANIOS) {
            medir(false, n);
            medir(true, n);
        }

        System.out.println("n\ttiempoV1_ns\ttiempoV2_ns\tveces_mas_rapido");
        for (int n : TAMANIOS) {
            long tiempoV1 = medir(false, n);
            long tiempoV2 = medir(true, n);
            double veces = tiempoV2 == 0 ? Double.NaN : (double) tiempoV1 / tiempoV2;
            System.out.printf("%d\t%d\t%d\t%.1f%n", n, tiempoV1, tiempoV2, veces);
        }
    }

    private static long medir(boolean usarV2, int n) {
        String patenteBuscada = "V" + (n - 1);
        long total = 0;

        for (int rep = 0; rep < REPETICIONES; rep++) {
            IGestorEsperaRepuestos gestor = usarV2
                    ? new GestorEsperaRepuestosV2()
                    : new GestorEsperaRepuestos(new Lista<>());

            for (int i = 0; i < n; i++) {
                gestor.agregar(crearVehiculo("V" + i));
            }

            long inicio = System.nanoTime();
            gestor.quitarPorPatente(patenteBuscada);
            long fin = System.nanoTime();
            total += (fin - inicio);
        }

        return total / REPETICIONES;
    }

    private static Vehiculo crearVehiculo(String patente) {
        return new Vehiculo(patente, "Marca", "Modelo", 2020, "Dueño",
                TipoIngreso.MANTENIMIENTO_PLANIFICADO, 1, LocalDate.now(),
                new Pila<>(), new Lista<>());
    }
}
