package ucu.edu.aed.tda;

/**
 * Define un Tipo de Dato Abstracto (TDA) Cola con Prioridad genérica.
 *
 * <p>Una cola con prioridad es una estructura en la que cada elemento se inserta
 * junto a un valor de prioridad. El elemento que sale primero no es necesariamente
 * el que ingresó primero, sino el de mayor prioridad.</p>
 *
 * <p>Esta interfaz extiende {@link TDACola}, por lo que hereda las operaciones
 * de cola y lista. Las operaciones propias modelan la inserción con prioridad
 * y el acceso al elemento más prioritario.</p>
 *
 * @param <T> el tipo de los elementos almacenados en la cola con prioridad
 */
public interface TDAColaPrioridad<T> extends TDACola<T> {

    /**
     * Inserta un elemento en la cola con la prioridad indicada.
     *
     * <p>A menor valor numérico de prioridad, mayor prioridad del elemento.</p>
     *
     * @param dato      el elemento a insertar
     * @param prioridad el valor de prioridad asociado al elemento
     */
    void encolar(T dato, int prioridad);

    /**
     * Remueve y retorna el elemento de mayor prioridad de la cola.
     *
     * <p>En caso de empate en prioridad, el criterio de desempate queda
     * sujeto a la implementación.</p>
     *
     * @return el elemento de mayor prioridad
     * @throws java.util.NoSuchElementException si la cola está vacía
     */
    T desencolarMasPrioritario();

    /**
     * Retorna el elemento de mayor prioridad sin removerlo de la cola.
     *
     * @return el elemento de mayor prioridad
     * @throws java.util.NoSuchElementException si la cola está vacía
     */
    T verMasPrioritario();
}
