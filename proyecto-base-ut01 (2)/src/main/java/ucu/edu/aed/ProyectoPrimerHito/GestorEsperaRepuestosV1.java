package ucu.edu.aed.ProyectoPrimerHito;

import ucu.edu.aed.tda.TDALista;

import java.util.Objects;

/**
 * Version "directa" de {@link IGestorEsperaRepuestos}: guarda los
 * vehiculos en espera de repuesto en una {@link TDALista} y, para
 * ubicar uno por patente, la recorre secuencialmente.
 *
 * <p>Complejidad de {@link #quitarPorPatente(String)}: O(n). No hay
 * estructura auxiliar de por medio — es el caso base contra el que se
 * compara {@code GestorEsperaRepuestosV2} (indice patente → nodo,
 * pensado para ubicar en O(1)) en el analisis del Desafio 3.</p>
 *
 * <p>La lista concreta que hay detras de {@code espera} (doblemente
 * enlazada, en el modelo de diseno) se recibe por inyeccion en el
 * constructor a proposito: esta clase le alcanza con conocer el TDA, no
 * la implementacion elegida por el grupo.</p>
 */
public class GestorEsperaRepuestosV1 implements IGestorEsperaRepuestos {

    private final TDALista<Vehiculo> espera;

    public GestorEsperaRepuestosV1(TDALista<Vehiculo> espera) {
        this.espera = Objects.requireNonNull(espera, "La lista de espera no puede ser nula.");
    }

    /** Complejidad: la que tenga {@code agregar} en la {@link TDALista} inyectada. */
    @Override
    public void agregar(Vehiculo vehiculo) {
        Objects.requireNonNull(vehiculo, "El vehiculo no puede ser nulo.");
        espera.agregar(vehiculo);
    }

    /**
     * Recorre {@code espera} buscando el vehiculo con esa patente
     * ({@link TDALista#buscar}) y, si lo encuentra, lo remueve
     * ({@link TDALista#remover(Object)} — {@link Vehiculo#equals} ya
     * compara por patente, asi que remueve el vehiculo correcto).
     *
     * <p>Complejidad: O(n) — dos recorridos secuenciales de la lista
     * (uno para ubicarlo, otro para removerlo), no uno anidado dentro del
     * otro.</p>
     */
    @Override
    public Vehiculo quitarPorPatente(String patente) {
        Objects.requireNonNull(patente, "La patente no puede ser nula.");
        Vehiculo encontrado = espera.buscar(v -> patente.equals(v.getPatente()));
        if (encontrado != null) {
            espera.remover(encontrado);
        }
        return encontrado;
    }

    /**
     * Expone la lista de espera para consultas de solo lectura. Ojo: como
     * {@code TDALista} no ofrece una vista inmutable, quien reciba esta
     * referencia podria modificarla "por afuera" — misma salvedad que en
     * {@link Vehiculo#getHistorialTrabajos()}.
     */
    @Override
    public TDALista<Vehiculo> listar() {
        return espera;
    }
}
