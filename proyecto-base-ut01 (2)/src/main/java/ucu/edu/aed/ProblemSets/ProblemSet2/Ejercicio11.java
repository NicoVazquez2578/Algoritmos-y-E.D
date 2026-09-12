package ucu.edu.aed.ProblemSets.ProblemSet2;

import java.util.ArrayList;
import java.util.List;

/**
 * EJERCICIO 11 — Operaciones complementarias del ABB (pseudocódigo + implementación Java)
 *
 * Parte 1 — Pseudocódigo y análisis para:
 *   1. Obtener la menor clave del árbol
 *   2. Obtener la mayor clave del árbol
 *   3. Obtener la clave inmediata anterior (predecesora) a una clave dada
 *   4. Obtener la cantidad de nodos de un nivel dado
 *   5. Listar todas las hojas, cada una con su nivel
 *   6. Verificar si el árbol es de búsqueda
 *
 * Parte 2 — Implementación Java con tests integrados en el main
 */
public class Ejercicio11 {

    // =========================================================================
    //  NodoABB
    // =========================================================================
    static class NodoABB {
        int clave;
        NodoABB izq, der;
        NodoABB(int c) { clave = c; }

        void insertar(int c) {
            if (c < clave) { if (izq == null) izq = new NodoABB(c); else izq.insertar(c); }
            else if (c > clave) { if (der == null) der = new NodoABB(c); else der.insertar(c); }
        }

        // ---------------------------------------------------------------
        // OP 1 — Nodo: menorClave
        //
        // Descripción: El menor elemento de un ABB siempre está en el
        //   extremo izquierdo (no tiene hijo izquierdo).
        //
        // Pre:  (ninguna)
        // Post: devuelve la menor clave del subárbol
        //
        // Pseudocódigo:
        //   function menorClave() : entero
        //     si this.hijoIzq == null entonces devolver this.clave
        //     devolver this.hijoIzq.menorClave()
        //
        // Complejidad: O(h). Caso promedio O(log n); peor caso O(n).
        // ---------------------------------------------------------------
        int menorClave() {
            return (izq == null) ? clave : izq.menorClave();
        }

        // ---------------------------------------------------------------
        // OP 2 — Nodo: mayorClave
        //
        // Descripción: El mayor elemento de un ABB siempre está en el
        //   extremo derecho (no tiene hijo derecho).
        //
        // Pre:  (ninguna)
        // Post: devuelve la mayor clave del subárbol
        //
        // Pseudocódigo:
        //   function mayorClave() : entero
        //     si this.hijoDer == null entonces devolver this.clave
        //     devolver this.hijoDer.mayorClave()
        //
        // Complejidad: O(h). Caso promedio O(log n); peor caso O(n).
        // ---------------------------------------------------------------
        int mayorClave() {
            return (der == null) ? clave : der.mayorClave();
        }

        // ---------------------------------------------------------------
        // OP 3 — Nodo: predecesora (clave inmediata anterior en inorden)
        //
        // Descripción: La predecesora de una clave X en un ABB es el
        //   mayor valor que sea estrictamente menor que X.
        //   Se busca X; si tiene subárbol izquierdo, la predecesora es
        //   el mayor de ese subárbol. Si no, es el último ancestro por
        //   cuyo lado derecho se descendió.
        //
        // Pre:  clave existe en el árbol; el árbol tiene al menos 2 nodos
        // Post: devuelve la predecesora de clave, o Integer.MIN_VALUE si no existe
        //
        // Pseudocódigo:
        //   function predecesora(objetivo : entero, candidato : entero) : entero
        //     si this.clave == objetivo entonces
        //       si this.hijoIzq != null entonces devolver this.hijoIzq.mayorClave()
        //       devolver candidato
        //     si objetivo < this.clave entonces
        //       si this.hijoIzq != null entonces
        //         devolver this.hijoIzq.predecesora(objetivo, candidato)
        //       devolver candidato
        //     sino
        //       si this.hijoDer != null entonces
        //         devolver this.hijoDer.predecesora(objetivo, this.clave)
        //       devolver this.clave
        //
        // Complejidad: O(h). Caso promedio O(log n); peor caso O(n).
        // ---------------------------------------------------------------
        int predecesora(int objetivo, int candidato) {
            if (clave == objetivo) {
                return (izq != null) ? izq.mayorClave() : candidato;
            }
            if (objetivo < clave) {
                return (izq != null) ? izq.predecesora(objetivo, candidato) : candidato;
            } else {
                return (der != null) ? der.predecesora(objetivo, clave) : clave;
            }
        }

