package ucu.edu.aed.ProblemSets.ProblemSet2.Ej13;

import java.util.ArrayList;
import java.util.List;

// Ejercicio 13 - AVL de naves indexado por código
public class RegistroNaves {

    private static class Nodo {
        Nave nave; Nodo izq, der; int altura;
        Nodo(Nave n) { nave = n; altura = 1; }
    }

    private Nodo raiz;

    private int altura(Nodo n) { return n == null ? 0 : n.altura; }
    private int balance(Nodo n) { return n == null ? 0 : altura(n.izq) - altura(n.der); }
    private void actualizarAltura(Nodo n) { n.altura = 1 + Math.max(altura(n.izq), altura(n.der)); }

    private Nodo rotarDerecha(Nodo y) {
        Nodo x = y.izq, T2 = x.der;
        x.der = y; y.izq = T2;
        actualizarAltura(y); actualizarAltura(x);
        return x;
    }

    private Nodo rotarIzquierda(Nodo x) {
        Nodo y = x.der, T2 = y.izq;
        y.izq = x; x.der = T2;
        actualizarAltura(x); actualizarAltura(y);
        return y;
    }

    public void insertar(Nave nave) { raiz = ins(raiz, nave); }

    private Nodo ins(Nodo n, Nave nave) {
        if (n == null) return new Nodo(nave);
        if (nave.getCodigo() < n.nave.getCodigo()) n.izq = ins(n.izq, nave);
        else if (nave.getCodigo() > n.nave.getCodigo()) n.der = ins(n.der, nave);
        else return n;

        actualizarAltura(n);
        int b = balance(n);

        if (b > 1  && nave.getCodigo() < n.izq.nave.getCodigo()) return rotarDerecha(n);
        if (b < -1 && nave.getCodigo() > n.der.nave.getCodigo()) return rotarIzquierda(n);
        if (b > 1  && nave.getCodigo() > n.izq.nave.getCodigo()) { n.izq = rotarIzquierda(n.izq); return rotarDerecha(n); }
        if (b < -1 && nave.getCodigo() < n.der.nave.getCodigo()) { n.der = rotarDerecha(n.der);   return rotarIzquierda(n); }
        return n;
    }

    // Inorden: lista de códigos de naves con clase "Explorador"
    public List<Integer> identificarNavesExploradoras() {
        List<Integer> lista = new ArrayList<>();
        exploradoresRec(raiz, lista);
        return lista;
    }

    private void exploradoresRec(Nodo n, List<Integer> lista) {
        if (n == null) return;
        exploradoresRec(n.izq, lista);
        if (n.nave.esExploradora()) lista.add(n.nave.getCodigo());
        exploradoresRec(n.der, lista);
    }

    // Promedio de combustible de naves exploradoras
    public double calcularCombustiblePromedio() {
        int[] acc = {0, 0}; // [suma, cantidad]
        promedioRec(raiz, acc);
        return acc[1] == 0 ? 0.0 : (double) acc[0] / acc[1];
    }

    private void promedioRec(Nodo n, int[] acc) {
        if (n == null) return;
        promedioRec(n.izq, acc);
        if (n.nave.esExploradora()) { acc[0] += n.nave.getCombustible(); acc[1]++; }
        promedioRec(n.der, acc);
    }

    public List<Nave> inorden() {
        List<Nave> l = new ArrayList<>();
        inRec(raiz, l); return l;
    }
    private void inRec(Nodo n, List<Nave> l) {
        if (n == null) return; inRec(n.izq, l); l.add(n.nave); inRec(n.der, l);
    }

    public int alturaArbol() { return altura(raiz); }
}
