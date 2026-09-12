package ucu.edu.aed.ProblemSets.ProblemSet2.Ej14;

import java.util.HashMap;
import java.util.Map;

/**
 * EJERCICIO 14 — Red de distribución de agua
 *
 * Árbol binario donde cada nodo es un Tanque. El agua fluye de la raíz
 * hacia los hijos en pasos de tiempo discretos.
 *
 * Reglas de simulación:
 *  - Cada nodo puede enviar como máximo 100 litros por paso; si tiene
 *    menos, envía todo lo que tiene.
 *  - Si el nodo tiene dos hijos, los litros se reparten equitativamente.
 *  - Si un tanque llega a su capacidad máxima, en el SIGUIENTE paso se
 *    vacía completamente, no envía agua, y se registra un "vaciado".
 *  - El procesamiento de los nodos se realiza en PREORDEN.
 *  - Para evitar que el agua recibida en un paso se propague en ese mismo
 *    paso, el flujo se calcula en dos fases: (1) calcular y acumular
 *    agua pendiente en los hijos, (2) aplicar el agua pendiente.
 */
public class RedDistribucion {

    private static final double CAUDAL_MAXIMO = 100.0;

    private Tanque raiz;
    private int tiempoActual;
    private final Map<String, Tanque> indice = new HashMap<>();

    // =========================================================================
    //  Construir la red
    // =========================================================================

    /** Agrega un nodo a la red sin conectarlo todavía. */
    public void agregarNodo(String nombre, int capacidadMaxima, double aguaInicial) {
        Tanque t = new Tanque(nombre, capacidadMaxima, aguaInicial);
        indice.put(nombre, t);
        if (raiz == null) raiz = t;
    }

    /** Conecta el nodo padre con sus hijos izquierdo y derecho (null = no hay). */
    public void conectar(String nombrePadre, String hijoIzq, String hijoDer) {
        Tanque padre = indice.get(nombrePadre);
        if (padre == null) throw new IllegalArgumentException("Nodo no encontrado: " + nombrePadre);
        if (hijoIzq != null) {
            padre.izq = indice.get(hijoIzq);
            if (padre.izq == null) throw new IllegalArgumentException("Nodo no encontrado: " + hijoIzq);
        }
        if (hijoDer != null) {
            padre.der = indice.get(hijoDer);
            if (padre.der == null) throw new IllegalArgumentException("Nodo no encontrado: " + hijoDer);
        }
    }

    // =========================================================================
    //  Simulación
    // =========================================================================

    /**
     * Avanza un paso de tiempo.
     *
     * Fase 1 (preorden): para cada nodo, calcula el agua a enviar y la
     *   acumula en el campo aguaPendiente de sus hijos. Vacía los nodos
     *   marcados del paso anterior.
     * Fase 2 (preorden): aplica el agua pendiente, actualiza aguaActual
     *   y marca los nodos que llegaron a capacidad para el próximo paso.
     */
    public void avanzarTiempo() {
        // Limpiar agua pendiente
        limpiarPendiente(raiz);

        // Fase 1: calcular y acumular flujos
        fase1(raiz);

        // Fase 2: aplicar flujos y detectar llenos
        fase2(raiz);

        tiempoActual++;
    }

    private void limpiarPendiente(Tanque t) {
        if (t == null) return;
        t.setAguaPendiente(0);
        limpiarPendiente(t.izq);
        limpiarPendiente(t.der);
    }

    private void fase1(Tanque t) {
        if (t == null) return;

        if (t.isMarcarVaciado()) {
            // Este nodo se vacía en este paso; no envía agua
            t.setAguaActual(0);
            t.setMarcarVaciado(false);
            t.registrarVaciado();
        } else {
            int numHijos = (t.izq != null ? 1 : 0) + (t.der != null ? 1 : 0);
            if (numHijos > 0) {
                double disponible = Math.min(t.getAguaActual(), CAUDAL_MAXIMO);
                double porHijo    = disponible / numHijos;

                if (porHijo > 0) {
                    double enviado = porHijo * numHijos;
                    t.setAguaActual(t.getAguaActual() - enviado);
                    t.agregarLitrosEnviados(enviado);

                    if (t.izq != null) t.izq.setAguaPendiente(t.izq.getAguaPendiente() + porHijo);
                    if (t.der != null) t.der.setAguaPendiente(t.der.getAguaPendiente() + porHijo);
                }
            }
        }

        fase1(t.izq);
        fase1(t.der);
    }

    private void fase2(Tanque t) {
        if (t == null) return;

        double nueva = t.getAguaActual() + t.getAguaPendiente();
        if (nueva >= t.getCapacidadMaxima()) {
            nueva = t.getCapacidadMaxima();
            t.setMarcarVaciado(true);
        }
        t.setAguaActual(nueva);

        fase2(t.izq);
        fase2(t.der);
    }

    /** Ejecuta la simulación durante N pasos. */
    public void ejecutarSimulacion(int pasos) {
        for (int i = 0; i < pasos; i++) {
            avanzarTiempo();
        }
    }

    // =========================================================================
    //  Consultas
    // =========================================================================

    public int    getTiempoActual()                  { return tiempoActual; }
    public double getAguaEnNodo(String nombre)       { return get(nombre).getAguaActual(); }
    public int    getVecesVaciado(String nombre)     { return get(nombre).getVecesVaciado(); }
    public double getLitrosEnviados(String nombre)   { return get(nombre).getLitrosEnviados(); }

    private Tanque get(String nombre) {
        Tanque t = indice.get(nombre);
        if (t == null) throw new IllegalArgumentException("Nodo no encontrado: " + nombre);
        return t;
    }

    /** Muestra el estado actual de toda la red (preorden). */
    public void mostrarEstado() {
        System.out.println("=== Estado en t=" + tiempoActual + " ===");
        mostrarRec(raiz, 0);
    }

    private void mostrarRec(Tanque t, int prof) {
        if (t == null) return;
        String ind = "  ".repeat(prof);
        System.out.printf("%s%s  (vaciados: %d, enviados: %.1f)%n",
            ind, t, t.getVecesVaciado(), t.getLitrosEnviados());
        mostrarRec(t.izq, prof + 1);
        mostrarRec(t.der, prof + 1);
    }
}
