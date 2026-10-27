package ucu.edu.aed.tda.TrieYHash.Impl;

import ucu.edu.aed.tda.TrieYHash.TNodoTrie;
import ucu.edu.aed.tda.TrieYHash.TNodoGenerico;
import ucu.edu.aed.tda.TrieYHash.TTrie;
import ucu.edu.aed.tda.TDALista;

public class NodoTrie<T extends Comparable<T>> implements TNodoTrie<T>{

    @Override 
    public boolean insertar(String palabra, T dato){
        NodoTrie<T> nodoActual = this;
        char[] letras = palabra.toCharArray();
        
        for (char letra : letras){
            int indice = letra - 'a';
            NodoTrie<T> unHijo = nodoActual.hijos[indice];
            if (unHijo == null){
                unHijo = new NodoTrie<>(dato);
                nodoActual.agregarHijo(unHijo, indice);
            }

            nodoActual = unHijo;

        }

        nodoActual.esFinDePalabra = true;
        return true;
    }

    @override
    public nonoTrie<T> buscar(String unaPalabra){
        nodoTrie<T> nodoActual = this;
        for (char car : unaPalabra.toCharArray()){
            unHijo = nodoActual.hijos[car]
            if unHijo == null:
                return null;
            else:
                nodoActual = unHijo;
            end if
        end for
        if nodoActual.esFinDePalabra:
            return nodoActual;
        else:
            return null;
        end if
        }

    }
}
