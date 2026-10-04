package ucu.edu.aed.ProblemSets.ProblemSet3.Ej07.medibles;

import ucu.edu.aed.ProblemSets.ProblemSet3.lib.Medible;
import ucu.edu.aed.ProblemSets.ProblemSet3.trie.TTrie;

import java.util.List;

public class MedicionBuscarTrie extends Medible<List<String>> {
    private final TTrie<String> trie;

    public MedicionBuscarTrie(TTrie<String> trie) { this.trie = trie; }

    @Override
    public void ejecutar(int repeticiones, List<String> palabras) {
        for (int i = 0; i < repeticiones; i++)
            for (String p : palabras) trie.buscar(p);
    }

    @Override
    public Object getObjetoAMedirMemoria() { return trie; }
}
