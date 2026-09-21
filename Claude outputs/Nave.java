package ucu.edu.aed.flota;

/**
 * Nave de la flota de la Federación Intergaláctica. Tiene un código único,
 * una clase (por ejemplo "Explorador", "Destructor", "Médica") y la cantidad
 * de combustible disponible. Se compara por código, que es la clave del AVL.
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
