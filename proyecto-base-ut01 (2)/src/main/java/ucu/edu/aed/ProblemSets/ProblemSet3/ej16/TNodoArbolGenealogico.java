package ucu.edu.aed.ProblemSets.ProblemSet3.ej16;

import java.util.ArrayList;
import java.util.List;

public class TNodoArbolGenealogico {
    private Persona persona;
    private TNodoArbolGenealogico primerHijo;
    private TNodoArbolGenealogico hermanoDerecho;

    public TNodoArbolGenealogico(Persona persona) {
        this.persona = persona;
        this.primerHijo = null;
        this.hermanoDerecho = null;
    }

    public Persona getPersona() {
        return persona;
    }

    public TNodoArbolGenealogico getPrimerHijo() {
        return primerHijo;
    }

    public TNodoArbolGenealogico getHermanoDerecho() {
        return hermanoDerecho;
    }

    public void setHermanoDerecho(TNodoArbolGenealogico hermano) {
        this.hermanoDerecho = hermano;
    }

    // Insertar un hijo al final de la lista de hijos
    public void agregarHijo(TNodoArbolGenealogico nuevoHijo) {
        if (this.primerHijo == null) {
            this.primerHijo = nuevoHijo;
        } else {
            TNodoArbolGenealogico aux = this.primerHijo;
            while (aux.getHermanoDerecho() != null) {
                aux = aux.getHermanoDerecho();
            }
            aux.setHermanoDerecho(nuevoHijo);
        }
    }

    // Devuelve todos los hijos directos de este nodo en una lista auxiliar
    public List<TNodoArbolGenealogico> getHijos() {
        List<TNodoArbolGenealogico> lista = new ArrayList<>();
        TNodoArbolGenealogico aux = this.primerHijo;
        while (aux != null) {
            lista.add(aux);
            aux = aux.getHermanoDerecho();
        }
        return lista;
    }
}