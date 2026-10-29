package ucu.edu.aed.ProblemSets.ProblemSet3.Ej12;

import ucu.edu.aed.ProblemSets.ProblemSet3.trie.Entry;
import ucu.edu.aed.utils.FileUtils;

import java.util.List;
import java.util.Scanner;

// Ejercicio 12 - App "autocompletar" usando TTrieHashMap
public class MainAutocompletar {

    public static void main(String[] args) {
        TTrieHashMap<String> trie = new TTrieHashMap<>();

        FileUtils.leerLineas("./ut03/listado-general-desordenado.txt", p -> trie.insertar(p, p));
        System.out.println("Trie cargado (HashMap). Escribí un prefijo para autocompletar (vacío para salir):\n");

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String linea = sc.nextLine().trim();
            if (linea.isEmpty()) break;

            List<Entry<String>> resultado = trie.predecir(linea);
            if (resultado.isEmpty()) {
                System.out.println("  (sin coincidencias)");
            } else {
                System.out.println("  " + resultado.size() + " sugerencia(s):");
                resultado.stream().limit(10)
                    .forEach(e -> System.out.println("    " + e.getPalabra()));
                if (resultado.size() > 10)
                    System.out.println("    ... (" + (resultado.size() - 10) + " más)");
            }
        }
        sc.close();
    }
}
