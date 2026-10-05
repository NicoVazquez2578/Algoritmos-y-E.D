package ucu.edu.aed.ProyectoPrimerHito;

import ucu.edu.aed.impl.ListaArray;
import ucu.edu.aed.tda.TDALista;

/**
 * Árbol general n-ario que representa la orden de trabajo de un vehículo.
 *
 * La raíz es el trabajo original por el que ingresó el vehículo.
 * Cuando al desarmar se detectan fallas derivadas, se agregan como hijos
 * del nodo donde se encontraron. Un hijo puede a su vez tener más hijos
 * si al resolverlo aparecen más problemas.
 *
 * Ejemplo:
 *   Cambio de correa de distribución (raíz)
 *     ├── Tensor desgastado (falla descubierta al destapar)
 *     │     └── Polea tensora rota (falla al sacar el tensor)
 *     └── Bomba de agua con pérdida (falla adicional detectada)
 *
 * INVARIANTE PRINCIPAL:
 *   Una tarea no puede marcarse TERMINADA si alguno de sus descendientes
 *   sigue PENDIENTE, EN_CURSO o SUSPENDIDA.
 *   (Los RECHAZADOS no bloquean porque el cliente ya los descartó.)
 *
 * ORDEN DE EJECUCIÓN:
 *   Postorden: primero se resuelven las tareas hoja (las más profundas,
 *   sin dependencias) y al final la raíz. Entre hermanos, el orden es
 *   de izquierda a derecha (orden de detección).
 *
 * ESTRUCTURAS USADAS:
 *   - ListaArray del Hito 1 para los hijos de cada nodo
 *   - Referencia al padre para poder subir al árbol si se necesita
 */
public class OrdenTrabajo {

    // ─── Nodo interno ─────────────────────────────────────────────────
    // Paquete-visible para que los tests puedan acceder si lo necesitan.
    static class NodoTarea {
        Tarea dato;
        NodoTarea padre;           // null solo para la raíz
        ListaArray<NodoTarea> hijos;

        NodoTarea(Tarea dato, NodoTarea padre) {
            this.dato = dato;
            this.padre = padre;
            this.hijos = new ListaArray<>();
        }
    }

    private NodoTarea raiz;

    /**
     * Crea una orden de trabajo con la tarea principal (la del ingreso del vehículo).
     */
    public OrdenTrabajo(Tarea tareaRaiz) {
        this.raiz = new NodoTarea(tareaRaiz, null);
        tareaRaiz.setEstado(EstadoTarea.PENDIENTE);
    }

    // ─── agregarTareaDerivada ─────────────────────────────────────────

    /**
     * Registra 'derivada' como trabajo que se desprendió de 'origen'.
     * Esto es lo que ocurre cuando al desarmar aparece una falla nueva:
     * se agrega como hijo del nodo donde se encontró.
     *
     * Usamos igualdad de referencia (==) para identificar el nodo, porque
     * cada Tarea es una instancia única en memoria y no tiene sentido
     * tener dos nodos con la misma instancia de Tarea.
     *
     * Complejidad: O(n) para encontrar el nodo origen.
     *
     * @throws IllegalArgumentException si 'origen' no está en la orden
     */
    public void agregarTareaDerivada(Tarea origen, Tarea derivada) {
        NodoTarea nodoOrigen = buscarNodo(raiz, origen);
        if (nodoOrigen == null) {
            throw new IllegalArgumentException("La tarea origen no pertenece a esta orden de trabajo.");
        }
        derivada.setEstado(EstadoTarea.PENDIENTE);
        nodoOrigen.hijos.agregar(new NodoTarea(derivada, nodoOrigen));
    }

    // ─── puedeTerminar ────────────────────────────────────────────────

    /**
     * Retorna true si TODOS LOS DESCENDIENTES de la tarea están
     * TERMINADOS o RECHAZADOS. Solo entonces el tallerista puede
     * cerrar ese nodo y avanzar hacia la raíz.
     *
     * No se evalúa el estado del nodo 't' en sí (puede ser PENDIENTE
     * o EN_CURSO): la pregunta es si sus sub-tareas ya están resueltas.
     * Una hoja (sin hijos) siempre puede terminarse.
     *
     * Complejidad: O(k) donde k es el tamaño del subárbol de 't'.
     */
    public boolean puedeTerminar(Tarea t) {
        NodoTarea nodo = buscarNodo(raiz, t);
        if (nodo == null) return false;
        // Verificamos cada hijo y su subárbol completo
        for (int i = 0; i < nodo.hijos.tamaño(); i++) {
            if (!subArbolListo(nodo.hijos.obtener(i))) return false;
        }
        return true;
    }

