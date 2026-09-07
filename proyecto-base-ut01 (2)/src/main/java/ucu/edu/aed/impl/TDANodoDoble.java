package ucu.edu.aed.impl;

/*
Esta implementacion la usamos para la lista doblemente enlazada y la lista circular doblemente enlazada.
A diferencia de TDANodo, que solo conoce al nodo siguiente, este conoce al nodo anteriror y al nodo siguiente.
 */
public class TDANodoDoble<T> {

    protected T dato;
    protected TDANodoDoble<T> anterior;
    protected TDANodoDoble<T> siguiente;

    public TDANodoDoble(T dato) {
        this.dato = dato;
        this.anterior = null;
        this.siguiente = null;
    }
}
