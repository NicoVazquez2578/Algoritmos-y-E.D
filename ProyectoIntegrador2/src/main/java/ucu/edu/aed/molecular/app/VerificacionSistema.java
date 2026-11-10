package ucu.edu.aed.molecular.app;

import ucu.edu.aed.molecular.compatibilidad.*;
import ucu.edu.aed.molecular.desafio3.OptimizacionWiener;
import ucu.edu.aed.molecular.molecula.*;
import ucu.edu.aed.molecular.servicios.*;
import ucu.edu.aed.molecular.sintesis.*;

import java.util.*;

/*
  Verificación automática: corre casos con resultado conocido (calculado a mano)
  y muestra OK / FALLA por cada uno. Sirve para comprobar rápido que todo el sistema funciona.
*/
public final class VerificacionSistema {

    private int ok = 0, fallas = 0;

    public static boolean ejecutar(LaboratorioQuimico lab) {
        VerificacionSistema v = new VerificacionSistema();
        v.correr(lab);
        System.out.printf("%nResultado: %d OK, %d FALLAS%n", v.ok, v.fallas);
        return v.fallas == 0;
    }

    private void check(String nombre, Object esperado, Object obtenido) {
        boolean bien = Objects.equals(esperado, obtenido);
        if (bien) ok++; else fallas++;
        System.out.printf("  [%s] %s -> %s%s%n", bien ? " OK " : "FALLA", nombre, obtenido,
                bien ? "" : "   (esperado: " + esperado + ")");
    }

