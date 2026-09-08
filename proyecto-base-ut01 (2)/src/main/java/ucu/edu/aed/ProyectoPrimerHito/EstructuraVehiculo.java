package ucu.edu.aed.ProyectoPrimerHito;

import ucu.edu.aed.impl.Cola;
import ucu.edu.aed.impl.ListaArray;
import ucu.edu.aed.tda.TDALista;

/**
 * Árbol general n-ario que representa la descomposición jerárquica
 * de un vehículo en sistemas, subsistemas y piezas.
 *
 * Ejemplo de cómo puede verse un árbol para un auto:
 *
 *   [SISTEMA] Motor
 *     ├── [SUBSISTEMA] Distribución
 *     │     ├── [PIEZA] Correa de distribución
 *     │     └── [PIEZA] Tensor
 *     └── [SUBSISTEMA] Culata
 *           └── [PIEZA] Válvula de admisión
 *   [SISTEMA] Tren delantero
 *     ├── [PIEZA] Amortiguador izquierdo
 *     └── [PIEZA] Amortiguador derecho
 *
 * La profundidad varía por rama: el tren delantero tiene 2 niveles,
 * el motor tiene 3. Usar un árbol n-ario (vs una lista) permite
 * reflejar esta estructura real sin forzar una profundidad fija.
 *
 * Cada trabajo se asocia a una ParteVehiculo de este árbol, lo que
 * permite generar facturas desglosadas por sistema.
 *
 * Recorrido BFS (por niveles): usa la Cola del Hito 1 como auxiliar.
 * Recorrido DFS (búsqueda): usa recursión.
 */
public class EstructuraVehiculo {

    // ─── Nodo interno ─────────────────────────────────────────────────
    // Cada nodo guarda la parte y una ListaArray de sus hijos directos.
    // Usamos ListaArray del Hito 1 porque necesitamos acceso por índice
    // para iterar los hijos en el recorrido.
    private static class Nodo {
        ParteVehiculo dato;
        ListaArray<Nodo> hijos;

        Nodo(ParteVehiculo dato) {
            this.dato = dato;
            this.hijos = new ListaArray<>();
        }
    }

    // El árbol puede tener múltiples "raíces" de sistemas (motor, tren, etc.)
    // Las guardamos en una lista de nodos de primer nivel.
    private final ListaArray<Nodo> sistemas;

    public EstructuraVehiculo() {
        this.sistemas = new ListaArray<>();
    }

    /**
     * Agrega un sistema de nivel raíz al vehículo (motor, tren delantero, etc.).
     * Varios sistemas pueden existir en paralelo sin depender entre sí.
     */
    public void agregarSistema(ParteVehiculo sistema) {
        sistemas.agregar(new Nodo(sistema));
    }

    /**
     * Agrega 'subparte' como hija directa de la parte con nombre 'nombrePadre'.
     * Busca el padre con DFS en todo el árbol.
     *
     * @throws IllegalArgumentException si no se encuentra la parte padre
     */
    public void agregarSubparte(String nombrePadre, ParteVehiculo subparte) {
        Nodo padre = buscarNodoEnSistemas(nombrePadre);
        if (padre == null) {
            throw new IllegalArgumentException("No se encontró la parte: " + nombrePadre);
        }
        padre.hijos.agregar(new Nodo(subparte));
    }

    /**
     * Busca y retorna una parte por nombre. Retorna null si no existe.
     * Recorrido DFS: revisa cada sistema y sus descendientes.
     * Complejidad: O(n) donde n es la cantidad total de partes.
     */
    public ParteVehiculo buscarParte(String nombre) {
        Nodo nodo = buscarNodoEnSistemas(nombre);
        return nodo != null ? nodo.dato : null;
    }

    /**
     * Retorna todas las partes en orden BFS (nivel por nivel).
     * Primero los sistemas, luego sus subsistemas, luego las piezas.
     *
     * Usa la Cola del Hito 1 como cola auxiliar para el BFS:
     *   1. Encolar los nodos de sistemas
     *   2. Mientras la cola no esté vacía: desencolar, agregar al resultado,
     *      encolar sus hijos
     *
     * Esto garantiza que procesamos todos los nodos de nivel k antes de
     * pasar a los de nivel k+1.
     * Complejidad: O(n).
     */
    public TDALista<ParteVehiculo> listarPorNiveles() {
        TDALista<ParteVehiculo> resultado = new ListaArray<>();
        if (sistemas.esVacio()) return resultado;

        Cola<Nodo> cola = new Cola<>();
        for (int i = 0; i < sistemas.tamaño(); i++) {
            cola.poneEnCola(sistemas.obtener(i));
        }

        while (!cola.esVacio()) {
            Nodo actual = cola.quitaDeCola();
            resultado.agregar(actual.dato);
            for (int i = 0; i < actual.hijos.tamaño(); i++) {
                cola.poneEnCola(actual.hijos.obtener(i));
            }
        }
        return resultado;
    }

    /**
     * Cantidad total de partes registradas en todos los sistemas.
     * Complejidad: O(n).
     */
    public int cantidadPartes() {
        int total = 0;
        for (int i = 0; i < sistemas.tamaño(); i++) {
            total += contarNodos(sistemas.obtener(i));
        }
        return total;
    }

    public boolean esVacio() {
        return sistemas.esVacio();
    }

    // ─── helpers privados ──────────────────────────────────────────────

    // Busca el nodo con ese nombre en todos los árboles de sistemas (DFS).
    private Nodo buscarNodoEnSistemas(String nombre) {
        for (int i = 0; i < sistemas.tamaño(); i++) {
            Nodo encontrado = buscarNodoDFS(sistemas.obtener(i), nombre);
            if (encontrado != null) return encontrado;
        }
        return null;
    }

    // DFS recursivo: busca el nodo cuya parte tiene ese nombre.
    private Nodo buscarNodoDFS(Nodo actual, String nombre) {
        if (actual == null) return null;
        if (actual.dato.getNombre().equals(nombre)) return actual;
        for (int i = 0; i < actual.hijos.tamaño(); i++) {
            Nodo encontrado = buscarNodoDFS(actual.hijos.obtener(i), nombre);
            if (encontrado != null) return encontrado;
        }
        return null;
    }

    private int contarNodos(Nodo nodo) {
        if (nodo == null) return 0;
        int total = 1;
        for (int i = 0; i < nodo.hijos.tamaño(); i++) {
            total += contarNodos(nodo.hijos.obtener(i));
        }
        return total;
    }
}
