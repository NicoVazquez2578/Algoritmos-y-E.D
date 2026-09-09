package ucu.edu.aed.impl;

import ucu.edu.aed.tda.TDAConjunto;

import java.util.Comparator;

/**
 * Implementación del TDA Conjunto, construida "arriba de" ListaArray.
 *
 * <p>Antes de leer el código conviene tener claro qué es un Conjunto acá:
 * una colección de elementos SIN repetidos, donde lo único que importa
 * es si un elemento pertenece o no — a diferencia de una lista común,
 * no importa "en qué posición" está cada uno. Por eso {@code TDAConjunto}
 * extiende {@code TDALista}: por dentro sigue siendo una lista, pero esta
 * clase se encarga de que se comporte como un conjunto de verdad (sin
 * duplicados) y, además, que se mantenga siempre ordenada — eso es lo
 * que hace posible que Unión e Intersección sean eficientes, como se
 * explica más abajo.</p>
 *
 * <p><b>¿Por qué se eligió ListaArray y no una lista enlazada (Lista,
 * ListaDoble, etc.)?</b> Unión e Intersección recorren los dos conjuntos
 * "en paralelo": van comparando el elemento en la posición {@code i} de
 * un conjunto contra el de la posición {@code j} del otro, y avanzan
 * {@code i} o {@code j} según el resultado de esa comparación (el
 * detalle completo, con ejemplo, está en Ejercicio24_Parte1.txt). Para
 * que ese recorrido sea rápido, "ir a buscar el elemento en la posición
 * i" tiene que ser prácticamente instantáneo. En un arreglo (ListaArray)
 * eso es literal: la posición i está a una distancia fija del principio
 * del arreglo, se calcula directo, sin recorrer nada — se dice que es
 * O(1), "tiempo constante". En una lista enlazada, en cambio, para
 * llegar al nodo i hay que ir saltando de nodo en nodo desde el
 * principio (no hay forma de "saltar" directo a la posición i). Si
 * hiciéramos eso en cada paso del recorrido de Unión/Intersección, el
 * algoritmo dejaría de ser eficiente: en vez de recorrer cada conjunto
 * una sola vez, terminaríamos recorriéndolo una y otra vez. Por eso la
 * base elegida es ListaArray.</p>
 *
 * <p>Dos invariantes (cosas que siempre tienen que ser ciertas mientras
 * se usa esta clase) que {@code Conjunto} agrega sobre lo que ya hace
 * {@code ListaArray}:</p>
 * <ol>
 *   <li>Nunca hay elementos repetidos (dos elementos son "el mismo" si
 *       {@code equals()} devuelve {@code true}).</li>
 *   <li>Los elementos siempre están ordenados según {@code criterioOrden}.</li>
 * </ol>
 * <p>Ambas se garantizan en {@link #agregar(Object)}: en vez de simplemente
 * agregar al final (como hace {@code ListaArray}), primero revisa si el
 * elemento ya está, y si no, busca la posición exacta donde debería
 * quedar para que la lista siga ordenada.</p>
 *
 * @param <T> el tipo de los elementos del conjunto
 */
public class Conjunto<T> extends ListaArray<T> implements TDAConjunto<T> {

    // El "criterio de orden" es lo que le dice a este Conjunto cómo
    // comparar dos elementos entre sí (cuál va "antes" y cuál "después").
    // Se recibe por constructor porque Conjunto es genérico: no puede
    // asumir de antemano cómo se ordenan los objetos de tipo T
    // (por ejemplo, para TAlumno se ordena por cédula).
    private final Comparator<T> criterioOrden;

    public Conjunto(Comparator<T> criterioOrden) {
        super();
        this.criterioOrden = criterioOrden;
    }

    /**
     * Agrega un elemento al conjunto, manteniendo las dos invariantes
     * (sin duplicados, siempre ordenado). Si el elemento ya estaba
     * presente, no hace nada — un conjunto matemático no "tiene dos
     * veces" el mismo elemento.
     *
     * <p>Para encontrar dónde insertarlo, se recorre desde el principio
     * comparando con {@code criterioOrden} hasta encontrar el primer
     * elemento que debería quedar "después" del nuevo (o hasta llegar
     * al final de la lista); esa es la posición correcta para mantener
     * todo ordenado.</p>
     */
    @Override
    public void agregar(T elem) {
        if (this.contiene(elem)) {
            return; // ya está en el conjunto, no se agrega de nuevo
        }
        int posicion = 0;
        while (posicion < this.tamaño()
                && criterioOrden.compare(this.obtener(posicion), elem) < 0) {
            posicion++;
        }
        super.agregar(posicion, elem); // ListaArray.agregar(int, T): inserta ahí y corre el resto
    }

