package ucu.edu.aed.tda.Arboles.Impl;

import java.util.function.Consumer;

import ucu.edu.aed.impl.Cola;
import ucu.edu.aed.tda.Arboles.TDAArbolBinario;
import ucu.edu.aed.tda.Arboles.TDAElemento;
import ucu.edu.aed.tda.TDACola;

public class ArbolBinarioBusqueda <T extends Comparable<T>> implements TDAArbolBinario<T> {
    protected TDAElemento<T> raiz;

    @Override
    public T buscar(Comparable<T> criterioBusqueda) {
        if (raiz == null) {
            return null;
        }
        TDAElemento<T> encontrado = raiz.buscar(criterioBusqueda);
        return (encontrado != null) ? encontrado.getDato() : null;
    }

    @Override
    public boolean eliminar(Comparable<T> criterioBusqueda) {
        if (raiz != null) {
            raiz = raiz.eliminar(criterioBusqueda);
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean insertar(Comparable<T> dato) {
        if (esVacio()) {
            if (dato != null) {
                raiz = new ElementoAB<>((T) dato);
                return true;
            }
            return false;
        } else {
            return raiz.insertar(dato);
        }
    }

    @Override
    public void inOrder(Consumer<T> consumidor) {
        if (this.raiz != null) {
            this.raiz.inOrder((t) -> consumidor.accept(t.getDato()));
        }
    }

    @Override
    public void preOrder(Consumer<T> consumidor) {
        if (this.raiz != null) {
            this.raiz.preOrder((t) -> consumidor.accept(t.getDato()));
        }
    }

    @Override
    public void postOrder(Consumer<T> consumidor) {
        if (this.raiz != null) {
            this.raiz.postOrder((t) -> consumidor.accept(t.getDato()));
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

    @Override
    public TDAElemento<T> obtenerRaiz() {
        return this.raiz;
    }

    @Override
    public void recorridoPorNiveles(Consumer<T> consumidor) {
        if (raiz == null) {
        return;
        }
    TDACola<TDAElemento<T>> cola = new Cola<>();
    cola.poneEnCola(raiz);
    while (!cola.esVacio()) {
        TDAElemento<T> actual = cola.quitaDeCola();
        consumidor.accept(actual.getDato());
        if (actual.getHijoIzquierdo() != null) {
            cola.poneEnCola(actual.getHijoIzquierdo());
            }
        if (actual.getHijoDerecho() != null) {
            cola.poneEnCola(actual.getHijoDerecho());
            }
        }   
    }   
}