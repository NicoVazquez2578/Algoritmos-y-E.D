package ucu.edu.aed.grimorio;

/**
 * Hechizo del grimorio de Aldric. Se identifica por un número de
 * identificación único (entero positivo) y tiene un nombre.
 * Se compara por ID, que es la clave del árbol binario de búsqueda.
 */
public class Hechizo implements Comparable<Hechizo> {

    private final int id;
    private final String nombre;

    public Hechizo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    /** Un hechizo está prohibido si su ID es impar. */
    public boolean esProhibido() {
        return id % 2 != 0;
    }

    @Override
    public int compareTo(Hechizo otro) {
        return Integer.compare(this.id, otro.id);
    }

    @Override
    public String toString() {
        return "(" + id + ", " + nombre + ")";
    }
}
