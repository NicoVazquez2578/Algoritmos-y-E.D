package ucu.edu.aed.ProblemSets.ProblemSet2.Ej12;

import java.util.ArrayList;
import java.util.List;

// Ejercicio 12 - ABB del Grimorio indexado por ID de hechizo
public class GrimorioBST {

    private static class Nodo {
        Hechizo h; Nodo izq, der;
        Nodo(Hechizo h) { this.h = h; }
    }

    private Nodo raiz;

    public void insertar(Hechizo h) { raiz = ins(raiz, h); }

    private Nodo ins(Nodo n, Hechizo h) {
        if (n == null) return new Nodo(h);
        if (h.getId() < n.h.getId()) n.izq = ins(n.izq, h);
        else if (h.getId() > n.h.getId()) n.der = ins(n.der, h);
        return n;
    }

    // Devuelve todos los hechizos con ID impar
    public List<Hechizo> consultarHechizosProhibidos() {
        List<Hechizo> lista = new ArrayList<>();
        prohibidosRec(raiz, lista);
        return lista;
    }

    private void prohibidosRec(Nodo n, List<Hechizo> lista) {
        if (n == null) return;
        if (n.h.esProhibido()) lista.add(n.h);
        prohibidosRec(n.izq, lista);
        prohibidosRec(n.der, lista);
    }

    // Inorden de hechizos prohibidos, separados por " - "
    public String generarCantico() {
        List<String> partes = new ArrayList<>();
        canticoRec(raiz, partes);
        return String.join(" - ", partes);
    }

    private void canticoRec(Nodo n, List<String> partes) {
        if (n == null) return;
        canticoRec(n.izq, partes);
        if (n.h.esProhibido()) partes.add(n.h.getNombre());
        canticoRec(n.der, partes);
    }

    public List<Hechizo> inorden() {
        List<Hechizo> l = new ArrayList<>();
        inRec(raiz, l); return l;
    }
    private void inRec(Nodo n, List<Hechizo> l) {
        if (n == null) return; inRec(n.izq, l); l.add(n.h); inRec(n.der, l);
    }
}
