package ucu.edu.aed.ProblemSets.ProblemSet2;

import java.util.ArrayList;
import java.util.List;

// Ejercicio 11 - ABB: menorClave, mayorClave, predecesora, nodosPorNivel, hojas con nivel, esABB
public class Ejercicio11 {

    static class NodoABB {
        int clave;
        NodoABB izq, der;
        NodoABB(int c) { clave = c; }

        void insertar(int c) {
            if (c < clave) { if (izq == null) izq = new NodoABB(c); else izq.insertar(c); }
            else if (c > clave) { if (der == null) der = new NodoABB(c); else der.insertar(c); }
        }

        int menorClave() { return izq == null ? clave : izq.menorClave(); }
        int mayorClave() { return der == null ? clave : der.mayorClave(); }

        // Retorna la clave inmediata anterior a 'objetivo'; candidato es el mejor visto hasta ahora
        int predecesora(int objetivo, int candidato) {
            if (clave == objetivo) return izq != null ? izq.mayorClave() : candidato;
            if (objetivo < clave)  return izq != null ? izq.predecesora(objetivo, candidato) : candidato;
            return der != null ? der.predecesora(objetivo, clave) : clave;
        }

        int cantidadNodosEnNivel(int nivel) {
            if (nivel == 0) return 1;
            int c = 0;
            if (izq != null) c += izq.cantidadNodosEnNivel(nivel - 1);
            if (der != null) c += der.cantidadNodosEnNivel(nivel - 1);
            return c;
        }

        void listarHojasConNivel(List<String> lista, int nivel) {
            if (izq == null && der == null) { lista.add(clave + "(n" + nivel + ")"); return; }
            if (izq != null) izq.listarHojasConNivel(lista, nivel + 1);
            if (der != null) der.listarHojasConNivel(lista, nivel + 1);
        }

        boolean esABB(int min, int max) {
            if (clave <= min || clave >= max) return false;
            if (izq != null && !izq.esABB(min, clave)) return false;
            if (der != null && !der.esABB(clave, max)) return false;
            return true;
        }
    }

    static class ABB {
        NodoABB raiz;

        void insertar(int c) {
            if (raiz == null) raiz = new NodoABB(c); else raiz.insertar(c);
        }

        int menorClave()               { return raiz.menorClave(); }
        int mayorClave()               { return raiz.mayorClave(); }
        int predecesora(int c)         { return raiz.predecesora(c, Integer.MIN_VALUE); }
        int cantidadNodosEnNivel(int n){ return raiz == null ? 0 : raiz.cantidadNodosEnNivel(n); }
        boolean esArbolDeBusqueda()    { return raiz == null || raiz.esABB(Integer.MIN_VALUE, Integer.MAX_VALUE); }

        List<String> listarHojasConNivel() {
            List<String> l = new ArrayList<>();
            if (raiz != null) raiz.listarHojasConNivel(l, 0);
            return l;
        }

        List<Integer> inorden() {
            List<Integer> l = new ArrayList<>();
            inRec(raiz, l); return l;
        }
        private void inRec(NodoABB n, List<Integer> l) {
            if (n == null) return; inRec(n.izq, l); l.add(n.clave); inRec(n.der, l);
        }
    }

    public static void main(String[] args) {
        ABB a = new ABB();
        for (int c : new int[]{50, 30, 70, 20, 40, 60, 80, 10, 35}) a.insertar(c);

        System.out.println("Inorden:           " + a.inorden());
        System.out.println("Menor:             " + a.menorClave());
        System.out.println("Mayor:             " + a.mayorClave());
        System.out.println("Predecesora(50):   " + a.predecesora(50));
        System.out.println("Predecesora(10):   " + a.predecesora(10));
        System.out.println("Nodos nivel 2:     " + a.cantidadNodosEnNivel(2));
        System.out.println("Hojas:             " + a.listarHojasConNivel());
        System.out.println("¿Es ABB?:          " + a.esArbolDeBusqueda());
    }
}
