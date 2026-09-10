package ucu.edu.aed.impl;

import ucu.edu.aed.tda.TDALista;

import java.util.NoSuchElementException;

/**
 * Árbol Binario de Búsqueda (BST) que asocia claves K con valores V.
 *
 * La clave K debe implementar Comparable para definir el orden.
 * Para cada nodo N, todo nodo del subárbol izquierdo tiene clave < N.clave
 * y todo nodo del subárbol derecho tiene clave > N.clave.
 *
 * Usamos este árbol en el Taller para:
 *   - buscarVehiculoPorMatricula → clave String (patente), O(log n) promedio
 *   - vehiculosEnRangoDeFechas   → clave LocalDate (fecha de ingreso), O(log n + k)
 *
 * Complejidades:
 *   insertar / buscar / eliminar: O(log n) promedio, O(n) peor caso (árbol degenerado)
 *   rango:                        O(log n + k), k = cantidad de resultados
 *
 * No se permiten claves duplicadas: insertar una clave existente actualiza el valor.
 */
// El bound "? super K" permite usar LocalDate (que es Comparable<ChronoLocalDate>)
// además de String, Integer u otros tipos que son Comparable<SíMismos>.
public class ArbolBinarioBusqueda<K extends Comparable<? super K>, V> {

    // ─── Nodo interno ─────────────────────────────────────────────────
    private static class Nodo<K, V> {
        K clave;
        V valor;
        Nodo<K, V> izquierdo, derecho;

        Nodo(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
        }
    }

    private Nodo<K, V> raiz;
    private int tamaño;

    public ArbolBinarioBusqueda() {
        raiz = null;
        tamaño = 0;
    }

    // ─── insertar ─────────────────────────────────────────────────────

    /**
     * Inserta la par (clave, valor). Si la clave ya existe, actualiza el valor.
     * Complejidad: O(log n) promedio.
     */
    public void insertar(K clave, V valor) {
        raiz = insertarRec(raiz, clave, valor);
    }

    private Nodo<K, V> insertarRec(Nodo<K, V> nodo, K clave, V valor) {
        if (nodo == null) {
            tamaño++;
            return new Nodo<>(clave, valor);
        }
        int cmp = clave.compareTo(nodo.clave);
        if (cmp < 0) {
            nodo.izquierdo = insertarRec(nodo.izquierdo, clave, valor);
        } else if (cmp > 0) {
            nodo.derecho = insertarRec(nodo.derecho, clave, valor);
        } else {
            // La clave ya existe: actualizamos el valor sin cambiar la estructura
            nodo.valor = valor;
        }
        return nodo;
    }

    // ─── buscar ───────────────────────────────────────────────────────

    /**
     * Retorna el valor asociado a la clave, o null si no existe.
     * Complejidad: O(log n) promedio.
     */
    public V buscar(K clave) {
        Nodo<K, V> nodo = buscarNodo(raiz, clave);
        return nodo != null ? nodo.valor : null;
    }

    private Nodo<K, V> buscarNodo(Nodo<K, V> nodo, K clave) {
        if (nodo == null) return null;
        int cmp = clave.compareTo(nodo.clave);
        if (cmp < 0) return buscarNodo(nodo.izquierdo, clave);
        if (cmp > 0) return buscarNodo(nodo.derecho, clave);
        return nodo; // cmp == 0: encontrado
    }

    // ─── eliminar ─────────────────────────────────────────────────────

    /**
     * Elimina la entrada con la clave dada. No hace nada si no existe.
     *
     * Cuando el nodo tiene dos hijos, lo reemplazamos con el sucesor inorden
     * (el mínimo del subárbol derecho), que es el sucesor inmediato en orden.
     * Complejidad: O(log n) promedio.
     */
    public void eliminar(K clave) {
        raiz = eliminarRec(raiz, clave);
    }

    private Nodo<K, V> eliminarRec(Nodo<K, V> nodo, K clave) {
        if (nodo == null) return null;
        int cmp = clave.compareTo(nodo.clave);
        if (cmp < 0) {
            nodo.izquierdo = eliminarRec(nodo.izquierdo, clave);
        } else if (cmp > 0) {
            nodo.derecho = eliminarRec(nodo.derecho, clave);
        } else {
            // Encontrado. Tres casos:
            tamaño--;
            if (nodo.izquierdo == null) return nodo.derecho; // Caso 1: sin hijo izq
            if (nodo.derecho == null) return nodo.izquierdo;  // Caso 2: sin hijo der
            // Caso 3: dos hijos → sucesor inorden (mínimo del subárbol derecho)
            Nodo<K, V> sucesor = minimoNodo(nodo.derecho);
            nodo.clave = sucesor.clave;
            nodo.valor = sucesor.valor;
            // Eliminamos el sucesor del subárbol derecho (que tiene a lo sumo un hijo)
            nodo.derecho = eliminarRec(nodo.derecho, sucesor.clave);
            tamaño++; // la recursión decrementó tamaño de más; lo compensamos
        }
        return nodo;
    }

    // ─── rango ────────────────────────────────────────────────────────

    /**
     * Retorna todos los valores cuya clave está entre [desde, hasta], inclusive.
     * Aprovecha la estructura del BST para no recorrer nodos fuera del rango:
     * si la clave del nodo actual es < desde, todo su subárbol izquierdo queda
     * descartado sin visitarlo.
     *
     * Complejidad: O(log n + k) donde k es la cantidad de resultados.
     * Esto es mucho más eficiente que O(n) con una lista plana.
     */
    public TDALista<V> rango(K desde, K hasta) {
        TDALista<V> resultado = new ListaArray<>();
        rangoRec(raiz, desde, hasta, resultado);
        return resultado;
    }

    private void rangoRec(Nodo<K, V> nodo, K desde, K hasta, TDALista<V> lista) {
        if (nodo == null) return;
        int cmpDesde = desde.compareTo(nodo.clave);
        int cmpHasta = hasta.compareTo(nodo.clave);

        // Si la clave actual es > desde, puede haber resultados a la izquierda
        if (cmpDesde < 0) {
            rangoRec(nodo.izquierdo, desde, hasta, lista);
        }
        // Si desde <= clave <= hasta, este nodo entra
        if (cmpDesde <= 0 && cmpHasta >= 0) {
            lista.agregar(nodo.valor);
        }
        // Si la clave actual es < hasta, puede haber resultados a la derecha
        if (cmpHasta > 0) {
            rangoRec(nodo.derecho, desde, hasta, lista);
        }
    }

    // ─── consultas ────────────────────────────────────────────────────

    public boolean esVacio() { return raiz == null; }
    public int tamaño()      { return tamaño; }

    // ─── helpers privados ─────────────────────────────────────────────

    private Nodo<K, V> minimoNodo(Nodo<K, V> nodo) {
        while (nodo.izquierdo != null) nodo = nodo.izquierdo;
        return nodo;
    }
}
