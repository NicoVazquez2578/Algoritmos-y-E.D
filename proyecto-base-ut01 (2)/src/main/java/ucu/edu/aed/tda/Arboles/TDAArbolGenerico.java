package ucu.edu.aed.tda.Arboles;

import java.util.function.Consumer;

/**
 * Define un Tipo de Dato Abstracto (TDA) Árbol Genérico (n-ario).
 *
 * <p>A diferencia de un árbol binario, en un árbol genérico cada nodo puede
 * tener una cantidad arbitraria de hijos (cero, uno o muchos), en vez de
 * como máximo dos.</p>
 *
 * <p>Esta interfaz proporciona operaciones para insertar la raíz, insertar
 * nodos como hijos de otro nodo ya existente, buscar y eliminar elementos,
 * recorrer el árbol (pre-order, post-order y por niveles) y obtener
 * información sobre su estructura.</p>
 *
 * @param <T> el tipo de los elementos almacenados en el árbol
 */
public interface TDAArbolGenerico<T> {

    /**
     * Inserta el dato como raíz del árbol. Solo tiene efecto si el árbol
     * está vacío.
     *
     * @param dato el elemento a insertar como raíz
     * @return {@code true} si el árbol estaba vacío y se insertó como raíz;
     * {@code false} si el árbol ya tenía raíz (usar {@link #insertarHijo}
     * en ese caso)
     */
    boolean insertar(T dato);

    /**
     * Inserta el dato como un nuevo hijo del nodo que coincide con el
     * criterio de búsqueda dado.
     *
     * @param dato          el elemento a insertar
     * @param criterioPadre criterio que identifica al nodo padre bajo el
     *                      cual se agrega el nuevo hijo
     * @return {@code true} si se encontró el nodo padre y se agregó el
     * hijo; {@code false} si no se encontró ningún nodo que cumpla el
     * criterio dado
     */
    boolean insertarHijo(T dato, Comparable<T> criterioPadre);

    /**
     * Busca y retorna el primer elemento que cumple con el criterio dado.
     *
     * @param criterioBusqueda el criterio que define qué elemento se busca
     * @return el primer elemento que cumple el criterio, o {@code null}
     * si no existe ninguno
     */
    T buscar(Comparable<T> criterioBusqueda);

    /**
     * Retorna el elemento raíz del árbol.
     *
     * @return el elemento raíz del árbol, o {@code null} si el árbol está
     * vacío
     */
    TDAElementoGenerico<T> obtenerRaiz();

    /**
     * Elimina el nodo que coincide con el criterio de búsqueda, junto con
     * todo su subárbol de descendientes.
     *
     * @param criterioBusqueda el criterio que identifica el nodo a eliminar
     * @return {@code true} si se encontró y eliminó el nodo;
     * {@code false} en caso contrario
     */
    boolean eliminar(Comparable<T> criterioBusqueda);

    /**
     * Recorre el árbol en pre-order (cada nodo se visita antes que sus
     * hijos, de izquierda a derecha).
     * {@snippet :
     * arbol.preOrder(dato -> {
     *     // procesar dato
     * });
     *}
     */
    void preOrder(Consumer<T> consumidor);

    /**
     * Recorre el árbol en post-order (cada nodo se visita después que
     * todos sus hijos, de izquierda a derecha).
     * {@snippet :
     * arbol.postOrder(dato -> {
     *     // procesar dato
     * });
     *}
     */
    void postOrder(Consumer<T> consumidor);

    /**
     * Recorre el árbol por niveles (BFS), de raíz a hojas y de izquierda
     * a derecha dentro de cada nivel. Debe implementarse con el TDA Cola
     * del Hito 1.
     * {@snippet :
     * arbol.recorridoPorNiveles(dato -> {
     *     // procesar dato
     * });
     *}
     */
    void recorridoPorNiveles(Consumer<T> consumidor);

    /**
     * Devuelve true si el árbol es vacío
     */
    boolean esVacio();

    /**
     * Devuelve la cantidad de nodos del árbol
     */
    int cantidadNodos();

    /**
     * Devuelve la cantidad de nodos que son hojas
     */
    int cantidadHojas();

    /**
     * Devuelve la cantidad de nodos que NO son hojas
     */
    int cantidadNodosInternos();
}