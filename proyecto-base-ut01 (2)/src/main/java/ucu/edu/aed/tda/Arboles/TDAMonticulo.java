package ucu.edu.aed.tda.Arboles;

/**
 * Define un Tipo de Dato Abstracto (TDA) Montículo (min-heap).
 *
 * <p>Un montículo es una estructura de datos basada en un arreglo que
 * mantiene, en todo momento, la propiedad de heap mínimo: el elemento
 * más chico según su orden natural siempre está disponible en O(1), y
 * tanto insertar como extraer el mínimo son operaciones O(log n).</p>
 *
 * @param <T> el tipo de los elementos almacenados en el montículo
 */
public interface TDAMonticulo<T> {

    /**
     * Inserta un nuevo elemento en el montículo, ubicándolo en la
     * posición que corresponde y reacomodando (bubble-up) para mantener
     * la propiedad de heap mínimo.
     *
     * @param dato el elemento a insertar
     * @return {@code true} si el elemento fue insertado correctamente
     */
    boolean insertar(T dato);

    /**
     * Quita y retorna el elemento mínimo del montículo, reacomodando
     * (bubble-down) para mantener la propiedad de heap mínimo.
     *
     * @return el elemento mínimo
     * @throws java.util.NoSuchElementException si el montículo está vacío
     */
    T extraerMinimo();

    /**
     * Retorna el elemento mínimo sin quitarlo del montículo.
     *
     * @return el elemento mínimo
     * @throws java.util.NoSuchElementException si el montículo está vacío
     */
    T verMinimo();

    /**
     * Devuelve true si el montículo es vacío
     */
    boolean esVacio();

    /**
     * Devuelve la cantidad de elementos que contiene el montículo
     */
    int cantidadElementos();
}