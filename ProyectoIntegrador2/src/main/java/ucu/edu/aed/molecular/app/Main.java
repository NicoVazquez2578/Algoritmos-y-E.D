package ucu.edu.aed.molecular.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ucu.edu.aed.molecular.compatibilidad.ParametrosObjetivo;
import ucu.edu.aed.molecular.compatibilidad.PerfilEstructural;
import ucu.edu.aed.molecular.compatibilidad.Rango;
import ucu.edu.aed.molecular.compatibilidad.ResultadoCompatibilidad;
import ucu.edu.aed.molecular.compatibilidad.TipoCarbono;
import ucu.edu.aed.molecular.desafio3.OptimizacionWiener;
import ucu.edu.aed.molecular.molecula.Atomo;
import ucu.edu.aed.molecular.molecula.Coincidencia;
import ucu.edu.aed.molecular.molecula.Elemento;
import ucu.edu.aed.molecular.molecula.Enlace;
import ucu.edu.aed.molecular.molecula.Molecula;
import ucu.edu.aed.molecular.molecula.TipoEnlace;
import ucu.edu.aed.molecular.servicios.DetectorGruposFuncionales;
import ucu.edu.aed.molecular.servicios.LaboratorioQuimico;
import ucu.edu.aed.molecular.servicios.RedReacciones;
import ucu.edu.aed.molecular.sintesis.Compuesto;
import ucu.edu.aed.molecular.sintesis.Criterio;
import ucu.edu.aed.molecular.sintesis.DatosCientificos;
import ucu.edu.aed.molecular.sintesis.Reaccion;
import ucu.edu.aed.molecular.sintesis.RutaSintesis;
import ucu.edu.aed.tda.grafo.model.edge.Edge;

/*
  Menu por consola del Proyecto Integrador 2 (Equipo 4 - Modelado Molecular).
  Carga los datos de ejemplo y deja probar todo lo del desafio 2 y los otros desafios.
  Si se ejecuta con el argumento "verificar" solo corre la verificacion y termina.
 */
public class Main {

    static Scanner sc = new Scanner(System.in);
    static LaboratorioQuimico lab;

    public static void main(String[] args) {
        lab = DatosEjemplo.crearLaboratorio();

        if (args.length > 0 && args[0].equals("verificar")) {
            boolean ok = VerificacionSistema.ejecutar(lab);
            System.exit(ok ? 0 : 1);
        }

        System.out.println("Proyecto Integrador 2 - Modelado Molecular (Equipo 4)");
        System.out.println("Se cargaron " + lab.getMoleculas().size() + " moleculas, "
                + lab.getRed().getCompuestos().size() + " compuestos y "
                + lab.getRed().getReacciones().size() + " reacciones.");

        int opcion = -1;
        while (opcion != 0) {
            mostrarMenu();
            opcion = leerInt("Ingrese una opcion: ");
            try {
                switch (opcion) {
                    case 1: listarMoleculas(); break;
                    case 2: verMolecula(); break;
                    case 3: gruposFuncionales(); break;
                    case 4: ranking(); break;
                    case 5: probarValidacion(); break;
                    case 6: listarCompuestos(); break;
                    case 7: listarReacciones(); break;
                    case 8: esObtenible(); break;
                    case 9: mejorRuta(); break;
                    case 10: planSintesis(); break;
                    case 11: consultas(); break;
                    case 12: DemoBiblioteca.ejecutar(); break;
                    case 13: OptimizacionWiener.main(new String[0]); break;
                    case 14: VerificacionSistema.ejecutar(lab); break;
                    case 0: System.out.println("Chau!"); break;
                    default: System.out.println("Esa opcion no existe");
                }
            } catch (RuntimeException e) {
                // por si algun metodo tira excepcion, que no se corte el programa
                System.out.println("Hubo un error: " + e.getMessage());
            }
        }
    }

