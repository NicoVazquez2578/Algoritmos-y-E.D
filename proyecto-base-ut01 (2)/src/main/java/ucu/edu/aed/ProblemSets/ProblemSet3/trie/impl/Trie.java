package ucu.edu.aed.ProblemSets.ProblemSet3.trie.impl;

import ucu.edu.aed.ProblemSets.ProblemSet3.trie.Entry;
import ucu.edu.aed.ProblemSets.ProblemSet3.trie.TTrie;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

// Trie con array fijo de 26 hijos (a-z)
public class Trie<T> implements TTrie<T>, Serializable {
    private final NodoTrie<T> raiz = new NodoTrie<>();

    @Override
    public boolean insertar(String palabra, T dato) {
        return palabra != null && raiz.insertar(palabra.toLowerCase(), dato);
    }

    @Override
    public Entry<T> buscar(String palabra) {
        return palabra == null ? null : raiz.buscar(palabra.toLowerCase());
    }

    @Override
    public List<Entry<T>> predecir(String prefijo) {
        return raiz.predecir(prefijo == null ? "" : prefijo.toLowerCase());
    }

    @Override
    public void recorrer(Consumer<Entry<T>> consumer) {
        raiz.recorrer(consumer);
    }
}