    // ─── tareasPendientesParaEntregar ────────────────────────────────

    /**
     * Lista las tareas que faltan en el orden en que deben ejecutarse
     * para poder entregar el vehículo: postorden (hijos antes que padre).
     *
     * No incluye tareas TERMINADAS ni RECHAZADAS (ya resueltas).
     * Entre hermanos, el orden es de izquierda a derecha (orden de detección).
     *
     * Complejidad: O(n).
     */
    public TDALista<Tarea> tareasPendientesParaEntregar() {
        TDALista<Tarea> resultado = new ListaArray<>();
        tareasPendientesPostorden(raiz, resultado);
        return resultado;
    }

    // ─── suspender / reanudar ─────────────────────────────────────────

    /**
     * Suspende la tarea y todos sus descendientes activos.
     * Las tareas ya TERMINADAS o RECHAZADAS no se tocan.
     * Las demás ramas del árbol (que no dependen de 't') quedan disponibles
     * para que el tallerista pueda continuar trabajando.
     *
     * Complejidad: O(k) donde k es el tamaño del subárbol.
     */
    public void suspender(Tarea t) {
        NodoTarea nodo = buscarNodo(raiz, t);
        if (nodo == null) return;
        suspenderSubarbol(nodo);
    }

    /**
     * Reanuda la tarea y todos sus descendientes SUSPENDIDOS.
     * Se llama cuando llega el repuesto que estaba bloqueando el trabajo.
     * Las tareas TERMINADAS o RECHAZADAS dentro del subárbol no se modifican.
     *
     * Complejidad: O(k).
     */
    public void reanudar(Tarea t) {
        NodoTarea nodo = buscarNodo(raiz, t);
        if (nodo == null) return;
        reanudarSubarbol(nodo);
    }

    // ─── costo ────────────────────────────────────────────────────────

    /**
     * Suma el costo de TODAS las tareas del árbol (incluyendo rechazadas).
     * Para el presupuesto total de la orden de trabajo.
     * Complejidad: O(n).
     */
    public double costoTotal() {
        return costoSubarbol(raiz);
    }

    /**
     * Suma el costo de la tarea 't' y todos sus descendientes.
     * Permite presupuestar una parte específica de la orden (ej: todo lo
     * relacionado al motor vs la suspensión).
     * Complejidad: O(n) peor caso.
     */
    public double costoParcial(Tarea t) {
        NodoTarea nodo = buscarNodo(raiz, t);
        if (nodo == null) return 0.0;
        return costoSubarbol(nodo);
    }

    // ─── aprobar / rechazar ───────────────────────────────────────────

    /**
     * Autoriza la tarea: la pasa a EN_CURSO.
     * No afecta a sus hijos (cada subparte puede aprobarse individualmente).
     */
    public void aprobar(Tarea t) {
        NodoTarea nodo = buscarNodo(raiz, t);
        if (nodo == null) return;
        // Solo aprobamos si está pendiente (no tiene sentido aprobar una terminada)
        if (nodo.dato.getEstado() == EstadoTarea.PENDIENTE) {
            nodo.dato.setEstado(EstadoTarea.EN_CURSO);
        }
    }

    /**
     * Rechaza la tarea y todos sus descendientes.
     * Una rama rechazada NO bloquea el cierre del padre: si todos los demás
     * hijos del padre están TERMINADOS o RECHAZADOS, el padre puede terminarse.
     *
     * Complejidad: O(k).
     */
    public void rechazar(Tarea t) {
        NodoTarea nodo = buscarNodo(raiz, t);
        if (nodo == null) return;
        rechazarSubarbol(nodo);
    }

    // ─── consultas ────────────────────────────────────────────────────

    /**
     * Lista todas las tareas actualmente SUSPENDIDAS.
     * Permite responder: "¿qué trabajos quedan bloqueados esperando un repuesto?"
     * Complejidad: O(n).
     */
    public TDALista<Tarea> trabajosSuspendidos() {
        TDALista<Tarea> resultado = new ListaArray<>();
        recopilarPorEstado(raiz, EstadoTarea.SUSPENDIDA, resultado);
        return resultado;
    }

    /**
     * Cantidad de tareas que no están TERMINADAS ni RECHAZADAS.
     * Útil para comparar qué vehículo tiene más trabajo pendiente.
     * Complejidad: O(n).
     */
    public int cantidadPendientes() {
        return contarNoTerminadas(raiz);
    }

    public Tarea getTareaRaiz() {
        return raiz != null ? raiz.dato : null;
    }

    public boolean esVacio() {
        return raiz == null;
    }

