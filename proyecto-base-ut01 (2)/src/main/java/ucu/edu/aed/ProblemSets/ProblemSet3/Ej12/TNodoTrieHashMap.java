package ucu.edu.aed.ProblemSets.ProblemSet3.Ej12;

import ucu.edu.aed.ProblemSets.ProblemSet3.trie.Entry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

// Ejercicio 12 - nodo del Trie usando HashMap en lugar de array fijo
public class TNodoTrieHashMap<T> implements Serializable {
    private final Map<Character, TNodoTrieHashMap<T>> children = new HashMap<>();
    private boolean esPalabra;
    private T dato;

    public boolean insertar(String palabra, T dato) {
        if (palabra.isEmpty()) {
            if (esPalabra) return false;
            esPalabra = true; this.dato = dato; return true;
        }
        char c = Character.toLowerCase(palabra.charAt(0));
        children.putIfAbsent(c, new TNodoTrieHashMap<>());
        return children.get(c).insertar(palabra.substring(1), dato);
    }

    public Entry<T> buscar(String palabra) {
        if (palabra.isEmpty()) return new Entry<>(dato, esPalabra, "");
        char c = Character.toLowerCase(palabra.charAt(0));
        TNodoTrieHashMap<T> hijo = children.get(c);
        if (hijo == null) return null;
        Entry<T> sub = hijo.buscar(palabra.substring(1));
        if (sub == null) return null;
        return new Entry<>(sub.getDato(), sub.esPalabra(), c + sub.getPalabra());
    }

    public List<Entry<T>> predecir(String prefijo) {
        List<Entry<T>> result = new ArrayList<>();
        if (!prefijo.isEmpty()) {
            char c = Character.toLowerCase(prefijo.charAt(0));
            TNodoTrieHashMap<T> hijo = children.get(c);
            if (hijo != null)
                for (Entry<T> e : hijo.predecir(prefijo.substring(1)))
                    result.add(new Entry<>(e.getDato(), e.esPalabra(), c + e.getPalabra()));
            return result;
        }
        if (esPalabra) result.add(new Entry<>(dato, true, ""));
        for (Map.Entry<Character, TNodoTrieHashMap<T>> kv : children.entrySet())
            for (Entry<T> e : kv.getValue().predecir(""))
                result.add(new Entry<>(e.getDato(), e.esPalabra(), kv.getKey() + e.getPalabra()));
        return result;
    }

    // Verifica si el texto contiene el patron usando la ruta del trie desde este nodo
    boolean matchDesde(String texto, int inicio) {
        TNodoTrieHashMap<T> nodo = this;
        for (int i = inicio; i < texto.length(); i++) {
            char c = Character.toLowerCase(texto.charAt(i));
            nodo = nodo.children.get(c);
            if (nodo == null) return false;
            if (nodo.esPalabra) return true;
        }
        return nodo != null && nodo.esPalabra;
    }

    public void recorrer(Consumer<Entry<T>> consumer) {
        predecir("").forEach(consumer);
    }

    public T getDato()         { return dato; }
    public boolean esPalabra() { return esPalabra; }
    Map<Character, TNodoTrieHashMap<T>> getChildren() { return children; }
}