    static void mostrarMenu() {
        System.out.println();
        System.out.println("===== MENU =====");
        System.out.println("1 - Ver moleculas");
        System.out.println("2 - Ver una molecula en detalle");
        System.out.println("3 - Grupos funcionales");
        System.out.println("4 - Ranking de compatibilidad");
        System.out.println("5 - Probar validaciones");
        System.out.println("6 - Ver compuestos y datos cientificos");
        System.out.println("7 - Ver reacciones");
        System.out.println("8 - Ver si un compuesto se puede obtener");
        System.out.println("9 - Mejor ruta de sintesis");
        System.out.println("10 - Plan de sintesis y orden de los pasos");
        System.out.println("11 - Consultas del grupo");
        System.out.println("12 - Prueba de la biblioteca de grafos (desafio 1)");
        System.out.println("13 - Floyd vs BFS para Wiener (desafio 3)");
        System.out.println("14 - Verificar que todo funcione");
        System.out.println("0 - Salir");
    }

    // ---------- Moleculas ----------

    static void listarMoleculas() {
        System.out.println("\nMoleculas cargadas:");
        int i = 1;
        for (Molecula m : lab.getMoleculas()) {
            String anillo = m.tieneAnillos() ? "tiene anillo" : "sin anillo";
            String valida = m.esValida() ? "valida" : "NO valida";
            System.out.printf("%d) %s - %s - %.2f g/mol - %d atomos - %s - %s%n",
                    i, m.getNombre(), m.formulaMolecular(), m.masaMolar(), m.cantidadAtomos(), anillo, valida);
            i++;
        }
        System.out.println("(los H no se cargan como atomos, se calculan con la valencia)");
    }

    static void verMolecula() {
        Molecula m = elegirMolecula();
        if (m == null) return;

        System.out.println("\n" + m.getNombre() + " -> " + m.formulaMolecular());
        System.out.printf("Masa molar: %.2f g/mol%n", m.masaMolar());
        System.out.println("Atomos: " + m.cantidadAtomos() + ", enlaces: " + m.cantidadEnlaces());
        for (Atomo a : m.atomos()) {
            String vecinos = "";
            for (Edge<Atomo, Enlace> e : m.adyacencias(a)) {
                vecinos += e.target() + tipoEnlace(e.dato().getTipo()) + " ";
            }
            System.out.println("  " + a + " usa " + m.valenciaUsada(a) + " de " + a.getElemento().getValencia()
                    + " de valencia, H implicitos: " + m.hidrogenosImplicitos(a) + ", vecinos: " + vecinos);
        }

        List<String> errores = m.validar();
        if (errores.isEmpty()) {
            System.out.println("La molecula es valida");
        } else {
            System.out.println("Errores: " + errores);
        }

        PerfilEstructural p = lab.getAnalizador().perfil(m);
        System.out.println("Cadena principal: " + p.getLongitudCadena() + " carbonos");
        System.out.println("Carbonos por tipo: " + p.getCarbonos());
        System.out.println("Indice de Wiener: " + p.getIndiceWiener());
    }

    static String tipoEnlace(TipoEnlace t) {
        if (t == TipoEnlace.DOBLE) return "(doble)";
        if (t == TipoEnlace.TRIPLE) return "(triple)";
        return "";
    }

    static void gruposFuncionales() {
        int op = leerInt("1 - Una molecula / 2 - Todas: ");
        List<Molecula> lista = new ArrayList<>();
        if (op == 1) {
            Molecula m = elegirMolecula();
            if (m == null) return;
            lista.add(m);
        } else {
            lista.addAll(lab.getMoleculas());
        }

        for (Molecula m : lista) {
            List<Coincidencia> grupos = lab.getDetector().detectar(m);
            if (grupos.isEmpty()) {
                System.out.println(m.getNombre() + ": no tiene grupos funcionales");
            } else {
                System.out.println(m.getNombre() + ": " + grupos);
            }
        }
    }

