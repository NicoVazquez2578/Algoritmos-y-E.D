package ucu.edu.aed.tda.Arboles;

import java.util.function.Consumer;

import ucu.edu.aed.tda.TDALista;

/**
 * Modela un nodo del árbol genérico (n-ario).
 * La implementación de esta estructura debe ser recursiva.
 */
public interface TDAElementoGenerico<T> {

    /**
     * Agrega un nuevo hijo al final de la lista de hijos del nodo actual.
     */
    void agregarHijo(TDAElementoGenerico<T> hijo);

    /**
     * Devuelve la lista de hijos del nodo actual. La lista está vacía si
     * el nodo es una hoja.
     */
    TDALista<TDAElementoGenerico<T>> getHijos();

    /**
     * Actualiza el dato del nodo actual.
     */
    void setDato(T dato);

    /**
     * devuelve el dato del nodo actual.
     */
    T getDato();

    /**
     * Busca un nodo por un criterio de búsqueda, recorriendo este nodo y
     * sus descendientes. Si no se encuentra, retorna nulo.
     */
    TDAElementoGenerico<T> buscar(Comparable<T> criterioBusqueda);

    /**
     * Inserta dato como hijo del nodo que coincide con criterioPadre,
     * buscando en este nodo y sus descendientes. Retorna true si se
     * encontró el padre y se insertó el hijo.
     */
    boolean insertarHijo(T dato, Comparable<T> criterioPadre);

    /**
     * Elimina, del subárbol de este nodo, al nodo que coincide con el
     * criterio de búsqueda (junto con todo su propio subárbol). Retorna
     * el nodo que debe quedar en el lugar de este nodo (a sí mismo, si el
     * eliminado fue un descendiente; o {@code null} si el propio nodo es
     * el que había que eliminar).
     */
    TDAElementoGenerico<T> eliminar(Comparable<T> criterioBusqueda);

    /**
     * {@snippet :
     * elemento.preOrder(dato ->{
     *     // procesar dato
     * });
     *}
     */
    void preOrder(Consumer<TDAElementoGenerico<T>> consumidor);

    /**
     * {@snippet :
     * elemento.postOrder(dato ->{
     *     // procesar dato
     * });
     *}
     */
    void postOrder(Consumer<TDAElementoGenerico<T>> consumidor);

    /**
     * retorna true si el nodo es hoja (no tiene hijos)
     */
    boolean esHoja();

    /**
     * retorna la cantidad de nodos que son hojas, contando este nodo y
     * sus descendientes
     */
    int cantidadHojas();

    /**
     * retorna la cantidad de nodos que no son hojas, contando este nodo y
     * sus descendientes
     */
    int cantidadNodosInternos();

    /**
     * retorna la cantidad de nodos que componen este subárbol (este nodo
     * y todos sus descendientes)
     */
    int cantidadNodos();

    /**
     * retorna la altura de este nodo
     */
    int altura();

    /**
     * retorna el nivel relativo del nodo que coincide con el criterio de
     * búsqueda, buscando en este nodo y sus descendientes. Si no se
     * encuentra, retorna -1
     */
    int obtenerNivel(Comparable<T> criterioBusqueda);
}
