package ucu.edu.aed.ProyectoPrimerHito;

import ucu.edu.aed.tda.TDALista;

/**
 * Contrato para administrar el conjunto de {@link Vehiculo} que quedaron
 * varados esperando un repuesto.
 *
 * <p>A diferencia de la cola de espera general del taller (FIFO puro), acá
 * hace falta poder sacar un vehiculo especifico de en medio de la
 * estructura cuando llega <em>su</em> repuesto — sin importar si es el
 * primero que entro o no. Por eso la operacion central no es "sacar el
 * primero" sino {@link #quitarPorPatente(String)}.</p>
 *
 * <p>Existe como interfaz (y no como una unica clase) porque el
 * Desafio 3 pide comparar dos formas de resolver esa busqueda por
 * patente: {@code GestorEsperaRepuestosV1} la busca recorriendo
 * secuencialmente — O(n) — y una eventual {@code GestorEsperaRepuestosV2}
 * mantendria ademas un indice auxiliar patente → nodo para ubicarla en
 * O(1). El {@code Taller} solo deberia conocer esta interfaz, nunca la
 * implementacion concreta, para poder intercambiarlas sin tocar el resto
 * del codigo.</p>
 */
public interface IGestorEsperaRepuestos {

    /**
     * Agrega {@code vehiculo} al conjunto de vehiculos esperando repuesto.
     *
     * @param vehiculo vehiculo a agregar; no puede ser {@code null}
     */
    void agregar(Vehiculo vehiculo);

    /**
     * Ubica el vehiculo cuya patente coincide con {@code patente}, lo saca
     * de la espera y lo devuelve.
     *
     * @param patente patente a buscar; no puede ser {@code null}
     * @return el vehiculo encontrado y removido, o {@code null} si ningun
     *         vehiculo en espera tiene esa patente
     */
    Vehiculo quitarPorPatente(String patente);

    /**
     * Expone los vehiculos actualmente en espera de repuesto.
     *
     * @return la lista de vehiculos en espera
     */
    TDALista<Vehiculo> listar();
}
