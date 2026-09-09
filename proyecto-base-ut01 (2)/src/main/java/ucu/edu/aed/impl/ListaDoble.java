package ucu.edu.aed.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import ucu.edu.aed.tda.TDALista;

/*
Implementacion de lista enlazada doble, donde sabemos cual es el nodo anterior y elnodo siguiente
 */
public class ListaDoble<T> implements TDALista<T> {

    protected TDANodoDoble<T> cabeza;
    protected TDANodoDoble<T> cola;
    protected int tamaño;

    public ListaDoble() {
        this.cabeza = null;
        this.cola = null;
        this.tamaño = 0;
    }

    @Override
    public void agregar(T elem) {
        TDANodoDoble<T> nuevo = new TDANodoDoble<>(elem);
        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            nuevo.anterior = cola;
            cola.siguiente = nuevo;
            cola = nuevo;
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
        if (index == 0) {
            nuevo.siguiente = cabeza;
            cabeza.anterior = nuevo;
            cabeza = nuevo;
        } else {
            TDANodoDoble<T> actual = nodoEn(index);
            TDANodoDoble<T> anterior = actual.anterior;
            nuevo.anterior = anterior;
            nuevo.siguiente = actual;
            anterior.siguiente = nuevo;
            actual.anterior = nuevo;
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
        desenlazar(objetivo);
        tamaño--;
        return objetivo.dato;
    }

    @Override
    public boolean remover(T elem) {
        TDANodoDoble<T> actual = cabeza;
        while (actual != null) {
            if (actual.dato.equals(elem)) {
                desenlazar(actual);
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
        while (actual != null) {
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
        int posicion = 0;
        while (actual != null) {
            if (actual.dato.equals(elem)) {
                return posicion;
            }
            actual = actual.siguiente;
            posicion++;
        }
        return -1;
    }

    @Override
    public T buscar(Predicate<T> criterio) {
        TDANodoDoble<T> actual = cabeza;
        while (actual != null) {
            if (criterio.test(actual.dato)) {
                return actual.dato;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    @Override
    public TDALista<T> ordenar(Comparator<T> comparator) {
        ListaDoble<T> listaOrdenada = new ListaDoble<>();
        if (cabeza == null) {
            return listaOrdenada;
        }
        List<T> arregloTemp = new ArrayList<>(tamaño);
        TDANodoDoble<T> actual = cabeza;
        while (actual != null) {
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
        cola = null;
        tamaño = 0;
    }

    /**
     * Ubica el nodo en la posición indicada, recorriendo desde el
     * extremo más cercano (cabeza o cola) según convenga.
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
            actual = cola;
            for (int i = tamaño - 1; i > index; i--) {
                actual = actual.anterior;
            }
        }
        return actual;
    }

    /**
     * Desconecta un nodo conocido de la lista, reconectando a su
     * predecesor y sucesor (o actualizando cabeza/cola si el nodo
     * removido era un extremo).
     */
    private void desenlazar(TDANodoDoble<T> nodo) {
        TDANodoDoble<T> anterior = nodo.anterior;
        TDANodoDoble<T> siguiente = nodo.siguiente;

        if (anterior != null) {
            anterior.siguiente = siguiente;
        } else {
            cabeza = siguiente;
        }

        if (siguiente != null) {
            siguiente.anterior = anterior;
        } else {
            cola = anterior;
        }
    }
}
