package ucu.edu.aed.impl;

import ucu.edu.aed.tda.TDALista;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Implementación de {@link TDALista} mediante una lista circular
 * simplemente enlazada: el último nodo no apunta a {@code null},
 * sino de vuelta a la cabeza.
 *
 * <p>Se reutiliza el mismo {@link TDANodo} de {@link Lista} — la
 * forma del nodo es idéntica, lo que cambia es cómo se conecta el
 * último nodo. Se mantiene una referencia directa al último nodo
 * ({@code ultimo}) para poder agregar al final en O(1) sin recorrer
 * toda la lista.</p>
 *
 * <p><b>Importante:</b> como ningún nodo tiene {@code siguiente == null},
 * todo recorrido debe acotarse contando hasta {@code tamaño}. Un
 * {@code while (actual != null)} como el de {@link Lista} entraría
 * en un ciclo infinito acá.</p>
 *
 * @param <T> el tipo de los elementos almacenados
 */
public class ListaCircular<T> implements TDALista<T> {

    protected TDANodo<T> cabeza;
    protected TDANodo<T> ultimo;
    protected int tamaño;

    public ListaCircular() {
        this.cabeza = null;
        this.ultimo = null;
        this.tamaño = 0;
    }

    @Override
    public void agregar(T elem) {
        TDANodo<T> nuevo = new TDANodo<>(elem);
        if (cabeza == null) {
            cabeza = nuevo;
            ultimo = nuevo;
            nuevo.siguiente = nuevo;
        } else {
            nuevo.siguiente = cabeza;
            ultimo.siguiente = nuevo;
            ultimo = nuevo;
        }
        tamaño++;
    }

    @Override
    public void agregar(int index, T elem) {
        if (index < 0 || index > tamaño) {
            throw new IndexOutOfBoundsException("Indice fuera de rango" + index);
        }
        if (index == tamaño) {
            agregar(elem);
            return;
        }
        TDANodo<T> nuevo = new TDANodo<>(elem);
        if (index == 0) {
            nuevo.siguiente = cabeza;
            ultimo.siguiente = nuevo;
            cabeza = nuevo;
        } else {
            TDANodo<T> actual = cabeza;
            for (int i = 0; i < index - 1; i++) {
                actual = actual.siguiente;
            }
            nuevo.siguiente = actual.siguiente;
            actual.siguiente = nuevo;
        }
        tamaño++;
    }

    @Override
    public T obtener(int index) {
        if (index < 0 || index >= tamaño) {
            throw new IndexOutOfBoundsException("Indice fuera de rango" + index);
        }
        TDANodo<T> actual = cabeza;
        for (int i = 0; i < index; i++) {
            actual = actual.siguiente;
        }
        return actual.dato;
    }

    @Override
    public T remover(int index) {
        if (index < 0 || index >= tamaño) {
            throw new IndexOutOfBoundsException("Indice fuera de rango" + index);
        }
        T datoRemovido;
        if (tamaño == 1) {
            datoRemovido = cabeza.dato;
            cabeza = null;
            ultimo = null;
        } else if (index == 0) {
            datoRemovido = cabeza.dato;
            cabeza = cabeza.siguiente;
            ultimo.siguiente = cabeza;
        } else {
            TDANodo<T> actual = cabeza;
            for (int i = 0; i < index - 1; i++) {
                actual = actual.siguiente;
            }
            TDANodo<T> objetivo = actual.siguiente;
            datoRemovido = objetivo.dato;
            actual.siguiente = objetivo.siguiente;
            if (objetivo == ultimo) {
                ultimo = actual;
            }
        }
        tamaño--;
        return datoRemovido;
    }

    @Override
    public boolean remover(T elem) {
        if (cabeza == null) {
            return false;
        }
        if (cabeza.dato.equals(elem)) {
            remover(0);
            return true;
        }
        TDANodo<T> actual = cabeza;
        for (int i = 0; i < tamaño - 1; i++) {
            if (actual.siguiente.dato.equals(elem)) {
                TDANodo<T> objetivo = actual.siguiente;
                actual.siguiente = objetivo.siguiente;
                if (objetivo == ultimo) {
                    ultimo = actual;
                }
                tamaño--;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    @Override
    public boolean contiene(T elem) {
        TDANodo<T> actual = cabeza;
        for (int i = 0; i < tamaño; i++) {
            if (actual.dato.equals(elem)) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    @Override
    public int indiceDe(T elem) {
        TDANodo<T> actual = cabeza;
        for (int i = 0; i < tamaño; i++) {
            if (actual.dato.equals(elem)) {
                return i;
            }
            actual = actual.siguiente;
        }
        return -1;
    }

    @Override
    public T buscar(Predicate<T> criterio) {
        TDANodo<T> actual = cabeza;
        for (int i = 0; i < tamaño; i++) {
            if (criterio.test(actual.dato)) {
                return actual.dato;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    @Override
    public TDALista<T> ordenar(Comparator<T> comparator) {
        ListaCircular<T> listaOrdenada = new ListaCircular<>();
        if (cabeza == null) {
            return listaOrdenada;
        }
        List<T> arregloTemp = new ArrayList<>(tamaño);
        TDANodo<T> actual = cabeza;
        for (int i = 0; i < tamaño; i++) {
            arregloTemp.add(actual.dato);
            actual = actual.siguiente;
        }
        for (int i = 0; i < tamaño - 1; i++) {
            for (int j = 0; j < tamaño - 1 - i; j++) {
                if (comparator.compare(arregloTemp.get(j), arregloTemp.get(j + 1)) > 0) {
                    T temp = arregloTemp.get(j);
                    arregloTemp.set(j, arregloTemp.get(j + 1));
                    arregloTemp.set(j + 1, temp);
                }
            }
        }
        for (T dato : arregloTemp) {
            listaOrdenada.agregar(dato);
        }
        return listaOrdenada;
    }

    @Override
    public int tamaño() {
        return tamaño;
    }

    @Override
    public boolean esVacio() {
        return tamaño == 0;
    }

    @Override
    public void vaciar() {
        cabeza = null;
        ultimo = null;
        tamaño = 0;
    }
}
