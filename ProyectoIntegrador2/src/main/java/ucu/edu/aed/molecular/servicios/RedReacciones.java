package ucu.edu.aed.molecular.servicios;

import ucu.edu.aed.molecular.grafo.DirectedGraph;
import ucu.edu.aed.molecular.grafo.algoritmos.DirectedGraphAlgorithms;
import ucu.edu.aed.molecular.sintesis.*;
import ucu.edu.aed.tda.grafo.model.edge.WeightedEdge;
import ucu.edu.aed.tda.grafo.model.result.IDijkstraResult;
import ucu.edu.aed.tda.grafo.model.result.Path;

import java.util.*;

/*
  Red de reacciones: GRAFO DIRIGIDO BIPARTITO  Compuesto -> Reaccion -> Compuesto.
   - arista reactivo -> reacción: peso = Criterio.peso(reacción) (costo, riesgo o combinado)
   - arista reacción -> producto: peso 0
  Así el peso de un camino es la suma de los pesos de las reacciones que atraviesa.

  Lógica Y vs. lógica O (decisión de diseño):
   Un camino en el grafo entra a una reacción desde UN reactivo, pero en la química la
   reacción necesita TODOS sus reactivos. Si se agregaran aristas desde todos los reactivos,
   Dijkstra encontraría rutas falsas (ej.: Oxígeno -> R4 -> Ácido acético cuesta 60, pero R4
   también necesita acetaldehído, que no está en stock). Por eso:
    1. compuestosObtenibles() calcula con un punto fijo qué se puede obtener exigiendo
       todos los reactivos (lógica Y).
    2. Grafo "lineal" para Dijkstra y todos los caminos: la arista reactivo X -> reacción r
       existe solo si los OTROS reactivos de r están en stock. Así cada camino es una ruta real:
       el compuesto que viene por el camino + reactivos que ya están en el laboratorio.
    3. planDeSintesis() resuelve el caso general (varios intermedios fabricados, ej. acetato
       de etilo = ácido + etanol) con programación dinámica sobre la red y ordena los pasos
       con clasificación topológica.
*/
public class RedReacciones {

    private final Map<String, Compuesto> compuestos = new LinkedHashMap<>();
    private final List<Reaccion> reacciones = new ArrayList<>();
    private final DirectedGraphAlgorithms alg = new DirectedGraphAlgorithms();

    // ─────────────── Registro ───────────────

    public void registrarCompuesto(Compuesto c) { compuestos.putIfAbsent(c.getNombre().toLowerCase(), c); }

    public void registrarReaccion(Reaccion r) {
        for (Reaccion existente : reacciones)
            if (existente.getId().equals(r.getId())) throw new IllegalArgumentException("Ya existe la reacción " + r.getId());
        r.getReactivos().forEach(this::registrarCompuesto);
        r.getProductos().forEach(this::registrarCompuesto);
        reacciones.add(r);
    }

    public Compuesto buscarCompuesto(String nombre) {
        return nombre == null ? null : compuestos.get(nombre.trim().toLowerCase());
    }

    public Collection<Compuesto> getCompuestos() { return Collections.unmodifiableCollection(compuestos.values()); }
    public List<Reaccion> getReacciones() { return Collections.unmodifiableList(reacciones); }

    public List<Compuesto> disponibles() {
        List<Compuesto> res = new ArrayList<>();
        for (Compuesto c : compuestos.values()) if (c.isDisponible()) res.add(c);
        return res;
    }

    // ─────────────── ¿Se puede obtener? (lógica Y) ───────────────

    /**
     * Punto fijo: se parte de los compuestos disponibles y se "disparan" las reacciones
     * cuyos reactivos ya están todos obtenidos, hasta que no cambie nada.
     * Costo: O(R · (R · k)), R = reacciones, k = reactivos por reacción.
     */
    public Set<Compuesto> compuestosObtenibles() {
        Set<Compuesto> obtenibles = new LinkedHashSet<>(disponibles());
        Set<Reaccion> disparadas = new HashSet<>();
        boolean cambio = true;
        while (cambio) {
            cambio = false;
            for (Reaccion r : reacciones) {
                if (disparadas.contains(r) || !obtenibles.containsAll(r.getReactivos())) continue;
                disparadas.add(r);
                if (obtenibles.addAll(r.getProductos())) cambio = true;
            }
        }
        return obtenibles;
    }

