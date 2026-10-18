package ucu.edu.aed.expresiones;

/**
 * Par (nombre de variable, valor) que se usa para sustituir las variables
 * de un árbol de expresión aritmética.
 */
public class Asignacion {

    private final String nombre;
    private final double valor;

    public Asignacion(String nombre, double valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public double getValor() {
        return valor;
    }
}
