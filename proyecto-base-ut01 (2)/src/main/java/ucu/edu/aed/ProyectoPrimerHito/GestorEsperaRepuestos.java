package ucu.edu.aed.ProyectoPrimerHito;

import java.util.Objects;

import ucu.edu.aed.tda.TDALista;

/*
  Version simple de IGestorEsperaRepuestos: busca por patente recorriendo
  la lista de principio a fin. Complejidad O(n).
 */
public class GestorEsperaRepuestos implements IGestorEsperaRepuestos {

    private final TDALista<Vehiculo> espera;

    public GestorEsperaRepuestos(TDALista<Vehiculo> espera) {
        this.espera = Objects.requireNonNull(espera, "La lista de espera no puede ser nula.");
    }

    @Override
    public void agregar(Vehiculo vehiculo) {
        Objects.requireNonNull(vehiculo, "El vehiculo no puede ser nulo.");
        espera.agregar(vehiculo);
    }

    // Costo: O(n). Recorre la lista elemento por elemento desde el inicio.
    // En el mejor caso es O(1) si está primero, pero en el peor/promedio
    // debe revisar hasta el final (n comparaciones).
    @Override
    public Vehiculo buscarPorPatente(String patente) {
        Objects.requireNonNull(patente, "La patente no puede ser nula.");
        return espera.buscar(v -> patente.equals(v.getPatente()));
    }

    // Costo: O(n). Busca el vehículo en la lista y luego lo remueve.
    @Override
    public Vehiculo quitarPorPatente(String patente) {
        Objects.requireNonNull(patente, "La patente no puede ser nula.");
        Vehiculo encontrado = buscarPorPatente(patente);
        if (encontrado != null) {
            espera.remover(encontrado);
        }
        return encontrado;
    }

    @Override
    public TDALista<Vehiculo> listar() {
        return espera;
    }
}