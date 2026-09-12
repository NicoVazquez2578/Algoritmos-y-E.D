package ucu.edu.aed.ProblemSets.ProblemSet2.Ej12;

import java.util.ArrayList;
import java.util.List;

/**
 * EJERCICIO 12 — El Grimorio del Archimago
 *
 * ABB indexado por ID de hechizo (entero positivo único).
 *
 * Operaciones marcadas con * (incluyen pseudocódigo):
 *   - consultarHechizosProhibidos(): devuelve lista de hechizos con ID impar
 *   - generarCantico():              inorden de hechizos prohibidos, separados por " - "
 */
public class GrimorioBST {

    // =========================================================================
    //  Nodo interno
    // =========================================================================
    private static class Nodo {
        Hechizo hechizo;
        Nodo izq, der;
        Nodo(Hechizo h) { hechizo = h; }
    }

    private Nodo raiz;

    // =========================================================================
    //  Insertar
    // =========================================================================
    public void insertar(Hechizo h) {
        raiz = insertarRec(raiz, h);
    }

    private Nodo insertarRec(Nodo nodo, Hechizo h) {
        if (nodo == null) return new Nodo(h);
        if (h.getId() < nodo.hechizo.getId())
            nodo.izq = insertarRec(nodo.izq, h);
        else if (h.getId() > nodo.hechizo.getId())
            nodo.der = insertarRec(nodo.der, h);
        // duplicado: se ignora
        return nodo;
    }

    // =========================================================================
    //  * OP 1 — consultarHechizosProhibidos
    //
    //  Descripción: Recorre el árbol en cualquier orden y recolecta todos los
    //    hechizos cuyo ID es impar (considerados prohibidos).
    //
    //  Pre:  el grimorio está construido (puede estar vacío)
    //  Post: devuelve una lista (posiblemente vacía) con todos los hechizos
    //        cuyo ID es impar, en el orden en que se visitaron
    //
    //  Pseudocódigo:
    //    function consultarHechizosProhibidos() : Lista<Hechizo>
    //      lista <- nueva Lista vacía
    //      prohibidosRec(raiz, lista)
    //      devolver lista
    //
    //    procedure prohibidosRec(nodo : Nodo, lista : Lista<Hechizo>)
    //      si nodo == null entonces devolver
    //      si nodo.hechizo.id mod 2 != 0 entonces lista.agregar(nodo.hechizo)
    //      prohibidosRec(nodo.izq, lista)
    //      prohibidosRec(nodo.der, lista)
    //
    //  Complejidad: O(n) — se visita cada nodo exactamente una vez.
    // =========================================================================
    public List<Hechizo> consultarHechizosProhibidos() {
        List<Hechizo> lista = new ArrayList<>();
        prohibidosRec(raiz, lista);
        return lista;
    }

    private void prohibidosRec(Nodo nodo, List<Hechizo> lista) {
        if (nodo == null) return;
        if (nodo.hechizo.esProhibido()) lista.add(nodo.hechizo);
        prohibidosRec(nodo.izq, lista);
        prohibidosRec(nodo.der, lista);
    }

    // =========================================================================
    //  * OP 2 — generarCantico
    //
    //  Descripción: Recorre el árbol en INORDEN (lo que garantiza el orden
    //    ascendente por ID) y concatena el nombre de cada hechizo prohibido
    //    separado por " - ", formando el cántico secreto de Aldric.
    //
    //  Pre:  el grimorio está construido (puede estar vacío)
    //  Post: devuelve un String con los nombres de los hechizos prohibidos
    //        en orden creciente de ID, separados por " - ";
    //        devuelve "" si no hay hechizos prohibidos
    //
    //  Pseudocódigo:
    //    function generarCantico() : String
    //      partes <- nueva Lista<String> vacía
    //      canticoRec(raiz, partes)
    //      devolver unir(partes, " - ")
    //
    //    procedure canticoRec(nodo : Nodo, partes : Lista<String>)
    //      si nodo == null entonces devolver
    //      canticoRec(nodo.izq, partes)
    //      si nodo.hechizo.id mod 2 != 0 entonces
    //        partes.agregar(nodo.hechizo.nombre)
    //      canticoRec(nodo.der, partes)
    //
    //  Complejidad: O(n) — se visita cada nodo exactamente una vez.
    // =========================================================================
    public String generarCantico() {
        List<String> partes = new ArrayList<>();
        canticoRec(raiz, partes);
        return String.join(" - ", partes);
    }

    private void canticoRec(Nodo nodo, List<String> partes) {
        if (nodo == null) return;
        canticoRec(nodo.izq, partes);
        if (nodo.hechizo.esProhibido()) partes.add(nodo.hechizo.getNombre());
        canticoRec(nodo.der, partes);
    }

    // Inorden completo para verificación
    public List<Hechizo> inorden() {
        List<Hechizo> lista = new ArrayList<>();
        inordenRec(raiz, lista);
        return lista;
    }
    private void inordenRec(Nodo n, List<Hechizo> lista) {
        if (n == null) return;
        inordenRec(n.izq, lista);
        lista.add(n.hechizo);
        inordenRec(n.der, lista);
    }
}
