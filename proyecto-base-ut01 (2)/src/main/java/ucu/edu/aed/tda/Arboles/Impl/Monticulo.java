package ucu.edu.aed.tda.Arboles.Impl;
import java.util.NoSuchElementException;

import ucu.edu.aed.tda.Arboles.TDAMonticulo;

public class Monticulo<T extends Comparable<T>> implements TDAMonticulo<T> {

    private static final int CAPACIDAD_INICIAL = 10;
    private Object[] datos;
    private int cantidad;

    public Monticulo() {
        this.datos = new Object[CAPACIDAD_INICIAL];
        this.cantidad = 0;
    }

    private void expandir() {
        Object[] nuevo = new Object[datos.length * 2];
        for (int i = 0; i < cantidad; i++) {
            nuevo[i] = datos[i];
        }
        datos = nuevo;
    }

    private T obtener(int indice) {
        return (T) datos[indice];
    }

    private void intercambiar(int i, int j) {
        Object temp = datos[i];
        datos[i] = datos[j];
        datos[j] = temp;
    }

    private int padre(int indice) {
        return (indice - 1) / 2;
    }

    private int hijoIzquierdo(int indice) {
        return 2 * indice + 1;
    }

    private int hijoDerecho(int indice) {
        return 2 * indice + 2;
    }

    // O(log n): en el peor caso sube desde una hoja hasta la raíz
    private void flotar(int indice) {
        while (indice > 0 && obtener(indice).compareTo(obtener(padre(indice))) < 0) {
            intercambiar(indice, padre(indice));
            indice = padre(indice);
        }
    }

    // O(log n): en el peor caso baja desde la raíz hasta una hoja
    private void hundir(int indice) {
        int menor = indice;
        int izq = hijoIzquierdo(indice);
        int der = hijoDerecho(indice);

        if (izq < cantidad && obtener(izq).compareTo(obtener(menor)) < 0) {
            menor = izq;
        }
        if (der < cantidad && obtener(der).compareTo(obtener(menor)) < 0) {
            menor = der;
        }
        if (menor != indice) {
            intercambiar(indice, menor);
            hundir(menor);
        }
    }

    @Override
    public boolean insertar(T dato) {
        if (dato == null) {
            return false;
        }
        if (cantidad == datos.length) {
            expandir();
        }
        datos[cantidad] = dato;
        flotar(cantidad);
        cantidad++;
        return true;
    }

    @Override
    public T extraerMinimo() {
        if (esVacio()) {
            throw new NoSuchElementException("El montículo está vacío");
        }
        T minimo = obtener(0);
        cantidad--;
        datos[0] = datos[cantidad];
        datos[cantidad] = null;
        if (cantidad > 0) {
            hundir(0);
        }
        return minimo;
    }

    @Override
    public T verMinimo() {
        if (esVacio()) {
            throw new NoSuchElementException("El montículo está vacío");
        }
        return obtener(0);
    }

    @Override
    public boolean esVacio() {
        return cantidad == 0;
    }

    @Override
    public int cantidadElementos() {
        return cantidad;
    }
}