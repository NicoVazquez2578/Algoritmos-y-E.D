package ucu.edu.aed.impl;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * Montículo binario (min-heap) implementado con arreglo dinámico.
 *
 * El elemento en la posición 0 siempre es el mínimo según el Comparator.
 * Para un nodo en posición i, sus hijos están en 2i+1 y 2i+2, y su
 * padre en (i-1)/2.
 *
 * Usamos Comparator en lugar de Comparable para que la misma clase
 * sirva con diferentes criterios de orden sin tocar el código:
 *   - Vehículos por fecha comprometida: Comparator.comparing(v::getFechaEntregaComprometida)
 *   - Enteros en orden natural: Integer::compareTo
 *
 * Complejidades:
 *   insertar:      O(log n)  — inserta al final y sube (sift-up)
 *   extraerMinimo: O(log n)  — saca la raíz, pone el último y baja (sift-down)
 *   verMinimo:     O(1)
 *   eliminar(T):   O(n) para encontrar + O(log n) para restaurar → O(n)
 */
public class Monticulo<T> {

    private Object[] datos;
    private int tamaño;
    private final Comparator<T> comparador;

    private static final int CAPACIDAD_INICIAL = 16;

    /**
     * Crea un montículo vacío con el comparador dado.
     * El elemento que el comparador considera "menor" siempre estará en la cima.
     */
    public Monticulo(Comparator<T> comparador) {
        this.comparador = comparador;
        this.datos = new Object[CAPACIDAD_INICIAL];
        this.tamaño = 0;
    }

    // ─── insertar ─────────────────────────────────────────────────────

    /**
     * Agrega el elemento al final del arreglo y lo sube hasta su posición
     * correcta comparando con su padre. Complejidad: O(log n).
     */
    public void insertar(T elemento) {
        if (tamaño == datos.length) expandir();
        datos[tamaño] = elemento;
        subirHastaRaiz(tamaño);
        tamaño++;
    }

    // ─── extraerMinimo ────────────────────────────────────────────────

    /**
     * Extrae y retorna el mínimo (la raíz). Mueve el último elemento
     * a la raíz y lo baja hasta restaurar la propiedad del heap.
     * Complejidad: O(log n).
     */
    public T extraerMinimo() {
        if (tamaño == 0) throw new NoSuchElementException("El montículo está vacío.");
        T minimo = obtener(0);
        tamaño--;
        if (tamaño > 0) {
            datos[0] = datos[tamaño];
            datos[tamaño] = null;
            bajarDesdeRaiz(0);
        } else {
            datos[0] = null;
        }
        return minimo;
    }

    // ─── verMinimo ────────────────────────────────────────────────────

    /**
     * Retorna el mínimo sin eliminarlo. Complejidad: O(1).
     */
    public T verMinimo() {
        if (tamaño == 0) throw new NoSuchElementException("El montículo está vacío.");
        return obtener(0);
    }

    // ─── eliminar ─────────────────────────────────────────────────────

    /**
     * Elimina una ocurrencia del elemento (comparación por equals).
     * Se usa para reprogramar un vehículo: se elimina el viejo y se
     * inserta el nuevo con la fecha actualizada.
     *
     * Complejidad: O(n) para buscar + O(log n) para restaurar → O(n).
     * @return true si se encontró y eliminó; false si no estaba.
     */
    public boolean eliminar(T elemento) {
        int pos = buscarPosicion(elemento);
        if (pos == -1) return false;

        tamaño--;
        datos[pos] = datos[tamaño];
        datos[tamaño] = null;

        // El elemento en 'pos' puede necesitar subir O bajar según corresponda
        if (pos < tamaño) {
            subirHastaRaiz(pos);
            bajarDesdeRaiz(pos);
        }
        return true;
    }

    // ─── consultas ────────────────────────────────────────────────────

    public boolean esVacio() { return tamaño == 0; }
    public int tamaño()      { return tamaño; }

    // ─── helpers privados ─────────────────────────────────────────────

    /**
     * Sube el elemento en 'pos' intercambiándolo con su padre
     * mientras sea menor que él. Así restauramos la propiedad del heap
     * después de insertar al final.
     */
    private void subirHastaRaiz(int pos) {
        while (pos > 0) {
            int padre = (pos - 1) / 2;
            if (comparador.compare(obtener(pos), obtener(padre)) < 0) {
                intercambiar(pos, padre);
                pos = padre;
            } else {
                break; // ya es mayor o igual que su padre: heap correcto
            }
        }
    }

    /**
     * Baja el elemento en 'pos' intercambiándolo con el menor de sus hijos
     * mientras sea mayor que él. Así restauramos la propiedad del heap
     * después de poner el último elemento en la raíz.
     */
    private void bajarDesdeRaiz(int pos) {
        while (true) {
            int menor = pos;
            int izq = 2 * pos + 1;
            int der = 2 * pos + 2;

            if (izq < tamaño && comparador.compare(obtener(izq), obtener(menor)) < 0) {
                menor = izq;
            }
            if (der < tamaño && comparador.compare(obtener(der), obtener(menor)) < 0) {
                menor = der;
            }
            if (menor == pos) break; // ya es el menor: heap correcto
            intercambiar(pos, menor);
            pos = menor;
        }
    }

    private int buscarPosicion(T elemento) {
        for (int i = 0; i < tamaño; i++) {
            if (obtener(i).equals(elemento)) return i;
        }
        return -1;
    }

    private void intercambiar(int a, int b) {
        Object tmp = datos[a];
        datos[a] = datos[b];
        datos[b] = tmp;
    }

    @SuppressWarnings("unchecked")
    private T obtener(int i) {
        return (T) datos[i];
    }

    // Cuando el arreglo está lleno, duplicamos su capacidad (igual que ListaArray).
    private void expandir() {
        Object[] nuevo = new Object[datos.length * 2];
        for (int i = 0; i < tamaño; i++) nuevo[i] = datos[i];
        datos = nuevo;
    }
}