    /**
     * Devuelve un conjunto nuevo con todos los elementos que están en
     * este conjunto, en {@code otro}, o en ambos — sin repetir ninguno.
     * Ni {@code this} ni {@code otro} se modifican.
     *
     * <p><b>Idea del algoritmo</b> (a esta técnica se la suele llamar
     * "mezcla" o "merge" — es el mismo truco que usa mergesort para
     * combinar dos mitades ya ordenadas en una sola lista ordenada):
     * como {@code this} y {@code otro} YA están ordenados, se pueden
     * recorrer los dos AL MISMO TIEMPO con dos índices, {@code i} para
     * este conjunto y {@code j} para el otro, en vez de comparar cada
     * elemento de uno contra todos los elementos del otro.</p>
     *
     * <p>En cada paso se comparan {@code this[i]} y {@code otro[j]}:</p>
     * <ul>
     *   <li>si {@code this[i]} es menor: como {@code otro} está ordenado,
     *       todo lo que queda por ver ahí es todavía mayor, así que
     *       {@code this[i]} no puede estar en {@code otro} — se agrega
     *       al resultado y se avanza {@code i};</li>
     *   <li>si {@code otro[j]} es menor: mismo razonamiento al revés,
     *       se agrega {@code otro[j]} y se avanza {@code j};</li>
     *   <li>si son iguales: el elemento está en ambos conjuntos, se
     *       agrega una sola vez al resultado y se avanzan los dos
     *       índices juntos.</li>
     * </ul>
     * <p>Cuando uno de los dos conjuntos se termina antes que el otro,
     * lo que haya quedado sin visitar en el que sigue se agrega
     * directamente: ya sabemos que no tiene "pareja" con quién
     * compararse, así que forma parte del resultado igual.</p>
     *
     * <p><b>Por qué es eficiente:</b> los índices {@code i} y {@code j}
     * solo avanzan hacia adelante, nunca retroceden ni se quedan quietos
     * los dos a la vez. Como mucho, {@code i} avanza tamaño(this) veces
     * y {@code j} avanza tamaño(otro) veces — el trabajo total es
     * proporcional a la suma de ambos tamaños (se escribe O(n + m)).
     * Compará eso con la alternativa "ingenua" de recorrer {@code this}
     * y, para cada elemento, preguntar si está contenido en {@code otro}
     * (una búsqueda que a su vez recorre todo {@code otro}): eso sería
     * O(n · m), muchísimo más lento cuando los conjuntos son grandes.</p>
     */
    @Override
    public TDAConjunto<T> union(TDAConjunto<T> otro) {
        Conjunto<T> resultado = new Conjunto<>(criterioOrden);
        int i = 0;
        int j = 0;
        int tamA = this.tamaño();
        int tamB = otro.tamaño();

        while (i < tamA && j < tamB) {
            T a = this.obtener(i);
            T b = otro.obtener(j);
            int cmp = criterioOrden.compare(a, b);
            if (cmp < 0) {
                resultado.agregarAlFinalSinValidar(a);
                i++;
            } else if (cmp > 0) {
                resultado.agregarAlFinalSinValidar(b);
                j++;
            } else {
                resultado.agregarAlFinalSinValidar(a); // está en ambos: se agrega una sola vez
                i++;
                j++;
            }
        }
        // A esta altura uno de los dos conjuntos ya se recorrió por
        // completo. Lo que haya quedado en el otro se agrega tal cual:
        // como ambos estaban ordenados, ya sabemos que sigue siendo el
        // orden correcto.
        while (i < tamA) {
            resultado.agregarAlFinalSinValidar(this.obtener(i));
            i++;
        }
        while (j < tamB) {
            resultado.agregarAlFinalSinValidar(otro.obtener(j));
            j++;
        }
        return resultado;
    }

