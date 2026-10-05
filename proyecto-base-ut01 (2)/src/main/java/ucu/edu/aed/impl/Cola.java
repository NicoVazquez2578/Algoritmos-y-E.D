package ucu.edu.aed.impl;

import ucu.edu.aed.tda.TDACola;
import ucu.edu.aed.tda.TDALista;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 * Implementación de TDACola con política FIFO (primero en entrar, primero en salir).
 * Usa un ListaArray internamente: el frente es el índice 0 y el final es el último.
 *
 * Se eligió composición (Cola contiene un ListaArray) en lugar de herencia para
 * poder cambiar la representación interna en el futuro sin afectar el contrato
 * de TDACola. Los métodos de TDALista simplemente delegan al arreglo interno.
 *
 * Trade-off conocido: quitaDeCola es O(n) porque remover del índice 0 desplaza
 * todos los elementos. Con una lista enlazada sería O(1), pero se sacrifica esa
 * eficiencia a cambio de la simplicidad del arreglo.
 *
 * @param <T> tipo de los elementos
 */
public class Cola<T> implements TDACola<T> {

    private final ListaArray<T> lista;

    public Cola() {
        lista = new ListaArray<>();
    }

    @Override
    public T frente() {
        if (esVacio()) throw new NoSuchElementException("La cola está vacía");
        return lista.obtener(0);
    }

    @Override
    public boolean poneEnCola(T dato) {
        lista.agregar(dato); // agrega al final: O(1) amortizado
        return true;
    }

    @Override
    public T quitaDeCola() {
        if (esVacio()) throw new NoSuchElementException("La cola está vacía");
        return lista.remover(0); // quita el frente y desplaza todo: O(n)
    }

    @Override public void agregar(T elem) { lista.agregar(elem); }
    @Override public void agregar(int index, T elem) { lista.agregar(index, elem); }
    @Override public T obtener(int index) { return lista.obtener(index); }
    @Override public T remover(int index) { return lista.remover(index); }
    @Override public boolean remover(T elem) { return lista.remover(elem); }
    @Override public boolean contiene(T elem) { return lista.contiene(elem); }
    @Override public int indiceDe(T elem) { return lista.indiceDe(elem); }
    @Override public T buscar(Predicate<T> criterio) { return lista.buscar(criterio); }
    @Override public TDALista<T> ordenar(Comparator<T> comparator) { return lista.ordenar(comparator); }
    @Override public int tamaño() { return lista.tamaño(); }
    @Override public boolean esVacio() { return lista.esVacio(); }
    @Override public void vaciar() { lista.vaciar(); }
}
