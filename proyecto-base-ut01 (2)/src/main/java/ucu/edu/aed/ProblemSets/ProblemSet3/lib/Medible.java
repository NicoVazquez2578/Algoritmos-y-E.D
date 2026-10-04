package ucu.edu.aed.ProblemSets.ProblemSet3.lib;

import java.io.Serializable;
import java.util.List;

public abstract class Medible<Data> implements Serializable {

    public Medicion medir(int repeticiones, Data data) {
        long init = System.nanoTime();
        ejecutar(repeticiones, data);
        long fin = System.nanoTime();
        return new Medicion(getClass().getSimpleName(),
            ObjectSizeFetcher.getObjectSize(getObjetoAMedirMemoria()), fin - init);
    }

    public abstract void ejecutar(int repeticiones, Data data);
    public abstract Object getObjetoAMedirMemoria();
}
