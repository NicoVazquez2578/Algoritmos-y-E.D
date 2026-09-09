package ucu.edu.aed.ProyectoPrimerHito;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ucu.edu.aed.impl.Lista;
import ucu.edu.aed.tda.TDALista;

/*
  Version optimizada de IGestorEsperaRepuestos.
 
  Un primer intento "facil" seria dejar la misma lista de V1 y agregarle
  arriba un Map(patente -> Vehiculo) para no tener que recorrerla al
  buscar. Lo probamos (ver ExperimentoRendimiento e informe) y no
  alcanza: encontrar el vehiculo pasa a O(1), pero sacarlo de la lista
  enlazada sigue siendo O(n), porque para desenlazar un nodo hace falta
  conocer sus vecinos, y TDALista no expone nodos (a proposito, es parte
  de ocultar la representacion interna). Esa version es mas rapida por
  una constante nada mas, no cambia el orden de crecimiento.
 
  Por eso esta clase NO envuelve una TDALista: maneja su propia lista
  doblemente enlazada (con nodos privados) mas un indice patente -> nodo.
  Al tener el nodo a mano, tanto encontrarlo como desenlazarlo son O(1),
  sin recorrer nada:
 
   - agregar(): crea el nodo y lo engancha al final (se guarda una
     referencia al ultimo), y lo registra en el indice.
   - quitarPorPatente(): busca el nodo en el indice (O(1)) y ajusta los
     punteros de sus vecinos para sacarlo (O(1)).
 
  Se asume que las patentes son unicas dentro de la espera (mismo
  supuesto que ya usa Vehiculo.equals()/hashCode() en todo el proyecto).
 */
public class GestorEsperaRepuestosV2 implements IGestorEsperaRepuestos {

    private static final class Nodo {
        final Vehiculo dato;
        Nodo anterior;
        Nodo siguiente;
        Nodo(Vehiculo dato) {
            this.dato = dato;
        }
    }

    private Nodo primero;
    private Nodo ultimo;
    private final Map<String, Nodo> indicePorPatente = new HashMap<>();

    /** Complejidad: O(1). */
    @Override
    public void agregar(Vehiculo vehiculo) {
        Objects.requireNonNull(vehiculo, "El vehiculo no puede ser nulo.");
        Nodo nuevo = new Nodo(vehiculo);
        if (primero == null) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            nuevo.anterior = ultimo;
            ultimo.siguiente = nuevo;
            ultimo = nuevo;
        }
        indicePorPatente.put(vehiculo.getPatente(), nuevo);
    }

    /**
     * Complejidad: O(1). Consulta directamente la tabla de hash por la clave patente.
     */
    @Override
    public Vehiculo buscarPorPatente(String patente) {
        Objects.requireNonNull(patente, "La patente no puede ser nula.");
        Nodo nodo = indicePorPatente.get(patente);
        return (nodo != null) ? nodo.dato : null;
    }

    /*
      Complejidad: O(1). Ubica el nodo por el indice (sin recorrer
      nada) y lo desenlaza ajustando directamente los punteros vecinos.
     */
    @Override
    public Vehiculo quitarPorPatente(String patente) {
        Objects.requireNonNull(patente, "La patente no puede ser nula.");
        Nodo nodo = indicePorPatente.remove(patente);
        if (nodo == null) {
            return null;
        }

        if (nodo.anterior != null) {
            nodo.anterior.siguiente = nodo.siguiente;
        } else {
            primero = nodo.siguiente;
        }
        if (nodo.siguiente != null) {
            nodo.siguiente.anterior = nodo.anterior;
        } else {
            ultimo = nodo.anterior;
        }
        return nodo.dato;
    }

    /*
      Complejidad: O(n). Arma una Lista nueva recorriendo la estructura
      una sola vez. No es la operacion que se optimiza en este desafio,
      y nadie en el proyecto la llama repetidamente en un camino
      caliente, asi que no hace falta que sea O(1).
     
      Diferencia con V1: V1 devuelve la referencia viva a su lista
      interna; esta version devuelve una copia. Ninguna parte de Taller
      modifica lo que devuelve listar(), asi que no cambia el
      comportamiento observable — pero es una diferencia real a tener
      en cuenta si se agrega codigo nuevo que si lo haga.
     */
    @Override
    public TDALista<Vehiculo> listar() {
        TDALista<Vehiculo> copia = new Lista<>();
        for (Nodo actual = primero; actual != null; actual = actual.siguiente) {
            copia.agregar(actual.dato);
        }
        return copia;
    }
}
