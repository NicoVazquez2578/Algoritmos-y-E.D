package ucu.edu.aed.ProblemSets.ProblemSet2;

import java.util.ArrayList;
import java.util.List;

// Ejercicio 10 - ABB: insertar nodo, contar hojas, suma claves, nodos por nivel
public class Ejercicio10 {

    static class NodoABB {
        int clave;
        NodoABB izq, der;
        NodoABB(int c) { clave = c; }

        void insertar(NodoABB nuevo) {
            if (nuevo.clave < clave) {
                if (izq == null) izq = nuevo; else izq.insertar(nuevo);
            } else if (nuevo.clave > clave) {
                if (der == null) der = nuevo; else der.insertar(nuevo);
            }
        }

        int contarHojas() {
            if (izq == null && der == null) return 1;
            int h = 0;
            if (izq != null) h += izq.contarHojas();
            if (der != null) h += der.contarHojas();
            return h;
        }

        int sumaClaves() {
            int s = clave;
            if (izq != null) s += izq.sumaClaves();
            if (der != null) s += der.sumaClaves();
            return s;
        }

        int cantidadNodosEnNivel(int nivel) {
            if (nivel == 0) return 1;
            int c = 0;
            if (izq != null) c += izq.cantidadNodosEnNivel(nivel - 1);
            if (der != null) c += der.cantidadNodosEnNivel(nivel - 1);
            return c;
        }
    }

    static class ABB {
        NodoABB raiz;

        void insertar(NodoABB nuevo) {
            if (nuevo == null) return;
            if (raiz == null) raiz = nuevo; else raiz.insertar(nuevo);
        }

        int contarHojas()               { return raiz == null ? 0 : raiz.contarHojas(); }
        int sumaClaves()                { return raiz == null ? 0 : raiz.sumaClaves(); }
        int cantidadNodosEnNivel(int n) { return raiz == null ? 0 : raiz.cantidadNodosEnNivel(n); }

        List<Integer> inorden() {
            List<Integer> l = new ArrayList<>();
            inRec(raiz, l);
            return l;
        }
        private void inRec(NodoABB n, List<Integer> l) {
            if (n == null) return;
            inRec(n.izq, l); l.add(n.clave); inRec(n.der, l);
        }
    }

    public static void main(String[] args) {
        ABB arbol = new ABB();
        for (int c : new int[]{12, 25, 14, 1, 33, 88, 45, 2, 7, 66, 5, 99})
            arbol.insertar(new NodoABB(c));

        System.out.println("Inorden:      " + arbol.inorden());
        System.out.println("Hojas:        " + arbol.contarHojas());
        System.out.println("Suma claves:  " + arbol.sumaClaves());
        for (int n = 0; n <= 6; n++) {
            int c = arbol.cantidadNodosEnNivel(n);
            if (c > 0) System.out.println("Nivel " + n + ":      " + c);
        }
    }
}
