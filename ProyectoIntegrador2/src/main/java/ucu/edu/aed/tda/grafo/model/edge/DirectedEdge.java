package ucu.edu.aed.tda.grafo.model.edge;

import java.util.Objects;

public class DirectedEdge<V, D> implements Edge<V, D> {
    private final V source, target;
    private final D dato;

    public DirectedEdge(V source, V target, D dato) {
        this.source = Objects.requireNonNull(source);
        this.target = Objects.requireNonNull(target);
        this.dato = dato;
    }

    public V source()      { return source; }
    public V target()      { return target; }
    public D dato()        { return dato; }
    public boolean directed() { return true; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectedEdge)) return false;
        DirectedEdge<?, ?> e = (DirectedEdge<?, ?>) o;
        return Objects.equals(source, e.source) && Objects.equals(target, e.target);
    }

    @Override
    public int hashCode() {
        return 31 * (31 + Objects.hashCode(source)) + Objects.hashCode(target);
    }

    @Override
    public String toString() { return source + " -" + dato + "-> " + target; }
}
