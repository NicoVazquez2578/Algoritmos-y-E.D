package ucu.edu.aed.ProblemSets.ProblemSet3.Ej12;

import java.util.List;

// Ejercicio 12 - Búsqueda de patrones en texto usando TTrieHashMap
public class MainPatrones {

    public static void main(String[] args) {
        TTrieHashMap<String> trie = new TTrieHashMap<>();

        String texto = "la casa de casandra estaba en la casilla cerca del castillo de cascada";
        String[] patrones = {"cas", "casa", "castillo", "la"};

        System.out.println("Texto: \"" + texto + "\"\n");

        for (String patron : patrones) {
            List<Integer> posiciones = trie.buscarPatron(texto, patron);
            System.out.printf("Patron %-12s -> %d ocurrencia(s) en posiciones: %s%n",
                "\"" + patron + "\"", posiciones.size(), posiciones);
        }

        System.out.println("\n--- Prefijos disponibles en el Trie ---");
        trie.predecir("ca").stream().limit(10)
            .forEach(e -> System.out.println("  " + e.getPalabra()));
    }
}
