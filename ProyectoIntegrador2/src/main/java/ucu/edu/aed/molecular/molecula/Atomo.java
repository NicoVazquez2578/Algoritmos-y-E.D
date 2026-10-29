package ucu.edu.aed.molecular.molecula;

import java.util.Objects;

/*
  Vértice del grafo de una molécula.
 
  La identidad del átomo es su id (único dentro de la molécula), NO su elemento:
  los dos carbonos del etanol son vértices distintos. Si equals comparara el elemento,
  el Map de adyacencia del grafo los fusionaría en uno solo.
 */
public class Atomo {

    private final int id;
    private final Elemento elemento;

    public Atomo(int id, Elemento elemento) {
        this.id = id;
        this.elemento = Objects.requireNonNull(elemento, "El elemento no puede ser null");
    }

    public int getId() { return id; }
    public Elemento getElemento() { return elemento; }

    public boolean es(Elemento e) { return elemento == e; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Atomo)) return false;
        return id == ((Atomo) o).id;
    }

    @Override
    public int hashCode() { return Integer.hashCode(id); }

    @Override
    public String toString() { return elemento.getSimbolo() + id; }
}
