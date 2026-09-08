package ucu.edu.aed.ProyectoPrimerHito;

import ucu.edu.aed.impl.ArbolBinarioBusqueda;
import ucu.edu.aed.impl.ListaArray;
import ucu.edu.aed.impl.Monticulo;
import ucu.edu.aed.tda.TDACola;
import ucu.edu.aed.tda.TDAColaPrioridad;
import ucu.edu.aed.tda.TDALista;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Gestiona el taller mecánico.
 *
 * HITO 1: Cola de espera FIFO, cola de urgencias por nivel, gestor de repuestos.
 *
 * HITO 2 (extensión sin romper el Hito 1):
 *
 *   indicePorPatente: ArbolBinarioBusqueda<String, Vehiculo>
 *     Permite buscarVehiculoPorMatricula en O(log n) sin recorrer la cola.
 *     Se actualiza en registrarVehiculo.
 *
 *   indicePorFechaIngreso: ArbolBinarioBusqueda<LocalDate, ListaArray<Vehiculo>>
 *     Permite vehiculosEnRangoDeFechas en O(log n + k) sin recorrer todo.
 *     Varias patentes pueden tener la misma fecha, por eso el valor es una lista.
 *
 *   monticuloPorFechaComprometida: Monticulo<Vehiculo>
 *     Reemplaza la ColaPrioridad (array ordenado) para vehículos con fecha
 *     comprometida. La estructura min-heap extrae el más urgente en O(log n)
 *     y permite reprogramar en O(n) (buscar) + O(log n) (restaurar heap).
 *     Los vehículos sin fecha comprometida siguen en colaEspera (FIFO).
 *
 * ORDEN DE ATENCIÓN (nuevo):
 *   1. Primero el monticuloPorFechaComprometida (fecha comprometida más próxima)
 *   2. Luego colaUrgencias (urgencia por nivelUrgencia >= 8, Hito 1)
 *   3. Finalmente colaEspera (FIFO, Hito 1)
 */
public class Taller {

    private static final int UMBRAL_URGENCIA = 8;

    // ── Estructuras Hito 1 (sin cambios) ──────────────────────────────
    private final TDACola<Vehiculo> colaEspera;
    private final IGestorEsperaRepuestos gestorEsperaRepuestos;
    private final TDALista<Tallerista> talleristas;
    private final TDALista<Vehiculo> historialEntregados;
    private final TDAColaPrioridad<Vehiculo> colaUrgencias;

    // ── Estructuras Hito 2 (agregadas sin tocar el constructor) ────────
    // BST indexado por patente: O(log n) para buscar un vehículo por matrícula.
    private final ArbolBinarioBusqueda<String, Vehiculo> indicePorPatente =
            new ArbolBinarioBusqueda<>();

    // BST indexado por fecha de ingreso: O(log n + k) para rango de fechas.
    // El valor es una lista porque varias patentes pueden ingresar el mismo día.
    private final ArbolBinarioBusqueda<LocalDate, ListaArray<Vehiculo>> indicePorFechaIngreso =
            new ArbolBinarioBusqueda<>();

    // Montículo (min-heap) para vehículos con fecha de entrega comprometida.
    // La fecha más cercana sale primero (comparación natural de LocalDate).
    private final Monticulo<Vehiculo> monticuloPorFechaComprometida =
            new Monticulo<>(Comparator.comparing(Vehiculo::getFechaEntregaComprometida));

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

    public void registrarVehiculo(Vehiculo vehiculo) {
        Objects.requireNonNull(vehiculo, "El vehículo no puede ser nulo.");

        // ── Hito 1: cola FIFO y cola de urgencias ──
        colaEspera.poneEnCola(vehiculo);
        if (vehiculo.getNivelUrgencia() >= UMBRAL_URGENCIA) {
            colaUrgencias.encolar(vehiculo, -vehiculo.getNivelUrgencia());
        }

        // ── Hito 2: índices BST ────────────────────
        // Indexar por patente para búsqueda O(log n)
        indicePorPatente.insertar(vehiculo.getPatente(), vehiculo);

        // Indexar por fecha de ingreso para rangos O(log n + k).
        // Si ya existe otra patente con esa fecha, agregar a la lista existente.
        LocalDate fechaIngreso = vehiculo.getFechaIngreso();
        ListaArray<Vehiculo> mismaFecha = indicePorFechaIngreso.buscar(fechaIngreso);
        if (mismaFecha == null) {
            mismaFecha = new ListaArray<>();
            indicePorFechaIngreso.insertar(fechaIngreso, mismaFecha);
        }
        mismaFecha.agregar(vehiculo);

        // Si el cliente dio una fecha comprometida de entrega, insertar en el montículo.
        // Así el vehículo con la fecha más próxima se atiende primero.
        if (vehiculo.getFechaEntregaComprometida() != null) {
            monticuloPorFechaComprometida.insertar(vehiculo);
        }
    }

