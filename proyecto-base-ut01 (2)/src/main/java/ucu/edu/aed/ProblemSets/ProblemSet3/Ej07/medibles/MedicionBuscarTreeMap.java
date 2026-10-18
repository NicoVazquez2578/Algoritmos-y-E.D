package ucu.edu.aed.ProblemSets.ProblemSet3.Ej07.medibles;

import ucu.edu.aed.ProblemSets.ProblemSet3.lib.Medible;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MedicionBuscarTreeMap extends Medible<List<String>> {
    private final Map<String, String> map;

    public MedicionBuscarTreeMap(TreeMap<String, String> map) { this.map = map; }

    @Override
    public void ejecutar(int repeticiones, List<String> palabras) {
        for (int i = 0; i < repeticiones; i++)
            for (String p : palabras) map.containsKey(p);
    }

    @Override
    public Object getObjetoAMedirMemoria() { return map; }
}
