package ucu.edu.aed.molecular.sintesis;

import java.util.Objects;

import ucu.edu.aed.molecular.molecula.Molecula;

/*
  Compuesto químico registrado en el laboratorio.
  Une las dos vistas del sistema:
   - estructural: su Molecula (grafo no dirigido de átomos), y
   - de síntesis: es un vértice del grafo de reacciones (implementa NodoSintesis).
 
  'disponible' indica si el laboratorio ya lo tiene en stock (compuesto o elemento base),
  es decir, si puede ser punto de partida de una ruta de síntesis.
 
  Identidad: el nombre. Dos compuestos con el mismo nombre son el mismo vértice.
 */
public class Compuesto implements NodoSintesis {

    private final String nombre;
    private final Molecula molecula;
    private DatosCientificos datos;
    private boolean disponible;

    public Compuesto(String nombre, Molecula molecula, DatosCientificos datos, boolean disponible) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El compuesto necesita un nombre");
        this.nombre = nombre;
        this.molecula = molecula;          // puede ser null si solo interesa en la red de reacciones
        this.datos = datos == null ? DatosCientificos.desconocidos() : datos;
        this.disponible = disponible;
    }

    public Compuesto(String nombre, Molecula molecula, DatosCientificos datos) {
        this(nombre, molecula, datos, false);
    }

    @Override public String getId() { return nombre; }
    @Override public boolean esReaccion() { return false; }

    public String getNombre() { return nombre; }
    public Molecula getMolecula() { return molecula; }
    public boolean tieneEstructura() { return molecula != null; }
    public DatosCientificos getDatos() { return datos; }
    public void setDatos(DatosCientificos datos) { this.datos = Objects.requireNonNull(datos); }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    /** Fórmula y masa salen de la molécula; si no hay estructura cargada, no se pueden calcular. */
    public String formula() { return molecula == null ? "?" : molecula.formulaMolecular(); }
    public double masaMolar() { return molecula == null ? Double.NaN : molecula.masaMolar(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Compuesto)) return false;
        return nombre.equalsIgnoreCase(((Compuesto) o).nombre);
    }

    @Override
    public int hashCode() { return nombre.toLowerCase().hashCode(); }

    @Override
    public String toString() { return nombre; }
}
