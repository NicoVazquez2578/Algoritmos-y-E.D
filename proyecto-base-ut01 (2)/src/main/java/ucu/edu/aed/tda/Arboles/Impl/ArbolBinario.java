package ucu.edu.aed.tda.Arboles.Impl;

import java.util.function.Consumer;

import ucu.edu.aed.impl.Cola;
import ucu.edu.aed.tda.Arboles.TDAArbolBinario;
import ucu.edu.aed.tda.Arboles.TDAElemento;
import ucu.edu.aed.tda.TDACola;

/**
 * Árbol binario genérico: no impone ningún orden entre los datos. Cada
 * inserción ocupa la primera posición libre recorriendo el árbol por
 * niveles, así que su forma es siempre la de un árbol completo. Al no
 * tener invariante de orden, buscar y eliminar tienen que recorrer el
 * árbol completo (a diferencia de ArbolBinarioBusqueda).
 */
public class ArbolBinario<T extends Comparable<T>> implements TDAArbolBinario<T> {
    protected TDAElemento<T> raiz;

    @Override
    public boolean insertar(Comparable<T> dato) {
        if (dato == null) {
            return false;
        }
        if (buscar(dato) != null) {
            return false; // ya existe: TDAArbolBinario no admite duplicados
        }
        T nuevoDato = (T) dato;
        if (esVacio()) {
            raiz = new ElementoAB<>(nuevoDato);
            return true;
        }
        TDACola<TDAElemento<T>> cola = new Cola<>();
        cola.poneEnCola(raiz);
        while (!cola.esVacio()) {
            TDAElemento<T> actual = cola.quitaDeCola();
            if (actual.getHijoIzquierdo() == null) {
                actual.setHijoIzquierdo(new ElementoAB<>(nuevoDato));
                return true;
            }
            if (actual.getHijoDerecho() == null) {
                actual.setHijoDerecho(new ElementoAB<>(nuevoDato));
                return true;
            }
            cola.poneEnCola(actual.getHijoIzquierdo());
            cola.poneEnCola(actual.getHijoDerecho());
        }
        return false; // inalcanzable: un árbol finito siempre tiene un lugar libre
    }

    @Override
    public T buscar(Comparable<T> criterioBusqueda) {
        if (raiz == null || criterioBusqueda == null) {
            return null;
        }
        TDACola<TDAElemento<T>> cola = new Cola<>();
        cola.poneEnCola(raiz);
        while (!cola.esVacio()) {
            TDAElemento<T> actual = cola.quitaDeCola();
            if (criterioBusqueda.compareTo(actual.getDato()) == 0) {
                return actual.getDato();
            }
            if (actual.getHijoIzquierdo() != null) {
                cola.poneEnCola(actual.getHijoIzquierdo());
            }
            if (actual.getHijoDerecho() != null) {
                cola.poneEnCola(actual.getHijoDerecho());
            }
        }
        return null;
    }

    @Override
    public boolean eliminar(Comparable<T> criterioBusqueda) {
        if (raiz == null || criterioBusqueda == null) {
            return false;
        }

        TDACola<TDAElemento<T>> cola = new Cola<>();
        cola.poneEnCola(raiz);

        TDAElemento<T> nodoAEliminar = null;
        TDAElemento<T> ultimo = raiz;
        TDAElemento<T> padreDeUltimo = null;
        boolean ultimoEsHijoIzquierdo = false;

        while (!cola.esVacio()) {
            TDAElemento<T> actual = cola.quitaDeCola();

            if (nodoAEliminar == null && criterioBusqueda.compareTo(actual.getDato()) == 0) {
                nodoAEliminar = actual;
            }
            if (actual.getHijoIzquierdo() != null) {
                padreDeUltimo = actual;
                ultimo = actual.getHijoIzquierdo();
                ultimoEsHijoIzquierdo = true;
                cola.poneEnCola(actual.getHijoIzquierdo());
            }
            if (actual.getHijoDerecho() != null) {
                padreDeUltimo = actual;
                ultimo = actual.getHijoDerecho();
                ultimoEsHijoIzquierdo = false;
                cola.poneEnCola(actual.getHijoDerecho());
            }
        }

        if (nodoAEliminar == null) {
            return false;
        }

        // Mueve el dato del último nodo (en orden de nivel) al nodo eliminado, y
        // desconecta ese último nodo, para conservar la forma de árbol completo
        nodoAEliminar.setDato(ultimo.getDato());
        if (padreDeUltimo == null) {
            raiz = null;
        } else if (ultimoEsHijoIzquierdo) {
            padreDeUltimo.setHijoIzquierdo(null);
        } else {
            padreDeUltimo.setHijoDerecho(null);
        }
        return true;
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
}
