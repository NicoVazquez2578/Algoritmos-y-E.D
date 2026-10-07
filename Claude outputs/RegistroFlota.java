package ucu.edu.aed.flota;

import ucu.edu.aed.impl.Lista;
import ucu.edu.aed.tda.TDALista;
import ucu.edu.aed.tda.Arboles.TDAArbolBinario;
import ucu.edu.aed.tda.Arboles.Impl.AVL;

/**
 * Registro centralizado de la flota. Las naves se guardan en un árbol AVL
 * indexado por código de nave, para que las búsquedas sean O(log n) aunque
 * lleguen incorporaciones masivas e impredecibles.
 */
public class RegistroFlota {

    private final TDAArbolBinario<Nave> naves;

    public RegistroFlota() {
        this.naves = new AVL<>();
    }

    /**
     * Registra una nave. El AVL se encarga de mantener el balance.
     *
     * Precondición: codigo > 0, clase no es nula ni vacía y combustible >= 0.
     * Postcondición: si el código no estaba registrado, la nave queda en el
     *                árbol (balanceado) y se devuelve true. Si el código ya
     *                existía o los datos no son válidos, el árbol no cambia
     *                y se devuelve false.
     *
     * Orden: O(log n), porque la altura de un AVL es O(log n) y el balanceo
     * (a lo sumo una rotación simple o doble por inserción) es O(1).
     * Armar el registro con n naves cuesta O(n log n).
     */
    public boolean registrarNave(int codigo, String clase, int combustible) {
        if (codigo <= 0 || clase == null || clase.isEmpty() || combustible < 0) {
            return false;
        }
        return naves.insertar(new Nave(codigo, clase, combustible));
    }

    /**
     * (*) Identificar las naves exploradoras.
     *
     * Descripción: se recorre todo el AVL en inorden y, por cada nave de
     * clase "Explorador", se agrega su código al final de una lista.
     *
     * Precondición: ninguna (el registro puede estar vacío).
     * Postcondición: devuelve una lista con el código de cada nave
     *                exploradora, ordenada de menor a mayor código.
     *                Si no hay exploradoras, la lista es vacía.
     *                El árbol no cambia.
     *
     * Orden: O(n). El árbol está indexado por código, no por clase, así que
     * hay que visitar todos los nodos. Cada uno se visita una vez y hace
     * trabajo constante (se asume que Lista.agregar al final es O(1)).
     * Espacio extra: O(log n) de recursión (altura del AVL) + O(k) de la lista.
     */
    public TDALista<Integer> codigosExploradoras() {
        TDALista<Integer> codigos = new Lista<>();
        naves.inOrder(nave -> {
            if (nave.esExploradora()) {
                codigos.agregar(nave.getCodigo());
            }
        });
        return codigos;
    }

    /**
     * (*) Calcular el combustible promedio de las naves exploradoras.
     *
     * Descripción: en un solo recorrido inorden se acumula la suma del
     * combustible y la cantidad de naves exploradoras. El promedio es
     * suma / cantidad, con división real.
     *
     * Precondición: ninguna. Si no hay exploradoras se devuelve 0 para no
     *               dividir por cero.
     * Postcondición: devuelve el promedio real del combustible de las naves
     *                de clase "Explorador", o 0 si no hay ninguna.
     *                El árbol no cambia.
     *
     * Orden: O(n) por el recorrido, más O(1) de la división final.
     * Espacio extra: O(log n) de recursión + O(1) del acumulador.
     */
    public double promedioCombustibleExploradoras() {
        Acumulador acum = new Acumulador();
        naves.inOrder(nave -> {
            if (nave.esExploradora()) {
                acum.suma += nave.getCombustible();
                acum.cantidad++;
            }
        });
        if (acum.cantidad == 0) {
            return 0;
        }
        return (double) acum.suma / acum.cantidad; // división real, no entera
    }

    /** Guarda la suma y la cantidad mientras se recorre el árbol. */
    private static class Acumulador {
        long suma = 0;
        int cantidad = 0;
    }
}
