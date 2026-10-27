package ucu.edu.aed.molecular.molecula;

import java.util.Collections;
import java.util.List;

/*
  Resultado de detectar un grupo funcional: qué grupo se encontró y qué átomos lo forman.
 */
public class Coincidencia {

    private final String grupo;
    private final List<Atomo> atomos;

    public Coincidencia(String grupo, List<Atomo> atomos) {
        this.grupo = grupo;
        this.atomos = List.copyOf(atomos);
    }

    public String getGrupo() { return grupo; }
    public List<Atomo> getAtomos() { return Collections.unmodifiableList(atomos); }

    @Override
    public String toString() { return grupo + " " + atomos; }
}
