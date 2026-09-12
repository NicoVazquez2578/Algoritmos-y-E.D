package ucu.edu.aed.ProblemSets.ProblemSet2;

import java.util.ArrayList;
import java.util.List;

/**
 * EJERCICIO 10 — Algoritmos sobre Árbol Binario de Búsqueda (método de árbol y método de nodo)
 *
 * Parte 1: Insertar un nuevo nodo (el nodo se pasa como parámetro al método del árbol)
 * Parte 2: Contar todas las hojas del árbol
 * Parte 3: Calcular la suma de las claves (claves enteras)
 * Parte 4: Devolver la cantidad de nodos que se encuentran en un nivel dado
 */
public class Ejercicio10 {

    // =========================================================================
    //  NodoABB
    // =========================================================================
    static class NodoABB {
        int clave;
        NodoABB hijoIzquierdo;
        NodoABB hijoDerecho;

        NodoABB(int clave) { this.clave = clave; }

        // ---------------------------------------------------------------
        // PARTE 1 — Nodo: insertar
        //
        // Descripción: Inserta recursivamente el nodo nuevo en el subárbol
        //   respetando la propiedad del ABB (izq < raíz < der).
        //   Si la clave ya existe, se ignora (sin duplicados).
        //
        // Pre:  nuevo != null
        // Post: el nodo queda en la posición correcta del subárbol sin romper
        //       la propiedad de búsqueda; el árbol no contiene duplicados.
        //
        // Pseudocódigo:
        //   procedure insertar(nuevo : NodoABB)
        //     si nuevo.clave < this.clave entonces
        //       si this.hijoIzq == null entonces
        //         this.hijoIzq <- nuevo
        //       sino
        //         this.hijoIzq.insertar(nuevo)
        //     sino si nuevo.clave > this.clave entonces
        //       si this.hijoDer == null entonces
        //         this.hijoDer <- nuevo
        //       sino
        //         this.hijoDer.insertar(nuevo)
        //     // clave igual: duplicado — no se hace nada
        //
        // Complejidad: O(h). Caso promedio O(log n); peor caso O(n).
        // ---------------------------------------------------------------
        void insertar(NodoABB nuevo) {
            if (nuevo.clave < this.clave) {
                if (hijoIzquierdo == null) hijoIzquierdo = nuevo;
                else hijoIzquierdo.insertar(nuevo);
            } else if (nuevo.clave > this.clave) {
                if (hijoDerecho == null) hijoDerecho = nuevo;
                else hijoDerecho.insertar(nuevo);
            }
        }

        // ---------------------------------------------------------------
        // PARTE 2 — Nodo: contarHojas
        //
        // Descripción: Cuenta las hojas del subárbol con raíz en este nodo.
        //   Una hoja es un nodo sin hijos.
        //
        // Pre:  (ninguna)
        // Post: devuelve la cantidad de hojas >= 1 (este nodo es al menos una hoja)
        //
        // Pseudocódigo:
        //   function contarHojas() : entero
        //     si this.hijoIzq == null Y this.hijoDer == null entonces
        //       devolver 1
        //     hojas <- 0
        //     si this.hijoIzq != null entonces hojas <- hojas + this.hijoIzq.contarHojas()
        //     si this.hijoDer != null entonces hojas <- hojas + this.hijoDer.contarHojas()
        //     devolver hojas
        //
        // Complejidad: O(n) — se visita exactamente cada nodo una vez.
        // ---------------------------------------------------------------
        int contarHojas() {
            if (hijoIzquierdo == null && hijoDerecho == null) return 1;
            int hojas = 0;
            if (hijoIzquierdo != null) hojas += hijoIzquierdo.contarHojas();
            if (hijoDerecho  != null) hojas += hijoDerecho.contarHojas();
            return hojas;
        }

        // ---------------------------------------------------------------
        // PARTE 3 — Nodo: sumaClaves
        //
        // Descripción: Suma todas las claves enteras del subárbol.
        //
        // Pre:  las claves son de tipo entero
        // Post: devuelve la suma de todas las claves del subárbol
        //
        // Pseudocódigo:
        //   function sumaClaves() : entero
        //     suma <- this.clave
        //     si this.hijoIzq != null entonces suma <- suma + this.hijoIzq.sumaClaves()
        //     si this.hijoDer != null entonces suma <- suma + this.hijoDer.sumaClaves()
        //     devolver suma
        //
        // Complejidad: O(n) — se visita exactamente cada nodo una vez.
        // ---------------------------------------------------------------
        int sumaClaves() {
            int suma = clave;
            if (hijoIzquierdo != null) suma += hijoIzquierdo.sumaClaves();
            if (hijoDerecho  != null) suma += hijoDerecho.sumaClaves();
            return suma;
        }

