package ucu.edu.aed.ProyectoPrimerHito;

import ucu.edu.aed.impl.Cola;
import ucu.edu.aed.impl.ColaPrioridad;
import ucu.edu.aed.impl.Lista;
import ucu.edu.aed.impl.Pila;
import ucu.edu.aed.tda.TDALista;
import ucu.edu.aed.tda.TDAPila;

import java.time.LocalDate;

/**
 * Demo interactiva del Hito 2.
 *
 * Historia: el taller recibe tres vehículos el mismo día.
 * Cada uno tiene su OrdenTrabajo con un árbol de tareas diferente.
 * Se demuestra: BST por patente, BST por rango de fechas, Montículo,
 * OrdenTrabajo (postorden, suspender/reanudar, costos, aprobar/rechazar)
 * y las tres consultas analíticas del Hito 2.
 *
 * Para correr:
 *   mvn compile exec:java -Dexec.mainClass="ucu.edu.aed.ProyectoPrimerHito.DemoHito2"
 */
public class DemoHito2 {

    // ── utilidades de impresión ──────────────────────────────────────────

    private static void titulo(String texto) {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║  " + texto);
        System.out.println("╚══════════════════════════════════════════════════╝");
    }

    private static void paso(String texto) {
        System.out.println("\n  ▶ " + texto);
    }

    private static void resultado(String texto) {
        System.out.println("    → " + texto);
    }

    private static void separador() {
        System.out.println("  ─────────────────────────────────────────────────");
    }

    // ── setup ────────────────────────────────────────────────────────────

    private static Taller armarTaller() {
        TDALista<Tallerista> talleristas = new Lista<>();
        talleristas.agregar(new Tallerista("T1", "Marta", "Motor"));
        talleristas.agregar(new Tallerista("T2", "Pedro", "Frenos"));
        talleristas.agregar(new Tallerista("T3", "Carla", "Eléctrico"));
        return new Taller(
                new Cola<>(),
                new GestorEsperaRepuestos(new Lista<>()),
                talleristas,
                new Lista<>(),
                new ColaPrioridad<>()
        );
    }

    private static Vehiculo crearVehiculo(String patente, String marca, String modelo,
                                           String propietario, int urgencia, int diasAtras) {
        TDAPila<Tarea> pila = new Pila<>();
        TDALista<Tarea> historial = new Lista<>();
        return new Vehiculo(patente, marca, modelo, 2020, propietario,
                TipoIngreso.MANTENIMIENTO_PLANIFICADO, urgencia,
                LocalDate.now().minusDays(diasAtras), pila, historial);
    }

    // ── main ─────────────────────────────────────────────────────────────

