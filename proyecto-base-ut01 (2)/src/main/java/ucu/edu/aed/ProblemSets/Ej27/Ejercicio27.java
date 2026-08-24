package ucu.edu.aed.ProblemSets.Ej27;

import java.util.NoSuchElementException;

public class Ejercicio27 {

    // COLA ENLAZADA
    public static class ColaEnlazada<T> {

        private static class Nodo<T> {
            T dato;
            Nodo<T> siguiente;

            Nodo(T dato) {
                this.dato = dato;
                this.siguiente = null;
            }
        }

        private Nodo<T> frente = null;
        private Nodo<T> fin = null;

        public void ponerEnCola(T dato) {
            Nodo<T> nuevo = new Nodo<>(dato);
            if (estaVacia()) {
                frente = nuevo;
            } else {
                fin.siguiente = nuevo;
            }
            fin = nuevo;
        }

        public T quitarDeCola() {
            if (estaVacia()) {
                throw new NoSuchElementException("La cola está vacía");
            }
            T dato = frente.dato;
            frente = frente.siguiente;
            if (frente == null) {
                fin = null;
            }
            return dato;
        }

        public T frente() {
            if (estaVacia()) {
                throw new NoSuchElementException("La cola está vacía");
            }
            return frente.dato;
        }

        public boolean estaVacia() {
            return frente == null;
        }
    }


    // COLA CIRCULAR
    public static class ColaCircular<T> {

        private final T[] vector;
        private int frente;
        private int fin;
        private int cantidad;
        private final int capacidad;

        @SuppressWarnings("unchecked")
        public ColaCircular(int capacidad) {
            this.capacidad = capacidad;
            this.vector = (T[]) new Object[capacidad];
            this.frente = 0;
            this.fin = -1;
            this.cantidad = 0;
        }

        public void ponerEnCola(T dato) {
            if (estaLlena()) {
                throw new IllegalStateException("Error: Cola Llena / Overflow");
            }
            fin = (fin + 1) % capacidad;
            vector[fin] = dato;
            cantidad++;
        }

        public T quitarDeCola() {
            if (estaVacia()) {
                throw new NoSuchElementException("Error: Cola Vacía / Underflow");
            }
            T dato = vector[frente];
            vector[frente] = null;
            frente = (frente + 1) % capacidad;
            cantidad--;
            return dato;
        }

        public T frente() {
            if (estaVacia()) {
                throw new NoSuchElementException("La cola está vacía");
            }
            return vector[frente];
        }

        public boolean estaVacia() {
            return cantidad == 0;
        }

        public boolean estaLlena() {
            return cantidad == capacidad;
        }
    }

    // MÉTODO MAIN CON LOS CASOS DE PRUEBA

    public static void main(String[] args) {
        System.out.println("=== PRUEBAS PARTE 1: COLA ENLAZADA ===");
        ColaEnlazada<Integer> colaE = new ColaEnlazada<>();
        colaE.ponerEnCola(10);
        colaE.ponerEnCola(20);
        System.out.println("Frente: " + colaE.frente()); // 10
        System.out.println("Quitar: " + colaE.quitarDeCola()); // 10
        System.out.println("Quitar: " + colaE.quitarDeCola()); // 20
        System.out.println("¿Está vacía?: " + colaE.estaVacia()); // true

        System.out.println("\n=== PRUEBAS PARTE 2: COLA CIRCULAR (WRAPAROUND) ===");
        ColaCircular<String> colaC = new ColaCircular<>(3);
        colaC.ponerEnCola("A");
        colaC.ponerEnCola("B");
        System.out.println("Quitar elemento 'A': " + colaC.quitarDeCola()); // Libera posición 0

        // Inserción que da la vuelta (Wraparound)
        colaC.ponerEnCola("C");
        colaC.ponerEnCola("D"); // Vuelve al índice 0
        System.out.println("¿Está llena?: " + colaC.estaLlena()); // true

        System.out.println("Quitar: " + colaC.quitarDeCola()); // B
        System.out.println("Quitar: " + colaC.quitarDeCola()); // C
        System.out.println("Quitar: " + colaC.quitarDeCola()); // D
        System.out.println("¿Está vacía?: " + colaC.estaVacia()); // true
    }
}