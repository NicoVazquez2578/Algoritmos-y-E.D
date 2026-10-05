package ucu.edu.aed.tda.Arboles.Impl;

import java.util.function.Consumer;

import ucu.edu.aed.impl.Cola;
import ucu.edu.aed.tda.TDACola;
import ucu.edu.aed.tda.TDALista;
import ucu.edu.aed.tda.Arboles.TDAElementoGenerico;
import ucu.edu.aed.tda.Arboles.TDAArbolGenerico;

public class ArbolGenerico <T> implements TDAArbolGenerico<T> {
    protected TDAElementoGenerico<T> raiz;

    @Override
    public boolean insertar(T dato) {
        if (esVacio() && dato != null) {
            raiz = new ElementoGenerico<>(dato);
            return true;
        }
        return false;
    }

    @Override
    public boolean insertarHijo(T dato, Comparable<T> criterioPadre) {
        if (raiz == null) {
            return false;
        }
        return raiz.insertarHijo(dato, criterioPadre);
    }

    @Override
    public T buscar(Comparable<T> criterioBusqueda) {
        if (raiz == null) {
            return null;
        }
        TDAElementoGenerico<T> encontrado = raiz.buscar(criterioBusqueda);
        return (encontrado != null) ? encontrado.getDato() : null;
    }

    @Override
    public TDAElementoGenerico<T> obtenerRaiz() {
        return this.raiz;
    }

    @Override
    public boolean eliminar(Comparable<T> criterioBusqueda) {
        if (raiz == null) {
            return false;
        }
        if (criterioBusqueda.compareTo(raiz.getDato()) == 0) {
            raiz = null;
            return true;
        }
        int cantidadAntes = cantidadNodos();
        raiz.eliminar(criterioBusqueda);
        return cantidadNodos() < cantidadAntes;
    }

    @Override
    public void preOrder(Consumer<T> consumidor) {
        if (raiz != null) {
            raiz.preOrder((nodo) -> consumidor.accept(nodo.getDato()));
        }
    }

    @Override
    public void postOrder(Consumer<T> consumidor) {
        if (raiz != null) {
            raiz.postOrder((nodo) -> consumidor.accept(nodo.getDato()));
        }
    }

    @Override
    public void recorridoPorNiveles(Consumer<T> consumidor) {
        if (raiz == null) {
            return;
        }
        TDACola<TDAElementoGenerico<T>> cola = new Cola<>();
        cola.poneEnCola(raiz);
        while (!cola.esVacio()) {
            TDAElementoGenerico<T> actual = cola.quitaDeCola();
            consumidor.accept(actual.getDato());
            TDALista<TDAElementoGenerico<T>> hijos = actual.getHijos();
            for (int i = 0; i < hijos.tamaño(); i++) {
                cola.poneEnCola(hijos.obtener(i));
            }
        }
    }

    @Override
    public boolean esVacio() {
        return raiz == null;
    }

    @Override
    public int cantidadNodos() {
        return (raiz != null) ? raiz.cantidadNodos() : 0;
    }

    @Override
    public int cantidadHojas() {
        return (raiz != null) ? raiz.cantidadHojas() : 0;
    }

    @Override
    public int cantidadNodosInternos() {
        return (raiz != null) ? raiz.cantidadNodosInternos() : 0;
    }
    
}
