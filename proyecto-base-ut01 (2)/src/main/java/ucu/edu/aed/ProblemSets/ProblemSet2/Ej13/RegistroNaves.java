package ucu.edu.aed.ProblemSets.ProblemSet2.Ej13;

import java.util.ArrayList;
import java.util.List;

/**
 * EJERCICIO 13 — Federación Intergaláctica
 *
 * Árbol AVL indexado por código de nave. Mantiene balance en todo momento
 * mediante rotaciones simples y dobles.
 *
 * Operaciones marcadas con * (incluyen pseudocódigo):
 *   - identificarNavesExploradoras(): lista de códigos de naves "Explorador"
 *   - calcularCombustiblePromedio():  promedio de combustible de las exploradoras
 */
public class RegistroNaves {

    // =========================================================================
    //  Nodo AVL
    // =========================================================================
    private static class Nodo {
        Nave nave;
        Nodo izq, der;
        int altura;

        Nodo(Nave n) {
            nave = n;
            altura = 1;
        }
    }

    private Nodo raiz;

    // =========================================================================
    //  Utilidades AVL
    // =========================================================================
    private int altura(Nodo n) { return (n == null) ? 0 : n.altura; }

    private void actualizarAltura(Nodo n) {
        n.altura = 1 + Math.max(altura(n.izq), altura(n.der));
    }

    private int balance(Nodo n) {
        return (n == null) ? 0 : altura(n.izq) - altura(n.der);
    }

    // Rotación simple a la derecha
    private Nodo rotarDerecha(Nodo y) {
        Nodo x  = y.izq;
        Nodo T2 = x.der;
        x.der = y;
        y.izq = T2;
        actualizarAltura(y);
        actualizarAltura(x);
        return x;
    }

    // Rotación simple a la izquierda
    private Nodo rotarIzquierda(Nodo x) {
        Nodo y  = x.der;
        Nodo T2 = y.izq;
        y.izq = x;
        x.der = T2;
        actualizarAltura(x);
        actualizarAltura(y);
        return y;
    }

    // =========================================================================
    //  Insertar (con rebalanceo AVL)
    // =========================================================================
    public void insertar(Nave nave) {
        raiz = insertarRec(raiz, nave);
    }

    private Nodo insertarRec(Nodo nodo, Nave nave) {
        if (nodo == null) return new Nodo(nave);

        if (nave.getCodigo() < nodo.nave.getCodigo())
            nodo.izq = insertarRec(nodo.izq, nave);
        else if (nave.getCodigo() > nodo.nave.getCodigo())
            nodo.der = insertarRec(nodo.der, nave);
        else
            return nodo; // duplicado

        actualizarAltura(nodo);
        int b = balance(nodo);

        // Caso LL
        if (b > 1 && nave.getCodigo() < nodo.izq.nave.getCodigo())
            return rotarDerecha(nodo);
        // Caso RR
        if (b < -1 && nave.getCodigo() > nodo.der.nave.getCodigo())
            return rotarIzquierda(nodo);
        // Caso LR
        if (b > 1 && nave.getCodigo() > nodo.izq.nave.getCodigo()) {
            nodo.izq = rotarIzquierda(nodo.izq);
            return rotarDerecha(nodo);
        }
        // Caso RL
        if (b < -1 && nave.getCodigo() < nodo.der.nave.getCodigo()) {
            nodo.der = rotarDerecha(nodo.der);
            return rotarIzquierda(nodo);
        }
        return nodo;
    }

    // =========================================================================
    //  * OP 1 — identificarNavesExploradoras
    //
    //  Descripción: Recorre el árbol AVL en inorden (lo que garantiza el orden
    //    ascendente de códigos) y recolecta el código de cada nave cuya clase
    //    sea "Explorador".
    //
    //  Pre:  el árbol está construido (puede estar vacío)
    //  Post: devuelve una lista (posiblemente vacía) con los códigos de las
    //        naves exploradoras, en orden creciente de código
    //
    //  Pseudocódigo:
    //    function identificarNavesExploradoras() : Lista<entero>
    //      lista <- nueva Lista vacía
    //      exploradoresRec(raiz, lista)
    //      devolver lista
    //
    //    procedure exploradoresRec(nodo : Nodo, lista : Lista<entero>)
    //      si nodo == null entonces devolver
    //      exploradoresRec(nodo.izq, lista)
    //      si nodo.nave.clase == "Explorador" entonces lista.agregar(nodo.nave.codigo)
    //      exploradoresRec(nodo.der, lista)
    //
    //  Complejidad: O(n) — se visita cada nodo exactamente una vez.
    // =========================================================================
    public List<Integer> identificarNavesExploradoras() {
        List<Integer> lista = new ArrayList<>();
        exploradoresRec(raiz, lista);
        return lista;
    }

    private void exploradoresRec(Nodo nodo, List<Integer> lista) {
        if (nodo == null) return;
        exploradoresRec(nodo.izq, lista);
        if (nodo.nave.esExploradora()) lista.add(nodo.nave.getCodigo());
        exploradoresRec(nodo.der, lista);
    }

    // =========================================================================
    //  * OP 2 — calcularCombustiblePromedio
    //
    //  Descripción: Recorre el árbol AVL para sumar el combustible de todas
    //    las naves exploradoras y dividir por la cantidad encontrada.
    //
    //  Pre:  el árbol está construido (puede estar vacío)
    //  Post: devuelve el promedio de combustible de las naves exploradoras;
    //        devuelve 0.0 si no hay naves exploradoras
    //
    //  Pseudocódigo:
    //    function calcularCombustiblePromedio() : real
    //      acumulador <- [suma: 0, cantidad: 0]
    //      promedioRec(raiz, acumulador)
    //      si acumulador.cantidad == 0 entonces devolver 0.0
    //      devolver acumulador.suma / acumulador.cantidad
    //
    //    procedure promedioRec(nodo : Nodo, acumulador)
    //      si nodo == null entonces devolver
    //      promedioRec(nodo.izq, acumulador)
    //      si nodo.nave.clase == "Explorador" entonces
    //        acumulador.suma <- acumulador.suma + nodo.nave.combustible
    //        acumulador.cantidad <- acumulador.cantidad + 1
    //      promedioRec(nodo.der, acumulador)
    //
    //  Complejidad: O(n) — se visita cada nodo exactamente una vez.
    // =========================================================================
    public double calcularCombustiblePromedio() {
        int[] acc = {0, 0}; // acc[0]=suma, acc[1]=cantidad
        promedioRec(raiz, acc);
        return (acc[1] == 0) ? 0.0 : (double) acc[0] / acc[1];
    }

    private void promedioRec(Nodo nodo, int[] acc) {
        if (nodo == null) return;
        promedioRec(nodo.izq, acc);
        if (nodo.nave.esExploradora()) {
            acc[0] += nodo.nave.getCombustible();
            acc[1]++;
        }
        promedioRec(nodo.der, acc);
    }

    // Inorden completo para verificación
    public List<Nave> inorden() {
        List<Nave> lista = new ArrayList<>();
        inordenRec(raiz, lista);
        return lista;
    }
    private void inordenRec(Nodo n, List<Nave> lista) {
        if (n == null) return;
        inordenRec(n.izq, lista);
        lista.add(n.nave);
        inordenRec(n.der, lista);
    }

    public int alturaArbol() { return altura(raiz); }
}
