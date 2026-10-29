package ucu.edu.aed.molecular.sintesis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
  Datos científicos asociados a un compuesto.
 
  Toxicidad e inflamabilidad usan la escala NFPA 704 (0 = sin peligro, 4 = peligro extremo),
  así son comparables y se pueden usar como filtro en las consultas.
  El punto de ebullición se guarda en °C; Double.NaN significa "dato desconocido".
 */
public class DatosCientificos {

    private final double puntoEbullicion;
    private final double puntoFusion;
    private final int toxicidad;
    private final int inflamabilidad;
    private final List<String> usos;

    public DatosCientificos(double puntoEbullicion, double puntoFusion,
                            int toxicidad, int inflamabilidad, List<String> usos) {
        validarEscala(toxicidad, "toxicidad");
        validarEscala(inflamabilidad, "inflamabilidad");
        this.puntoEbullicion = puntoEbullicion;
        this.puntoFusion = puntoFusion;
        this.toxicidad = toxicidad;
        this.inflamabilidad = inflamabilidad;
        this.usos = usos == null ? new ArrayList<>() : new ArrayList<>(usos);
    }

    /** Datos vacíos para compuestos de los que no se conoce nada (ej. intermedios). */
    public static DatosCientificos desconocidos() {
        return new DatosCientificos(Double.NaN, Double.NaN, 0, 0, List.of());
    }

    private static void validarEscala(int valor, String campo) {
        if (valor < 0 || valor > 4) throw new IllegalArgumentException(campo + " debe estar entre 0 y 4 (NFPA)");
    }

    public double getPuntoEbullicion() { return puntoEbullicion; }
    public double getPuntoFusion() { return puntoFusion; }
    public int getToxicidad() { return toxicidad; }
    public int getInflamabilidad() { return inflamabilidad; }
    public List<String> getUsos() { return Collections.unmodifiableList(usos); }

    public boolean tieneUso(String uso) {
        return usos.stream().anyMatch(u -> u.equalsIgnoreCase(uso));
    }

    /** Estado a temperatura ambiente (25 °C), si se conocen los puntos de fusión y ebullición. */
    public String estadoA25Grados() {
        if (Double.isNaN(puntoFusion) || Double.isNaN(puntoEbullicion)) return "desconocido";
        if (25 < puntoFusion) return "sólido";
        if (25 < puntoEbullicion) return "líquido";
        return "gas";
    }

    @Override
    public String toString() {
        return "Peb=" + puntoEbullicion + "°C, tox=" + toxicidad + ", infl=" + inflamabilidad + ", usos=" + usos;
    }
}
