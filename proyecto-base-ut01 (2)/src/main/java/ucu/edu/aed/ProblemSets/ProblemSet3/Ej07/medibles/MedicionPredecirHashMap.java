package ucu.edu.aed.ProblemSets.ProblemSet3.Ej07.medibles;

import ucu.edu.aed.ProblemSets.ProblemSet3.lib.Medible;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicionPredecirHashMap extends Medible<List<String>> {
    private final Map<String, String> map;
    private final String prefijo;

    public MedicionPredecirHashMap(HashMap<String, String> map, String prefijo) {
        this.map = map; this.prefijo = prefijo;
    }

    @Override
    public void ejecutar(int repeticiones, List<String> ignorado) {
        for (int i = 0; i < repeticiones; i++) {
            List<String> resultado = new ArrayList<>();
            for (String k : map.keySet()) if (k.startsWith(prefijo)) resultado.add(k);
        }
    }

    @Override
    public Object getObjetoAMedirMemoria() { return map; }
}