        // ---------------------------------------------------------------
        // OP 4 — Nodo: cantidadNodosEnNivel  (igual que Ejercicio 10)
        //
        // Pre:  nivel >= 0
        // Post: cantidad de nodos en ese nivel del subárbol
        // Complejidad: O(n)
        // ---------------------------------------------------------------
        int cantidadNodosEnNivel(int nivel) {
            if (nivel == 0) return 1;
            int c = 0;
            if (izq != null) c += izq.cantidadNodosEnNivel(nivel - 1);
            if (der != null) c += der.cantidadNodosEnNivel(nivel - 1);
            return c;
        }

        // ---------------------------------------------------------------
        // OP 5 — Nodo: listarHojasConNivel
        //
        // Descripción: Recorre el subárbol recolectando todas las hojas
        //   junto con el nivel absoluto en el que se encuentran.
        //
        // Pre:  lista != null; nivelActual >= 0
        // Post: lista contiene strings con formato "clave(nivelActual)" para
        //       cada hoja encontrada
        //
        // Pseudocódigo:
        //   procedure listarHojasConNivel(lista, nivelActual : entero)
        //     si this.hijoIzq == null Y this.hijoDer == null entonces
        //       lista.agregar(this.clave + "(" + nivelActual + ")")
        //       devolver
        //     si this.hijoIzq != null entonces
        //       this.hijoIzq.listarHojasConNivel(lista, nivelActual + 1)
        //     si this.hijoDer != null entonces
        //       this.hijoDer.listarHojasConNivel(lista, nivelActual + 1)
        //
        // Complejidad: O(n) — se visita cada nodo exactamente una vez.
        // ---------------------------------------------------------------
        void listarHojasConNivel(List<String> lista, int nivelActual) {
            if (izq == null && der == null) {
                lista.add(clave + "(nivel " + nivelActual + ")");
                return;
            }
            if (izq != null) izq.listarHojasConNivel(lista, nivelActual + 1);
            if (der != null) der.listarHojasConNivel(lista, nivelActual + 1);
        }

        // ---------------------------------------------------------------
        // OP 6 — Nodo: esABB
        //
        // Descripción: Verifica la propiedad del ABB en el subárbol
        //   comprobando que cada nodo esté dentro del rango (min, max).
        //
        // Pre:  min y max son los límites válidos para this.clave
        // Post: true si el subárbol es un ABB válido; false en caso contrario
        //
        // Pseudocódigo:
        //   function esABB(min : entero, max : entero) : booleano
        //     si this.clave <= min O this.clave >= max entonces devolver false
        //     si this.hijoIzq != null Y NOT this.hijoIzq.esABB(min, this.clave)
        //       entonces devolver false
        //     si this.hijoDer != null Y NOT this.hijoDer.esABB(this.clave, max)
        //       entonces devolver false
        //     devolver true
        //
        // Complejidad: O(n) — se visita exactamente cada nodo una vez.
        // ---------------------------------------------------------------
        boolean esABB(int min, int max) {
            if (clave <= min || clave >= max) return false;
            if (izq != null && !izq.esABB(min, clave)) return false;
            if (der != null && !der.esABB(clave, max)) return false;
            return true;
        }
    }

    // =========================================================================
    //  ABB (métodos de árbol)
    // =========================================================================
    static class ABB {
        NodoABB raiz;

        void insertar(int clave) {
            if (raiz == null) raiz = new NodoABB(clave);
            else raiz.insertar(clave);
        }

        // OP 1 — Árbol: menorClave
        // Pre:  el árbol no está vacío
        // Post: devuelve la menor clave; lanza excepción si el árbol está vacío
        int menorClave() {
            if (raiz == null) throw new IllegalStateException("Árbol vacío");
            return raiz.menorClave();
        }

        // OP 2 — Árbol: mayorClave
        // Pre:  el árbol no está vacío
        // Post: devuelve la mayor clave; lanza excepción si el árbol está vacío
        int mayorClave() {
            if (raiz == null) throw new IllegalStateException("Árbol vacío");
            return raiz.mayorClave();
        }

