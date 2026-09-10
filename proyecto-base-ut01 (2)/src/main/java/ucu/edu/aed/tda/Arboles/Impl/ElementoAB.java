package ucu.edu.aed.tda.Arboles.Impl;

import java.util.function.Consumer;

import ucu.edu.aed.tda.Arboles.TDAElemento;

public class ElementoAB <T extends Comparable <T>> implements TDAElemento<T> {

    private T dato;
    private TDAElemento<T> hijoIzquierdo;
    private TDAElemento<T> hijoDerecho;

    public ElementoAB(T dato){
        this.dato = dato;
        this.hijoDerecho = null;
        this.hijoIzquierdo = null;
    }

    @Override
    public void setHijoIzquierdo(TDAElemento<T> hijoIzquierdo) {
        this.hijoIzquierdo = hijoIzquierdo;
    }

    @Override
    public void setHijoDerecho(TDAElemento<T> hijoDerecho) {
        this.hijoDerecho = hijoDerecho;
    }

    @Override
    public TDAElemento<T> getHijoIzquierdo() {
        return this.hijoIzquierdo;
    }

    @Override
    public TDAElemento<T> getHijoDerecho() {
       return this.hijoDerecho;
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
    public TDAElemento<T> buscar(Comparable<T> criterioBusqueda) {
       TDAElemento<T> resultado = null;
       if (criterioBusqueda.compareTo(this.dato) == 0) {
            resultado = this;
        }
        else{
            if (criterioBusqueda.compareTo(this.dato)< 0){
                if (hijoIzquierdo != null){
                    resultado = hijoIzquierdo.buscar(criterioBusqueda);
                }
            }
            else{
                if (hijoDerecho != null){
                    resultado = hijoDerecho.buscar(criterioBusqueda);
                }
            }
        }
        return resultado;
    }

    @Override
    public TDAElemento<T> eliminar(Comparable<T> criterioBusqueda) {
       if (criterioBusqueda.compareTo(this.dato)<0){
        if (this.hijoIzquierdo != null){
            this.hijoIzquierdo = this.hijoIzquierdo.eliminar(criterioBusqueda);
        }
        return this;
       }
       else if (criterioBusqueda.compareTo(dato)> 0){
        if (this.hijoDerecho != null){
            this.hijoDerecho = this.hijoDerecho.eliminar(criterioBusqueda);
        }
        return this;
       }
       return quitarNodo();
    }

    public TDAElemento<T> quitarNodo(){
        if (this.hijoIzquierdo == null){
            return this.hijoDerecho;
        }
        else if (this.hijoDerecho == null){
            return this.hijoIzquierdo;
        }
        else{
            TDAElemento<T> elPadre = this;
            TDAElemento<T> elHijo = this.hijoIzquierdo;

            while (elHijo.getHijoDerecho() != null) {
                elPadre = elHijo;
                elHijo = elHijo.getHijoDerecho();
            }
            if (elPadre != this){
                elPadre.setHijoDerecho(elHijo.getHijoIzquierdo());
                elHijo.setHijoIzquierdo(this.getHijoIzquierdo());
            }
            elHijo.setHijoDerecho(this.getHijoDerecho());
            return elHijo;
        }
    }

    @Override
    //@SuppressWarnings("unchecked")
    public boolean insertar(Comparable<T> nuevoDato) {
        if (nuevoDato.compareTo(this.dato) > 0){
            if(this.hijoDerecho == null){
                this.hijoDerecho = new ElementoAB<>((T) nuevoDato);
            }
            else{
                return this.hijoDerecho.insertar(nuevoDato);
            }
        }
        else if (nuevoDato.compareTo(this.dato) < 0){
            if (this.hijoIzquierdo == null){
                this.hijoIzquierdo = new ElementoAB<>((T) nuevoDato);
            }
            else{
                return this.hijoIzquierdo.insertar(nuevoDato);
            }
        }
        // dato igual al nodo actual: duplicado, se ignora sin modificar el árbol
        return true;
    }

    @Override
    public void inOrder(Consumer<TDAElemento<T>> consumidor) {
        if (this.hijoIzquierdo != null){
            this.hijoIzquierdo.inOrder(consumidor);
        }
        consumidor.accept(this);
        if (this.hijoDerecho != null){
            this.hijoDerecho.inOrder(consumidor);
        }
    }

    @Override
    public void preOrder(Consumer<TDAElemento<T>> consumidor) {
        consumidor.accept(this);
        if (this.hijoIzquierdo != null){
            this.hijoIzquierdo.preOrder(consumidor);
        }
        if(this.hijoDerecho != null){
            this.hijoDerecho.preOrder(consumidor);
        }
    }

    @Override
    public void postOrder(Consumer<TDAElemento<T>> consumidor) {
        if (this.hijoIzquierdo != null){
            this.hijoIzquierdo.postOrder(consumidor);
        }
        if (this.hijoDerecho != null){
            this.hijoDerecho.postOrder(consumidor);
        }
        consumidor.accept(this);
    }

    @Override
    public boolean esHoja() {
        return (hijoIzquierdo == null && hijoDerecho == null);
    }

    @Override
    public int cantidadHojas() {
        if (esHoja()){
            return 1;
        }
        int hojasIzquierda = 0;
        if (hijoIzquierdo != null){
            hojasIzquierda = hijoIzquierdo.cantidadHojas();
        }
        int hojasDerecha = 0;
        if (hijoDerecho != null){
            hojasDerecha = hijoDerecho.cantidadHojas();
        }
        return hojasIzquierda + hojasDerecha;
    }

    @Override
    public int cantidadNodosInternos() {
        return cantidadNodos() - cantidadHojas();
    }

    @Override
    public int cantidadNodos() {
        int nodosIzquierda = 0;
        if(hijoIzquierdo!= null){
            nodosIzquierda = hijoIzquierdo.cantidadNodos();
        }
        int nodosDerecha = 0;
        if (hijoDerecho != null){
            nodosDerecha = hijoDerecho.cantidadNodos();
        }
        return 1 + nodosIzquierda + nodosDerecha;
    }

    @Override
    public int altura() {
        int hi = 0;
        int hd = 0;
        if (this.getHijoIzquierdo() != null) {
            hi = this.getHijoIzquierdo().altura();
        }  
        if (this.getHijoDerecho() != null){
            hd = this.getHijoDerecho().altura();
        }
        return Math.max(hi + 1, hd + 1);
    }

    @Override
    public int obtenerNivel(Comparable<T> criterioBusqueda) {
        if (criterioBusqueda.compareTo(this.dato) == 0){
            return 0;
        }
        if (hijoIzquierdo != null){
            int nivelIzq = hijoIzquierdo.obtenerNivel(criterioBusqueda);
            if (nivelIzq != -1){
                return nivelIzq + 1;
            }
        }
        if (hijoDerecho != null){
            int nivelDer = hijoDerecho.obtenerNivel(criterioBusqueda);
            if (nivelDer != -1){
                return nivelDer + 1;
            }
        }
        return -1;
    }
}