    public Tallerista atenderProximoVehiculo() {
        Tallerista libre = talleristas.buscar(Tallerista::estaDisponible);
        if (libre == null) {
            throw new IllegalStateException("No hay talleristas disponibles en este momento.");
        }

        Vehiculo siguiente;
        if (!colaUrgencias.esVacio()) {
            siguiente = colaUrgencias.desencolarMasPrioritario();
            colaEspera.remover(siguiente);
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

    public void registrarFallaAdicional(String patente, Tarea tarea) {
        Vehiculo vehiculo = buscarVehiculoActivo(patente);
        if (vehiculo == null) {
            throw new NoSuchElementException("No se encontró un vehículo activo con patente " + patente);
        }
        vehiculo.agregarTareaPendiente(tarea);
    }

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

    public void repuestoDisponible(String patente) {
        Vehiculo vehiculo = gestorEsperaRepuestos.quitarPorPatente(patente);
        if (vehiculo == null) {
            throw new NoSuchElementException("Ningún vehículo con patente " + patente + " está esperando repuesto.");
        }
        vehiculo.cambiarEstado(EstadoVehiculo.EN_ESPERA);
        if (vehiculo.getNivelUrgencia() >= UMBRAL_URGENCIA) {
            colaUrgencias.encolar(vehiculo, -vehiculo.getNivelUrgencia());
        } else {
            colaEspera.poneEnCola(vehiculo);
        }
    }

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

    public TDALista<Vehiculo> vehiculosMasUrgentes(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n no puede ser negativo.");
        }

        Comparator<Vehiculo> porUrgenciaDesc =
                Comparator.comparingInt(Vehiculo::getNivelUrgencia).reversed();

        TDALista<Vehiculo> resultado = colaEspera.ordenar(porUrgenciaDesc);

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

    // ═══════════════════════════════════════════════════════════════════
    // HITO 2 — Métodos de gestión de OrdenTrabajo (por patente)
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Registra una tarea derivada de otra dentro de la OrdenTrabajo del vehículo.
     * Se usa cuando el tallerista, al trabajar en 'origen', descubre un problema
     * nuevo ('derivada') que debe resolverse antes de poder terminar 'origen'.
     *
     * Requiere que el vehículo ya tenga una OrdenTrabajo iniciada
     * (llamando antes a vehiculo.iniciarOrden(raiz)).
     *
     * @throws NoSuchElementException   si no existe vehículo con esa patente
     * @throws IllegalStateException    si el vehículo no tiene OrdenTrabajo iniciada
     */
    public void agregarTareaDerivada(String patente, Tarea origen, Tarea derivada) {
        OrdenTrabajo orden = obtenerOrdenObligatoria(patente);
        orden.agregarTareaDerivada(origen, derivada);
    }

    /**
     * Retorna true si la tarea (y todos sus descendientes en la OrdenTrabajo)
     * están TERMINADOS o RECHAZADOS: el tallerista puede cerrar este nodo.
     *
     * @throws NoSuchElementException si no existe vehículo con esa patente
     */
    public boolean puedeTerminar(String patente, Tarea tarea) {
        OrdenTrabajo orden = obtenerOrdenObligatoria(patente);
        return orden.puedeTerminar(tarea);
    }

    /**
     * Lista las tareas pendientes del vehículo en orden postorden (hijos antes
     * que padre), que es el orden correcto para ejecutarlas: primero hay que
     * resolver los problemas derivados antes de cerrar el trabajo que los originó.
     *
     * @throws NoSuchElementException si no existe vehículo con esa patente
     */
    public TDALista<Tarea> tareasPendientesParaEntregar(String patente) {
        OrdenTrabajo orden = obtenerOrdenObligatoria(patente);
        return orden.tareasPendientesParaEntregar();
    }

    /**
     * Suspende una tarea y todo su subárbol porque falta un repuesto.
     * La suspensión es parcial: solo afecta a la rama indicada, el resto de la
     * OrdenTrabajo sigue activo para que el tallerista pueda trabajar en otra cosa.
     *
     * @throws NoSuchElementException si no existe vehículo con esa patente
     */
    public void suspenderPorRepuesto(String patente, Tarea tarea) {
        OrdenTrabajo orden = obtenerOrdenObligatoria(patente);
        orden.suspender(tarea);
    }

    /**
     * Reanuda la tarea y su subárbol cuando el repuesto ya está disponible.
     * Solo las tareas que estaban SUSPENDIDAS vuelven a PENDIENTE;
     * las TERMINADAS y RECHAZADAS no se modifican.
     *
     * @throws NoSuchElementException si no existe vehículo con esa patente
     */
    public void reanudarPorRepuesto(String patente, Tarea tarea) {
        OrdenTrabajo orden = obtenerOrdenObligatoria(patente);
        orden.reanudar(tarea);
    }

    /**
     * Costo total de TODAS las tareas de la orden de trabajo.
     * Incluye las rechazadas (el presupuesto original incluía esa rama).
     *
     * @throws NoSuchElementException si no existe vehículo con esa patente
     */
    public double costoTotal(String patente) {
        OrdenTrabajo orden = obtenerOrdenObligatoria(patente);
        return orden.costoTotal();
    }

    /**
     * Costo parcial: solo la tarea indicada y sus descendientes.
     * Permite presupuestar una rama específica (ej: todo lo del motor).
     *
     * @throws NoSuchElementException si no existe vehículo con esa patente
     */
    public double costoParcial(String patente, Tarea tarea) {
        OrdenTrabajo orden = obtenerOrdenObligatoria(patente);
        return orden.costoParcial(tarea);
    }

    /**
     * Aprueba la tarea: la pasa de PENDIENTE a EN_CURSO.
     * El cliente autorizó este trabajo; el tallerista puede proceder.
     *
     * @throws NoSuchElementException si no existe vehículo con esa patente
     */
    public void aprobarParte(String patente, Tarea tarea) {
        OrdenTrabajo orden = obtenerOrdenObligatoria(patente);
        orden.aprobar(tarea);
    }

    /**
     * Rechaza la tarea y toda su rama. El cliente decidió no hacer ese trabajo.
     * Los nodos rechazados no bloquean el cierre de su padre.
     *
     * @throws NoSuchElementException si no existe vehículo con esa patente
     */
    public void rechazarParte(String patente, Tarea tarea) {
        OrdenTrabajo orden = obtenerOrdenObligatoria(patente);
        orden.rechazar(tarea);
    }

    // ═══════════════════════════════════════════════════════════════════
    // HITO 2 — Búsquedas eficientes con los índices BST y Montículo
    // ═══════════════════════════════════════════════════════════════════

    /**
     * CONSULTA: busca un vehículo por su matrícula/patente en O(log n).
     *
     * Sin el BST sería necesario recorrer toda la cola (O(n)).
     * Con el BST, la clave es el String de la patente y el árbol nos lleva
     * directamente al nodo del vehículo sin revisar los demás.
     *
     * @return el Vehículo, o null si no existe en el sistema
     */
    public Vehiculo buscarVehiculoPorMatricula(String patente) {
        return indicePorPatente.buscar(patente);
    }

    /**
     * CONSULTA: lista los vehículos ingresados en un rango de fechas [desde, hasta].
     *
     * Usa el recorrido rango del BST (O(log n + k)) en lugar de revisar todos
     * los vehículos (O(n)): el árbol poda ramas enteras cuyas fechas quedan
     * fuera del rango antes de siquiera visitarlas.
     *
     * @return lista de vehículos con fechaIngreso en [desde, hasta]
     */
    public TDALista<Vehiculo> vehiculosEnRangoDeFechas(LocalDate desde, LocalDate hasta) {
        // El BST por fecha devuelve listas de vehículos agrupados por fecha.
        // Aplanamos todas esas listas en una sola lista de resultado.
        TDALista<ListaArray<Vehiculo>> gruposPorFecha = indicePorFechaIngreso.rango(desde, hasta);
        TDALista<Vehiculo> resultado = new ListaArray<>();
        for (int i = 0; i < gruposPorFecha.tamaño(); i++) {
            ListaArray<Vehiculo> grupo = gruposPorFecha.obtener(i);
            for (int j = 0; j < grupo.tamaño(); j++) {
                resultado.agregar(grupo.obtener(j));
            }
        }
        return resultado;
    }

    /**
     * Cambia la fecha de entrega comprometida de un vehículo y actualiza
     * su posición en el montículo de fechas.
     *
     * El montículo mantiene el vehículo con fecha más próxima al frente.
     * Al cambiar la fecha hay que: eliminar el vehículo del heap (O(n) para
     * encontrarlo, O(log n) para restaurar), actualizar el campo, y volver a
     * insertar (O(log n) para el sift-up).
     *
     * Si el vehículo no tenía fecha comprometida, se inserta por primera vez.
     *
     * @throws NoSuchElementException si no existe vehículo con esa patente
     */
    public void reprogramarVehiculo(String patente, LocalDate nuevaFecha) {
        Vehiculo vehiculo = indicePorPatente.buscar(patente);
        if (vehiculo == null) {
            throw new NoSuchElementException("No existe vehículo con patente: " + patente);
        }
        // Si ya tenía fecha comprometida, eliminarlo del heap antes de cambiarla.
        // (eliminar busca por equals, que en Vehiculo compara por patente)
        if (vehiculo.getFechaEntregaComprometida() != null) {
            monticuloPorFechaComprometida.eliminar(vehiculo);
        }
        vehiculo.setFechaEntregaComprometida(nuevaFecha);
        monticuloPorFechaComprometida.insertar(vehiculo);
    }

    // ═══════════════════════════════════════════════════════════════════
    // HITO 2 — Consultas analíticas del taller
    // ═══════════════════════════════════════════════════════════════════

    /**
     * CONSULTA 1: lista las tareas bloqueadas esperando un repuesto para el vehículo.
     *
     * En la OrdenTrabajo, cuando llega la notificación de que falta un repuesto,
     * se suspende la rama afectada. Esta consulta lista todas las tareas SUSPENDIDAS
     * de ese vehículo para que el encargado sepa qué trabajos están en pausa.
     *
     * @throws NoSuchElementException si no existe vehículo con esa patente
     */
    public TDALista<Tarea> trabajosBloqueadosPorRepuesto(String patente) {
        OrdenTrabajo orden = obtenerOrdenObligatoria(patente);
        return orden.trabajosSuspendidos();
    }

    /**
     * CONSULTA 2: encuentra el vehículo activo con más tareas pendientes (no
     * terminadas ni rechazadas en su OrdenTrabajo).
     *
     * Necesidad: ayuda al jefe de taller a priorizar cuál vehículo tiene más
     * trabajo por delante para asignar más talleristas o avisar al cliente.
     *
     * Complejidad: O(t * n) donde t = talleristas y n = tamaño de la orden.
     * No se puede hacer mejor sin mantener un índice adicional.
     *
     * @return el Vehículo con más tareas pendientes, o null si nadie tiene orden
     */
    public Vehiculo vehiculoConMasTareasPendientes() {
        Vehiculo candidato = null;
        int maxPendientes = -1;

        // Revisamos los vehículos que están siendo atendidos en este momento
        for (int i = 0; i < talleristas.tamaño(); i++) {
            Vehiculo v = talleristas.obtener(i).getVehiculoActual();
            if (v != null && v.getOrdenTrabajo() != null) {
                int pendientes = v.getOrdenTrabajo().cantidadPendientes();
                if (pendientes > maxPendientes) {
                    maxPendientes = pendientes;
                    candidato = v;
                }
            }
        }

        // También revisamos los que esperan repuesto (también tienen orden activa)
        TDALista<Vehiculo> enEspera = gestorEsperaRepuestos.listar();
        for (int i = 0; i < enEspera.tamaño(); i++) {
            Vehiculo v = enEspera.obtener(i);
            if (v.getOrdenTrabajo() != null) {
                int pendientes = v.getOrdenTrabajo().cantidadPendientes();
                if (pendientes > maxPendientes) {
                    maxPendientes = pendientes;
                    candidato = v;
                }
            }
        }

        return candidato;
    }

    /**
     * CONSULTA 3: costo promedio por sistema del vehículo.
     *
     * Agrupa las tareas de la OrdenTrabajo por el sistema (primer nivel de
     * la EstructuraVehiculo) al que pertenece la parte de cada tarea.
     * Retorna el costo promedio entre todos los sistemas con al menos una tarea.
     *
     * Permite responder: "¿cuánto cuesta en promedio cada área del vehículo?"
     * útil para presupuestos y para comparar dónde se concentra el costo.
     *
     * @throws NoSuchElementException si no existe vehículo con esa patente
     * @return costo promedio por sistema (0.0 si no hay tareas con parte asignada)
     */
    public double costoPromedioPorSistema(String patente) {
        Vehiculo vehiculo = indicePorPatente.buscar(patente);
        if (vehiculo == null) {
            throw new NoSuchElementException("No existe vehículo con patente: " + patente);
        }
        if (vehiculo.getOrdenTrabajo() == null) return 0.0;

        // Obtenemos todas las tareas en postorden (el orden no importa aquí,
        // solo queremos recorrer todos los nodos del árbol de trabajo)
        TDALista<Tarea> todas = vehiculo.getOrdenTrabajo().tareasPendientesParaEntregar();

        // Acumulamos costo por nombre de sistema (primer nivel de EstructuraVehiculo)
        // Usamos dos arreglos paralelos porque no tenemos un Map disponible
        // (solo las estructuras implementadas en el proyecto)
        ListaArray<String> nombresSistema = new ListaArray<>();
        ListaArray<Double> costosPorSistema = new ListaArray<>();

        for (int i = 0; i < todas.tamaño(); i++) {
            Tarea t = todas.obtener(i);
            if (t.getParte() == null) continue; // tarea sin parte asignada, no se agrupa

            // El nombre del sistema es la parte de tipo SISTEMA a la que pertenece.
            // Si la parte en sí es un sistema, usamos su nombre directamente.
            // Si es subsistema o pieza, debería buscarse su padre — para simplificar
            // usamos el nombre de la parte disponible en la tarea.
            String nombreSistema = t.getParte().getNombre();
            if (t.getParte().getTipo() != TipoParteVehiculo.SISTEMA) {
                // Fallback: agrupamos por el nombre de la parte directa
                // En un caso real habría que subir al sistema padre en EstructuraVehiculo
                nombreSistema = t.getParte().getNombre();
            }

            // Buscar si ya existe este sistema en nuestra lista de acumuladores
            int indice = -1;
            for (int j = 0; j < nombresSistema.tamaño(); j++) {
                if (nombresSistema.obtener(j).equals(nombreSistema)) {
                    indice = j;
                    break;
                }
            }
            if (indice == -1) {
                nombresSistema.agregar(nombreSistema);
                costosPorSistema.agregar(t.getCosto());
            } else {
                // remover el viejo valor y reinsertar la suma en la misma posición
                double costoAcumulado = costosPorSistema.remover(indice) + t.getCosto();
                costosPorSistema.agregar(indice, costoAcumulado);
            }
        }

        if (nombresSistema.esVacio()) return 0.0;

        // Calcular promedio
        double sumaCostos = 0.0;
        for (int i = 0; i < costosPorSistema.tamaño(); i++) {
            sumaCostos += costosPorSistema.obtener(i);
        }
        return sumaCostos / nombresSistema.tamaño();
    }

    // ═══════════════════════════════════════════════════════════════════
    // Helpers privados (Hito 1 originales + nuevos Hito 2)
    // ═══════════════════════════════════════════════════════════════════

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

    /**
     * Obtiene la OrdenTrabajo del vehículo con esa patente.
     * Lanza excepción si la patente no existe o si el vehículo no tiene
     * una orden iniciada (iniciarOrden no fue llamado aún).
     */
    private OrdenTrabajo obtenerOrdenObligatoria(String patente) {
        Vehiculo vehiculo = indicePorPatente.buscar(patente);
        if (vehiculo == null) {
            throw new NoSuchElementException("No existe vehículo con patente: " + patente);
        }
        OrdenTrabajo orden = vehiculo.getOrdenTrabajo();
        if (orden == null) {
            throw new IllegalStateException(
                    "El vehículo " + patente + " no tiene una OrdenTrabajo iniciada. " +
                    "Llamar a vehiculo.iniciarOrden(tareaRaiz) primero.");
        }
        return orden;
    }
}
