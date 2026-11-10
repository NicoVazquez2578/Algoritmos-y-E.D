package ucu.edu.aed.molecular.app;

import ucu.edu.aed.molecular.molecula.*;
import ucu.edu.aed.molecular.servicios.LaboratorioQuimico;
import ucu.edu.aed.molecular.servicios.RedReacciones;
import ucu.edu.aed.molecular.sintesis.*;

import java.util.List;

/*
  Conjunto de datos de ejemplo del dominio.
  Los hidrógenos se dejan implícitos (se calculan por valencia), salvo en el H2.
  Los datos científicos son aproximados (°C y escala NFPA 0-4); el modelo es introductorio.
*/
public final class DatosEjemplo {

    private DatosEjemplo() { }

    public static LaboratorioQuimico crearLaboratorio() {
        LaboratorioQuimico lab = new LaboratorioQuimico();
        RedReacciones red = lab.getRed();

        // ── Moléculas ──
        Molecula metano = new Molecula("Metano");
        metano.agregarAtomo(Elemento.C);

        Molecula etanol = new Molecula("Etanol");
        List<Atomo> e = etanol.agregarAtomos(Elemento.C, 2);
        Atomo eO = etanol.agregarAtomo(Elemento.O);
        etanol.enlazar(e.get(0), e.get(1));
        etanol.enlazar(e.get(1), eO);

        Molecula propanol = new Molecula("1-Propanol");
        List<Atomo> p = propanol.agregarAtomos(Elemento.C, 3);
        propanol.enlazarEnCadena(p);
        propanol.enlazar(p.get(2), propanol.agregarAtomo(Elemento.O));

        Molecula metanol = new Molecula("Metanol");
        metanol.enlazar(metanol.agregarAtomo(Elemento.C), metanol.agregarAtomo(Elemento.O));

        Molecula acido = new Molecula("Acido acetico");
        List<Atomo> a = acido.agregarAtomos(Elemento.C, 2);
        acido.enlazar(a.get(0), a.get(1));
        acido.enlazar(a.get(1), acido.agregarAtomo(Elemento.O), TipoEnlace.DOBLE);
        acido.enlazar(a.get(1), acido.agregarAtomo(Elemento.O));

        Molecula acetaldehido = new Molecula("Acetaldehido");
        List<Atomo> ac = acetaldehido.agregarAtomos(Elemento.C, 2);
        acetaldehido.enlazar(ac.get(0), ac.get(1));
        acetaldehido.enlazar(ac.get(1), acetaldehido.agregarAtomo(Elemento.O), TipoEnlace.DOBLE);

        Molecula acetato = new Molecula("Acetato de etilo");     // CH3-C(=O)-O-CH2-CH3
        List<Atomo> at = acetato.agregarAtomos(Elemento.C, 2);
        acetato.enlazar(at.get(0), at.get(1));
        acetato.enlazar(at.get(1), acetato.agregarAtomo(Elemento.O), TipoEnlace.DOBLE);
        Atomo oEster = acetato.agregarAtomo(Elemento.O);
        acetato.enlazar(at.get(1), oEster);
        List<Atomo> etilo = acetato.agregarAtomos(Elemento.C, 2);
        acetato.enlazar(oEster, etilo.get(0));
        acetato.enlazar(etilo.get(0), etilo.get(1));

        Molecula etilamina = new Molecula("Etilamina");
        List<Atomo> ea = etilamina.agregarAtomos(Elemento.C, 2);
        etilamina.enlazar(ea.get(0), ea.get(1));
        etilamina.enlazar(ea.get(1), etilamina.agregarAtomo(Elemento.N));

        Molecula glicina = new Molecula("Glicina");               // H2N-CH2-COOH
        Atomo gN = glicina.agregarAtomo(Elemento.N);
        List<Atomo> g = glicina.agregarAtomos(Elemento.C, 2);
        glicina.enlazar(gN, g.get(0));
        glicina.enlazar(g.get(0), g.get(1));
        glicina.enlazar(g.get(1), glicina.agregarAtomo(Elemento.O), TipoEnlace.DOBLE);
        glicina.enlazar(g.get(1), glicina.agregarAtomo(Elemento.O));

        Molecula benceno = anillo("Benceno", true);
        Molecula ciclohexano = anillo("Ciclohexano", false);

        Molecula fenol = anillo("Fenol", true);
        fenol.enlazar(fenol.buscarAtomo(1), fenol.agregarAtomo(Elemento.O));

        Molecula butano = cadena("Butano", 4);
        Molecula hexano = cadena("Hexano", 6);

        Molecula isobutano = new Molecula("Isobutano");           // C central con 3 metilos
        Atomo centroIso = isobutano.agregarAtomo(Elemento.C);
        for (Atomo c : isobutano.agregarAtomos(Elemento.C, 3)) isobutano.enlazar(centroIso, c);

        Molecula neopentano = new Molecula("Neopentano");         // C central con 4 metilos
        Atomo centroNeo = neopentano.agregarAtomo(Elemento.C);
        for (Atomo c : neopentano.agregarAtomos(Elemento.C, 4)) neopentano.enlazar(centroNeo, c);

        Molecula metilpentano = new Molecula("2-Metilpentano");   // C1-C2(-C6)-C3-C4-C5
        List<Atomo> mp = metilpentano.agregarAtomos(Elemento.C, 5);
        metilpentano.enlazarEnCadena(mp);
        metilpentano.enlazar(mp.get(1), metilpentano.agregarAtomo(Elemento.C));

        Molecula etileno = new Molecula("Etileno");
        etileno.enlazar(etileno.agregarAtomo(Elemento.C), etileno.agregarAtomo(Elemento.C), TipoEnlace.DOBLE);

        Molecula agua = new Molecula("Agua");
        agua.agregarAtomo(Elemento.O);

        Molecula oxigeno = new Molecula("Oxigeno");
        oxigeno.enlazar(oxigeno.agregarAtomo(Elemento.O), oxigeno.agregarAtomo(Elemento.O), TipoEnlace.DOBLE);

        Molecula hidrogeno = new Molecula("Hidrogeno");
        hidrogeno.enlazar(hidrogeno.agregarAtomo(Elemento.H), hidrogeno.agregarAtomo(Elemento.H));

        Molecula amoniaco = new Molecula("Amoniaco");
        amoniaco.agregarAtomo(Elemento.N);

        for (Molecula m : List.of(metano, etanol, propanol, metanol, acido, acetaldehido, acetato, etilamina, glicina,
                benceno, ciclohexano, fenol, butano, hexano, isobutano, neopentano, metilpentano, etileno, agua,
                oxigeno, hidrogeno, amoniaco)) {
            lab.registrarMolecula(m);
        }

        // ── Compuestos con datos científicos: (Peb °C, Pf °C, toxicidad, inflamabilidad, usos) ──
        // disponible = true: el laboratorio lo tiene en stock
        Compuesto cEtileno = new Compuesto("Etileno", etileno, datos(-103.7, -169.2, 2, 4, "plasticos", "maduracion de frutas"), true);
        Compuesto cAgua = new Compuesto("Agua", agua, datos(100, 0, 0, 0, "solvente"), true);
        Compuesto cOxigeno = new Compuesto("Oxigeno", oxigeno, datos(-183, -218.8, 0, 0, "oxidante"), true);
        Compuesto cAmoniaco = new Compuesto("Amoniaco", amoniaco, datos(-33.3, -77.7, 3, 1, "fertilizantes"), true);
        Compuesto cMetanol = new Compuesto("Metanol", metanol, datos(64.7, -97.6, 3, 3, "solvente", "combustible"), true);
        Compuesto cCO = new Compuesto("Monoxido de carbono", null, datos(-191.5, -205, 3, 4, "sintesis industrial"), true);
        Compuesto cHidrogeno = new Compuesto("Hidrogeno", hidrogeno, datos(-252.9, -259.1, 0, 4, "combustible"), true);
        Compuesto cMetano = new Compuesto("Metano", metano, datos(-161.5, -182.5, 1, 4, "combustible"), true);

        Compuesto cEtanol = new Compuesto("Etanol", etanol, datos(78.4, -114.1, 2, 3, "solvente", "combustible", "desinfectante"));
        Compuesto cAcetaldehido = new Compuesto("Acetaldehido", acetaldehido, datos(20.2, -123.4, 3, 4, "sintesis"));
        Compuesto cAcido = new Compuesto("Acido acetico", acido, datos(118.1, 16.6, 3, 2, "vinagre", "industria textil"));
        Compuesto cAcetato = new Compuesto("Acetato de etilo", acetato, datos(77.1, -83.6, 2, 3, "solvente", "esmaltes"));
        Compuesto cEtilamina = new Compuesto("Etilamina", etilamina, datos(16.6, -81, 3, 4, "sintesis", "colorantes"));
        Compuesto cBenceno = new Compuesto("Benceno", benceno, datos(80.1, 5.5, 2, 3, "solvente"));
        Compuesto cCiclohexano = new Compuesto("Ciclohexano", ciclohexano, datos(80.7, 6.5, 1, 3, "nylon"));
        Compuesto cPropanol = new Compuesto("1-Propanol", propanol, datos(97, -126, 2, 3, "solvente"));
        Compuesto cGlicina = new Compuesto("Glicina", glicina, datos(Double.NaN, 233, 0, 1, "suplemento", "farmacia"));
        Compuesto cFenol = new Compuesto("Fenol", fenol, datos(181.7, 40.5, 3, 2, "desinfectante", "resinas"));

        // ── Reacciones: costo en USD por lote, riesgo 0-10 ──
        red.registrarReaccion(new Reaccion("R1", "Hidratacion del etileno", List.of(cEtileno, cAgua), List.of(cEtanol), 120, 6));
        red.registrarReaccion(new Reaccion("R2", "Oxidacion directa del etanol", List.of(cEtanol, cOxigeno), List.of(cAcido), 300, 2));
        red.registrarReaccion(new Reaccion("R3", "Oxidacion parcial del etanol", List.of(cEtanol, cOxigeno), List.of(cAcetaldehido), 50, 5));
        red.registrarReaccion(new Reaccion("R4", "Oxidacion del acetaldehido", List.of(cAcetaldehido, cOxigeno), List.of(cAcido), 60, 4));
        red.registrarReaccion(new Reaccion("R5", "Carbonilacion del metanol", List.of(cMetanol, cCO), List.of(cAcido), 260, 9));
        red.registrarReaccion(new Reaccion("R6", "Esterificacion", List.of(cAcido, cEtanol), List.of(cAcetato, cAgua), 80, 3));
        red.registrarReaccion(new Reaccion("R7", "Aminacion del etanol", List.of(cEtanol, cAmoniaco), List.of(cEtilamina), 150, 5));
        red.registrarReaccion(new Reaccion("R8", "Hidrogenacion del benceno", List.of(cBenceno, cHidrogeno), List.of(cCiclohexano), 200, 6));
        red.registrarReaccion(new Reaccion("R9", "Hidroxilacion del benceno", List.of(cBenceno, cOxigeno), List.of(cFenol), 180, 7));
        red.registrarCompuesto(cPropanol);
        red.registrarCompuesto(cGlicina);
        red.registrarCompuesto(cMetano);
        return lab;
    }

    private static Molecula cadena(String nombre, int n) {
        Molecula m = new Molecula(nombre);
        m.enlazarEnCadena(m.agregarAtomos(Elemento.C, n));
        return m;
    }

    /** Anillo de 6 carbonos; aromático = enlaces dobles alternados. */
    private static Molecula anillo(String nombre, boolean aromatico) {
        Molecula m = new Molecula(nombre);
        List<Atomo> c = m.agregarAtomos(Elemento.C, 6);
        for (int i = 0; i < 6; i++)
            m.enlazar(c.get(i), c.get((i + 1) % 6), aromatico && i % 2 == 0 ? TipoEnlace.DOBLE : TipoEnlace.SIMPLE);
        return m;
    }

    private static DatosCientificos datos(double eb, double fus, int tox, int infl, String... usos) {
        return new DatosCientificos(eb, fus, tox, infl, List.of(usos));
    }
}
