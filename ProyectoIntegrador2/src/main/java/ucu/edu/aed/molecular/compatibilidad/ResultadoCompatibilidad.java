package ucu.edu.aed.molecular.compatibilidad;

/*
  Una molécula candidata con su perfil y su puntaje de adecuación a los parámetros.
 */
public class ResultadoCompatibilidad implements Comparable<ResultadoCompatibilidad> {

    private final PerfilEstructural perfil;
    private final double puntaje;
    private final int criteriosCumplidos;
    private final int criteriosTotales;

    public ResultadoCompatibilidad(PerfilEstructural perfil, double puntaje,
                                   int criteriosCumplidos, int criteriosTotales) {
        if (puntaje < 0 || puntaje > 1) throw new IllegalArgumentException("El puntaje debe estar entre 0 y 1");
        this.perfil = perfil;
        this.puntaje = puntaje;
        this.criteriosCumplidos = criteriosCumplidos;
        this.criteriosTotales = criteriosTotales;
    }

    public PerfilEstructural getPerfil() { return perfil; }
    public String getNombreMolecula() { return perfil.getNombreMolecula(); }
    public double getPuntaje() { return puntaje; }
    public int getCriteriosCumplidos() { return criteriosCumplidos; }
    public int getCriteriosTotales() { return criteriosTotales; }
    public boolean cumpleTodo() { return criteriosCumplidos == criteriosTotales; }

    @Override
    public int compareTo(ResultadoCompatibilidad o) {
        int c = Double.compare(o.puntaje, this.puntaje);                 // mayor puntaje primero
        if (c != 0) return c;
        c = Integer.compare(o.criteriosCumplidos, this.criteriosCumplidos); // más criterios cumplidos
        if (c != 0) return c;
        return getNombreMolecula().compareTo(o.getNombreMolecula());     // orden estable por nombre
    }

    @Override
    public String toString() {
        return String.format("%s → %.3f (%d/%d rangos)", getNombreMolecula(), puntaje, criteriosCumplidos, criteriosTotales);
    }
}