    /**
     * Devuelve un conjunto nuevo con únicamente los elementos que están
     * en {@code this} Y en {@code otro} al mismo tiempo. Ni {@code this}
     * ni {@code otro} se modifican.
     *
     * <p>Usa el mismo recorrido de dos índices que {@link #union}: la
     * diferencia es que acá solo se agrega al resultado cuando
     * {@code this[i]} y {@code otro[j]} son iguales (es decir, cuando el
     * elemento aparece en los dos conjuntos); si uno es menor que el
     * otro, ese elemento no puede estar en la intersección, así que
     * simplemente se descarta avanzando el índice correspondiente, sin
     * agregarlo a ningún lado. Por el mismo argumento que en union, esto
     * es O(n + m).</p>
     */
    @Override
    public TDAConjunto<T> interseccion(TDAConjunto<T> otro) {
        Conjunto<T> resultado = new Conjunto<>(criterioOrden);
        int i = 0;
        int j = 0;
        int tamA = this.tamaño();
        int tamB = otro.tamaño();

        while (i < tamA && j < tamB) {
            T a = this.obtener(i);
            T b = otro.obtener(j);
            int cmp = criterioOrden.compare(a, b);
            if (cmp < 0) {
                i++; // "a" no está en "otro": se descarta, no se agrega
            } else if (cmp > 0) {
                j++; // "b" no está en "this": se descarta, no se agrega
            } else {
                resultado.agregarAlFinalSinValidar(a); // está en los dos
                i++;
                j++;
            }
        }
        return resultado;
    }

    /**
     * Devuelve un conjunto nuevo con los elementos que están en
     * {@code this} pero NO en {@code otro}.
     *
     * <p>Mismo recorrido de dos índices otra vez: cuando
     * {@code this[i]} es menor, ese elemento no aparece en {@code otro}
     * (todavía no llegamos a comparar con nada igual o mayor), así que
     * pertenece a la diferencia. Cuando {@code otro[j]} es menor, se
     * descarta avanzando {@code j} sin tocar el resultado. Cuando son
     * iguales, el elemento está en ambos conjuntos, así que NO pertenece
     * a la diferencia — se descartan los dos.</p>
     */
    @Override
    public TDAConjunto<T> diferencia(TDAConjunto<T> otro) {
        Conjunto<T> resultado = new Conjunto<>(criterioOrden);
        int i = 0;
        int j = 0;
        int tamA = this.tamaño();
        int tamB = otro.tamaño();

        while (i < tamA && j < tamB) {
            T a = this.obtener(i);
            T b = otro.obtener(j);
            int cmp = criterioOrden.compare(a, b);
            if (cmp < 0) {
                resultado.agregarAlFinalSinValidar(a); // "a" no está en "otro"
                i++;
            } else if (cmp > 0) {
                j++;
            } else {
                i++; // está en ambos: no va en la diferencia
                j++;
            }
        }
        // Lo que quede sin visitar en "this" tampoco tuvo con qué
        // compararse en "otro", así que forma parte de la diferencia.
        while (i < tamA) {
            resultado.agregarAlFinalSinValidar(this.obtener(i));
            i++;
        }
        return resultado;
    }

    /**
     * Indica si todos los elementos de {@code this} están también en
     * {@code otro} (es decir, si {@code this} es subconjunto de
     * {@code otro}).
     *
     * <p>De nuevo, el mismo recorrido de dos índices: si en algún
     * momento {@code this[i]} es menor que {@code otro[j]}, quiere
     * decir que {@code this[i]} nunca va a aparecer más adelante en
     * {@code otro} (que sigue ordenado y creciendo), así que ya
     * sabemos que {@code this} NO es subconjunto — se puede cortar y
     * devolver {@code false} sin terminar de recorrer nada. Si se
     * llega al final de {@code this} sin que eso pase, es porque cada
     * elemento de {@code this} encontró su pareja en {@code otro}.</p>
     */
    @Override
    public boolean esSubconjuntoDe(TDAConjunto<T> otro) {
        int i = 0;
        int j = 0;
        int tamA = this.tamaño();
        int tamB = otro.tamaño();

        while (i < tamA && j < tamB) {
            T a = this.obtener(i);
            T b = otro.obtener(j);
            int cmp = criterioOrden.compare(a, b);
            if (cmp == 0) {
                i++;
                j++;
            } else if (cmp > 0) {
                j++; // "b" es menor que "a": no es la pareja, seguir buscando en "otro"
            } else {
                return false; // "a" es menor que todo lo que queda en "otro": no está
            }
        }
        return i == tamA; // true solo si se recorrió TODO "this" encontrando pareja
    }

    // Método privado de uso interno para union/interseccion/diferencia.
    // Esos tres métodos ya recorren los conjuntos de izquierda a derecha
    // (de menor a mayor), así que cada elemento que agregan es siempre
    // mayor que el anterior agregado. Eso significa que no hace falta
    // repetir la búsqueda de posición que hace agregar(T) — directamente
    // se puede insertar al final. Por eso este método llama a
    // super.agregar(elem) (el de ListaArray) y no a this.agregar(elem)
    // (el de Conjunto, que buscaría la posición de nuevo sin necesidad).
    private void agregarAlFinalSinValidar(T elem) {
        super.agregar(elem);
    }
}
