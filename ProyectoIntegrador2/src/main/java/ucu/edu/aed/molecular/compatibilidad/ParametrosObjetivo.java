package ucu.edu.aed.molecular.compatibilidad;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/*
  Rangos aceptables que el analista pide para las moléculas candidatas.
  Cualquier propiedad puede quedar sin rango (null / ausente): en ese caso no se evalúa.
 */
public class ParametrosObjetivo {

    private Rango cadena;
    private Rango wiener;
    private final Map<TipoCarbono, Rango> carbonos = new EnumMap<>(TipoCarbono.class);

    public ParametrosObjetivo conCadena(Rango r) { this.cadena = r; return this; }
    public ParametrosObjetivo conWiener(Rango r) { this.wiener = r; return this; }
    public ParametrosObjetivo conCarbonos(TipoCarbono tipo, Rango r) { carbonos.put(tipo, r); return this; }

    public Rango getCadena() { return cadena; }
    public Rango getWiener() { return wiener; }
    public Map<TipoCarbono, Rango> getCarbonos() { return Collections.unmodifiableMap(carbonos); }

    /** Cantidad de propiedades con rango definido. Sirve para promediar el puntaje. */
    public int cantidadCriterios() {
        return (cadena != null ? 1 : 0) + (wiener != null ? 1 : 0) + carbonos.size();
    }

    @Override
    public String toString() {
        return "Objetivo{cadena=" + cadena + ", wiener=" + wiener + ", carbonos=" + carbonos + "}";
    }
}
