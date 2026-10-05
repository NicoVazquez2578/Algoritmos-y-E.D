package ucu.edu.aed.impl;

import ucu.edu.aed.tda.TDAPila;
import java.util.NoSuchElementException;

public class Pila<T> extends Lista<T> implements TDAPila<T> {

    @Override
    public void mete(T dato) {
        // Inserta al principio de la lista (índice 0)
        this.agregar(0, dato);
    }

    @Override
    public T saca() {
        if (this.esVacio()) {
            throw new NoSuchElementException("La pila está vacía");
        }
        // Elimina y retorna el primer elemento
        return this.remover(0);
    }

    @Override
    public T tope() {
        if (this.esVacio()) {
            throw new NoSuchElementException("La pila está vacía");
        }
        // Retorna el primer elemento sin eliminarlo
        return this.obtener(0);
    }
}