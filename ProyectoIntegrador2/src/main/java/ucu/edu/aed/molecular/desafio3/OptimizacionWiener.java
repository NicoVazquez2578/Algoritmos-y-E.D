package ucu.edu.aed.molecular.desafio3;

import ucu.edu.aed.molecular.grafo.DirectedGraph;
import ucu.edu.aed.molecular.grafo.algoritmos.DirectedGraphAlgorithms;
import ucu.edu.aed.molecular.molecula.Atomo;
import ucu.edu.aed.molecular.molecula.Elemento;
import ucu.edu.aed.molecular.molecula.Enlace;
import ucu.edu.aed.molecular.molecula.Molecula;
import ucu.edu.aed.molecular.molecula.TipoEnlace;
import ucu.edu.aed.tda.grafo.model.edge.Edge;
import ucu.edu.aed.tda.grafo.model.result.IFloydWarshallResult;

import java.util.*;


public class OptimizacionWiener {

    private static final DirectedGraphAlgorithms ALGORITMOS = new DirectedGraphAlgorithms();
    private static final Map<Molecula, Long> CACHE = new HashMap<>();

    // 1. SOLUCION BASE: Usando Floyd-Warshall (Costo O(V^3))

    public static long calcularConFloyd(Molecula molecula) {
        if (molecula == null || molecula.cantidadAtomos() <= 1) {
            return 0;
        }

        // 1. Pasamos la molecula a grafo dirigido para usar el Floyd de la biblioteca
        DirectedGraph<Atomo, Enlace> dirigido = molecula.comoGrafoDirigido();
        IFloydWarshallResult<Atomo> resultado = ALGORITMOS.floyd(dirigido);

        // 2. Sumamos las distancias de cada par de atomos (i < j para no duplicar)
        List<Atomo> atomos = new ArrayList<>(molecula.atomos());
        long suma = 0;

        for (int i = 0; i < atomos.size(); i++) {
            Atomo a1 = atomos.get(i);
            for (int j = i + 1; j < atomos.size(); j++) {
                Atomo a2 = atomos.get(j);
                double d = resultado.getCost(a1, a2);
                if (d != Double.POSITIVE_INFINITY) {
                    suma += (long) d;
                }
            }
        }
        return suma;
    }

    // 2. SOLUCION OPTIMIZADA: Usando BFS desde cada atomo (Costo O(V^2))
    public static long calcularConBFS(Molecula molecula) {
        if (molecula == null || molecula.cantidadAtomos() <= 1) {
            return 0;
        }

        // Guardamos los vecinos de cada atomo en un mapa para consultarlos rapido
        Map<Atomo, List<Atomo>> vecinos = new HashMap<>();
        for (Atomo a : molecula.atomos()) {
            vecinos.put(a, new ArrayList<>());
        }
        for (Edge<Atomo, Enlace> e : molecula.getGrafo().aristas()) {
            vecinos.get(e.source()).add(e.target());
            vecinos.get(e.target()).add(e.source());
        }

        long sumaTotal = 0;

        // Hacemos un recorrido BFS saliendo desde cada atomo
        for (Atomo origen : molecula.atomos()) {
            Queue<Atomo> cola = new LinkedList<>();
            Map<Atomo, Integer> distancia = new HashMap<>();

            cola.add(origen);
            distancia.put(origen, 0);

            while (!cola.isEmpty()) {
                Atomo actual = cola.poll();
                int dActual = distancia.get(actual);

                for (Atomo vecino : vecinos.get(actual)) {
                    if (!distancia.containsKey(vecino)) {
                        int dNueva = dActual + 1;
                        distancia.put(vecino, dNueva);
                        sumaTotal += dNueva;
                        cola.add(vecino);
                    }
                }
            }
        }

        // Dividimos entre 2 porque contamos cada par dos veces: de A a B y de B a A
        return sumaTotal / 2;
    }