    public static void main(String[] args) {

        Taller taller = armarTaller();

        // ── Vehículo 1: Toyota Corolla de Ana ──────────────────────────
        // Orden: Motor (raíz) → Aceite (hoja), Filtro (hoja)
        //                     → Distribución → Correa (hoja), Tensor (hoja)
        Vehiculo v1 = crearVehiculo("ABC1234", "Toyota", "Corolla", "Ana García", 5, 3);
        Tarea motorAceite     = new Tarea("Cambio aceite",        TipoTarea.MANTENIMIENTO,     LocalDate.now(), 1500.0,
                new ParteVehiculo("Motor", TipoParteVehiculo.SISTEMA));
        Tarea filtroAceite    = new Tarea("Cambio filtro aceite", TipoTarea.MANTENIMIENTO,     LocalDate.now(), 800.0,
                new ParteVehiculo("Motor", TipoParteVehiculo.SISTEMA));
        Tarea distribucion    = new Tarea("Kit distribución",     TipoTarea.REPARACION_ORIGINAL, LocalDate.now(), 0.0,
                new ParteVehiculo("Motor", TipoParteVehiculo.SISTEMA));
        Tarea correaDistrib   = new Tarea("Cambio correa",        TipoTarea.REPARACION_ORIGINAL, LocalDate.now(), 3200.0,
                new ParteVehiculo("Distribución", TipoParteVehiculo.SUBSISTEMA));
        Tarea tensorDistrib   = new Tarea("Cambio tensor",        TipoTarea.REPARACION_ORIGINAL, LocalDate.now(), 1100.0,
                new ParteVehiculo("Distribución", TipoParteVehiculo.SUBSISTEMA));
        v1.iniciarOrden(motorAceite);
        v1.getOrdenTrabajo().agregarTareaDerivada(motorAceite, filtroAceite);
        v1.getOrdenTrabajo().agregarTareaDerivada(motorAceite, distribucion);
        v1.getOrdenTrabajo().agregarTareaDerivada(distribucion, correaDistrib);
        v1.getOrdenTrabajo().agregarTareaDerivada(distribucion, tensorDistrib);
        v1.setFechaEntregaComprometida(LocalDate.now().plusDays(5)); // cliente la necesita en 5 días

        // ── Vehículo 2: Chevrolet Onix de Beto ─────────────────────────
        // Orden: Frenos (raíz) → PastillasDel (hoja), PastillasTras (hoja)
        //                      → Disco → DiscoIzq (hoja), DiscoDer (hoja)
        Vehiculo v2 = crearVehiculo("XYZ9999", "Chevrolet", "Onix", "Beto Ruiz", 9, 1); // URGENTE
        Tarea frenos       = new Tarea("Revisión frenos",     TipoTarea.REPARACION_ORIGINAL, LocalDate.now(), 0.0,
                new ParteVehiculo("Frenos", TipoParteVehiculo.SISTEMA));
        Tarea pastillasDel = new Tarea("Pastillas delantera", TipoTarea.FALLA_ADICIONAL,     LocalDate.now(), 2400.0,
                new ParteVehiculo("Frenos", TipoParteVehiculo.SISTEMA));
        Tarea pastillasTra = new Tarea("Pastillas traseras",  TipoTarea.FALLA_ADICIONAL,     LocalDate.now(), 2100.0,
                new ParteVehiculo("Frenos", TipoParteVehiculo.SISTEMA));
        Tarea disco        = new Tarea("Discos de freno",     TipoTarea.FALLA_ADICIONAL,     LocalDate.now(), 0.0,
                new ParteVehiculo("Frenos", TipoParteVehiculo.SISTEMA));
        Tarea discoIzq     = new Tarea("Disco izquierdo",     TipoTarea.FALLA_ADICIONAL,     LocalDate.now(), 3800.0,
                new ParteVehiculo("Disco", TipoParteVehiculo.SUBSISTEMA));
        Tarea discoDer     = new Tarea("Disco derecho",       TipoTarea.FALLA_ADICIONAL,     LocalDate.now(), 3800.0,
                new ParteVehiculo("Disco", TipoParteVehiculo.SUBSISTEMA));
        v2.iniciarOrden(frenos);
        v2.getOrdenTrabajo().agregarTareaDerivada(frenos, pastillasDel);
        v2.getOrdenTrabajo().agregarTareaDerivada(frenos, pastillasTra);
        v2.getOrdenTrabajo().agregarTareaDerivada(frenos, disco);
        v2.getOrdenTrabajo().agregarTareaDerivada(disco, discoIzq);
        v2.getOrdenTrabajo().agregarTareaDerivada(disco, discoDer);
        v2.setFechaEntregaComprometida(LocalDate.now().plusDays(2)); // cliente la necesita en 2 días (urgente)

        // ── Vehículo 3: Ford Focus de Cami ─────────────────────────────
        // Orden simple: Batería (raíz) → Reemplazar batería (hoja)
        Vehiculo v3 = crearVehiculo("MNO4567", "Ford", "Focus", "Cami López", 3, 10);
        Tarea bateria         = new Tarea("Sistema eléctrico",  TipoTarea.MANTENIMIENTO, LocalDate.now(), 0.0,
                new ParteVehiculo("Eléctrico", TipoParteVehiculo.SISTEMA));
        Tarea cambiarBateria  = new Tarea("Reemplazar batería", TipoTarea.MANTENIMIENTO, LocalDate.now(), 4500.0,
                new ParteVehiculo("Batería", TipoParteVehiculo.PIEZA));
        v3.iniciarOrden(bateria);
        v3.getOrdenTrabajo().agregarTareaDerivada(bateria, cambiarBateria);
        // Sin fecha comprometida — va por FIFO

        // ══════════════════════════════════════════════════════════════
        titulo("BLOQUE 1 — Registrar vehículos");
        // ══════════════════════════════════════════════════════════════

        paso("Registrando ABC1234 (Toyota, urgencia 5, entrega en 5 días)");
        taller.registrarVehiculo(v1);
        resultado("registrado OK");

        paso("Registrando XYZ9999 (Chevrolet, urgencia 9 = URGENTE, entrega en 2 días)");
        taller.registrarVehiculo(v2);
        resultado("registrado OK");

        paso("Registrando MNO4567 (Ford, urgencia 3, sin fecha comprometida)");
        taller.registrarVehiculo(v3);
        resultado("registrado OK");

        // ══════════════════════════════════════════════════════════════
        titulo("BLOQUE 2 — BST por patente: buscar en O(log n)");
        // ══════════════════════════════════════════════════════════════

        paso("buscarVehiculoPorMatricula(\"ABC1234\")");
        Vehiculo encontrado = taller.buscarVehiculoPorMatricula("ABC1234");
        resultado(encontrado != null
                ? encontrado.getMarca() + " " + encontrado.getModelo() + " de " + encontrado.getPropietario()
                : "null");

        paso("buscarVehiculoPorMatricula(\"ZZZ0000\")  ← patente inexistente");
        resultado("" + taller.buscarVehiculoPorMatricula("ZZZ0000"));

        // ══════════════════════════════════════════════════════════════
        titulo("BLOQUE 3 — BST por fecha: rango de fechas O(log n + k)");
        // ══════════════════════════════════════════════════════════════

        LocalDate desde = LocalDate.now().minusDays(5);
        LocalDate hasta = LocalDate.now();
        paso("vehiculosEnRangoDeFechas(" + desde + ", " + hasta + ")");
        TDALista<Vehiculo> enRango = taller.vehiculosEnRangoDeFechas(desde, hasta);
        for (int i = 0; i < enRango.tamaño(); i++) {
            resultado(enRango.obtener(i).getPatente() + " ingresó " + enRango.obtener(i).getFechaIngreso());
        }

        paso("vehiculosEnRangoDeFechas hace 20 días → hace 8 días  ← solo Ford Focus");
        TDALista<Vehiculo> rango2 = taller.vehiculosEnRangoDeFechas(
                LocalDate.now().minusDays(20), LocalDate.now().minusDays(8));
        resultado(rango2.tamaño() + " vehículo(s) en ese rango: "
                + (rango2.tamaño() > 0 ? rango2.obtener(0).getPatente() : "ninguno"));

        // ══════════════════════════════════════════════════════════════
        titulo("BLOQUE 4 — Montículo: reprogramar y orden por fecha comprometida");
        // ══════════════════════════════════════════════════════════════

        paso("Estado inicial: XYZ9999 tiene fecha más próxima (+" + 2 + " días), ABC1234 (+" + 5 + " días)");
        resultado("El montículo pone XYZ9999 arriba (fecha más próxima = más urgente)");

        paso("reprogramarVehiculo(\"ABC1234\", mañana) ← ahora ABC1234 es el más urgente");
        taller.reprogramarVehiculo("ABC1234", LocalDate.now().plusDays(1));
        resultado("ABC1234 reprogramado para " + LocalDate.now().plusDays(1));
        resultado("Ahora el montículo tiene ABC1234 arriba (mañana < en 2 días)");

        paso("Restaurar fecha original de ABC1234 para continuar la demo");
        taller.reprogramarVehiculo("ABC1234", LocalDate.now().plusDays(5));

        // ══════════════════════════════════════════════════════════════
        titulo("BLOQUE 5 — OrdenTrabajo de ABC1234: árbol de tareas");
        // ══════════════════════════════════════════════════════════════

        paso("Árbol de tareas de ABC1234:");
        System.out.println("    Cambio aceite (Motor)");
        System.out.println("    ├── Cambio filtro aceite (Motor)");
        System.out.println("    └── Kit distribución (Motor)");
        System.out.println("        ├── Cambio correa (Distribución)");
        System.out.println("        └── Cambio tensor (Distribución)");

        separador();
        paso("tareasPendientesParaEntregar(\"ABC1234\") → postorden (hojas antes que raíz)");
        TDALista<Tarea> postorden = taller.tareasPendientesParaEntregar("ABC1234");
        for (int i = 0; i < postorden.tamaño(); i++) {
            resultado((i + 1) + ". " + postorden.obtener(i).getDescripcion());
        }

        separador();
        paso("puedeTerminar(\"ABC1234\", distribucion) ANTES de terminar los hijos");
        resultado("" + taller.puedeTerminar("ABC1234", distribucion)
                + "  ← false, correa y tensor aún PENDIENTES");

        paso("Terminamos correa y tensor (marcarResuelta)");
        correaDistrib.marcarResuelta();
        tensorDistrib.marcarResuelta();
        paso("puedeTerminar(\"ABC1234\", distribucion) DESPUÉS de terminar los hijos");
        resultado("" + taller.puedeTerminar("ABC1234", distribucion)
                + "  ← true, todos sus descendientes están TERMINADOS");

        separador();
        paso("costoTotal(\"ABC1234\") — suma todas las tareas del árbol");
        resultado("$" + taller.costoTotal("ABC1234"));

        paso("costoParcial(\"ABC1234\", distribucion) — solo el subárbol de distribución");
        resultado("$" + taller.costoParcial("ABC1234", distribucion)
                + "  (correa $3200 + tensor $1100)");

        // ══════════════════════════════════════════════════════════════
        titulo("BLOQUE 6 — Suspender / Reanudar por repuesto");
        // ══════════════════════════════════════════════════════════════

        paso("Falta el kit de distribución → suspendemos esa rama en ABC1234");
        taller.suspenderPorRepuesto("ABC1234", distribucion);
        resultado("trabajosBloqueadosPorRepuesto(\"ABC1234\"):");
        TDALista<Tarea> bloqueadas = taller.trabajosBloqueadosPorRepuesto("ABC1234");
        for (int i = 0; i < bloqueadas.tamaño(); i++) {
            resultado("  • " + bloqueadas.obtener(i).getDescripcion()
                    + " [" + bloqueadas.obtener(i).getEstado() + "]");
        }

        paso("Llegó el repuesto → reanudar esa rama");
        taller.reanudarPorRepuesto("ABC1234", distribucion);
        bloqueadas = taller.trabajosBloqueadosPorRepuesto("ABC1234");
        resultado("tareas bloqueadas después de reanudar: " + bloqueadas.tamaño()
                + "  ← 0, todas volvieron a PENDIENTE");

        // ══════════════════════════════════════════════════════════════
        titulo("BLOQUE 7 — Aprobar / Rechazar partes");
        // ══════════════════════════════════════════════════════════════

        paso("El cliente no aprueba las pastillas traseras de XYZ9999");
        taller.rechazarParte("XYZ9999", pastillasTra);
        resultado("pastillasTra.estado = " + pastillasTra.getEstado());

        paso("El cliente aprueba el resto de la orden de XYZ9999");
        taller.aprobarParte("XYZ9999", frenos);
        taller.aprobarParte("XYZ9999", pastillasDel);
        taller.aprobarParte("XYZ9999", disco);
        resultado("frenos.estado = " + frenos.getEstado());
        resultado("pastillasDel.estado = " + pastillasDel.getEstado());

        paso("puedeTerminar(\"XYZ9999\", disco)  ← discos aún no terminados");
        resultado("" + taller.puedeTerminar("XYZ9999", disco));

        paso("Terminamos discoIzq y discoDer");
        discoIzq.marcarResuelta();
        discoDer.marcarResuelta();
        paso("puedeTerminar(\"XYZ9999\", disco)  ← ahora sí");
        resultado("" + taller.puedeTerminar("XYZ9999", disco));

        // ══════════════════════════════════════════════════════════════
        titulo("BLOQUE 8 — Consultas analíticas");
        // ══════════════════════════════════════════════════════════════

        // Para vehiculoConMasTareasPendientes necesitamos que estén siendo atendidos
        paso("Asignamos talleristas para activar la consulta vehiculoConMasTareasPendientes");
        Tallerista t1 = taller.atenderProximoVehiculo(); // XYZ9999 (urgente)
        Tallerista t2 = taller.atenderProximoVehiculo(); // ABC1234
        resultado(t1.getNombre() + " → " + t1.getVehiculoActual().getPatente());
        resultado(t2.getNombre() + " → " + t2.getVehiculoActual().getPatente());

        separador();
        paso("vehiculoConMasTareasPendientes()");
        Vehiculo masOcupado = taller.vehiculoConMasTareasPendientes();
        resultado(masOcupado != null
                ? masOcupado.getPatente() + " con "
                        + masOcupado.getOrdenTrabajo().cantidadPendientes() + " tarea(s) pendientes"
                : "ninguno");

        separador();
        paso("costoPromedioPorSistema(\"ABC1234\")  — agrupa tareas por parte/sistema");
        double promedio = taller.costoPromedioPorSistema("ABC1234");
        resultado(String.format("promedio = $%.2f por sistema", promedio));

        // ══════════════════════════════════════════════════════════════
        titulo("FIN DE LA DEMO");
        // ══════════════════════════════════════════════════════════════
        System.out.println("\n  Todas las funcionalidades del Hito 2 demostradas:");
        System.out.println("  [✓] BST por patente — busqueda O(log n)");
        System.out.println("  [✓] BST por fecha   — rango O(log n + k)");
        System.out.println("  [✓] Monticulo       — reprogramar fecha comprometida");
        System.out.println("  [✓] OrdenTrabajo    — postorden, puedeTerminar, costos");
        System.out.println("  [✓] Suspender/Reanudar por repuesto");
        System.out.println("  [✓] Aprobar/Rechazar partes");
        System.out.println("  [✓] Consulta 1 — trabajosBloqueadosPorRepuesto");
        System.out.println("  [✓] Consulta 2 — vehiculoConMasTareasPendientes");
        System.out.println("  [✓] Consulta 3 — costoPromedioPorSistema");
        System.out.println();
    }
}