    static void ranking() {
        System.out.println("Ingrese los rangos (si deja vacio usa el valor entre corchetes)");
        double cadMin = leerDouble("Cadena minima [5]: ", 5);
        double cadMax = leerDouble("Cadena maxima [6]: ", 6);
        double wMin = leerDouble("Wiener minimo [30]: ", 30);
        double wMax = leerDouble("Wiener maximo [40]: ", 40);
        double terc = leerDouble("Maximo de carbonos terciarios [0]: ", 0);
        double cuat = leerDouble("Maximo de carbonos cuaternarios [0]: ", 0);

        ParametrosObjetivo param = new ParametrosObjetivo();
        param.conCadena(new Rango(cadMin, cadMax));
        param.conWiener(new Rango(wMin, wMax));
        param.conCarbonos(TipoCarbono.TERCIARIO, new Rango(0, terc));
        param.conCarbonos(TipoCarbono.CUATERNARIO, new Rango(0, cuat));

        // solo tiene sentido comparar moleculas que tengan carbonos
        List<Molecula> candidatas = new ArrayList<>();
        for (Molecula m : lab.getMoleculas()) {
            if (m.atomosDe(Elemento.C).size() > 0) candidatas.add(m);
        }

        List<ResultadoCompatibilidad> res = lab.rankingCompatibilidad(candidatas, param);
        System.out.println("\nRanking (puntaje = 1 / (1 + suma de desvios), 1 es cumplir todo):");
        int pos = 1;
        for (ResultadoCompatibilidad r : res) {
            System.out.printf("%d. %s  puntaje %.3f  (cumple %d de %d)  cadena=%d wiener=%d%n",
                    pos, r.getNombreMolecula(), r.getPuntaje(), r.getCriteriosCumplidos(),
                    r.getCriteriosTotales(), r.getPerfil().getLongitudCadena(), r.getPerfil().getIndiceWiener());
            pos++;
        }
    }

    static void probarValidacion() {
        System.out.println("Armamos una molecula con un O unido a dos C y probamos cosas que no deberian dejarse:");
        Molecula m = new Molecula("Prueba");
        Atomo o = m.agregarAtomo(Elemento.O);
        Atomo c1 = m.agregarAtomo(Elemento.C);
        Atomo c2 = m.agregarAtomo(Elemento.C);
        m.enlazar(o, c1);
        m.enlazar(o, c2);

        try {
            m.enlazar(o, m.agregarAtomo(Elemento.C));
            System.out.println("- Tercer enlace al O: se dejo (MAL)");
        } catch (IllegalArgumentException e) {
            System.out.println("- Tercer enlace al O: no se dejo -> " + e.getMessage());
        }
        try {
            m.enlazar(c1, c1);
            System.out.println("- C enlazado con el mismo: se dejo (MAL)");
        } catch (IllegalArgumentException e) {
            System.out.println("- C enlazado con el mismo: no se dejo -> " + e.getMessage());
        }
        try {
            m.enlazar(o, c1);
            System.out.println("- Enlace repetido O-C: se dejo (MAL)");
        } catch (IllegalArgumentException e) {
            System.out.println("- Enlace repetido O-C: no se dejo -> " + e.getMessage());
        }
        try {
            // C2 ya tiene 1 enlace, con el triple llega a 4 y despues no le entra otro doble
            m.enlazar(c1, c2, TipoEnlace.TRIPLE);
            m.enlazar(c1, m.agregarAtomo(Elemento.C), TipoEnlace.DOBLE);
            System.out.println("- C con mas de 4 enlaces: se dejo (MAL)");
        } catch (IllegalArgumentException e) {
            System.out.println("- C con mas de 4 enlaces: no se dejo -> " + e.getMessage());
        }

        Molecula separada = new Molecula("Separada");
        separada.agregarAtomos(Elemento.C, 2);
        System.out.println("- Dos C sin unir, validar() da: " + separada.validar());
    }

    // ---------- Compuestos y reacciones ----------

    static void listarCompuestos() {
        System.out.println("\nCompuestos (temperaturas en C, toxicidad e inflamabilidad de 0 a 4):");
        int i = 1;
        for (Compuesto c : lab.getRed().getCompuestos()) {
            DatosCientificos d = c.getDatos();
            String stock = c.isDisponible() ? "EN STOCK" : "";
            System.out.println(i + ") " + c.getNombre() + " " + c.formula() + " | ebullicion " + numero(d.getPuntoEbullicion())
                    + " | fusion " + numero(d.getPuntoFusion()) + " | tox " + d.getToxicidad()
                    + " | infl " + d.getInflamabilidad() + " | usos " + d.getUsos() + " " + stock);
            i++;
        }
    }

