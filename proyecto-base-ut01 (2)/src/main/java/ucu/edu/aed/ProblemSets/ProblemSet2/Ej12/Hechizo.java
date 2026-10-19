package ucu.edu.aed.ProblemSets.ProblemSet2.Ej12;
<<<<<<< HEAD
<<<<<<< HEAD
/*
  Hechizo del grimorio de Aldric. Se identifica por un número de
  identificación único (entero positivo) y tiene un nombre.
  Se compara por ID, que es la clave del árbol binario de búsqueda.
 */
public class Hechizo implements Comparable<Hechizo> {
 
    private final int id;
    private final String nombre;
 
=======
=======
>>>>>>> e375feff9fbf1b983dd66eb60784089a8f5ecb96

/**
 * Elemento del Grimorio del Archimago.
 * El ID es la clave del ABB; el nombre es el valor almacenado.
 */
public class Hechizo {
    private final int id;
    private final String nombre;

<<<<<<< HEAD
>>>>>>> 0a8e58794c3cd21f6f03ec0fe58df85eef8777bf
=======
>>>>>>> e375feff9fbf1b983dd66eb60784089a8f5ecb96
    public Hechizo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
<<<<<<< HEAD
<<<<<<< HEAD
 
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
 
=======
=======
>>>>>>> e375feff9fbf1b983dd66eb60784089a8f5ecb96

    public int getId()      { return id; }
    public String getNombre() { return nombre; }

    /** Un hechizo es "prohibido" si su ID es impar. */
    public boolean esProhibido() { return id % 2 != 0; }

    @Override
    public String toString() { return "(" + id + ", \"" + nombre + "\")"; }
}
<<<<<<< HEAD
>>>>>>> 0a8e58794c3cd21f6f03ec0fe58df85eef8777bf
=======
>>>>>>> e375feff9fbf1b983dd66eb60784089a8f5ecb96
