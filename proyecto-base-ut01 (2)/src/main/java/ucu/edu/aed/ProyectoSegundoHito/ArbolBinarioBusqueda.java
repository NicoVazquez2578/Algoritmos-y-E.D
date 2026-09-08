package ucu.edu.aed.ProyectoSegundoHito;

import java.util.Objects;
import java.util.function.Consumer;
import ucu.edu.aed.impl.Lista;
import ucu.edu.aed.tda.TDALista;

/**
 * Árbol Binario de Búsqueda (ABB) simple para almacenar pares (clave, valor).
 * Las claves menores van a la izquierda y las mayores a la derecha.
 * Búsqueda e inserción: O(log n) en promedio.
 */
public class ArbolBinarioBusqueda<K extends Comparable<K>, V> {

    // Nodo interno del árbol
    public static class NodoABB<K, V> {
        public K clave;
        public V valor;
        public NodoABB<K, V> izquierdo;
        public NodoABB<K, V> derecho;

        public NodoABB(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
        }
    }

    private NodoABB<K, V> raiz;
    private int tamaño;

    public ArbolBinarioBusqueda() {
        this.raiz = null;
        this.tamaño = 0;
    }

    // Inserta una clave y su valor en el árbol.
    // Si la clave ya existe, actualiza el valor.
    public void insertar(K clave, V valor) {
        Objects.requireNonNull(clave, "La clave no puede ser nula.");
        if (raiz == null) {
            raiz = new NodoABB<>(clave, valor);
            tamaño++;
            return;
        }

        NodoABB<K, V> actual = raiz;
        NodoABB<K, V> padre = null;
        int cmp = 0;

        // Recorre hacia izquierda o derecha hasta encontrar el lugar vacío
        while (actual != null) {
            padre = actual;
            cmp = clave.compareTo(actual.clave);
            if (cmp < 0) {
                actual = actual.izquierdo;
            } else if (cmp > 0) {
                actual = actual.derecho;
            } else {
                actual.valor = valor; // Si ya existe, actualiza el valor
                return;
            }
        }

        // Engancha el nuevo nodo al padre correspondiente
        NodoABB<K, V> nuevo = new NodoABB<>(clave, valor);
        if (cmp < 0) {
            padre.izquierdo = nuevo;
        } else {
            padre.derecho = nuevo;
        }
        tamaño++;
    }

    // Busca un valor por su clave en O(log n) promedio.
    // Devuelve el valor o null si no existe.
    public V buscar(K clave) {
        Objects.requireNonNull(clave, "La clave no puede ser nula.");
        NodoABB<K, V> actual = raiz;
        while (actual != null) {
            int cmp = clave.compareTo(actual.clave);
            if (cmp < 0) {
                actual = actual.izquierdo;
            } else if (cmp > 0) {
                actual = actual.derecho;
            } else {
                return actual.valor; // Encontrado
            }
        }
        return null; // No existe
    }

    // Elimina un nodo por su clave y devuelve el valor que tenía.
    public V eliminar(K clave) {
        Objects.requireNonNull(clave, "La clave no puede ser nula.");
        V valor = buscar(clave);
        if (valor != null) {
            raiz = eliminarNodo(raiz, clave);
            tamaño--;
        }
        return valor;
    }

    private NodoABB<K, V> eliminarNodo(NodoABB<K, V> nodo, K clave) {
        if (nodo == null) return null;

        int cmp = clave.compareTo(nodo.clave);
        if (cmp < 0) {
            nodo.izquierdo = eliminarNodo(nodo.izquierdo, clave);
        } else if (cmp > 0) {
            nodo.derecho = eliminarNodo(nodo.derecho, clave);
        } else {
            // Caso 1 y 2: 0 o 1 hijo
            if (nodo.izquierdo == null) return nodo.derecho;
            if (nodo.derecho == null) return nodo.izquierdo;

            // Caso 3: 2 hijos. Busca el sucesor (el más chico del lado derecho)
            NodoABB<K, V> sucesor = nodo.derecho;
            while (sucesor.izquierdo != null) {
                sucesor = sucesor.izquierdo;
            }
            nodo.clave = sucesor.clave;
            nodo.valor = sucesor.valor;
            nodo.derecho = eliminarNodo(nodo.derecho, sucesor.clave);
        }
        return nodo;
    }

    // Recorrido in-order (visita los elementos ordenados de menor a mayor)
    public void inOrder(Consumer<V> consumidor) {
        recorrerInOrder(raiz, consumidor);
    }

    private void recorrerInOrder(NodoABB<K, V> nodo, Consumer<V> consumidor) {
        if (nodo != null) {
            recorrerInOrder(nodo.izquierdo, consumidor);
            consumidor.accept(nodo.valor);
            recorrerInOrder(nodo.derecho, consumidor);
        }
    }

    // Devuelve todos los valores ordenados en una lista
    public TDALista<V> listar() {
        TDALista<V> lista = new Lista<>();
        inOrder(lista::agregar);
        return lista;
    }

    public int tamaño() {
        return tamaño;
    }

    public boolean esVacio() {
        return tamaño == 0;
    }
}