    static String numero(double x) {
        if (Double.isNaN(x)) return "?";
        return String.format("%.1f", x);
    }

    static void listarReacciones() {
        System.out.println("\nReacciones:");
        for (Reaccion r : lab.getRed().getReacciones()) {
            System.out.println(r.getId() + " " + r.getDescripcion() + ": " + r.getReactivos() + " -> " + r.getProductos()
                    + " (costo " + r.getCosto() + ", riesgo " + r.getRiesgo() + ")");
        }
    }

    static void esObtenible() {
        Compuesto c = elegirCompuesto();
        if (c == null) return;

        if (c.isDisponible()) {
            System.out.println(c + " ya esta en stock");
        } else if (lab.getRed().esObtenible(c)) {
            System.out.println(c + " se puede obtener con lo que hay en el laboratorio");
        } else {
            System.out.println(c + " NO se puede obtener (falta algun reactivo)");
        }
        System.out.println("Todo lo que se puede obtener: " + lab.getRed().compuestosObtenibles());
    }

    static void mejorRuta() {
        Compuesto c = elegirCompuesto();
        if (c == null) return;
        RedReacciones red = lab.getRed();

        if (c.isDisponible()) {
            System.out.println(c + " ya esta en stock, no hay que sintetizarlo");
            return;
        }
        if (!red.esObtenible(c)) {
            System.out.println(c + " no se puede obtener");
            return;
        }

        System.out.println("Ruta mas barata: " + red.mejorRuta(c, Criterio.COSTO));
        System.out.println("Ruta menos riesgosa: " + red.mejorRuta(c, Criterio.RIESGO));

        // para el combinado normalizamos el costo con el costo mas alto de todas las reacciones
        double costoMax = 0;
        for (Reaccion r : red.getReacciones()) {
            if (r.getCosto() > costoMax) costoMax = r.getCosto();
        }
        System.out.println("Mitad costo y mitad riesgo: " + red.mejorRuta(c, Criterio.combinado(0.5, costoMax)));

        List<RutaSintesis> otras = red.rutasAlternativas(c);
        System.out.println("\nTodas las rutas (de la mas barata a la mas cara):");
        if (otras.isEmpty()) {
            System.out.println("No hay rutas directas, hay que fabricar varios intermedios. Ver opcion 10.");
        }
        for (RutaSintesis r : otras) {
            System.out.println("  " + r);
        }
    }

    static void planSintesis() {
        Compuesto c = elegirCompuesto();
        if (c == null) return;

        RutaSintesis plan = lab.getRed().planDeSintesis(c, Criterio.COSTO);
        if (plan == null) {
            System.out.println(c + " no se puede obtener");
            return;
        }
        if (plan.esVacia()) {
            System.out.println(c + " ya esta en stock");
            return;
        }

        System.out.println("\nPasos para obtener " + c + " (ordenados con clasificacion topologica):");
        int paso = 1;
        for (Reaccion r : plan.getPasos()) {
            System.out.println(paso + ". " + r.getId() + ": " + r.getReactivos() + " -> " + r.getProductos()
                    + " (costo " + r.getCosto() + ", riesgo " + r.getRiesgo() + ")");
            paso++;
        }
        System.out.println("Costo total: " + plan.costoTotal() + " | riesgo total: " + plan.riesgoTotal()
                + " | paso mas riesgoso: " + plan.riesgoMaximo());
    }

    // ---------- Consultas del grupo ----------

