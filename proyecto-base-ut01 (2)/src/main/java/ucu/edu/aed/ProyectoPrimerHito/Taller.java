package ucu.edu.aed.ProyectoPrimerHito;

import ucu.edu.aed.tda.TDACola;
import ucu.edu.aed.tda.TDAColaPrioridad;
import ucu.edu.aed.tda.TDALista;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Coordina la gestión del taller mecánico: ingreso de vehículos, atención
 * por parte de los talleristas, espera de repuestos y entrega final.
 *
 * <p>Todas las estructuras se reciben por inyección en el constructor
 * (mismo criterio que se usó en {@link Vehiculo}): esta clase conoce los
 * TDA, no las implementaciones concretas que el grupo elija por detrás.</p>
 *
 * <h2>Decisiones de diseño a revisar con el equipo</h2>
 * <ul>
 *   <li><b>Urgencia:</b> todo vehículo registrado entra a {@code colaEspera}
 *       (orden de llegada). Si su {@code nivelUrgencia} supera
 *       {@link #UMBRAL_URGENCIA}, además entra a {@code colaUrgencias}.
 *       Al atender, se prioriza lo urgente por sobre el orden de llegada.
 *       Ajustar el umbral o el criterio si no representa lo que quieren
 *       modelar.</li>
 *   <li><b>tiempoPromedioEspera():</b> como {@code Vehiculo} no guarda una
 *       fecha de entrega, se aproxima usando {@code fechaIngreso} hasta
 *       "hoy" para los vehículos ya entregados. Para un cálculo exacto
 *       convendría agregar un campo {@code fechaEntrega} a {@code Vehiculo}
 *       y setearlo en {@link #finalizarVehiculo(String)}.</li>
 * </ul>
 */
public class Taller {

    /** A partir de qué nivel de urgencia un vehículo se considera prioritario. */
    private static final int UMBRAL_URGENCIA = 8;

    private final TDACola<Vehiculo> colaEspera;
    private final IGestorEsperaRepuestos gestorEsperaRepuestos;
    private final TDALista<Tallerista> talleristas;
    private final TDALista<Vehiculo> historialEntregados;
    private final TDAColaPrioridad<Vehiculo> colaUrgencias;

    public Taller(TDACola<Vehiculo> colaEspera,
                  IGestorEsperaRepuestos gestorEsperaRepuestos,
                  TDALista<Tallerista> talleristas,
                  TDALista<Vehiculo> historialEntregados,
                  TDAColaPrioridad<Vehiculo> colaUrgencias) {
        this.colaEspera = Objects.requireNonNull(colaEspera);
        this.gestorEsperaRepuestos = Objects.requireNonNull(gestorEsperaRepuestos);
        this.talleristas = Objects.requireNonNull(talleristas);
        this.historialEntregados = Objects.requireNonNull(historialEntregados);
        this.colaUrgencias = Objects.requireNonNull(colaUrgencias);
    }

    /**
     * Registra el ingreso de un vehículo al taller.
     *
     * <p>Complejidad: O(1) para {@code poneEnCola}; O(log n) o O(n) para
     * {@code encolar} en la cola de prioridad si aplica, según cómo la
     * implementen (heap vs. lista ordenada).</p>
     */
    public void registrarVehiculo(Vehiculo vehiculo) {
        Objects.requireNonNull(vehiculo, "El vehículo no puede ser nulo.");
        colaEspera.poneEnCola(vehiculo);
        if (vehiculo.getNivelUrgencia() >= UMBRAL_URGENCIA) {
            // Convención: a menor valor numérico, mayor prioridad (según
            // el javadoc de TDAColaPrioridad), por eso invertimos el signo.
            colaUrgencias.encolar(vehiculo, -vehiculo.getNivelUrgencia());
        }
    }

    /**
     * Asigna el próximo vehículo a atender a un tallerista disponible,
     * priorizando urgencias por sobre el orden de llegada.
     *
     * <p>Complejidad: O(n) por el {@code buscar} de un tallerista libre
     * (recorre la lista de talleristas); O(n) por el {@code remover(T)}
     * sobre {@code colaEspera} cuando la próxima atención viene de
     * {@code colaUrgencias} (hay que ubicar ese vehículo dentro de la
     * cola normal para sacarlo también de ahí).</p>
     *
     * @throws IllegalStateException si no hay talleristas disponibles
     * @throws NoSuchElementException si no hay vehículos esperando
     */
    public Tallerista atenderProximoVehiculo() {
        Tallerista libre = talleristas.buscar(Tallerista::estaDisponible);
        if (libre == null) {
            throw new IllegalStateException("No hay talleristas disponibles en este momento.");
        }

        Vehiculo siguiente;
        if (!colaUrgencias.esVacio()) {
            siguiente = colaUrgencias.desencolarMasPrioritario();
            colaEspera.remover(siguiente); // TDACola extiende TDALista -> remover por valor
        } else {
            if (colaEspera.esVacio()) {
                throw new NoSuchElementException("No hay vehículos esperando ser atendidos.");
            }
            siguiente = colaEspera.quitaDeCola();
        }

        libre.asignar(siguiente);
        siguiente.cambiarEstado(EstadoVehiculo.EN_REPARACION);
        return libre;
    }

    /**
     * Registra una falla adicional detectada durante la inspección de un
     * vehículo que actualmente está siendo atendido, en espera o en cola.
     *
     * <p>Complejidad: O(n) para ubicar el vehículo (recorre hasta 3
     * estructuras); O(1) para apilar la tarea ({@link Vehiculo#agregarTareaPendiente}).</p>
     */
    public void registrarFallaAdicional(String patente, Tarea tarea) {
        Vehiculo vehiculo = buscarVehiculoActivo(patente);
        if (vehiculo == null) {
            throw new NoSuchElementException("No se encontró un vehículo activo con patente " + patente);
        }
        vehiculo.agregarTareaPendiente(tarea);
    }

    /**
     * Pausa la reparación de un vehículo por falta de repuesto, liberando
     * al tallerista que lo atendía para que pueda tomar otro trabajo.
     *
     * <p>Complejidad: O(n) para ubicar al tallerista que lo atiende.</p>
     */
    public void marcarEsperaRepuesto(String patente) {
        Tallerista tallerista = buscarTalleristaQueAtiende(patente);
        if (tallerista == null) {
            throw new NoSuchElementException("Ningún tallerista está atendiendo el vehículo " + patente);
        }
        Vehiculo vehiculo = tallerista.getVehiculoActual();
        tallerista.liberar();
        vehiculo.cambiarEstado(EstadoVehiculo.ESPERANDO_REPUESTO);
        gestorEsperaRepuestos.agregar(vehiculo);
    }

    /**
     * Marca que llegó el repuesto de un vehículo, reincorporándolo a la
     * cola correspondiente para que un tallerista continúe el trabajo.
     *
     * <p>Complejidad: la del {@link IGestorEsperaRepuestos} inyectado
     * (O(n) con {@code GestorEsperaRepuestosV1}).</p>
     */
    public void repuestoDisponible(String patente) {
        Vehiculo vehiculo = gestorEsperaRepuestos.quitarPorPatente(patente);
        vehiculo.cambiarEstado(EstadoVehiculo.EN_ESPERA);
        if (vehiculo.getNivelUrgencia() >= UMBRAL_URGENCIA) {
            colaUrgencias.encolar(vehiculo, -vehiculo.getNivelUrgencia());
        } else {
            colaEspera.poneEnCola(vehiculo);
        }
    }

    /**
     * Cierra el trabajo sobre un vehículo: libera al tallerista, lo marca
     * como entregado y lo mueve al historial.
     *
     * <p>Complejidad: O(n) para ubicar al tallerista; O(1) amortizado
     * para agregar al historial (según implementación de TDALista).</p>
     */
    public void finalizarVehiculo(String patente) {
        Tallerista tallerista = buscarTalleristaQueAtiende(patente);
        if (tallerista == null) {
            throw new NoSuchElementException("Ningún tallerista está atendiendo el vehículo " + patente);
        }
        Vehiculo vehiculo = tallerista.getVehiculoActual();
        tallerista.liberar();
        vehiculo.cambiarEstado(EstadoVehiculo.ENTREGADO);
        historialEntregados.agregar(vehiculo);
    }

    /**
     * Calcula el tiempo promedio (en días) entre el ingreso y "hoy" para
     * los vehículos ya entregados. Ver nota de diseño en el javadoc de
     * la clase sobre la aproximación usada.
     *
     * <p>Complejidad: O(n), recorre todo el historial.</p>
     */
    public double tiempoPromedioEspera() {
        if (historialEntregados.esVacio()) {
            return 0.0;
        }
        long sumaDias = 0;
        int cantidad = historialEntregados.tamaño();
        for (int i = 0; i < cantidad; i++) {
            Vehiculo v = historialEntregados.obtener(i);
            sumaDias += ChronoUnit.DAYS.between(v.getFechaIngreso(), LocalDate.now());
        }
        return (double) sumaDias / cantidad;
    }

    /**
     * Retorna hasta {@code n} vehículos (entre los que están en cola y los
     * que esperan repuesto) ordenados de mayor a menor nivel de urgencia.
     *
     * <p>Complejidad: O(m log m), siendo {@code m} la cantidad total de
     * vehículos considerados, dominada por {@code ordenar}.</p>
     */
    public TDALista<Vehiculo> vehiculosMasUrgentes(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n no puede ser negativo.");
        }

        Comparator<Vehiculo> porUrgenciaDesc =
                Comparator.comparingInt(Vehiculo::getNivelUrgencia).reversed();

        // Partimos de colaEspera.ordenar(...): nos devuelve una nueva
        // TDALista ya del tipo concreto que use el grupo, sin que esta
        // clase necesite instanciar esa implementación directamente.
        TDALista<Vehiculo> resultado = colaEspera.ordenar(porUrgenciaDesc);

        // Sumamos también los que están esperando repuesto: siguen
        // "dentro" del taller aunque no estén en colaEspera.
        TDALista<Vehiculo> enEsperaRepuesto = gestorEsperaRepuestos.listar();
        for (int i = 0; i < enEsperaRepuesto.tamaño(); i++) {
            resultado.agregar(enEsperaRepuesto.obtener(i));
        }
        resultado = resultado.ordenar(porUrgenciaDesc);

        while (resultado.tamaño() > n) {
            resultado.remover(resultado.tamaño() - 1);
        }
        return resultado;
    }

    // ---- Helpers privados de búsqueda ----

    private Vehiculo buscarVehiculoActivo(String patente) {
        Vehiculo enCola = colaEspera.buscar(v -> v.getPatente().equals(patente));
        if (enCola != null) {
            return enCola;
        }

        Vehiculo enEsperaRepuesto = gestorEsperaRepuestos.listar()
                .buscar(v -> v.getPatente().equals(patente));
        if (enEsperaRepuesto != null) {
            return enEsperaRepuesto;
        }

        Tallerista tallerista = buscarTalleristaQueAtiende(patente);
        return (tallerista != null) ? tallerista.getVehiculoActual() : null;
    }

    private Tallerista buscarTalleristaQueAtiende(String patente) {
        return talleristas.buscar(t -> t.getVehiculoActual() != null
                && t.getVehiculoActual().getPatente().equals(patente));
    }
}