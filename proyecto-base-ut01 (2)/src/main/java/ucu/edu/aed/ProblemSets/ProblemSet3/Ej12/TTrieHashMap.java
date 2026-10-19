package ucu.edu.aed.ProblemSets.ProblemSet3.Ej12;

import ucu.edu.aed.ProblemSets.ProblemSet3.trie.Entry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

// Ejercicio 12 - Trie con HashMap en cada nodo (flexible para cualquier alfabeto)
public class TTrieHashMap<T> implements Serializable {
    private final TNodoTrieHashMap<T> raiz = new TNodoTrieHashMap<>();

    public boolean insertar(String palabra, T dato) {
        return palabra != null && raiz.insertar(palabra.toLowerCase(), dato);
    }

    public Entry<T> buscar(String palabra) {
        return palabra == null ? null : raiz.buscar(palabra.toLowerCase());
    }

    public List<Entry<T>> predecir(String prefijo) {
        return raiz.predecir(prefijo == null ? "" : prefijo.toLowerCase());
    }

    // Retorna posiciones en 'texto' donde 'patron' comienza, usando el trie para la búsqueda
    public List<Integer> buscarPatron(String texto, String patron) {
        insertar(patron, null);
        String t = texto.toLowerCase();
        String p = patron.toLowerCase();
        List<Integer> posiciones = new ArrayList<>();
        for (int i = 0; i <= t.length() - p.length(); i++) {
            TNodoTrieHashMap<T> nodo = raiz;
            boolean match = true;
            for (int j = 0; j < p.length(); j++) {
                nodo = nodo.getChildren().get(t.charAt(i + j));
                if (nodo == null) { match = false; break; }
            }
            if (match && nodo != null && nodo.esPalabra()) posiciones.add(i);
        }
        return posiciones;
    }

    public void recorrer(Consumer<Entry<T>> consumer) {
        raiz.recorrer(consumer);
    }
}
