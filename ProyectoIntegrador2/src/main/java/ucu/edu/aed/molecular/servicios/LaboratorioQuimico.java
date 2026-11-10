package ucu.edu.aed.molecular.servicios;

import ucu.edu.aed.molecular.compatibilidad.ParametrosObjetivo;
import ucu.edu.aed.molecular.compatibilidad.ResultadoCompatibilidad;
import ucu.edu.aed.molecular.grafo.DirectedGraph;
import ucu.edu.aed.molecular.grafo.algoritmos.DirectedGraphAlgorithms;
import ucu.edu.aed.molecular.grafo.algoritmos.UndirectedGraphAlgorithms;
import ucu.edu.aed.molecular.molecula.Atomo;
import ucu.edu.aed.molecular.molecula.Enlace;
import ucu.edu.aed.molecular.molecula.Molecula;
import ucu.edu.aed.molecular.sintesis.Compuesto;
import ucu.edu.aed.molecular.sintesis.RutaSintesis;

import java.util.*;

/*
  Fachada del sistema: junta las moléculas, la red de reacciones y los servicios,
  y define las CONSULTAS PROPIAS del grupo (cada una usa un algoritmo de la biblioteca):

   1. compuestosConGrupoYToxicidadMaxima  -> recorrido BEA (grupos funcionales) + datos científicos
   2. atomosPuente                         -> puntos de articulación
   3. contarAnillos                        -> Kruskal (E - aristas del árbol abarcador)
   4. rutaMasSeguraConPresupuesto          -> todos los caminos + filtro por costo
   5. atomoCentral                         -> centro del grafo (Floyd + excentricidad)
*/
public class LaboratorioQuimico {

    private final List<Molecula> moleculas = new ArrayList<>();
    private final RedReacciones red = new RedReacciones();
    private final DetectorGruposFuncionales detector = new DetectorGruposFuncionales();
    private final AnalizadorEstructural analizador = new AnalizadorEstructural();
    private final EvaluadorCompatibilidad evaluador = new EvaluadorCompatibilidad(analizador);
    private final UndirectedGraphAlgorithms algNoDirigidos = new UndirectedGraphAlgorithms();
    private final DirectedGraphAlgorithms algDirigidos = new DirectedGraphAlgorithms();

    public void registrarMolecula(Molecula m) {
        List<String> errores = m.validar();
        if (!errores.isEmpty()) throw new IllegalArgumentException(m.getNombre() + " no es válida: " + errores);
        moleculas.add(m);
    }

    public List<Molecula> getMoleculas() { return Collections.unmodifiableList(moleculas); }
    public RedReacciones getRed() { return red; }
    public DetectorGruposFuncionales getDetector() { return detector; }
    public AnalizadorEstructural getAnalizador() { return analizador; }
    public EvaluadorCompatibilidad getEvaluador() { return evaluador; }

    public Molecula buscarMolecula(String nombre) {
        for (Molecula m : moleculas) if (m.getNombre().equalsIgnoreCase(nombre.trim())) return m;
        return null;
    }

    public List<ResultadoCompatibilidad> rankingCompatibilidad(List<Molecula> candidatas, ParametrosObjetivo p) {
        return evaluador.ordenar(candidatas, p);
    }

    // ─────────────── Consultas propias ───────────────

    /** 1. Compuestos con estructura que tienen el grupo funcional y toxicidad NFPA <= máximo. */
    public List<Compuesto> compuestosConGrupoYToxicidadMaxima(String grupo, int toxicidadMaxima) {
        List<Compuesto> res = new ArrayList<>();
        for (Compuesto c : red.getCompuestos())
            if (c.tieneEstructura() && c.getDatos().getToxicidad() <= toxicidadMaxima
                    && detector.contiene(c.getMolecula(), grupo)) res.add(c);
        return res;
    }

    /** 2. Átomos cuya ruptura separa la molécula en fragmentos (puntos de articulación). */
    public Set<Atomo> atomosPuente(Molecula m) { return algNoDirigidos.puntosDeArticulacion(m.getGrafo()); }

    /** 3. Cantidad de anillos independientes (Kruskal). */
    public int contarAnillos(Molecula m) { return detector.contarAnillos(m); }

    /** 4. Entre todas las rutas cuyo costo total no supera el presupuesto, la de menor riesgo. */
    public RutaSintesis rutaMasSeguraConPresupuesto(String objetivo, double presupuesto) {
        RutaSintesis mejor = null;
        for (RutaSintesis r : red.rutasAlternativas(red.buscarCompuesto(objetivo))) {
            if (r.costoTotal() > presupuesto) continue;
            if (mejor == null || r.riesgoTotal() < mejor.riesgoTotal()) mejor = r;
        }
        return mejor;
    }

    /** 5. Átomo central de la molécula: el de menor excentricidad (centro del grafo). */
    public Atomo atomoCentral(Molecula m) {
        if (m.cantidadAtomos() == 0) return null;
        return algDirigidos.obtenerCentroGrafo(m.comoGrafoDirigido());
    }

    public double excentricidad(Molecula m, Atomo a) {
        DirectedGraph<Atomo, Enlace> d = m.comoGrafoDirigido();
        return algDirigidos.obtenerExcentricidad(d, d.construirComparable(a));
    }
}