    public boolean esObtenible(Compuesto objetivo) { return compuestosObtenibles().contains(objetivo); }

    public List<Reaccion> reaccionesHabilitadas() {
        Set<Compuesto> obtenibles = compuestosObtenibles();
        List<Reaccion> res = new ArrayList<>();
        for (Reaccion r : reacciones) if (obtenibles.containsAll(r.getReactivos())) res.add(r);
        return res;
    }

    // ─────────────── Grafo ───────────────

    /**
     * @param soloLineal true: arista X -> r solo si los otros reactivos de r están en stock (para rutas);
     *                   false: arista desde todos los reactivos (alcanzabilidad sin restricciones).
     */
    public DirectedGraph<NodoSintesis, PasoSintesis> construirGrafo(Criterio criterio, Collection<Reaccion> usar,
                                                                    boolean soloLineal) {
        DirectedGraph<NodoSintesis, PasoSintesis> g = new DirectedGraph<>();
        for (Compuesto c : compuestos.values()) g.agregarVertice(c);
        for (Reaccion r : usar) {
            g.agregarVertice(r);
            for (Compuesto c : r.getReactivos())
                if (!soloLineal || otrosReactivosEnStock(r, c)) g.agregarArista(c, r, PasoSintesis.entrada(r, criterio));
            for (Compuesto c : r.getProductos()) g.agregarArista(r, c, PasoSintesis.salida(r));
        }
        return g;
    }

    private boolean otrosReactivosEnStock(Reaccion r, Compuesto entrada) {
        for (Compuesto otro : r.getReactivos()) if (!otro.equals(entrada) && !otro.isDisponible()) return false;
        return true;
    }

    // ─────────────── Mejor ruta (Dijkstra) ───────────────

    /**
     * Ruta de menor peso desde algún compuesto disponible hasta el objetivo (grafo lineal).
     * Se corre Dijkstra desde cada compuesto disponible y se queda con el mejor.
     * Si no hay ruta lineal (hacen falta varios intermedios fabricados), usa planDeSintesis.
     * Devuelve null si el objetivo no es obtenible; una ruta vacía si ya está en stock.
     * Costo: B · O((V + E) log V), B = compuestos disponibles.
     */
    public RutaSintesis mejorRuta(Compuesto objetivo, Criterio criterio) {
        if (objetivo == null || !esObtenible(objetivo)) return null;
        if (objetivo.isDisponible()) return new RutaSintesis(objetivo, List.of());
        DirectedGraph<NodoSintesis, PasoSintesis> g = construirGrafo(criterio, reaccionesHabilitadas(), true);
        double mejorCosto = Double.POSITIVE_INFINITY;
        List<NodoSintesis> mejorCamino = null;
        for (Compuesto origen : disponibles()) {
            IDijkstraResult<NodoSintesis> d = alg.dijkstra(g.construirComparable(origen), g);
            double costo = d.getCost(objetivo);
            if (costo < mejorCosto) { mejorCosto = costo; mejorCamino = d.getPath(objetivo); }
        }
        return mejorCamino == null ? planDeSintesis(objetivo, criterio) : new RutaSintesis(objetivo, reaccionesDe(mejorCamino));
    }

    // ─────────────── Rutas alternativas (todos los caminos) ───────────────

    /** Todas las rutas simples desde compuestos disponibles al objetivo, ordenadas por costo, riesgo y pasos. */
    public List<RutaSintesis> rutasAlternativas(Compuesto objetivo) {
        List<RutaSintesis> rutas = new ArrayList<>();
        if (objetivo == null || objetivo.isDisponible() || !esObtenible(objetivo)) return rutas;
        DirectedGraph<NodoSintesis, PasoSintesis> g = construirGrafo(Criterio.COSTO, reaccionesHabilitadas(), true);
        Set<List<Reaccion>> vistas = new HashSet<>();
        for (Compuesto origen : disponibles()) {
            List<Path<NodoSintesis>> caminos = alg.obtenerTodosLosCaminos(
                    g.construirComparable(origen), g.construirComparable(objetivo), g);
            for (Path<NodoSintesis> p : caminos) {
                List<Reaccion> pasos = reaccionesDe(p.getPath());
                if (vistas.add(pasos)) rutas.add(new RutaSintesis(objetivo, pasos));
            }
        }
        Collections.sort(rutas);
        return rutas;
    }

