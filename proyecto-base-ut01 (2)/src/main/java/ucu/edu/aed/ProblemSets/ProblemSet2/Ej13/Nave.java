package ucu.edu.aed.ProblemSets.ProblemSet2.Ej13;

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
