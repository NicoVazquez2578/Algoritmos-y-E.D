package ucu.edu.aed.ProblemSets.ProblemSet3.Ej07.medibles;

import ucu.edu.aed.ProblemSets.ProblemSet3.lib.Medible;

import java.util.LinkedList;
import java.util.List;

public class MedicionBuscarLinkedList extends Medible<List<String>> {
    private final LinkedList<String> list;

    public MedicionBuscarLinkedList(LinkedList<String> list) { this.list = list; }

    @Override
    public void ejecutar(int repeticiones, List<String> palabras) {
        for (int i = 0; i < repeticiones; i++)
            for (String p : palabras) list.contains(p);
    }

    @Override
    public Object getObjetoAMedirMemoria() { return list; }
}