        // ---------------------------------------------------------------
        // PARTE 4 — Nodo: cantidadNodosEnNivel
        //
        // Descripción: Devuelve la cantidad de nodos en el nivel indicado
        //   del subárbol. El nivel 0 es la raíz de este subárbol.
        //
        // Pre:  nivel >= 0
        // Post: devuelve >= 0; 0 si el nivel excede la altura del subárbol
        //
        // Pseudocódigo:
        //   function cantidadNodosEnNivel(nivel : entero) : entero
        //     si nivel == 0 entonces devolver 1
        //     cantidad <- 0
        //     si this.hijoIzq != null entonces
        //       cantidad <- cantidad + this.hijoIzq.cantidadNodosEnNivel(nivel - 1)
        //     si this.hijoDer != null entonces
        //       cantidad <- cantidad + this.hijoDer.cantidadNodosEnNivel(nivel - 1)
        //     devolver cantidad
        //
        // Complejidad: O(n) en el peor caso (nivel = altura, árbol completo).
        // ---------------------------------------------------------------
        int cantidadNodosEnNivel(int nivel) {
            if (nivel == 0) return 1;
            int cantidad = 0;
            if (hijoIzquierdo != null) cantidad += hijoIzquierdo.cantidadNodosEnNivel(nivel - 1);
            if (hijoDerecho  != null) cantidad += hijoDerecho.cantidadNodosEnNivel(nivel - 1);
            return cantidad;
        }
    }

    // =========================================================================
    //  ABB (método de árbol)
    // =========================================================================
    static class ABB {
        NodoABB raiz;

        // PARTE 1 — Árbol: insertar
        // Pre:  nuevo != null
        // Post: el nodo queda insertado respetando la propiedad del ABB;
        //       si el árbol estaba vacío, el nodo pasa a ser la raíz
        void insertar(NodoABB nuevo) {
            if (nuevo == null) return;
            if (raiz == null) raiz = nuevo;
            else raiz.insertar(nuevo);
        }

        // PARTE 2 — Árbol: contarHojas
        // Pre:  (ninguna)
        // Post: devuelve la cantidad de hojas; 0 si el árbol está vacío
        int contarHojas() {
            return (raiz == null) ? 0 : raiz.contarHojas();
        }

        // PARTE 3 — Árbol: sumaClaves
        // Pre:  (ninguna)
        // Post: devuelve la suma de todas las claves; 0 si el árbol está vacío
        int sumaClaves() {
            return (raiz == null) ? 0 : raiz.sumaClaves();
        }

        // PARTE 4 — Árbol: cantidadNodosEnNivel
        // Pre:  nivel >= 0
        // Post: devuelve los nodos en ese nivel; 0 si el árbol está vacío o el nivel no existe
        int cantidadNodosEnNivel(int nivel) {
            return (raiz == null) ? 0 : raiz.cantidadNodosEnNivel(nivel);
        }

        List<Integer> inorden() {
            List<Integer> lista = new ArrayList<>();
            inordenRec(raiz, lista);
            return lista;
        }
        private void inordenRec(NodoABB n, List<Integer> lista) {
            if (n == null) return;
            inordenRec(n.hijoIzquierdo, lista);
            lista.add(n.clave);
            inordenRec(n.hijoDerecho, lista);
        }
    }

    // =========================================================================
    //  Main de demostración
    // =========================================================================
    public static void main(String[] args) {
        ABB arbol = new ABB();
        int[] claves = {12, 25, 14, 1, 33, 88, 45, 2, 7, 66, 5, 99};

        System.out.println("=== EJERCICIO 10 ===");
        System.out.print("Insertando: ");
        for (int c : claves) {
            arbol.insertar(new NodoABB(c));
            System.out.print(c + " ");
        }
        System.out.println("\nInorden: " + arbol.inorden());
        System.out.println();

        // Parte 2
        System.out.println("[Parte 2] Cantidad de hojas:    " + arbol.contarHojas());

        // Parte 3
        System.out.println("[Parte 3] Suma de claves:       " + arbol.sumaClaves());

        // Parte 4
        System.out.println("[Parte 4] Nodos por nivel:");
        for (int n = 0; n <= 6; n++) {
            int cant = arbol.cantidadNodosEnNivel(n);
            if (cant > 0)
                System.out.println("          Nivel " + n + ": " + cant + " nodo(s)");
        }
    }
}
