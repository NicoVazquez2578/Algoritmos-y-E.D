package ucu.edu.aed.ProyectoSegundoHito;

import java.util.Objects;
import ucu.edu.aed.ProyectoPrimerHito.IGestorEsperaRepuestos;
import ucu.edu.aed.ProyectoPrimerHito.Vehiculo;
import ucu.edu.aed.impl.Lista;
import ucu.edu.aed.tda.Arboles.TDAArbolBinario;
import ucu.edu.aed.tda.Arboles.Impl.ArbolBinarioBusqueda; // Usa la clase de tu compañero
import ucu.edu.aed.tda.TDALista;

/**
 * Gestor de espera de repuestos optimizado con un Árbol Binario de Búsqueda (ABB).
 * Indexa los vehículos por su patente (aprovechando Comparable en Vehiculo).
 */
public class GestorEsperaRepuestosABB implements IGestorEsperaRepuestos {

    private final TDAArbolBinario<Vehiculo> arbol;

    public GestorEsperaRepuestosABB() {
        this(new ArbolBinarioBusqueda<>());
    }

    public GestorEsperaRepuestosABB(TDAArbolBinario<Vehiculo> arbol) {
        this.arbol = Objects.requireNonNull(arbol, "El árbol no puede ser nulo.");
    }

    @Override
    public void agregar(Vehiculo vehiculo) {
        Objects.requireNonNull(vehiculo, "El vehículo no puede ser nulo.");
        arbol.insertar(vehiculo);
    }

    @Override
    public Vehiculo buscarPorPatente(String patente) {
        Objects.requireNonNull(patente, "La patente no puede ser nula.");
        return arbol.buscar(v -> patente.compareTo(v.getPatente()));
    }

    @Override
    public Vehiculo quitarPorPatente(String patente) {
        Objects.requireNonNull(patente, "La patente no puede ser nula.");
        Vehiculo encontrado = buscarPorPatente(patente);
        if (encontrado != null) {
            arbol.eliminar(v -> patente.compareTo(v.getPatente()));
        }
        return encontrado;
    }

    @Override
    public TDALista<Vehiculo> listar() {
        TDALista<Vehiculo> lista = new Lista<>();
        arbol.inOrder(lista::agregar);
        return lista;
    }

    public int cantidadVehiculos() {
        return arbol.cantidadNodos();
    }
}