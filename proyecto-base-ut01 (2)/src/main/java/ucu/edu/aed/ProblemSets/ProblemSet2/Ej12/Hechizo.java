package ucu.edu.aed.ProblemSets.ProblemSet2.Ej12;

/**
 * Elemento del Grimorio del Archimago.
 * El ID es la clave del ABB; el nombre es el valor almacenado.
 */
public class Hechizo {
    private final int id;
    private final String nombre;

    public Hechizo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId()      { return id; }
    public String getNombre() { return nombre; }

    /** Un hechizo es "prohibido" si su ID es impar. */
    public boolean esProhibido() { return id % 2 != 0; }

    @Override
    public String toString() { return "(" + id + ", \"" + nombre + "\")"; }
}
