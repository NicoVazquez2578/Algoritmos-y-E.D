package ucu.edu.aed.ProblemSets.ProblemSet3.trie.impl;

import ucu.edu.aed.ProblemSets.ProblemSet3.trie.Entry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NodoTrie<T> implements Serializable {
    private static final int SIZE = 26;

    @SuppressWarnings("unchecked")
    private final NodoTrie<T>[] children = new NodoTrie[SIZE];
    private boolean esPalabra;
    private T dato;

    private int idx(char c) { return Character.toLowerCase(c) - 'a'; }

    public boolean insertar(String palabra, T dato) {
        if (palabra.isEmpty()) {
            if (esPalabra) return false;
            esPalabra = true; this.dato = dato; return true;
        }
        int i = idx(palabra.charAt(0));
        if (i < 0 || i >= SIZE) return false;
        if (children[i] == null) children[i] = new NodoTrie<>();
        return children[i].insertar(palabra.substring(1), dato);
    }

    public Entry<T> buscar(String palabra) {
        if (palabra.isEmpty()) return new Entry<>(dato, esPalabra, "");
        int i = idx(palabra.charAt(0));
        if (i < 0 || i >= SIZE || children[i] == null) return null;
        Entry<T> sub = children[i].buscar(palabra.substring(1));
        if (sub == null) return null;
        return new Entry<>(sub.getDato(), sub.esPalabra(), palabra.charAt(0) + sub.getPalabra());
    }

    public List<Entry<T>> predecir(String prefijo) {
        List<Entry<T>> result = new ArrayList<>();
        if (!prefijo.isEmpty()) {
            int i = idx(prefijo.charAt(0));
            if (i >= 0 && i < SIZE && children[i] != null) {
                char c = prefijo.charAt(0);
                for (Entry<T> e : children[i].predecir(prefijo.substring(1)))
                    result.add(new Entry<>(e.getDato(), e.esPalabra(), c + e.getPalabra()));
            }
            return result;
        }
        if (esPalabra) result.add(new Entry<>(dato, true, ""));
        for (int i = 0; i < SIZE; i++) {
            if (children[i] != null) {
                char c = (char) ('a' + i);
                for (Entry<T> e : children[i].predecir(""))
                    result.add(new Entry<>(e.getDato(), e.esPalabra(), c + e.getPalabra()));
            }
        }
        return result;
    }

    public void recorrer(Consumer<Entry<T>> consumer) {
        predecir("").forEach(consumer);
    }

    public T getDato()         { return dato; }
    public boolean esPalabra() { return esPalabra; }
}
