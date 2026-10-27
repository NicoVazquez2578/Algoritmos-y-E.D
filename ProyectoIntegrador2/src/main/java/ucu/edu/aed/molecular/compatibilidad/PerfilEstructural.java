package ucu.edu.aed.molecular.compatibilidad;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/*
  Propiedades estructurales de una molécula, ya calculadas con los algoritmos de grafos.
  Es inmutable: se calcula una vez y se reutiliza al comparar contra varios parámetros.
 
   - longitudCadena: cantidad de carbonos de la cadena carbonada más larga.
   - carbonos: cuántos carbonos hay de cada tipo.
   - indiceWiener: suma de las distancias (en enlaces) entre todos los pares de átomos.
 */
public class PerfilEstructural {

    private final String nombreMolecula;
    private final int longitudCadena;
    private final Map<TipoCarbono, Integer> carbonos;
    private final long indiceWiener;

    public PerfilEstructural(String nombreMolecula, int longitudCadena,
                             Map<TipoCarbono, Integer> carbonos, long indiceWiener) {
        if (longitudCadena < 0 || indiceWiener < 0) throw new IllegalArgumentException("Valores negativos no válidos");
        this.nombreMolecula = nombreMolecula;
        this.longitudCadena = longitudCadena;
        this.carbonos = new EnumMap<>(TipoCarbono.class);
        for (TipoCarbono t : TipoCarbono.values()) this.carbonos.put(t, carbonos.getOrDefault(t, 0));
        this.indiceWiener = indiceWiener;
    }

    public String getNombreMolecula() { return nombreMolecula; }
    public int getLongitudCadena() { return longitudCadena; }
    public long getIndiceWiener() { return indiceWiener; }
    public int getCarbonos(TipoCarbono tipo) { return carbonos.get(tipo); }
    public Map<TipoCarbono, Integer> getCarbonos() { return Collections.unmodifiableMap(carbonos); }

    public int totalCarbonos() {
        int total = 0;
        for (int n : carbonos.values()) total += n;
        return total;
    }

    @Override
    public String toString() {
        return nombreMolecula + "{cadena=" + longitudCadena + ", carbonos=" + carbonos + ", wiener=" + indiceWiener + "}";
    }
}
