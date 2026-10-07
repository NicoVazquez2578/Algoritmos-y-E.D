package ucu.edu.aed.grimorio;

import ucu.edu.aed.impl.Lista;
import ucu.edu.aed.tda.TDALista;
import ucu.edu.aed.tda.Arboles.TDAArbolBinario;
import ucu.edu.aed.tda.Arboles.Impl.ArbolBinarioBusqueda;

/**
 * Grimorio del Archimago Aldric: guarda los hechizos en un árbol binario de
 * búsqueda, usando el ID del hechizo como clave.
 */
public class Grimorio {

    private final TDAArbolBinario<Hechizo> hechizos;

    public Grimorio() {
        this.hechizos = new ArbolBinarioBusqueda<>();
    }

    /**
     * Agrega un hechizo al grimorio.
     *
     * Precondición: id > 0 y nombre no es nulo ni vacío.
     * Postcondición: si el ID no estaba en el árbol, el hechizo queda
     *                insertado y se devuelve true. Si el ID ya existía o los
     *                datos no son válidos, el árbol no cambia y se devuelve false.
     *
     * Orden: O(h), con h la altura del árbol. En promedio O(log n); en el
     * peor caso (árbol degenerado, IDs insertados ya ordenados) O(n).
     * Construir el grimorio con n hechizos cuesta entonces O(n log n) en
     * promedio y O(n^2) en el peor caso.
     */
    public boolean agregarHechizo(int id, String nombre) {
        if (id <= 0 || nombre == null || nombre.isEmpty()) {
            return false;
        }
        return hechizos.insertar(new Hechizo(id, nombre));
    }

    /**
     * (*) Consultar los hechizos prohibidos.
     *
     * Descripción: se recorre todo el árbol en inorden y cada hechizo cuyo ID
     * es impar se agrega al final de una lista. La lista se devuelve.
     *
     * Precondición: ninguna (el grimorio puede estar vacío).
     * Postcondición: devuelve una lista con todos los hechizos de ID impar,
     *                ordenados por ID de menor a mayor (porque el inorden de
     *                un ABB recorre las claves en orden). El grimorio no cambia.
     *                Si no hay hechizos prohibidos, la lista es vacía.
     *
     * Orden: hay que visitar todos los nodos porque los IDs impares están
     * repartidos por todo el árbol y el orden del ABB no sirve para descartar
     * ramas por paridad. Cada nodo se visita una vez y el trabajo por nodo es
     * constante (un módulo y, a lo sumo, un agregar al final), entonces O(n).
     * Se asume que Lista.agregar al final es O(1).
     */
    public TDALista<Hechizo> hechizosProhibidos() {
        TDALista<Hechizo> prohibidos = new Lista<>();
        hechizos.inOrder(hechizo -> {
            if (hechizo.esProhibido()) {
                prohibidos.agregar(hechizo);
            }
        });
        return prohibidos;
    }

    /**
     * (*) Generar el cántico secreto.
     *
     * Descripción: se recorre el árbol en inorden y, por cada hechizo
     * prohibido, se agrega su nombre a un texto. Entre dos nombres se pone
     * el separador " - ".
     *
     * Precondición: ninguna (el grimorio puede estar vacío).
     * Postcondición: devuelve el texto con los nombres de los hechizos
     *                prohibidos, ordenados por ID y separados por " - ".
     *                Si no hay hechizos prohibidos devuelve "".
     *                El grimorio no cambia.
     *
     * Orden: un solo recorrido inorden, O(n), con trabajo constante por nodo
     * (StringBuilder.append es O(1) amortizado por nombre). Sumando el copiado
     * de los caracteres del texto final queda O(n + L), con L la cantidad total
     * de letras de los nombres prohibidos.
     */
    public String generarCantico() {
        StringBuilder cantico = new StringBuilder();
        hechizos.inOrder(hechizo -> {
            if (hechizo.esProhibido()) {
                if (cantico.length() > 0) {
                    cantico.append(" - ");
                }
                cantico.append(hechizo.getNombre());
            }
        });
        return cantico.toString();
    }
}
