package ucu.edu.aed.tda.Arboles.Impl;

import java.util.function.Consumer;

import ucu.edu.aed.impl.Lista;
import ucu.edu.aed.tda.TDALista;
import ucu.edu.aed.tda.Arboles.TDAElementoGenerico;

public class ElementoGenerico<T> implements TDAElementoGenerico<T> {

    private T dato;
    private TDALista<TDAElementoGenerico<T>> hijos;

    public ElementoGenerico(T dato) {
        this.dato = dato;
        this.hijos = new Lista<>();
    }

    @Override
    public void agregarHijo(TDAElementoGenerico<T> hijo) {
        this.hijos.agregar(hijo);
    }

    @Override
    public TDALista<TDAElementoGenerico<T>> getHijos() {
        return this.hijos;
    }

    @Override
    public void setDato(T dato) {
        this.dato = dato;
    }

    @Override
    public T getDato() {
        return this.dato;
    }

    @Override
    public TDAElementoGenerico<T> buscar(Comparable<T> criterioBusqueda) {
        if (criterioBusqueda.compareTo(this.dato) == 0) {
            return this;
        }
        for (int i = 0; i < hijos.tamaño(); i++) {
            TDAElementoGenerico<T> encontrado = hijos.obtener(i).buscar(criterioBusqueda);
            if (encontrado != null) {
                return encontrado;
            }
        }
        return null;
    }

    @Override
    public boolean insertarHijo(T dato, Comparable<T> criterioPadre) {
        if (criterioPadre.compareTo(this.dato) == 0) {
            agregarHijo(new ElementoGenerico<>(dato));
            return true;
        }
        for (int i = 0; i < hijos.tamaño(); i++) {
            if (hijos.obtener(i).insertarHijo(dato, criterioPadre)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public TDAElementoGenerico<T> eliminar(Comparable<T> criterioBusqueda) {
        if (criterioBusqueda.compareTo(this.dato) == 0) {
            // este nodo, junto con todo su subárbol, se elimina
            return null;
        }
        for (int i = 0; i < hijos.tamaño(); i++) {
            TDAElementoGenerico<T> hijo = hijos.obtener(i);
            if (criterioBusqueda.compareTo(hijo.getDato()) == 0) {
                // se encontró como hijo directo: se quita de la lista junto con su subárbol
                hijos.remover(i);
                return this;
            }
        }
        // no es hijo directo: seguir buscando más abajo
        for (int i = 0; i < hijos.tamaño(); i++) {
            hijos.obtener(i).eliminar(criterioBusqueda);
        }
        return this;
    }

    @Override
    public void preOrder(Consumer<TDAElementoGenerico<T>> consumidor) {
        consumidor.accept(this);
        for (int i = 0; i < hijos.tamaño(); i++) {
            hijos.obtener(i).preOrder(consumidor);
        }
    }

    @Override
    public void postOrder(Consumer<TDAElementoGenerico<T>> consumidor) {
        for (int i = 0; i < hijos.tamaño(); i++) {
            hijos.obtener(i).postOrder(consumidor);
        }
        consumidor.accept(this);
    }

    @Override
    public boolean esHoja() {
        return hijos.esVacio();
    }

    @Override
    public int cantidadHojas() {
        if (esHoja()) {
            return 1;
        }
        int total = 0;
        for (int i = 0; i < hijos.tamaño(); i++) {
            total += hijos.obtener(i).cantidadHojas();
        }
        return total;
    }

    @Override
    public int cantidadNodosInternos() {
        return cantidadNodos() - cantidadHojas();
    }

    @Override
    public int cantidadNodos() {
        int total = 1;
        for (int i = 0; i < hijos.tamaño(); i++) {
            total += hijos.obtener(i).cantidadNodos();
        }
        return total;
    }

    @Override
    public int altura() {
        if (esHoja()) {
            return 1;
        }
        int maxAlturaHijos = 0;
        for (int i = 0; i < hijos.tamaño(); i++) {
            int alturaHijo = hijos.obtener(i).altura();
            if (alturaHijo > maxAlturaHijos) {
                maxAlturaHijos = alturaHijo;
            }
        }
        return 1 + maxAlturaHijos;
    }

    @Override
    public int obtenerNivel(Comparable<T> criterioBusqueda) {
        if (criterioBusqueda.compareTo(this.dato) == 0) {
            return 0;
        }
        for (int i = 0; i < hijos.tamaño(); i++) {
            int nivelHijo = hijos.obtener(i).obtenerNivel(criterioBusqueda);
            if (nivelHijo != -1) {
                return nivelHijo + 1;
            }
        }
        return -1;
    }
}