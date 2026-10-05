package ucu.edu.aed.ProblemSets.ProblemSet3.ej16;

import java.util.ArrayList;
import java.util.List;

public class TNodoArbolGenealogico {
    private Persona persona;
    private List<TNodoArbolGenealogico> hijos;

    public TNodoArbolGenealogico(Persona persona) {
        this.persona = persona;
        this.hijos = new ArrayList<>();
    }

    public Persona getPersona() {
        return persona;
    }

    public List<TNodoArbolGenealogico> getHijos() {
        return hijos;
    }

    public void agregarHijo(TNodoArbolGenealogico hijo) {
        this.hijos.add(hijo);
    }
}