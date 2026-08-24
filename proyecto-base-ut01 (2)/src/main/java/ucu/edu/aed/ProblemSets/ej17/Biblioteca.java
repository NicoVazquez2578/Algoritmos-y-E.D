package ucu.edu.aed.ProblemSets.ej17;

import java.util.Comparator;

import ucu.edu.aed.impl.ListaArray;
import ucu.edu.aed.tda.TDALista;

/**
 * Gestiona el catálogo de libros de la biblioteca.
 * Usa ListaArray como estructura interna ya que permite acceso por índice en O(1),
 * útil para listar y ordenar. La búsqueda por código es O(n), aceptable para un catálogo.
 */
public class Biblioteca {

    private final TDALista<Libro> catalogo;

    public Biblioteca() {
        catalogo = new ListaArray<>();
    }

    // 1. Incorporar un nuevo libro; si el código ya existe, suma los ejemplares
    public void incorporarLibro(Libro libro) {
        Libro existente = buscarPorCodigo(libro.getCodigo());
        if (existente != null) {
            existente.agregarEjemplares(libro.getEjemplares());
        } else {
            catalogo.agregar(libro);
        }
    }

    // 2. Agregar ejemplares a un libro ya registrado
    public boolean agregarEjemplares(String codigo, int cantidad) {
        Libro libro = buscarPorCodigo(codigo);
        if (libro == null) return false;
        libro.agregarEjemplares(cantidad);
        return true;
    }

    // 3. Registrar préstamo o devolución; devuelve la cantidad efectivamente procesada
    public int registrarMovimiento(String codigo, String tipo, int cantidad) {
        Libro libro = buscarPorCodigo(codigo);
        if (libro == null) return 0;
        if ("PRESTAMO".equalsIgnoreCase(tipo)) return libro.prestar(cantidad);
        if ("DEVOLUCION".equalsIgnoreCase(tipo)) { libro.devolver(cantidad); return cantidad; }
        return 0;
    }

    // 4. Retirar un libro del catálogo por código
    public boolean retirarLibro(String codigo) {
        Libro libro = buscarPorCodigo(codigo);
        if (libro == null) return false;
        return catalogo.remover(libro);
    }

    // 5. Consultar existencias por código; devuelve -1 si no existe
    public int consultarExistencias(String codigo) {
        Libro libro = buscarPorCodigo(codigo);
        return libro != null ? libro.getEjemplares() : -1;
    }

    // 6. Listar todos los libros ordenados alfabéticamente por título
    public TDALista<Libro> listarPorTitulo() {
        return catalogo.ordenar(Comparator.comparing(Libro::getTitulo));
    }

    // Búsqueda interna por código; devuelve null si no se encuentra
    public Libro buscarPorCodigo(String codigo) {
        return catalogo.buscar(l -> l.getCodigo().equals(codigo));
    }

    public int totalLibros() { return catalogo.tamaño(); }
    public boolean esVacia() { return catalogo.esVacio(); }
}
