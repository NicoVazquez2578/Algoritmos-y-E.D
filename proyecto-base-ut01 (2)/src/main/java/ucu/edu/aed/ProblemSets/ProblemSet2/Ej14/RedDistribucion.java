package ucu.edu.aed.ProblemSets.ProblemSet2.Ej14;

import java.util.HashMap;
import java.util.Map;

// Ejercicio 14 - árbol binario de Tanques; el agua fluye de la raíz hacia los hijos en pasos discretos
public class RedDistribucion {

    private static final double CAUDAL_MAXIMO = 100.0;

    private Tanque raiz;
    private int tiempoActual;
    private final Map<String, Tanque> indice = new HashMap<>();

    public void agregarNodo(String nombre, int capacidadMaxima, double aguaInicial) {
        Tanque t = new Tanque(nombre, capacidadMaxima, aguaInicial);
        indice.put(nombre, t);
        if (raiz == null) raiz = t;
    }

    public void conectar(String nombrePadre, String hijoIzq, String hijoDer) {
        Tanque padre = indice.get(nombrePadre);
        if (padre == null) throw new IllegalArgumentException("Nodo no encontrado: " + nombrePadre);
        if (hijoIzq != null) { padre.izq = indice.get(hijoIzq); if (padre.izq == null) throw new IllegalArgumentException("Nodo no encontrado: " + hijoIzq); }
        if (hijoDer != null) { padre.der = indice.get(hijoDer); if (padre.der == null) throw new IllegalArgumentException("Nodo no encontrado: " + hijoDer); }
    }

    // Dos fases para evitar que el agua recibida en un paso se propague en ese mismo paso
    public void avanzarTiempo() {
        limpiarPendiente(raiz);
        fase1(raiz);
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
            t.setAguaActual(0);
            t.setMarcarVaciado(false);
            t.registrarVaciado();
        } else {
            int numHijos = (t.izq != null ? 1 : 0) + (t.der != null ? 1 : 0);
            if (numHijos > 0) {
                double disponible = Math.min(t.getAguaActual(), CAUDAL_MAXIMO);
                double porHijo    = disponible / numHijos;
                if (porHijo > 0) {
                    t.setAguaActual(t.getAguaActual() - porHijo * numHijos);
                    t.agregarLitrosEnviados(porHijo * numHijos);
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
        if (nueva >= t.getCapacidadMaxima()) { nueva = t.getCapacidadMaxima(); t.setMarcarVaciado(true); }
        t.setAguaActual(nueva);
        fase2(t.izq);
        fase2(t.der);
    }

    public void ejecutarSimulacion(int pasos) { for (int i = 0; i < pasos; i++) avanzarTiempo(); }

    public int    getTiempoActual()                { return tiempoActual; }
    public double getAguaEnNodo(String nombre)     { return get(nombre).getAguaActual(); }
    public int    getVecesVaciado(String nombre)   { return get(nombre).getVecesVaciado(); }
    public double getLitrosEnviados(String nombre) { return get(nombre).getLitrosEnviados(); }

    private Tanque get(String nombre) {
        Tanque t = indice.get(nombre);
        if (t == null) throw new IllegalArgumentException("Nodo no encontrado: " + nombre);
        return t;
    }

    public void mostrarEstado() {
        System.out.println("=== Estado en t=" + tiempoActual + " ===");
        mostrarRec(raiz, 0);
    }

    private void mostrarRec(Tanque t, int prof) {
        if (t == null) return;
        System.out.printf("%s%s  (vaciados: %d, enviados: %.1f)%n",
            "  ".repeat(prof), t, t.getVecesVaciado(), t.getLitrosEnviados());
        mostrarRec(t.izq, prof + 1);
        mostrarRec(t.der, prof + 1);
    }
}