    private void correr(LaboratorioQuimico lab) {
        DetectorGruposFuncionales det = lab.getDetector();
        AnalizadorEstructural an = lab.getAnalizador();
        RedReacciones red = lab.getRed();

        System.out.println("\n1) Representacion de moleculas");
        check("Formula etanol", "C2H6O", mol(lab, "Etanol").formulaMolecular());
        check("Masa etanol (g/mol)", "46.07", String.format(Locale.US, "%.2f", mol(lab, "Etanol").masaMolar()));
        check("Formula acido acetico", "C2H4O2", mol(lab, "Acido acetico").formulaMolecular());
        check("Formula glicina", "C2H5NO2", mol(lab, "Glicina").formulaMolecular());
        check("Formula benceno", "C6H6", mol(lab, "Benceno").formulaMolecular());
        check("Formula acetato de etilo", "C4H8O2", mol(lab, "Acetato de etilo").formulaMolecular());
        check("Formula agua (H implicitos)", "H2O", mol(lab, "Agua").formulaMolecular());
        check("Todas las moleculas cargadas son validas", true, lab.getMoleculas().stream().allMatch(Molecula::esValida));
        Molecula prueba = new Molecula("Prueba");
        Atomo o = prueba.agregarAtomo(Elemento.O);
        prueba.enlazar(o, prueba.agregarAtomo(Elemento.C));
        prueba.enlazar(o, prueba.agregarAtomo(Elemento.C));
        check("Oxigeno con 3 enlaces se rechaza", "rechazado", intentar(() -> prueba.enlazar(o, prueba.agregarAtomo(Elemento.C))));
        Molecula suelta = new Molecula("Fragmentos");
        suelta.agregarAtomos(Elemento.C, 2);
        check("Molecula no conexa es invalida", false, suelta.esValida());

        System.out.println("\n2) Grupos funcionales");
        check("Etanol", Set.of(DetectorGruposFuncionales.ALCOHOL), det.gruposPresentes(mol(lab, "Etanol")));
        check("Acido acetico", Set.of(DetectorGruposFuncionales.ACIDO), det.gruposPresentes(mol(lab, "Acido acetico")));
        check("Etilamina", Set.of(DetectorGruposFuncionales.AMINA), det.gruposPresentes(mol(lab, "Etilamina")));
        check("Glicina (amina + acido)", Set.of(DetectorGruposFuncionales.AMINA, DetectorGruposFuncionales.ACIDO),
                det.gruposPresentes(mol(lab, "Glicina")));
        check("Acetaldehido", Set.of(DetectorGruposFuncionales.CARBONILO), det.gruposPresentes(mol(lab, "Acetaldehido")));
        check("Benceno", Set.of(DetectorGruposFuncionales.ANILLO), det.gruposPresentes(mol(lab, "Benceno")));
        check("Fenol: anillo y no alcohol", Set.of(DetectorGruposFuncionales.ANILLO), det.gruposPresentes(mol(lab, "Fenol")));
        check("Acetato de etilo (ester): ni alcohol ni acido", Set.of(), det.gruposPresentes(mol(lab, "Acetato de etilo")));
        check("Butano sin grupos", Set.of(), det.gruposPresentes(mol(lab, "Butano")));
        check("Anillos del benceno (Kruskal)", 1, det.contarAnillos(mol(lab, "Benceno")));

        System.out.println("\n3) Analisis estructural");
        check("Cadena principal hexano", 6, an.longitudCadenaPrincipal(mol(lab, "Hexano")));
        check("Cadena principal 2-metilpentano", 5, an.longitudCadenaPrincipal(mol(lab, "2-Metilpentano")));
        check("Cadena principal neopentano", 3, an.longitudCadenaPrincipal(mol(lab, "Neopentano")));
        check("Cadena principal benceno (con anillo)", 6, an.longitudCadenaPrincipal(mol(lab, "Benceno")));
        check("Carbonos neopentano", "{PRIMARIO=4, SECUNDARIO=0, TERCIARIO=0, CUATERNARIO=1}",
                an.clasificarCarbonos(mol(lab, "Neopentano")).toString());
        check("Carbonos isobutano: 1 terciario", 1, an.clasificarCarbonos(mol(lab, "Isobutano")).get(TipoCarbono.TERCIARIO));
        check("Wiener butano (1+2+3+1+2+1)", 10L, an.indiceWiener(mol(lab, "Butano")));
        check("Wiener hexano (n(n^2-1)/6)", 35L, an.indiceWiener(mol(lab, "Hexano")));
        check("Wiener neopentano (4x1 + 6x2)", 16L, an.indiceWiener(mol(lab, "Neopentano")));
        check("Wiener: Floyd == BFS (2-metilpentano)", OptimizacionWiener.calcularConFloyd(mol(lab, "2-Metilpentano")),
                OptimizacionWiener.calcularConBFS(mol(lab, "2-Metilpentano")));

        System.out.println("\n4) Compatibilidad");
        ParametrosObjetivo p = new ParametrosObjetivo().conCadena(new Rango(5, 6)).conWiener(new Rango(30, 40))
                .conCarbonos(TipoCarbono.TERCIARIO, new Rango(0, 0));
        List<ResultadoCompatibilidad> ranking = lab.rankingCompatibilidad(
                List.of(mol(lab, "Butano"), mol(lab, "Hexano"), mol(lab, "2-Metilpentano"), mol(lab, "Neopentano")), p);
        check("Primero: hexano (cumple todo)", "Hexano", ranking.get(0).getNombreMolecula());
        check("Hexano puntaje 1.0", 1.0, ranking.get(0).getPuntaje());
        check("Ultimo: neopentano", "Neopentano", ranking.get(ranking.size() - 1).getNombreMolecula());

        System.out.println("\n5) Reacciones y rutas de sintesis");
        Compuesto acido = red.buscarCompuesto("Acido acetico");
        check("Acido acetico es obtenible", true, red.esObtenible(acido));
        check("Ciclohexano NO es obtenible (no hay benceno)", false, red.esObtenible(red.buscarCompuesto("Ciclohexano")));
        RutaSintesis porCosto = red.mejorRuta(acido, Criterio.COSTO);
        check("Menor costo: R1 -> R3 -> R4", List.of("R1", "R3", "R4"), ids(porCosto.getPasos()));
        check("Costo de esa ruta", 230.0, porCosto.costoTotal());
        RutaSintesis porRiesgo = red.mejorRuta(acido, Criterio.RIESGO);
        check("Menor riesgo: R1 -> R2", List.of("R1", "R2"), ids(porRiesgo.getPasos()));
        check("Rutas alternativas al acido", 3, red.rutasAlternativas(acido).size());
        RutaSintesis plan = red.planDeSintesis(red.buscarCompuesto("Acetato de etilo"), Criterio.COSTO);
        check("Plan acetato: R6 va al final", "R6", plan.getPasos().get(plan.cantidadPasos() - 1).getId());
        check("Plan acetato: R1 va primero", "R1", plan.getPasos().get(0).getId());

        System.out.println("\n6) Consultas propias");
        check("Atomo puente del isobutano: el C central", 1, lab.atomosPuente(mol(lab, "Isobutano")).size());
        check("Benceno no tiene atomos puente", 0, lab.atomosPuente(mol(lab, "Benceno")).size());
        check("Ruta mas segura al acido con presupuesto 300", List.of("R5"),
                ids(lab.rutaMasSeguraConPresupuesto("Acido acetico", 300).getPasos()));
        check("Atomo central del butano (excentricidad 2)", 2.0,
                lab.excentricidad(mol(lab, "Butano"), lab.atomoCentral(mol(lab, "Butano"))));
        check("Alcoholes con toxicidad <= 2", List.of("Etanol", "1-Propanol"),
                nombres(lab.compuestosConGrupoYToxicidadMaxima(DetectorGruposFuncionales.ALCOHOL, 2)));
    }

    private static Molecula mol(LaboratorioQuimico lab, String nombre) { return lab.buscarMolecula(nombre); }

    private static String intentar(Runnable r) {
        try { r.run(); return "aceptado"; } catch (IllegalArgumentException e) { return "rechazado"; }
    }

    private static List<String> ids(List<Reaccion> pasos) {
        List<String> res = new ArrayList<>();
        for (Reaccion r : pasos) res.add(r.getId());
        return res;
    }

    private static List<String> nombres(List<Compuesto> cs) {
        List<String> res = new ArrayList<>();
        for (Compuesto c : cs) res.add(c.getNombre());
        return res;
    }
}
