package ucu.edu.aed.ProblemSets.ProblemSet3.trie;

import java.util.List;
import java.util.function.Consumer;

public interface TTrie<T> {
    void recorrer(Consumer<Entry<T>> consumer);
    Entry<T> buscar(String palabra);
    boolean insertar(String palabra, T dato);
    List<Entry<T>> predecir(String prefijo);
}
