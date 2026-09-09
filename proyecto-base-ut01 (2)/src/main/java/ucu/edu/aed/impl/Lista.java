package ucu.edu.aed.impl;
import ucu.edu.aed.tda.TDALista;

import java.util.function.Predicate;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

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

    @Override
    public void agregar(int index, T elem){
        if (index < 0 || index > tamaño){
            throw new IndexOutOfBoundsException("Indice fuera de rango" + index);
        }
        TDANodo<T> nuevo = new TDANodo<>(elem);
        if (index == 0){
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
        }
        else{
            TDANodo<T> actual = cabeza;
            for (int i = 0; i< index -1; i++){
                actual = actual.siguiente;
            }
            nuevo.siguiente = actual.siguiente;
            actual.siguiente = nuevo;
        }
        tamaño++;
    }

    @Override
    public T obtener(int index){
        if (index < 0 || index >= tamaño){
            throw new IndexOutOfBoundsException("Indice fuera de rango" + index);
        }
        TDANodo<T> actual = cabeza;
        for(int i = 0; i < index; i++){
            actual = actual.siguiente;
        }
        return actual.dato;
    }

    @Override
    public T remover (int index){
        if(index < 0 || index >= tamaño){
            throw new IndexOutOfBoundsException("Indice fuera de rango" + index);
        }
        T datoremovido;

        if (index == 0){
            datoremovido = cabeza.dato;
            cabeza = cabeza.siguiente;
        }
        else{
            TDANodo<T> actual = cabeza;

            for (int i = 0; i < index -1; i++){
                actual = actual.siguiente;
            }
            TDANodo<T> nodoARemover = actual.siguiente;
            datoremovido = nodoARemover.dato;
            actual.siguiente = nodoARemover.siguiente;
        }
        tamaño--;
        return datoremovido;
    }

    @Override
    public boolean remover (T elem) {
        
        if (cabeza == null){
            return false;
        }
        
        if(cabeza.dato.equals(elem)){
            cabeza = cabeza.siguiente;
            tamaño = tamaño - 1;
            return true;
        }
        TDANodo<T> actual = cabeza;
        while (actual.siguiente != null){
            if(actual.siguiente.dato.equals(elem)){
                actual.siguiente = actual.siguiente.siguiente;
                tamaño = tamaño - 1;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    @Override
    public boolean contiene(T elem){
        TDANodo<T> actual = cabeza;
        while (actual != null){
            
            if(actual.dato.equals(elem)){
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    @Override
    public int indiceDe(T elem){
        
        TDANodo<T> actual = cabeza;
        int posicion = 0;
        while(actual != null){
            
            if(actual.dato.equals(elem)){
                return posicion;
            }
            actual = actual.siguiente;
            posicion++;
        }
        return -1;
    }

    @Override
    public T buscar(Predicate<T> criterio){

        TDANodo<T> actual = cabeza;
        while(actual != null){

            if(criterio.test(actual.dato)){
                return actual.dato;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    @Override
    public TDALista<T> ordenar(Comparator<T> comparator){
       
       //Crear nueva lista para el resultado
        Lista<T> listaOrdenada = new Lista<T>();

        if (cabeza == null){
            return listaOrdenada;
        }
        //Usar el tamaño real de la lista
        int tam = this.tamaño;
        
        //Crear y llenar la lista temporal
        List<T> arregloTemp = new ArrayList<>(tam);

        TDANodo<T> actual = cabeza;
        for (int i = 0; i < tam; i++){

            arregloTemp.add(actual.dato);
            actual = actual.siguiente;
        }
        //Bubble sort
        for(int i = 0; i < tam -1; i++){
            for (int j = 0; j < tam - 1 - i; j++) {
            
                // Si el elemento actual es mayor que el siguiente
            if (comparator.compare(arregloTemp.get(j), arregloTemp.get(j + 1)) > 0) {
                // Intercambiar
                T temp = arregloTemp.get(j);
                arregloTemp.set(j, arregloTemp.get(j + 1));
                arregloTemp.set(j + 1, temp);
                }
            }
        }   
        // Construir lista ordenada
        for (int i = 0; i < tam; i++) {
        listaOrdenada.agregar(arregloTemp.get(i));
        }
    
        return listaOrdenada;
    }
    
    @Override
    public int tamaño(){
        return tamaño;
    }

    @Override
    public boolean esVacio(){
        return tamaño == 0;
    }
    
    @Override
    public void vaciar(){
        cabeza = null;
        tamaño = 0;
    }
}
