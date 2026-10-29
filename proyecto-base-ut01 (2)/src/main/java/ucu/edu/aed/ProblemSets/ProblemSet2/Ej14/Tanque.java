package ucu.edu.aed.ProblemSets.ProblemSet2.Ej14;

/**
 * Nodo del árbol binario que representa un tanque o estación de distribución
 * en la red de agua.
 */
public class Tanque {
    private final String nombre;
    private final int capacidadMaxima;

    private double aguaActual;
    private double aguaPendiente;   // agua que llegará al final del paso actual
    private boolean marcarVaciado;  // si llegó a capacidad máxima, vaciar en el siguiente paso

    private int    vecesVaciado;
    private double litrosEnviados;

    Tanque izq;
    Tanque der;

    public Tanque(String nombre, int capacidadMaxima, double aguaInicial) {
        this.nombre          = nombre;
        this.capacidadMaxima = capacidadMaxima;
        this.aguaActual      = Math.min(aguaInicial, capacidadMaxima);
        this.aguaPendiente   = 0;
        this.marcarVaciado   = false;
        this.vecesVaciado    = 0;
        this.litrosEnviados  = 0;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public String getNombre()         { return nombre; }
    public int    getCapacidadMaxima(){ return capacidadMaxima; }
    public double getAguaActual()     { return aguaActual; }
    public int    getVecesVaciado()   { return vecesVaciado; }
    public double getLitrosEnviados() { return litrosEnviados; }

    // ── Paquete interno ──────────────────────────────────────────────────────

    void setAguaActual(double v)     { aguaActual    = v; }
    void setAguaPendiente(double v)  { aguaPendiente = v; }
    void setMarcarVaciado(boolean v) { marcarVaciado = v; }
    boolean isMarcarVaciado()        { return marcarVaciado; }
    double  getAguaPendiente()       { return aguaPendiente; }

    void registrarVaciado()          { vecesVaciado++; }
    void agregarLitrosEnviados(double d) { litrosEnviados += d; }

    @Override
    public String toString() {
        return String.format("%s [%.1f/%d]", nombre, aguaActual, capacidadMaxima);
    }
}