    // ─────────────── Plan completo + orden de ejecución ───────────────

    /**
     * Plan completo para obtener el objetivo: elige para cada compuesto que falta la reacción
     * de menor peso (contando también lo que cuesta fabricar sus reactivos) y ordena los pasos
     * con clasificación topológica. Devuelve null si no es obtenible.
     */
    public RutaSintesis planDeSintesis(Compuesto objetivo, Criterio criterio) {
        if (objetivo == null || !esObtenible(objetivo)) return null;
        Map<Compuesto, Double> memo = new HashMap<>();
        Map<Compuesto, Reaccion> elegida = new HashMap<>();
        costoMinimo(objetivo, criterio, memo, elegida, new HashSet<>());
        Set<Reaccion> plan = new LinkedHashSet<>();
        juntarPlan(objetivo, elegida, plan, new HashSet<>());
        return new RutaSintesis(objetivo, ordenDeEjecucion(plan));
    }

    private double costoMinimo(Compuesto c, Criterio crit, Map<Compuesto, Double> memo,
                               Map<Compuesto, Reaccion> elegida, Set<Compuesto> enCurso) {
        if (c.isDisponible()) return 0;
        if (memo.containsKey(c)) return memo.get(c);
        if (!enCurso.add(c)) return Double.POSITIVE_INFINITY;   // evita ciclos A -> B -> A
        double mejor = Double.POSITIVE_INFINITY;
        for (Reaccion r : reacciones) {
            if (!r.produce(c)) continue;
            double total = crit.peso(r);
            for (Compuesto reactivo : r.getReactivos()) total += costoMinimo(reactivo, crit, memo, elegida, enCurso);
            if (total < mejor) { mejor = total; elegida.put(c, r); }
        }
        enCurso.remove(c);
        memo.put(c, mejor);
        return mejor;
    }

    private void juntarPlan(Compuesto c, Map<Compuesto, Reaccion> elegida, Set<Reaccion> plan, Set<Compuesto> vistos) {
        if (c.isDisponible() || !vistos.add(c)) return;
        Reaccion r = elegida.get(c);
        if (r == null) return;
        for (Compuesto reactivo : r.getReactivos()) juntarPlan(reactivo, elegida, plan, vistos);
        plan.add(r);
    }

    /**
     * Orden válido para ejecutar un conjunto de reacciones: grafo dirigido entre reacciones
     * (r1 -> r2 si r1 fabrica un reactivo de r2 que no está en stock) + clasificación topológica de la biblioteca.
     * Costo: O(P²·k) para armar el grafo + O(P + A) de la clasificación, P = pasos.
     */
    public List<Reaccion> ordenDeEjecucion(Collection<Reaccion> pasos) {
        DirectedGraph<Reaccion, WeightedEdge> dependencias = new DirectedGraph<>();
        for (Reaccion r : pasos) dependencias.agregarVertice(r);
        for (Reaccion r1 : pasos)
            for (Reaccion r2 : pasos)
                if (r1 != r2 && fabricaReactivoDe(r1, r2)) dependencias.agregarArista(r1, r2, new WeightedEdge(1));
        return alg.calcularClasificacionTopologica(dependencias);
    }

    /** r1 debe ir antes que r2 si fabrica un reactivo de r2 que NO está en stock (si está en stock no hace falta esperarlo). */
    private boolean fabricaReactivoDe(Reaccion r1, Reaccion r2) {
        for (Compuesto p : r1.getProductos()) if (!p.isDisponible() && r2.consume(p)) return true;
        return false;
    }

    private List<Reaccion> reaccionesDe(List<NodoSintesis> camino) {
        List<Reaccion> pasos = new ArrayList<>();
        for (NodoSintesis n : camino) if (n.esReaccion()) pasos.add((Reaccion) n);
        return pasos;
    }
}