        // OP 3 — Árbol: predecesora
        // Pre:  clave existe en el árbol; el árbol no está vacío
        // Post: devuelve la predecesora de clave, o Integer.MIN_VALUE si no tiene predecesora
        int predecesora(int clave) {
            if (raiz == null) throw new IllegalStateException("Árbol vacío");
            return raiz.predecesora(clave, Integer.MIN_VALUE);
        }

        // OP 4 — Árbol: cantidadNodosEnNivel
        // Pre:  nivel >= 0
        // Post: cantidad de nodos en ese nivel; 0 si el árbol está vacío o el nivel no existe
        int cantidadNodosEnNivel(int nivel) {
            return (raiz == null) ? 0 : raiz.cantidadNodosEnNivel(nivel);
        }

        // OP 5 — Árbol: listarHojasConNivel
        // Pre:  (ninguna)
        // Post: lista con "clave(nivel)" para cada hoja; vacía si el árbol está vacío
        List<String> listarHojasConNivel() {
            List<String> lista = new ArrayList<>();
            if (raiz != null) raiz.listarHojasConNivel(lista, 0);
            return lista;
        }

        // OP 6 — Árbol: esArbolDeBusqueda
        // Pre:  (ninguna)
        // Post: true si cumple la propiedad del ABB en todos sus nodos; true si está vacío
        boolean esArbolDeBusqueda() {
            if (raiz == null) return true;
            return raiz.esABB(Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        List<Integer> inorden() {
            List<Integer> lista = new ArrayList<>();
            inordenRec(raiz, lista);
            return lista;
        }
        private void inordenRec(NodoABB n, List<Integer> lista) {
            if (n == null) return;
            inordenRec(n.izq, lista);
            lista.add(n.clave);
            inordenRec(n.der, lista);
        }
    }

    // =========================================================================
    //  Main — tests de las 6 operaciones
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== EJERCICIO 11 ===");

        ABB arbol = new ABB();
        for (int c : new int[]{50, 30, 70, 20, 40, 60, 80, 10, 35}) {
            arbol.insertar(c);
        }
        System.out.println("Árbol insertado (inorden): " + arbol.inorden());
        System.out.println();

        // OP 1 — Menor clave
        System.out.println("[OP 1] Menor clave:                    " + arbol.menorClave());
        // esperado: 10

        // OP 2 — Mayor clave
        System.out.println("[OP 2] Mayor clave:                    " + arbol.mayorClave());
        // esperado: 80

        // OP 3 — Predecesora
        System.out.println("[OP 3] Predecesora de 50:              " + arbol.predecesora(50));
        // esperado: 40
        System.out.println("[OP 3] Predecesora de 30:              " + arbol.predecesora(30));
        // esperado: 20
        System.out.println("[OP 3] Predecesora de 10 (no existe):  " + arbol.predecesora(10));
        // esperado: Integer.MIN_VALUE

        // OP 4 — Nodos por nivel
        System.out.println("[OP 4] Nodos en nivel 0: " + arbol.cantidadNodosEnNivel(0)); // 1
        System.out.println("[OP 4] Nodos en nivel 1: " + arbol.cantidadNodosEnNivel(1)); // 2
        System.out.println("[OP 4] Nodos en nivel 2: " + arbol.cantidadNodosEnNivel(2)); // 4
        System.out.println("[OP 4] Nodos en nivel 3: " + arbol.cantidadNodosEnNivel(3)); // 2

        // OP 5 — Hojas con nivel
        System.out.println("[OP 5] Hojas: " + arbol.listarHojasConNivel());
        // esperado: 10, 35, 60, 80 en sus niveles

        // OP 6 — Es ABB
        System.out.println("[OP 6] ¿Es ABB?:                       " + arbol.esArbolDeBusqueda());
        // esperado: true

        // Prueba con árbol inválido (roto manualmente)
        ABB arbolRoto = new ABB();
        arbolRoto.raiz = new NodoABB(10);
        arbolRoto.raiz.izq = new NodoABB(5);
        arbolRoto.raiz.der = new NodoABB(3); // ¡viola la propiedad del ABB!
        System.out.println("[OP 6] ¿Árbol roto es ABB?:            " + arbolRoto.esArbolDeBusqueda());
        // esperado: false
    }
}
