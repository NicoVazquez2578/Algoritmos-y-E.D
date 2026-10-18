package ucu.edu.aed.ProblemSets.ProblemSet3.Ej07.medibles;

import ucu.edu.aed.ProblemSets.ProblemSet3.lib.Medible;
import ucu.edu.aed.ProblemSets.ProblemSet3.trie.TTrie;

import java.util.List;

public class MedicionPredecirTrie extends Medible<List<String>> {
    private final TTrie<String> trie;
    private final String prefijo;

    public MedicionPredecirTrie(TTrie<String> trie, String prefijo) {
        this.trie = trie; this.prefijo = prefijo;
    }

    @Override
    public void ejecutar(int repeticiones, List<String> ignorado) {
        for (int i = 0; i < repeticiones; i++) trie.predecir(prefijo);
    }

    @Override
    public Object getObjetoAMedirMemoria() { return trie; }
}
