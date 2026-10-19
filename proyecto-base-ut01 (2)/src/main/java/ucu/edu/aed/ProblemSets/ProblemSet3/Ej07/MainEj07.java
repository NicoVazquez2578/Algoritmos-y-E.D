package ucu.edu.aed.ProblemSets.ProblemSet3.Ej07;

import ucu.edu.aed.ProblemSets.ProblemSet3.Ej07.medibles.*;
import ucu.edu.aed.ProblemSets.ProblemSet3.lib.Medible;
import ucu.edu.aed.ProblemSets.ProblemSet3.lib.Medicion;
import ucu.edu.aed.ProblemSets.ProblemSet3.trie.TTrie;
import ucu.edu.aed.ProblemSets.ProblemSet3.trie.impl.Trie;
import ucu.edu.aed.utils.FileUtils;

import java.util.*;

// Ejercicio 7 - Benchmark de búsqueda en LinkedList, ArrayList, TTrie, HashMap, TreeMap
public class MainEj07 {
    private static final int REPETICIONES = 20;

    public static void main(String[] args) {
        TTrie<String> trie       = new Trie<>();
        LinkedList<String> ll    = new LinkedList<>();
        ArrayList<String> al     = new ArrayList<>();
        Map<String,String> hm    = new HashMap<>();
        Map<String,String> tm    = new TreeMap<>();

        List<String> agregar  = new LinkedList<>();
        List<String> buscar   = new LinkedList<>();
        FileUtils.leerLineas("./ut03/listado-general-desordenado.txt",  agregar::add);
        FileUtils.leerLineas("./ut03/listado-general-palabrasBuscar.txt", buscar::add);

        for (String p : agregar) {
            trie.insertar(p, p);
            ll.add(p);
            al.add(p);
            hm.put(p, p);
            tm.put(p, p);
        }

        System.out.println("=== Ejercicio 7 — Benchmark búsqueda (" + REPETICIONES + " repeticiones) ===");
        System.out.printf("Palabras cargadas: %d | palabras a buscar: %d%n%n", agregar.size(), buscar.size());

        List<Medible<List<String>>> medibles = new ArrayList<>();
        medibles.add(new MedicionBuscarLinkedList(ll));
        medibles.add(new MedicionBuscarArrayList(al));
        medibles.add(new MedicionBuscarTrie(trie));
        medibles.add(new MedicionBuscarHashMap((HashMap<String,String>) hm));
        medibles.add(new MedicionBuscarTreeMap((TreeMap<String,String>) tm));

        System.out.printf("%-35s  %-12s  %s%n", "Estructura", "Memoria", "Tiempo");
        System.out.println("-".repeat(65));
        for (Medible<List<String>> m : medibles) {
            Medicion mi = m.medir(REPETICIONES, buscar);
            mi.print();
        }
    }
}
