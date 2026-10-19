package ucu.edu.aed.ProblemSets.ProblemSet2.Ej13;

<<<<<<< HEAD
/*
  Nave de la flota de la Federación Intergaláctica. Tiene un código único,
  una clase (por ejemplo "Explorador", "Destructor", "Médica") y la cantidad
  de combustible disponible. Se compara por código, que es la clave del AVL.
 */
public class Nave implements Comparable<Nave> {
 
    public static final String CLASE_EXPLORADOR = "Explorador";
 
    private final int codigo;
    private final String clase;
    private final int combustible;
 
    public Nave(int codigo, String clase, int combustible) {
        this.codigo = codigo;
        this.clase = clase;
        this.combustible = combustible;
    }
 
    public int getCodigo() {
        return codigo;
    }
 
    public String getClase() {
        return clase;
    }
 
    public int getCombustible() {
        return combustible;
    }
 
    public boolean esExploradora() {
        return CLASE_EXPLORADOR.equals(clase);
    }
 
    @Override
    public int compareTo(Nave otra) {
        return Integer.compare(this.codigo, otra.codigo);
    }
 
    @Override
    public String toString() {
        return "(" + codigo + ", " + clase + ", " + combustible + ")";
    }
}
 
=======
/**
 * Nave registrada en la Federación Intergaláctica.
 * El código es la clave del árbol AVL.
 */
public class Nave {
    private final int codigo;
    private final String clase;
    private final int combustible;

    public Nave(int codigo, String clase, int combustible) {
        this.codigo      = codigo;
        this.clase       = clase;
        this.combustible = combustible;
    }

    public int    getCodigo()      { return codigo; }
    public String getClase()       { return clase; }
    public int    getCombustible() { return combustible; }

    public boolean esExploradora() { return "Explorador".equals(clase); }

    @Override
    public String toString() {
        return "(" + codigo + ", \"" + clase + "\", " + combustible + ")";
    }
}
>>>>>>> 0a8e58794c3cd21f6f03ec0fe58df85eef8777bf
