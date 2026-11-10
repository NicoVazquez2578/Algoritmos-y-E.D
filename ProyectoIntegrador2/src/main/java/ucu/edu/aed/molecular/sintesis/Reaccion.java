package ucu.edu.aed.molecular.sintesis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/*
  Reacción química: transforma uno o más reactivos en uno o más productos.
  Es un vértice del grafo de reacciones (aristas reactivo → reacción → producto).
 
  costo: costo monetario del paso (reactivos, materiales, energía), en USD por lote. ≥ 0.
  riesgo: nivel de riesgo del paso en escala 0 a 10 (toxicidad, inflamabilidad, temperatura).
 */
public class Reaccion implements NodoSintesis {

    public static final double RIESGO_MAXIMO = 10.0;

    private final String id;
    private final String descripcion;
    private final List<Compuesto> reactivos;
    private final List<Compuesto> productos;
    private final double costo;
    private final double riesgo;

    public Reaccion(String id, String descripcion, List<Compuesto> reactivos, List<Compuesto> productos,
                    double costo, double riesgo) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("La reacción necesita un id");
        if (reactivos == null || reactivos.isEmpty()) throw new IllegalArgumentException("La reacción necesita al menos un reactivo");
        if (productos == null || productos.isEmpty()) throw new IllegalArgumentException("La reacción necesita al menos un producto");
        if (costo < 0) throw new IllegalArgumentException("El costo no puede ser negativo");
        if (riesgo < 0 || riesgo > RIESGO_MAXIMO) throw new IllegalArgumentException("El riesgo debe estar entre 0 y " + RIESGO_MAXIMO);
        for (Compuesto p : productos) {
            if (reactivos.contains(p)) throw new IllegalArgumentException(p + " no puede ser reactivo y producto a la vez");
        }
        this.id = id;
        this.descripcion = descripcion == null ? "" : descripcion;
        this.reactivos = new ArrayList<>(reactivos);
        this.productos = new ArrayList<>(productos);
        this.costo = costo;
        this.riesgo = riesgo;
    }

    @Override public String getId() { return id; }
    @Override public boolean esReaccion() { return true; }

    public String getDescripcion() { return descripcion; }
    public List<Compuesto> getReactivos() { return Collections.unmodifiableList(reactivos); }
    public List<Compuesto> getProductos() { return Collections.unmodifiableList(productos); }
    public double getCosto() { return costo; }
    public double getRiesgo() { return riesgo; }

    public boolean produce(Compuesto c) { return productos.contains(c); }
    public boolean consume(Compuesto c) { return reactivos.contains(c); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reaccion)) return false;
        return id.equals(((Reaccion) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return id + ": " + reactivos + " → " + productos + " (costo=" + costo + ", riesgo=" + riesgo + ")";
    }
}
