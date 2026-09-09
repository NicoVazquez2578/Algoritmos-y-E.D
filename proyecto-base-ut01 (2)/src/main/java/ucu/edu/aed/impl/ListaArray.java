package ucu.edu.aed.impl;

import ucu.edu.aed.tda.TDALista;
import java.util.Comparator;
import java.util.function.Predicate;

/**
 * Implementación de TDALista sobre un arreglo dinámico.
 * Se eligió el arreglo porque permite acceso en O(1) por índice (obtener),
 * lo cual es clave para que Cola y ColaPrioridad sean eficientes.
 *
 * @param <T> tipo de los elementos
 */
public class ListaArray<T> implements TDALista<T> {

    private static final int CAPACIDAD_INICIAL = 10;

    // Object[] en lugar de T[] porque Java borra los genéricos en tiempo de
    // ejecución (type erasure), haciendo imposible escribir "new T[n]".
    private Object[] datos;
    private int tamaño;

    public ListaArray() {
        datos = new Object[CAPACIDAD_INICIAL];
        tamaño = 0;
    }

    // Se duplica la capacidad (no se suma 1) para garantizar O(1) amortizado
    // al agregar. Si se creciera de a 1, insertar N elementos costaría O(N²).
    private void expandir() {
        Object[] nuevo = new Object[datos.length * 2];
        for (int i = 0; i < tamaño; i++) nuevo[i] = datos[i];
        datos = nuevo;
    }

    private void validarIndice(int index) {
        if (index < 0 || index >= tamaño)
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
    }

    @Override
    public void agregar(T elem) {
        if (tamaño == datos.length) expandir();
        datos[tamaño++] = elem;
    }

    // Insertar en el medio requiere desplazar todos los elementos a la derecha: O(n)
    @Override
    public void agregar(int index, T elem) {
        if (index < 0 || index > tamaño) throw new IndexOutOfBoundsException("Índice: " + index);
        if (tamaño == datos.length) expandir();
        for (int i = tamaño; i > index; i--) datos[i] = datos[i - 1];
        datos[index] = elem;
        tamaño++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T obtener(int index) {
        validarIndice(index);
        return (T) datos[index];
    }

    // Remover en el medio desplaza los siguientes a la izquierda: O(n)
    @Override
    @SuppressWarnings("unchecked")
    public T remover(int index) {
        validarIndice(index);
        T elem = (T) datos[index];
        for (int i = index; i < tamaño - 1; i++) datos[i] = datos[i + 1];
        datos[--tamaño] = null; // null evita que el arreglo retenga la referencia (memory leak)
        return elem;
    }

    @Override
    public boolean remover(T elem) {
        int i = indiceDe(elem);
        if (i == -1) return false;
        remover(i);
        return true;
    }

    @Override
    public boolean contiene(T elem) {
        return indiceDe(elem) != -1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int indiceDe(T elem) {
        for (int i = 0; i < tamaño; i++) {
            // Se verifica null explícitamente para no lanzar NullPointerException
            if (elem == null ? datos[i] == null : elem.equals(datos[i])) return i;
        }
        return -1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T buscar(Predicate<T> criterio) {
        for (int i = 0; i < tamaño; i++) {
            T e = (T) datos[i];
            if (criterio.test(e)) return e;
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TDALista<T> ordenar(Comparator<T> comparator) {
        // Se trabaja sobre una copia para no modificar el estado de esta lista
        Object[] copia = new Object[tamaño];
        for (int i = 0; i < tamaño; i++) copia[i] = datos[i];

        // Insertion sort: simple, sin librerías externas, estable (mantiene el
        // orden relativo de elementos iguales) y eficiente para listas pequeñas.
        for (int i = 1; i < tamaño; i++) {
            T clave = (T) copia[i];
            int j = i - 1;
            while (j >= 0 && comparator.compare((T) copia[j], clave) > 0) {
                copia[j + 1] = copia[j];
                j--;
            }
            copia[j + 1] = clave;
        }

        ListaArray<T> resultado = new ListaArray<>();
        for (int i = 0; i < tamaño; i++) resultado.agregar((T) copia[i]);
        return resultado;
    }

    @Override
    public int tamaño() { return tamaño; }

    @Override
    public boolean esVacio() { return tamaño == 0; }

    @Override
    public void vaciar() {
        for (int i = 0; i < tamaño; i++) datos[i] = null;
        tamaño = 0;
    }
}
