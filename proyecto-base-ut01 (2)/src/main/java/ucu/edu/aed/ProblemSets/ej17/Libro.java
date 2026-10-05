package ucu.edu.aed.ProblemSets.ej17;

/**
 * Representa un libro del catálogo de la biblioteca.
 * La igualdad entre libros se determina por código (ISBN o interno).
 */
public class Libro {

    private final String codigo;
    private final String titulo;
    private double precioReposicion;
    private int ejemplares;

    public Libro(String codigo, String titulo, double precioReposicion, int ejemplares) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.precioReposicion = precioReposicion;
        this.ejemplares = ejemplares;
    }

    public String getCodigo() { return codigo; }
    public String getTitulo() { return titulo; }
    public double getPrecioReposicion() { return precioReposicion; }
    public int getEjemplares() { return ejemplares; }

    public void agregarEjemplares(int cantidad) {
        ejemplares += cantidad;
    }

    // Presta hasta donde alcanza el stock; devuelve la cantidad efectivamente prestada
    public int prestar(int cantidad) {
        int prestado = Math.min(cantidad, ejemplares);
        ejemplares -= prestado;
        return prestado;
    }

    public void devolver(int cantidad) {
        ejemplares += cantidad;
    }

    // Dos libros son iguales si tienen el mismo código
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Libro)) return false;
        return codigo.equals(((Libro) obj).codigo);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | Ejemplares: %d | Precio: $%.2f",
                codigo, titulo, ejemplares, precioReposicion);
    }
}
