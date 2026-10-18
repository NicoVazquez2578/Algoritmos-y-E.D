package ucu.edu.aed.ProblemSets.ProblemSet3.Ej07;

import ucu.edu.aed.ProblemSets.ProblemSet3.Ej07.medibles.*;
import ucu.edu.aed.ProblemSets.ProblemSet3.lib.Medible;
import ucu.edu.aed.ProblemSets.ProblemSet3.lib.Medicion;
import ucu.edu.aed.ProblemSets.ProblemSet3.trie.TTrie;
import ucu.edu.aed.ProblemSets.ProblemSet3.trie.impl.Trie;
import ucu.edu.aed.utils.FileUtils;

import java.util.*;

// Ejercicio 7 Parte 5 - Benchmark de predecir/autocompletar para prefijo "cas"
public class MainPredecirEj07 {
    private static final int REPETICIONES = 20;
    private static final String PREFIJO   = "cas";

    public static void main(String[] args) {
        TTrie<String> trie    = new Trie<>();
        LinkedList<String> ll = new LinkedList<>();
        Map<String,String> hm = new HashMap<>();

        List<String> palabras = new LinkedList<>();
        FileUtils.leerLineas("./ut03/listado-general-desordenado.txt", palabras::add);

        for (String p : palabras) {
            trie.insertar(p, p);
            ll.add(p);
            hm.put(p, p);
        }

        System.out.println("=== Ejercicio 7 Parte 5 — Benchmark predecir prefijo \"" + PREFIJO + "\" ===");
        System.out.printf("Palabras cargadas: %d%n%n", palabras.size());

        // Mostrar cuántas palabras comienzan con el prefijo
        List<?> predicciones = trie.predecir(PREFIJO);
        System.out.println("Palabras que comienzan con \"" + PREFIJO + "\": " + predicciones.size());
        predicciones.stream().limit(10).forEach(e -> System.out.println("  " + e));
        System.out.println();

        List<Medible<List<String>>> medibles = new ArrayList<>();
        medibles.add(new MedicionPredecirTrie(trie, PREFIJO));
        medibles.add(new MedicionPredecirLinkedList(ll, PREFIJO));
        medibles.add(new MedicionPredecirHashMap((HashMap<String,String>) hm, PREFIJO));

        System.out.printf("%-35s  %-12s  %s%n", "Estructura", "Memoria", "Tiempo");
        System.out.println("-".repeat(65));
        for (Medible<List<String>> m : medibles) {
            Medicion mi = m.medir(REPETICIONES, Collections.emptyList());
            mi.print();
        }
    }
}
