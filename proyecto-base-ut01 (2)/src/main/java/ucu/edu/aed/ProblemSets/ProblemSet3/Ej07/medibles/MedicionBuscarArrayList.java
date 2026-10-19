package ucu.edu.aed.ProblemSets.ProblemSet3.Ej07.medibles;

import ucu.edu.aed.ProblemSets.ProblemSet3.lib.Medible;

import java.util.ArrayList;
import java.util.List;

public class MedicionBuscarArrayList extends Medible<List<String>> {
    private final ArrayList<String> list;

    public MedicionBuscarArrayList(ArrayList<String> list) { this.list = list; }

    @Override
    public void ejecutar(int repeticiones, List<String> palabras) {
        for (int i = 0; i < repeticiones; i++)
            for (String p : palabras) list.contains(p);
    }

    @Override
    public Object getObjetoAMedirMemoria() { return list; }
}
