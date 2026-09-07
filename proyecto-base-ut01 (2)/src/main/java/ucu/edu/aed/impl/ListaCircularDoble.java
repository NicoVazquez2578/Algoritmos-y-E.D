package ucu.edu.aed.impl;

import ucu.edu.aed.tda.TDALista;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Implementación de {@link TDALista} mediante una lista circular
 * doblemente enlazada.
 *
 * <p>Combina las dos ideas anteriores: cada nodo conoce a su
 * predecesor y sucesor ({@link TDANodoDoble}, igual que en
 * {@link ListaDoble}), y el último nodo se conecta de vuelta a la
 * cabeza en lugar de apuntar a {@code null} (igual que en
 * {@link ListaCircular}).</p>
 *
 * <p>Una ventaja de esta combinación: no hace falta mantener una
 * referencia aparte a la cola, porque siempre es accesible en O(1)
 * como {@code cabeza.anterior}. Igual que en la lista circular
 * simple, todo recorrido debe acotarse contando hasta {@code tamaño}.</p>
 *
 * @param <T> el tipo de los elementos almacenados
 */
public class ListaCircularDoble<T> implements TDALista<T> {

    protected TDANodoDoble<T> cabeza;
    protected int tamaño;

    public ListaCircularDoble() {
        this.cabeza = null;
        this.tamaño = 0;
    }

    @Override
    public void agregar(T elem) {
        TDANodoDoble<T> nuevo = new TDANodoDoble<>(elem);
        if (cabeza == null) {
            nuevo.anterior = nuevo;
            nuevo.siguiente = nuevo;
            cabeza = nuevo;
        } else {
            TDANodoDoble<T> ultimo = cabeza.anterior;
            ultimo.siguiente = nuevo;
            nuevo.anterior = ultimo;
            nuevo.siguiente = cabeza;
            cabeza.anterior = nuevo;
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
        TDANodoDoble<T> nuevo = new TDANodoDoble<>(elem);
        TDANodoDoble<T> actual = nodoEn(index);
        TDANodoDoble<T> anterior = actual.anterior;

        nuevo.anterior = anterior;
        nuevo.siguiente = actual;
        anterior.siguiente = nuevo;
        actual.anterior = nuevo;

        if (index == 0) {
            cabeza = nuevo;
        }
        tamaño++;
    }

    @Override
    public T obtener(int index) {
        if (index < 0 || index >= tamaño) {
            throw new IndexOutOfBoundsException("Indice fuera de rango" + index);
        }
        return nodoEn(index).dato;
    }

    @Override
    public T remover(int index) {
        if (index < 0 || index >= tamaño) {
            throw new IndexOutOfBoundsException("Indice fuera de rango" + index);
        }
        TDANodoDoble<T> objetivo = nodoEn(index);
        T datoRemovido = objetivo.dato;

        if (tamaño == 1) {
            cabeza = null;
        } else {
            objetivo.anterior.siguiente = objetivo.siguiente;
            objetivo.siguiente.anterior = objetivo.anterior;
            if (objetivo == cabeza) {
                cabeza = objetivo.siguiente;
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
        TDANodoDoble<T> actual = cabeza;
        for (int i = 0; i < tamaño; i++) {
            if (actual.dato.equals(elem)) {
                if (tamaño == 1) {
                    cabeza = null;
                } else {
                    actual.anterior.siguiente = actual.siguiente;
                    actual.siguiente.anterior = actual.anterior;
                    if (actual == cabeza) {
                        cabeza = actual.siguiente;
                    }
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
        TDANodoDoble<T> actual = cabeza;
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
        TDANodoDoble<T> actual = cabeza;
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
        TDANodoDoble<T> actual = cabeza;
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
        ListaCircularDoble<T> listaOrdenada = new ListaCircularDoble<>();
        if (cabeza == null) {
            return listaOrdenada;
        }
        List<T> arregloTemp = new ArrayList<>(tamaño);
        TDANodoDoble<T> actual = cabeza;
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
        tamaño = 0;
    }

    /**
     * Ubica el nodo en la posición indicada, recorriendo desde el
     * extremo más cercano (cabeza o "cola" = cabeza.anterior) según
     * convenga.
     *
     * @param index posición válida (0 &lt;= index &lt; tamaño)
     */
    private TDANodoDoble<T> nodoEn(int index) {
        TDANodoDoble<T> actual;
        if (index <= tamaño / 2) {
            actual = cabeza;
            for (int i = 0; i < index; i++) {
                actual = actual.siguiente;
            }
        } else {
            actual = cabeza.anterior;
            for (int i = tamaño - 1; i > index; i--) {
                actual = actual.anterior;
            }
        }
        return actual;
    }
}
