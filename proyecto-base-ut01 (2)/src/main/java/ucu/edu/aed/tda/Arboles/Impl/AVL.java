package ucu.edu.aed.tda.Arboles.Impl;
import ucu.edu.aed.tda.Arboles.TDAElemento;

public class AVL <T extends Comparable<T>> extends ArbolBinarioBusqueda<T>{

    @Override
    @SuppressWarnings("unchecked")
    public boolean insertar(Comparable<T> dato) {
        if (dato == null) {
            return false;
        }
        else {
            raiz = insertarAVL(raiz, (T) dato);
            return true;
        }
    }
    
    public TDAElemento<T> insertarAVL(TDAElemento<T> nodo, T dato){
        if (nodo == null){
            return new ElementoAB<>(dato);
        }
        else {
            if(dato.compareTo(nodo.getDato()) < 0){
                nodo.setHijoIzquierdo(insertarAVL(nodo.getHijoIzquierdo(), dato));
            }
            if(dato.compareTo(nodo.getDato()) > 0){
                nodo.setHijoDerecho(insertarAVL(nodo.getHijoDerecho(), dato));
            }
            
            int balance = calcularBalance(nodo);
            if(balance>1 && dato.compareTo(nodo.getHijoIzquierdo().getDato()) >= 0){
                return rotacionLR(nodo);
            }
            if(balance>1 && dato.compareTo(nodo.getHijoIzquierdo().getDato()) < 0){
                return rotacionLL(nodo);
            }
            if(balance < -1 && dato.compareTo(nodo.getHijoDerecho().getDato()) <= 0){
                return rotacionRL(nodo);
            }
            if(balance < -1 && dato.compareTo(nodo.getHijoDerecho().getDato()) > 0){
                return rotacionRR(nodo);
            }
            return nodo;
        }
    }

    public TDAElemento<T> eliminarAVL(TDAElemento<T> nodo, Comparable<T> criterio){
        if(nodo == null){
            return null;
        }
        else {
            TDAElemento<T> tmp = nodo;
            if (criterio.compareTo(nodo.getDato()) < 0){
                nodo.setHijoIzquierdo(eliminarAVL(nodo.getHijoIzquierdo(), criterio));
            }
            if (criterio.compareTo(nodo.getDato()) > 0) {
                nodo.setHijoDerecho(eliminarAVL(nodo.getHijoDerecho(), criterio));
            }
            else {
                tmp = nodo.eliminar(criterio);
            }

            int balance = calcularBalance(tmp);
            if (balance > 1 && calcularBalance(tmp.getHijoIzquierdo()) >= 0){
                return rotacionLL(tmp);
            }
            if (balance > 1 && calcularBalance(tmp.getHijoIzquierdo()) < 0) {
                return rotacionLR(tmp);
            }
            if (balance < -1 && calcularBalance(tmp.getHijoDerecho()) <= 0) {
                return rotacionRR(tmp);
            }
            if (balance < -1 && calcularBalance(tmp.getHijoDerecho()) > 0) {
                return rotacionRL(tmp);
            }
            return tmp;
        }
    }

    public int calcularBalance(TDAElemento<T> nodo){
        if (nodo == null) {
            return 0;
        }
        int alturaDerecha = 0;
        if (nodo.getHijoDerecho() != null) {
            alturaDerecha = nodo.getHijoDerecho().altura();
        }
        int alturaIzquierdo = 0;
        if (nodo.getHijoIzquierdo() != null) {
            alturaIzquierdo = nodo.getHijoIzquierdo().altura();
        }
        return alturaIzquierdo - alturaDerecha;
    }

    public TDAElemento<T> rotacionLL(TDAElemento<T> k2){
        TDAElemento<T> k1 = k2.getHijoIzquierdo();
        k2.setHijoIzquierdo(k1.getHijoDerecho());
        k1.setHijoDerecho(k2);
        return k1;
    }

    public TDAElemento<T> rotacionRR(TDAElemento<T> k1){
        TDAElemento<T> k2 = k1.getHijoDerecho();
        k1.setHijoDerecho(k2.getHijoIzquierdo());
        k2.setHijoIzquierdo(k1);
        return k2;
    }

    public TDAElemento<T> rotacionLR(TDAElemento<T> k3){
        k3.setHijoIzquierdo(rotacionRR(k3.getHijoIzquierdo()));
        return rotacionLL(k3);
    }

    public TDAElemento<T> rotacionRL(TDAElemento<T> k1){
        k1.setHijoDerecho(rotacionLL(k1.getHijoDerecho()));
        return rotacionRR(k1);
    }
}