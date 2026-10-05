package ucu.edu.aed.impl;

import ucu.edu.aed.tda.TDAColaPrioridad;
import ucu.edu.aed.tda.TDALista;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 * Implementación de TDAColaPrioridad con arreglo ordenado por prioridad.
 * Menor número = mayor prioridad (prioridad 1 sale antes que prioridad 5).
 *
 * El arreglo se mantiene siempre ordenado de menor a mayor número de prioridad,
 * por lo que el elemento más prioritario siempre está en el índice 0.
 * Esto hace que desencolar y ver el más prioritario sean O(1), pagando O(n)
 * al encolar. Se eligió así porque en el taller se desencola con más frecuencia
 * de lo que se encola, por lo que conviene tener las lecturas baratas.
 *
 * Alternativa descartada: lista desordenada. Encolar sería O(1) pero desencolar
 * requeriría recorrer todo el arreglo buscando el mínimo: O(n). Peor para el uso real.
 *
 * @param <T> tipo de los elementos
 */
public class ColaPrioridad<T> implements TDAColaPrioridad<T> {

    // Agrupa dato y prioridad en un solo objeto para poder almacenarlos juntos.
    // Es privada porque es un detalle interno que no le corresponde conocer al usuario.
    private static class Elemento<T> {
        T dato;
        int prioridad;
        Elemento(T dato, int prioridad) {
            this.dato = dato;
            this.prioridad = prioridad;
        }
    }

    private final ListaArray<Elemento<T>> lista;

    public ColaPrioridad() {
        lista = new ListaArray<>();
    }

    @Override
    public void encolar(T dato, int prioridad) {
        Elemento<T> nuevo = new Elemento<>(dato, prioridad);
        // Busca la posición correcta para mantener el orden ascendente por prioridad
        int i = 0;
        while (i < lista.tamaño() && lista.obtener(i).prioridad <= prioridad) i++;
        lista.agregar(i, nuevo);
    }

    @Override
    public T desencolarMasPrioritario() {
        if (esVacio()) throw new NoSuchElementException("La cola con prioridad está vacía");
        return lista.remover(0).dato; // índice 0 siempre tiene el más prioritario
    }

    @Override
    public T verMasPrioritario() {
        if (esVacio()) throw new NoSuchElementException("La cola con prioridad está vacía");
        return lista.obtener(0).dato;
    }

    @Override
    public T frente() { return verMasPrioritario(); }

    @Override
    public boolean poneEnCola(T dato) {
        // Sin prioridad explícita se usa Integer.MAX_VALUE para que siempre
        // quede detrás de cualquier elemento encolado con prioridad real.
        encolar(dato, Integer.MAX_VALUE);
        return true;
    }

    @Override
    public T quitaDeCola() { return desencolarMasPrioritario(); }

    @Override public void agregar(T elem) { encolar(elem, Integer.MAX_VALUE); }

    @Override
    public void agregar(int index, T elem) {
        lista.agregar(index, new Elemento<>(elem, Integer.MAX_VALUE));
    }

    @Override public T obtener(int index) { return lista.obtener(index).dato; }
    @Override public T remover(int index) { return lista.remover(index).dato; }

    @Override
    public boolean remover(T elem) {
        for (int i = 0; i < lista.tamaño(); i++) {
            T dato = lista.obtener(i).dato;
            if (elem == null ? dato == null : elem.equals(dato)) {
                lista.remover(i);
                return true;
            }
        }
        return false;
    }

    @Override public boolean contiene(T elem) { return indiceDe(elem) != -1; }

    @Override
    public int indiceDe(T elem) {
        for (int i = 0; i < lista.tamaño(); i++) {
            T dato = lista.obtener(i).dato;
            if (elem == null ? dato == null : elem.equals(dato)) return i;
        }
        return -1;
    }

    @Override
    public T buscar(Predicate<T> criterio) {
        for (int i = 0; i < lista.tamaño(); i++) {
            T dato = lista.obtener(i).dato;
            if (criterio.test(dato)) return dato;
        }
        return null;
    }

    @Override
    public TDALista<T> ordenar(Comparator<T> comparator) {
        // Extrae solo los datos (sin prioridad) y ordena con el criterio recibido
        ListaArray<T> datos = new ListaArray<>();
        for (int i = 0; i < lista.tamaño(); i++) datos.agregar(lista.obtener(i).dato);
        return datos.ordenar(comparator);
    }

    @Override public int tamaño() { return lista.tamaño(); }
    @Override public boolean esVacio() { return lista.esVacio(); }
    @Override public void vaciar() { lista.vaciar(); }
}
