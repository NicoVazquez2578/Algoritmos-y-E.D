package ucu.edu.aed.ProblemSets.ej17;

import ucu.edu.aed.impl.ListaArray;
import ucu.edu.aed.tda.TDALista;

/**
 * Ejercicio 18 — Discusión: "Quitar" vs "Eliminar" en TDA Lista.
 *
 * --- ¿Cuál es la diferencia? ---
 *
 * QUITAR: se saca el elemento de la estructura, pero el objeto sigue existiendo
 *         en memoria. Quien llamó puede conservar la referencia y seguir usando
 *         el objeto. En TDALista esto se modela con remover(int) que devuelve
 *         el elemento removido.
 *
 * ELIMINAR: se saca el elemento y se da por destruido; el llamador no espera
 *           usarlo más. En TDALista esto se modela con remover(T) que solo
 *           devuelve un boolean y no expone el objeto.
 *
 * --- ¿Qué pasa con el "Siguiente"? ---
 *
 * En una lista simplemente enlazada, el nodo removido todavía tiene su campo
 * "siguiente" apuntando al próximo nodo. Si el llamador conservó la referencia
 * al nodo removido (comportamiento "Quitar"), podría recorrer la lista desde
 * ese punto aunque ya no forme parte de ella — lo que es un acceso indebido.
 * La solución es poner "siguiente = null" al remover el nodo, cortando ese
 * vínculo aunque el objeto siga vivo en memoria.
 * En nuestra implementación con array (ListaArray) este problema no existe
 * porque los elementos son objetos directos, no nodos con punteros.
 *
 * --- Comportamiento acordado ---
 *
 * Se implementan AMBAS variantes en TDALista:
 *   remover(int indice)  → Quitar: devuelve el objeto para que el llamador lo use.
 *   remover(T elem)      → Eliminar: devuelve boolean, el objeto queda sin referencia
 *                          interna y el GC lo recolecta si nadie más lo referencia.
 */
public class RemoverDiscusion {

    public static void main(String[] args) {
        TDALista<Libro> lista = new ListaArray<>();
        Libro libro = new Libro("ISBN001", "Clean Code", 38.0, 2);
        lista.agregar(libro);

        // Comportamiento QUITAR — el objeto sigue siendo usable
        Libro quitado = lista.remover(0);
        System.out.println("Quitado y aún usable: " + quitado.getTitulo());

        lista.agregar(libro);

        // Comportamiento ELIMINAR — solo nos importa si estaba o no
        boolean eliminado = lista.remover(libro);
        System.out.println("Eliminado exitosamente: " + eliminado);
        // 'libro' sigue en memoria porque esta variable aún lo referencia,
        // pero la lista ya no lo conoce.
    }
}