    static void consultas() {
        System.out.println("\n1 - Compuestos con un grupo funcional y toxicidad maxima");
        System.out.println("2 - Atomos puente de una molecula (puntos de articulacion)");
        System.out.println("3 - Cantidad de anillos (Kruskal)");
        System.out.println("4 - Ruta mas segura con un presupuesto (todos los caminos)");
        System.out.println("5 - Atomo central de una molecula (centro del grafo)");
        int op = leerInt("Consulta: ");

        if (op == 1) {
            String[] grupos = {DetectorGruposFuncionales.ALCOHOL, DetectorGruposFuncionales.ACIDO,
                    DetectorGruposFuncionales.AMINA, DetectorGruposFuncionales.CARBONILO, DetectorGruposFuncionales.ANILLO};
            for (int i = 0; i < grupos.length; i++) {
                System.out.println((i + 1) + " - " + grupos[i]);
            }
            int g = leerInt("Grupo: ");
            if (g < 1 || g > grupos.length) {
                System.out.println("Grupo invalido");
                return;
            }
            int tox = leerInt("Toxicidad maxima (0 a 4): ");
            List<Compuesto> res = lab.compuestosConGrupoYToxicidadMaxima(grupos[g - 1], tox);
            if (res.isEmpty()) System.out.println("No hay ninguno");
            for (Compuesto c : res) {
                System.out.println("- " + c + " " + c.formula() + ", toxicidad " + c.getDatos().getToxicidad()
                        + ", ebullicion " + numero(c.getDatos().getPuntoEbullicion()) + ", usos " + c.getDatos().getUsos());
            }
        } else if (op == 2) {
            Molecula m = elegirMolecula();
            if (m == null) return;
            System.out.println("Atomos puente de " + m.getNombre() + ": " + lab.atomosPuente(m));
            System.out.println("(si se rompe uno de esos la molecula queda partida)");
        } else if (op == 3) {
            Molecula m = elegirMolecula();
            if (m == null) return;
            System.out.println(m.getNombre() + " tiene " + lab.contarAnillos(m) + " anillo/s");
        } else if (op == 4) {
            Compuesto c = elegirCompuesto();
            if (c == null) return;
            double presupuesto = leerDouble("Presupuesto [300]: ", 300);
            RutaSintesis r = lab.rutaMasSeguraConPresupuesto(c.getNombre(), presupuesto);
            if (r == null) {
                System.out.println("No hay ninguna ruta con ese presupuesto");
            } else {
                System.out.println("La mas segura es: " + r);
            }
        } else if (op == 5) {
            Molecula m = elegirMolecula();
            if (m == null) return;
            Atomo a = lab.atomoCentral(m);
            System.out.println("Atomo central de " + m.getNombre() + ": " + a
                    + " (excentricidad " + lab.excentricidad(m, a) + ")");
        } else {
            System.out.println("Esa consulta no existe");
        }
    }

    // ---------- Auxiliares para leer datos ----------

    static Molecula elegirMolecula() {
        List<Molecula> lista = lab.getMoleculas();
        for (int i = 0; i < lista.size(); i++) {
            System.out.println((i + 1) + " - " + lista.get(i).getNombre());
        }
        int n = leerInt("Numero de molecula: ");
        if (n < 1 || n > lista.size()) {
            System.out.println("Numero invalido");
            return null;
        }
        return lista.get(n - 1);
    }

    static Compuesto elegirCompuesto() {
        List<Compuesto> lista = new ArrayList<>(lab.getRed().getCompuestos());
        for (int i = 0; i < lista.size(); i++) {
            System.out.println((i + 1) + " - " + lista.get(i).getNombre());
        }
        int n = leerInt("Numero de compuesto: ");
        if (n < 1 || n > lista.size()) {
            System.out.println("Numero invalido");
            return null;
        }
        return lista.get(n - 1);
    }

    static int leerInt(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (!sc.hasNextLine()) System.exit(0);
            String linea = sc.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Tiene que ser un numero");
            }
        }
    }

    // si el usuario deja vacio devuelve el valor por defecto
    static double leerDouble(String mensaje, double porDefecto) {
        while (true) {
            System.out.print(mensaje);
            if (!sc.hasNextLine()) return porDefecto;
            String linea = sc.nextLine().trim().replace(',', '.');
            if (linea.isEmpty()) return porDefecto;
            try {
                return Double.parseDouble(linea);
            } catch (NumberFormatException e) {
                System.out.println("Tiene que ser un numero");
            }
        }
    }
}
