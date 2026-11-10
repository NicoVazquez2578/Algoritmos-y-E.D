package ucu.edu.aed.molecular.compatibilidad;

/*
  Intervalo cerrado [min, max] de valores aceptables para una propiedad.
 */
public class Rango {

    private final double min;
    private final double max;

    public Rango(double min, double max) {
        if (min > max) throw new IllegalArgumentException("min (" + min + ") no puede ser mayor que max (" + max + ")");
        this.min = min;
        this.max = max;
    }

    public static Rango exacto(double valor) { return new Rango(valor, valor); }

    public double getMin() { return min; }
    public double getMax() { return max; }

    public boolean contiene(double x) { return x >= min && x <= max; }

    /** Si el rango es un solo valor, se usa ancho 1 para no dividir entre 0. */
    public double ancho() { return Math.max(max - min, 1.0); }

    public double desviacion(double x) {
        if (contiene(x)) return 0.0;
        double distancia = x < min ? min - x : x - max;
        return distancia / ancho();
    }

    @Override
    public String toString() { return "[" + min + ", " + max + "]"; }
}
