package ucu.edu.aed.molecular.sintesis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
  Secuencia de reacciones que lleva desde compuestos disponibles hasta un compuesto objetivo.
 
  Métricas para comparar rutas alternativas:
   - costoTotal: suma de costos (el dinero se acumula).
   - riesgoTotal: suma de riesgos (más pasos peligrosos = más exposición).
   - riesgoMaximo: el paso más peligroso (sirve para descartar rutas con un paso inaceptable).
 */
public class RutaSintesis implements Comparable<RutaSintesis> {

    private final Compuesto objetivo;
    private final List<Reaccion> pasos;

    public RutaSintesis(Compuesto objetivo, List<Reaccion> pasos) {
        this.objetivo = objetivo;
        this.pasos = new ArrayList<>(pasos);
    }

    public Compuesto getObjetivo() { return objetivo; }
    public List<Reaccion> getPasos() { return Collections.unmodifiableList(pasos); }
    public int cantidadPasos() { return pasos.size(); }
    public boolean esVacia() { return pasos.isEmpty(); }

    public double costoTotal() {
        double total = 0;
        for (Reaccion r : pasos) total += r.getCosto();
        return total;
    }

    public double riesgoTotal() {
        double total = 0;
        for (Reaccion r : pasos) total += r.getRiesgo();
        return total;
    }

    public double riesgoMaximo() {
        double max = 0;
        for (Reaccion r : pasos) max = Math.max(max, r.getRiesgo());
        return max;
    }

    /** Peso total de la ruta según un criterio cualquiera. */
    public double pesoTotal(Criterio criterio) {
        double total = 0;
        for (Reaccion r : pasos) total += criterio.peso(r);
        return total;
    }

    /** Orden natural: menor costo primero; a igual costo, menor riesgo; luego menos pasos. */
    @Override
    public int compareTo(RutaSintesis o) {
        int c = Double.compare(costoTotal(), o.costoTotal());
        if (c != 0) return c;
        c = Double.compare(riesgoTotal(), o.riesgoTotal());
        if (c != 0) return c;
        return Integer.compare(cantidadPasos(), o.cantidadPasos());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Ruta a " + objetivo + ": ");
        for (int i = 0; i < pasos.size(); i++) {
            if (i > 0) sb.append(" → ");
            sb.append(pasos.get(i).getId());
        }
        return sb.append(String.format(" [costo=%.2f, riesgo=%.1f, riesgoMax=%.1f]",
                costoTotal(), riesgoTotal(), riesgoMaximo())).toString();
    }
}
