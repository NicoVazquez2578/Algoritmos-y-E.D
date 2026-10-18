package ucu.edu.aed.ProblemSets.ProblemSet3.lib;

public class Medicion {
    private final String texto;
    private final long memoria;
    private final long tiempoEjecucion;

    public Medicion(String texto, long memoria, long tiempoEjecucion) {
        this.texto = texto;
        this.memoria = memoria;
        this.tiempoEjecucion = tiempoEjecucion;
    }

    public String toCSV() {
        return String.format("%s,%s,%s", texto, Formatter.formatMemory(memoria), Formatter.formatNanos(tiempoEjecucion, 2));
    }

    public void print() {
        System.out.printf("%-35s  mem=%-10s  tiempo=%s%n",
            texto, Formatter.formatMemory(memoria), Formatter.formatNanos(tiempoEjecucion, 2));
    }
}
