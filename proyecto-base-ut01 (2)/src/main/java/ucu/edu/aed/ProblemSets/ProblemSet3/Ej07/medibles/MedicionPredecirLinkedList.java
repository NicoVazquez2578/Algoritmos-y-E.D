package ucu.edu.aed.ProblemSets.ProblemSet3.Ej07.medibles;

import ucu.edu.aed.ProblemSets.ProblemSet3.lib.Medible;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MedicionPredecirLinkedList extends Medible<List<String>> {
    private final LinkedList<String> list;
    private final String prefijo;

    public MedicionPredecirLinkedList(LinkedList<String> list, String prefijo) {
        this.list = list; this.prefijo = prefijo;
    }

    @Override
    public void ejecutar(int repeticiones, List<String> ignorado) {
        for (int i = 0; i < repeticiones; i++) {
            List<String> resultado = new ArrayList<>();
            for (String p : list) if (p.startsWith(prefijo)) resultado.add(p);
        }
    }

    @Override
    public Object getObjetoAMedirMemoria() { return list; }
}
