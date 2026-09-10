package ucu.edu.aed.ProyectoPrimerHito;

import junit.framework.TestCase;
import ucu.edu.aed.tda.TDALista;

import java.time.LocalDate;

/**
 * Tests para OrdenTrabajo — árbol n-ario de tareas de un vehículo.
 *
 * Estructura usada en los tests:
 *
 *   raiz: "Cambio correa" (PENDIENTE)
 *     ├── hijo1: "Tensor desgastado" (PENDIENTE)
 *     │     └── nieto: "Polea rota" (PENDIENTE)
 *     └── hijo2: "Bomba de agua" (PENDIENTE)
 *
 * Todos los tests crean su propia instancia de OrdenTrabajo para evitar
 * estado compartido entre casos.
 */
public class OrdenTrabajoTest extends TestCase {

    // ─── helpers para crear tareas de prueba ──────────────────────────

    private Tarea tarea(String desc, double costo) {
        return new Tarea(desc, TipoTarea.FALLA_ADICIONAL, LocalDate.now(), costo, null);
    }

    /**
     * Arma el árbol de ejemplo y devuelve un array con las instancias de Tarea
     * en el orden: [raiz, hijo1, hijo2, nieto].
     * El array permite que cada test opere sobre las mismas instancias de Tarea
     * con las que se construyó el árbol (== referencia).
     */
    private Object[] crearOrdenConArbol() {
        Tarea raiz   = tarea("Cambio correa", 500.0);
        Tarea hijo1  = tarea("Tensor desgastado", 200.0);
        Tarea hijo2  = tarea("Bomba de agua", 300.0);
        Tarea nieto  = tarea("Polea rota", 150.0);

        OrdenTrabajo orden = new OrdenTrabajo(raiz);
        orden.agregarTareaDerivada(raiz, hijo1);
        orden.agregarTareaDerivada(raiz, hijo2);
        orden.agregarTareaDerivada(hijo1, nieto);

        return new Object[]{orden, raiz, hijo1, hijo2, nieto};
    }

    // ─── constructor ──────────────────────────────────────────────────

    /** Al crear la orden, la raíz debe quedar PENDIENTE. */
    public void testConstructorEstadoRaiz() {
        Tarea raiz = tarea("Diagnóstico inicial", 0.0);
        OrdenTrabajo orden = new OrdenTrabajo(raiz);
        assertEquals(EstadoTarea.PENDIENTE, raiz.getEstado());
        assertFalse(orden.esVacio());
        assertEquals(raiz, orden.getTareaRaiz());
    }

    // ─── agregarTareaDerivada ─────────────────────────────────────────

    /** Agregar hijo a raíz: debe aparecer en el postorden de pendientes. */
    public void testAgregarTareaDerivadaAlaRaiz() {
        Tarea raiz = tarea("Raíz", 0.0);
        Tarea hijo = tarea("Hijo", 100.0);
        OrdenTrabajo orden = new OrdenTrabajo(raiz);
        orden.agregarTareaDerivada(raiz, hijo);
        assertEquals(EstadoTarea.PENDIENTE, hijo.getEstado());
        // postorden: hijo antes que raíz
        assertEquals(2, orden.tareasPendientesParaEntregar().tamaño());
    }