    // ─── helpers privados ──────────────────────────────────────────────

    // Busca el nodo que contiene exactamente esa instancia de Tarea (DFS preorden).
    // Usamos == porque cada Tarea es un objeto único en memoria.
    private NodoTarea buscarNodo(NodoTarea actual, Tarea buscada) {
        if (actual == null) return null;
        if (actual.dato == buscada) return actual;
        for (int i = 0; i < actual.hijos.tamaño(); i++) {
            NodoTarea encontrado = buscarNodo(actual.hijos.obtener(i), buscada);
            if (encontrado != null) return encontrado;
        }
        return null;
    }

    // Verdadero si todos los nodos del subárbol están TERMINADOS o RECHAZADOS.
    private boolean subArbolListo(NodoTarea nodo) {
        EstadoTarea e = nodo.dato.getEstado();
        // PENDIENTE, EN_CURSO y SUSPENDIDA bloquean el cierre
        if (e == EstadoTarea.PENDIENTE || e == EstadoTarea.EN_CURSO
                || e == EstadoTarea.SUSPENDIDA) {
            return false;
        }
        for (int i = 0; i < nodo.hijos.tamaño(); i++) {
            if (!subArbolListo(nodo.hijos.obtener(i))) return false;
        }
        return true;
    }

    // Postorden: primero hijos (en orden izq→der), luego el nodo actual.
    // Solo agrega las tareas que no están TERMINADAS ni RECHAZADAS.
    private void tareasPendientesPostorden(NodoTarea nodo, TDALista<Tarea> lista) {
        if (nodo == null) return;
        for (int i = 0; i < nodo.hijos.tamaño(); i++) {
            tareasPendientesPostorden(nodo.hijos.obtener(i), lista);
        }
        EstadoTarea e = nodo.dato.getEstado();
        if (e != EstadoTarea.TERMINADA && e != EstadoTarea.RECHAZADA) {
            lista.agregar(nodo.dato);
        }
    }

    private void suspenderSubarbol(NodoTarea nodo) {
        if (nodo == null) return;
        EstadoTarea e = nodo.dato.getEstado();
        // Solo suspendemos si la tarea todavía está activa
        if (e == EstadoTarea.PENDIENTE || e == EstadoTarea.EN_CURSO) {
            nodo.dato.setEstado(EstadoTarea.SUSPENDIDA);
        }
        for (int i = 0; i < nodo.hijos.tamaño(); i++) {
            suspenderSubarbol(nodo.hijos.obtener(i));
        }
    }

    private void reanudarSubarbol(NodoTarea nodo) {
        if (nodo == null) return;
        // Solo reanudamos las SUSPENDIDAS (las TERMINADAS y RECHAZADAS se quedan igual)
        if (nodo.dato.getEstado() == EstadoTarea.SUSPENDIDA) {
            nodo.dato.setEstado(EstadoTarea.PENDIENTE);
        }
        for (int i = 0; i < nodo.hijos.tamaño(); i++) {
            reanudarSubarbol(nodo.hijos.obtener(i));
        }
    }

    private double costoSubarbol(NodoTarea nodo) {
        if (nodo == null) return 0.0;
        double total = nodo.dato.getCosto();
        for (int i = 0; i < nodo.hijos.tamaño(); i++) {
            total += costoSubarbol(nodo.hijos.obtener(i));
        }
        return total;
    }

    private void rechazarSubarbol(NodoTarea nodo) {
        if (nodo == null) return;
        nodo.dato.setEstado(EstadoTarea.RECHAZADA);
        for (int i = 0; i < nodo.hijos.tamaño(); i++) {
            rechazarSubarbol(nodo.hijos.obtener(i));
        }
    }

    private void recopilarPorEstado(NodoTarea nodo, EstadoTarea estado, TDALista<Tarea> lista) {
        if (nodo == null) return;
        if (nodo.dato.getEstado() == estado) lista.agregar(nodo.dato);
        for (int i = 0; i < nodo.hijos.tamaño(); i++) {
            recopilarPorEstado(nodo.hijos.obtener(i), estado, lista);
        }
    }

    private int contarNoTerminadas(NodoTarea nodo) {
        if (nodo == null) return 0;
        EstadoTarea e = nodo.dato.getEstado();
        int propio = (e != EstadoTarea.TERMINADA && e != EstadoTarea.RECHAZADA) ? 1 : 0;
        int hijos = 0;
        for (int i = 0; i < nodo.hijos.tamaño(); i++) {
            hijos += contarNoTerminadas(nodo.hijos.obtener(i));
        }
        return propio + hijos;
    }
}
