package ucu.edu.aed.impl;

public class TDANodo<T> {
    protected T dato;
    protected TDANodo<T> siguiente;

    public TDANodo(T dato){
        this.dato = dato;
        this.siguiente = null;
    }

    public T getDato(){return this.dato;}
    
    public TDANodo<T> getSiguiente(){
        return this.siguiente;
    }
}