    /** Agregar tarea derivada de un nodo que no existe debe lanzar excepción. */
    public void testAgregarTareaDerivadaOrigenInexistente() {
        Tarea raiz = tarea("Raíz", 0.0);
        Tarea ajena = tarea("Ajena", 0.0);    // nunca fue insertada en la orden
        Tarea nueva = tarea("Nueva", 0.0);
        OrdenTrabajo orden = new OrdenTrabajo(raiz);
        try {
            orden.agregarTareaDerivada(ajena, nueva);
            fail("Debería lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // correcto
        }
    }

    // ─── puedeTerminar ────────────────────────────────────────────────

    /**
     * Una tarea con descendientes PENDIENTES no puede terminarse.
     * Solo puede hacerlo cuando todos sus descendientes están TERMINADOS/RECHAZADOS.
     */
    public void testPuedeTerminarConHijosPendientes() {
        Object[] partes = crearOrdenConArbol();
        OrdenTrabajo orden = (OrdenTrabajo) partes[0];
        Tarea raiz = (Tarea) partes[1];
        assertFalse(orden.puedeTerminar(raiz));
    }

    /** Una hoja sin descendientes puede terminarse cuando no está rechazada. */
    public void testPuedeTerminarHojaSinHijos() {
        Object[] partes = crearOrdenConArbol();
        OrdenTrabajo orden = (OrdenTrabajo) partes[0];
        Tarea nieto = (Tarea) partes[4];
        // La hoja no tiene hijos, pero sigue PENDIENTE — puede ser ejecutada
        // puedeTerminar evalúa si TODOS los descendientes están listos, no el nodo en sí
        assertTrue(orden.puedeTerminar(nieto));
    }

    /** La raíz puede terminarse cuando todos sus descendientes están TERMINADOS. */
    public void testPuedeTerminarCuandoTodosTerminados() {
        Object[] partes = crearOrdenConArbol();
        OrdenTrabajo orden = (OrdenTrabajo) partes[0];
        Tarea raiz  = (Tarea) partes[1];
        Tarea hijo1 = (Tarea) partes[2];
        Tarea hijo2 = (Tarea) partes[3];
        Tarea nieto = (Tarea) partes[4];
        // Terminamos todos los hijos y nietos
        nieto.setEstado(EstadoTarea.TERMINADA);
        hijo1.setEstado(EstadoTarea.TERMINADA);
        hijo2.setEstado(EstadoTarea.TERMINADA);
        assertTrue(orden.puedeTerminar(raiz));
    }

    // ─── tareasPendientesParaEntregar ────────────────────────────────

    /**
     * El postorden debe ser: nieto, hijo1, hijo2, raiz.
     * Primero se resuelven los problemas más profundos (derivados),
     * luego el trabajo que los originó.
     */
    public void testPostordenOrdenCorrecto() {
        Object[] partes = crearOrdenConArbol();
        OrdenTrabajo orden = (OrdenTrabajo) partes[0];
        Tarea raiz  = (Tarea) partes[1];
        Tarea hijo1 = (Tarea) partes[2];
        Tarea hijo2 = (Tarea) partes[3];
        Tarea nieto = (Tarea) partes[4];

        TDALista<Tarea> lista = orden.tareasPendientesParaEntregar();
        assertEquals(4, lista.tamaño());
        // postorden: nieto → hijo1 → hijo2 → raiz
        assertEquals(nieto, lista.obtener(0));
        assertEquals(hijo1, lista.obtener(1));
        assertEquals(hijo2, lista.obtener(2));
        assertEquals(raiz,  lista.obtener(3));
    }

    /** Las tareas TERMINADAS y RECHAZADAS no aparecen en el postorden. */
    public void testPostordenExcluyeTerminadasYRechazadas() {
        Object[] partes = crearOrdenConArbol();
        OrdenTrabajo orden = (OrdenTrabajo) partes[0];
        Tarea hijo2 = (Tarea) partes[3];
        Tarea nieto = (Tarea) partes[4];
        hijo2.setEstado(EstadoTarea.RECHAZADA);
        nieto.setEstado(EstadoTarea.TERMINADA);

        TDALista<Tarea> lista = orden.tareasPendientesParaEntregar();
        // Solo quedan raiz e hijo1
        assertEquals(2, lista.tamaño());
        for (int i = 0; i < lista.tamaño(); i++) {
            assertFalse(lista.obtener(i).isResuelta());
        }
    }

    // ─── suspender / reanudar ─────────────────────────────────────────

    /**
     * suspender(hijo1) debe cambiar hijo1 y nieto a SUSPENDIDA,
     * pero hijo2 y raíz quedan intactos.
     */
    public void testSuspenderSubarbol() {
        Object[] partes = crearOrdenConArbol();
        OrdenTrabajo orden = (OrdenTrabajo) partes[0];
        Tarea raiz  = (Tarea) partes[1];
        Tarea hijo1 = (Tarea) partes[2];
        Tarea hijo2 = (Tarea) partes[3];
        Tarea nieto = (Tarea) partes[4];

        orden.suspender(hijo1);

        assertEquals(EstadoTarea.SUSPENDIDA, hijo1.getEstado());
        assertEquals(EstadoTarea.SUSPENDIDA, nieto.getEstado());
        // El resto no se toca
        assertEquals(EstadoTarea.PENDIENTE, hijo2.getEstado());
        assertEquals(EstadoTarea.PENDIENTE, raiz.getEstado());
    }

    /** reanudar después de suspender debe volver todo a PENDIENTE. */
    public void testReanudarSubarbol() {
        Object[] partes = crearOrdenConArbol();
        OrdenTrabajo orden = (OrdenTrabajo) partes[0];
        Tarea hijo1 = (Tarea) partes[2];
        Tarea nieto = (Tarea) partes[4];

        orden.suspender(hijo1);
        orden.reanudar(hijo1);

        assertEquals(EstadoTarea.PENDIENTE, hijo1.getEstado());
        assertEquals(EstadoTarea.PENDIENTE, nieto.getEstado());
    }

    /** reanudar no afecta a tareas TERMINADAS dentro del subárbol suspendido. */
    public void testReanudarNoTocaTerminadas() {
        Object[] partes = crearOrdenConArbol();
        OrdenTrabajo orden = (OrdenTrabajo) partes[0];
        Tarea hijo1 = (Tarea) partes[2];
        Tarea nieto = (Tarea) partes[4];

        nieto.setEstado(EstadoTarea.TERMINADA); // terminado antes de suspender
        orden.suspender(hijo1);
        // suspender no debe tocar una tarea ya TERMINADA
        assertEquals(EstadoTarea.TERMINADA, nieto.getEstado());

        orden.reanudar(hijo1);
        // reanudar tampoco debe cambiar TERMINADA
        assertEquals(EstadoTarea.TERMINADA, nieto.getEstado());
        assertEquals(EstadoTarea.PENDIENTE, hijo1.getEstado());
    }

    // ─── costo ────────────────────────────────────────────────────────

    /** El costo total es la suma de todos los nodos del árbol. */
    public void testCostoTotal() {
        Object[] partes = crearOrdenConArbol();
        OrdenTrabajo orden = (OrdenTrabajo) partes[0];
        // 500 + 200 + 300 + 150 = 1150
        assertEquals(1150.0, orden.costoTotal(), 0.001);
    }

    /**
     * costoParcial(hijo1) suma hijo1 + nieto: 200 + 150 = 350.
     * No incluye raíz ni hijo2.
     */
    public void testCostoParcial() {
        Object[] partes = crearOrdenConArbol();
        OrdenTrabajo orden = (OrdenTrabajo) partes[0];
        Tarea hijo1 = (Tarea) partes[2];
        assertEquals(350.0, orden.costoParcial(hijo1), 0.001);
    }

    // ─── aprobar / rechazar ───────────────────────────────────────────

    /** aprobar pasa una tarea PENDIENTE a EN_CURSO (solo ese nodo). */
    public void testAprobar() {
        Object[] partes = crearOrdenConArbol();
        OrdenTrabajo orden = (OrdenTrabajo) partes[0];
        Tarea raiz = (Tarea) partes[1];
        Tarea hijo1 = (Tarea) partes[2];

        orden.aprobar(raiz);
        assertEquals(EstadoTarea.EN_CURSO, raiz.getEstado());
        // hijo1 sigue PENDIENTE — aprobar es individual
        assertEquals(EstadoTarea.PENDIENTE, hijo1.getEstado());
    }

    /** rechazar propaga RECHAZADA a todo el subárbol. */
    public void testRechazarSubarbol() {
        Object[] partes = crearOrdenConArbol();
        OrdenTrabajo orden = (OrdenTrabajo) partes[0];
        Tarea hijo1 = (Tarea) partes[2];
        Tarea nieto = (Tarea) partes[4];

        orden.rechazar(hijo1);
        assertEquals(EstadoTarea.RECHAZADA, hijo1.getEstado());
        assertEquals(EstadoTarea.RECHAZADA, nieto.getEstado());
    }

    /**
     * Con hijo1 (y nieto) RECHAZADOS, y hijo2 TERMINADO,
     * la raíz puede terminarse.
     */
    public void testPuedeTerminarConMezclaTerminadosRechazados() {
        Object[] partes = crearOrdenConArbol();
        OrdenTrabajo orden = (OrdenTrabajo) partes[0];
        Tarea raiz  = (Tarea) partes[1];
        Tarea hijo1 = (Tarea) partes[2];
        Tarea hijo2 = (Tarea) partes[3];

        orden.rechazar(hijo1);  // hijo1 y nieto → RECHAZADA
        hijo2.setEstado(EstadoTarea.TERMINADA);

        assertTrue(orden.puedeTerminar(raiz));
    }

    // ─── consultas ────────────────────────────────────────────────────

    /** trabajosSuspendidos lista exactamente las tareas SUSPENDIDAS. */
    public void testTrabajosSuspendidos() {
        Object[] partes = crearOrdenConArbol();
        OrdenTrabajo orden = (OrdenTrabajo) partes[0];
        Tarea hijo1 = (Tarea) partes[2];

        orden.suspender(hijo1);
        TDALista<Tarea> suspendidos = orden.trabajosSuspendidos();
        // hijo1 y nieto deben estar suspendidos (2 tareas)
        assertEquals(2, suspendidos.tamaño());
    }

    /** cantidadPendientes excluye TERMINADAS y RECHAZADAS. */
    public void testCantidadPendientes() {
        Object[] partes = crearOrdenConArbol();
        OrdenTrabajo orden = (OrdenTrabajo) partes[0];
        Tarea hijo2 = (Tarea) partes[3];
        Tarea nieto = (Tarea) partes[4];

        assertEquals(4, orden.cantidadPendientes()); // todos pendientes al inicio

        hijo2.setEstado(EstadoTarea.TERMINADA);
        nieto.setEstado(EstadoTarea.RECHAZADA);
        assertEquals(2, orden.cantidadPendientes()); // raiz e hijo1 siguen activos
    }
}
