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

    // Busca el vehiculo por patente y lo remueve. Vehiculo.equals() ya
    // compara por patente, asi que remover(encontrado) saca el correcto.
    @Override
    public Vehiculo quitarPorPatente(String patente) {
        Objects.requireNonNull(patente, "La patente no puede ser nula.");
        Vehiculo encontrado = espera.buscar(v -> patente.equals(v.getPatente()));
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