    // 3. REUTILIZACION DE RESULTADOS: Cache en memoria (Costo O(1))
    public static long calcularConCache(Molecula molecula) {
        if (molecula == null) return 0;

        // Si ya lo calculamos antes, lo devolvemos directo
        if (CACHE.containsKey(molecula)) {
            return CACHE.get(molecula);
        }

        // Si es la primera vez, lo calculamos con BFS y lo guardamos
        long valor = calcularConBFS(molecula);
        CACHE.put(molecula, valor);
        return valor;
    }

    public static void limpiarCache() {
        CACHE.clear();
    }

    // 4. METODO AUXILIAR: Arma una cadena de carbonos de largo N
    public static Molecula crearCadena(int n) {
        Molecula m = new Molecula("Cadena-C" + n);
        List<Atomo> carbonos = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            carbonos.add(m.agregarAtomo(Elemento.C));
        }
        for (int i = 0; i + 1 < n; i++) {
            m.enlazar(carbonos.get(i), carbonos.get(i + 1), TipoEnlace.SIMPLE);
        }
        return m;
    }

    // Formula teorica para comprobar que el resultado sea correcto: (N * (N^2 - 1)) / 6
    public static long formulaTeorica(int n) {
        if (n <= 1) return 0;
        long nl = n;
        return (nl * (nl * nl - 1)) / 6;
    }

    // 5. METODO PRINCIPAL: Medicion y comparacion de tiempos

    public static void main(String[] args) {
        System.out.println("   DESAFIO 3: COMPARACION DE TIEMPOS - INDICE DE WIENER");
        int[] tamanos = { 10, 25, 50, 75, 100, 150 };

        System.out.println("1. COMPARACION SEGUN EL TAMANO DE LA MOLECULA (V)");
        System.out.printf("%-10s | %-12s | %-14s | %-14s | %-12s%n",
                "Atomos (V)", "Wiener", "Tiempo Floyd", "Tiempo BFS", "Speedup");

        for (int v : tamanos) {
            Molecula mol = crearCadena(v);
            long valorEsperado = formulaTeorica(v);

            // Medimos Floyd
            long t0 = System.nanoTime();
            long rFloyd = calcularConFloyd(mol);
            long t1 = System.nanoTime();
            double msFloyd = (t1 - t0) / 1000000.0;

            // Medimos BFS
            long t2 = System.nanoTime();
            long rBFS = calcularConBFS(mol);
            long t3 = System.nanoTime();
            double msBFS = (t3 - t2) / 1000000.0;

            double speedup = msBFS > 0 ? (msFloyd / msBFS) : 1.0;

            System.out.printf("%-10d | %-12d | %-11.3f ms | %-11.3f ms | %.1fx mas rapido%n",
                    v, valorEsperado, msFloyd, msBFS, speedup);
        }

        System.out.println("\n2. CONSULTAS REPETIDAS (REUTILIZAR RESULTADOS CON CACHE)");
        Molecula mol100 = crearCadena(100);
        int repeticiones = 50;

        // Sin cache (recalculando 50 veces)
        long tInicioSin = System.nanoTime();
        for (int i = 0; i < repeticiones; i++) {
            calcularConBFS(mol100);
        }
        long tFinSin = System.nanoTime();
        double msSinCache = (tFinSin - tInicioSin) / 1000000.0;

        // Con cache (calcula la primera vez y 49 veces devuelve de memoria)
        limpiarCache();
        long tInicioCon = System.nanoTime();
        for (int i = 0; i < repeticiones; i++) {
            calcularConCache(mol100);
        }
        long tFinCon = System.nanoTime();
        double msConCache = (tFinCon - tInicioCon) / 1000000.0;

        System.out.println("Molecula evaluada: 100 atomos");
        System.out.println("Cantidad de consultas: " + repeticiones);
        System.out.printf("Tiempo SIN cache: %.3f ms%n", msSinCache);
        System.out.printf("Tiempo CON cache: %.3f ms%n", msConCache);
        System.out.printf("Aceleracion: %.1fx mas rapido%n", (msSinCache / msConCache));

        System.out.println("                 FIN DE LA COMPARACION");
    }
}

