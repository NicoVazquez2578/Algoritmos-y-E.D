package ucu.edu.aed.tda.Implementaciones;
import ucu.edu.aed.tda.TDALista;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class Lista<T> implements TDALista<T> {
    
    protected TDANodo<T> cabeza;
    protected int tamaño;

    public Lista(){
        this.cabeza = null;
        this.tamaño = 0;
    }

    @Override
    public void agregar(T elem){
        TDANodo<T> nuevo = new TDANodo<>(elem);

        if (cabeza == null){
            cabeza = nuevo;
        }
        else{
            TDANodo<T> actual = cabeza;
            while(actual.siguiente != null){
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        tamaño++;
    }
}
