package ucu.edu.aed.ProyectoSegundoHito;

import java.util.Objects;
import ucu.edu.aed.ProyectoPrimerHito.IGestorEsperaRepuestos;
import ucu.edu.aed.ProyectoPrimerHito.Vehiculo;
import ucu.edu.aed.tda.TDALista;

/**
 * Gestor de espera de repuestos optimizado con un Árbol Binario de Búsqueda (ABB).
 * Indexa los vehículos por su patente para buscar en O(log n) en vez de O(n).
 */
public class GestorEsperaRepuestosABB implements IGestorEsperaRepuestos {

    private final ArbolBinarioBusqueda<String, Vehiculo> arbol;

    public GestorEsperaRepuestosABB() {
        this.arbol = new ArbolBinarioBusqueda<>();
    }

    // Costo: O(log n) promedio. Inserta el vehículo en el árbol usando su patente.
    @Override
    public void agregar(Vehiculo vehiculo) {
        Objects.requireNonNull(vehiculo, "El vehículo no puede ser nulo.");
        arbol.insertar(vehiculo.getPatente(), vehiculo);
    }

    // Costo: O(log n) promedio. Busca por patente dividiendo el árbol a la mitad en cada paso.
    @Override
    public Vehiculo buscarPorPatente(String patente) {
        Objects.requireNonNull(patente, "La patente no puede ser nula.");
        return arbol.buscar(patente);
    }

    // Costo: O(log n) promedio. Busca y saca el vehículo del árbol.
    @Override
    public Vehiculo quitarPorPatente(String patente) {
        Objects.requireNonNull(patente, "La patente no puede ser nula.");
        return arbol.eliminar(patente);
    }

    // Costo: O(n). Recorre el árbol in-order y devuelve la lista de vehículos ordenados.
    @Override
    public TDALista<Vehiculo> listar() {
        return arbol.listar();
    }

    public int cantidadVehiculos() {
        return arbol.tamaño();
    }
}
