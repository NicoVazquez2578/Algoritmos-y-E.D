package ucu.edu.aed.impl;

/**
 * Nodo utilizado por las implementaciones enlazadas que necesitan
 * navegar en ambos sentidos: {@link ListaDoble} y {@link ListaCircularDoble}.
 *
 * <p>A diferencia de {@link TDANodo}, que solo conoce al nodo
 * siguiente, este nodo también mantiene una referencia al nodo
 * anterior. Eso permite recorrer la lista en ambas direcciones y
 * remover un nodo ya ubicado en O(1), sin tener que recorrer desde
 * el principio para encontrar a su predecesor.</p>
 *
 * @param <T> el tipo del dato almacenado en el nodo
 */
public class TDANodoDoble<T> {

    protected T dato;
    protected TDANodoDoble<T> anterior;
    protected TDANodoDoble<T> siguiente;

    public TDANodoDoble(T dato) {
        this.dato = dato;
        this.anterior = null;
        this.siguiente = null;
    }
}
