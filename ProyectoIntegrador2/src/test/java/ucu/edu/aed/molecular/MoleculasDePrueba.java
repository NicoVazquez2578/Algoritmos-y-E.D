package ucu.edu.aed.molecular;

import ucu.edu.aed.molecular.molecula.Atomo;
import ucu.edu.aed.molecular.molecula.Elemento;
import ucu.edu.aed.molecular.molecula.Molecula;
import ucu.edu.aed.molecular.molecula.TipoEnlace;

import java.util.List;

/** Fábrica de moléculas conocidas para los tests (hidrógenos implícitos). */
public final class MoleculasDePrueba {

    private MoleculasDePrueba() { }

    /** Cadena lineal de n carbonos: n=4 butano, n=6 hexano. */
    public static Molecula cadena(String nombre, int n) {
        Molecula m = new Molecula(nombre);
        m.enlazarEnCadena(m.agregarAtomos(Elemento.C, n));
        return m;
    }

    /** C1-C2-O3 */
    public static Molecula etanol() {
        Molecula m = new Molecula("Etanol");
        List<Atomo> c = m.agregarAtomos(Elemento.C, 2);
        Atomo o = m.agregarAtomo(Elemento.O);
        m.enlazar(c.get(0), c.get(1));
        m.enlazar(c.get(1), o);
        return m;
    }

    /** C1-O2 */
    public static Molecula metanol() {
        Molecula m = new Molecula("Metanol");
        m.enlazar(m.agregarAtomo(Elemento.C), m.agregarAtomo(Elemento.O));
        return m;
    }

    /** C1-C2(=O3)-O4 */
    public static Molecula acidoAcetico() {
        Molecula m = new Molecula("Acido acetico");
        List<Atomo> c = m.agregarAtomos(Elemento.C, 2);
        m.enlazar(c.get(0), c.get(1));
        m.enlazar(c.get(1), m.agregarAtomo(Elemento.O), TipoEnlace.DOBLE);
        m.enlazar(c.get(1), m.agregarAtomo(Elemento.O));
        return m;
    }

    /** C1-C2(=O3) */
    public static Molecula acetaldehido() {
        Molecula m = new Molecula("Acetaldehido");
        List<Atomo> c = m.agregarAtomos(Elemento.C, 2);
        m.enlazar(c.get(0), c.get(1));
        m.enlazar(c.get(1), m.agregarAtomo(Elemento.O), TipoEnlace.DOBLE);
        return m;
    }

    /** C1-C2(=O3)-C4 */
    public static Molecula acetona() {
        Molecula m = new Molecula("Acetona");
        List<Atomo> c = m.agregarAtomos(Elemento.C, 2);
        m.enlazar(c.get(0), c.get(1));
        m.enlazar(c.get(1), m.agregarAtomo(Elemento.O), TipoEnlace.DOBLE);
        m.enlazar(c.get(1), m.agregarAtomo(Elemento.C));
        return m;
    }

    /** C1-C2-N3 */
    public static Molecula etilamina() {
        Molecula m = new Molecula("Etilamina");
        List<Atomo> c = m.agregarAtomos(Elemento.C, 2);
        m.enlazar(c.get(0), c.get(1));
        m.enlazar(c.get(1), m.agregarAtomo(Elemento.N));
        return m;
    }

    /** Amida: C1-C2(=O3)-N4. El N está unido a un C=O, no es amina. */
    public static Molecula acetamida() {
        Molecula m = new Molecula("Acetamida");
        List<Atomo> c = m.agregarAtomos(Elemento.C, 2);
        m.enlazar(c.get(0), c.get(1));
        m.enlazar(c.get(1), m.agregarAtomo(Elemento.O), TipoEnlace.DOBLE);
        m.enlazar(c.get(1), m.agregarAtomo(Elemento.N));
        return m;
    }

    /** N1-C2-C3(=O4)-O5 */
    public static Molecula glicina() {
        Molecula m = new Molecula("Glicina");
        Atomo n = m.agregarAtomo(Elemento.N);
        List<Atomo> c = m.agregarAtomos(Elemento.C, 2);
        m.enlazar(n, c.get(0));
        m.enlazar(c.get(0), c.get(1));
        m.enlazar(c.get(1), m.agregarAtomo(Elemento.O), TipoEnlace.DOBLE);
        m.enlazar(c.get(1), m.agregarAtomo(Elemento.O));
        return m;
    }

    /** Éster: C1-C2(=O3)-O4-C5-C6 */
    public static Molecula acetatoDeEtilo() {
        Molecula m = new Molecula("Acetato de etilo");
        List<Atomo> c = m.agregarAtomos(Elemento.C, 2);
        m.enlazar(c.get(0), c.get(1));
        m.enlazar(c.get(1), m.agregarAtomo(Elemento.O), TipoEnlace.DOBLE);
        Atomo o = m.agregarAtomo(Elemento.O);
        m.enlazar(c.get(1), o);
        List<Atomo> etilo = m.agregarAtomos(Elemento.C, 2);
        m.enlazar(o, etilo.get(0));
        m.enlazar(etilo.get(0), etilo.get(1));
        return m;
    }

    /** Anillo de 6 C; aromático = dobles alternados (benceno), si no ciclohexano. */
    public static Molecula anillo6(String nombre, boolean aromatico) {
        Molecula m = new Molecula(nombre);
        List<Atomo> c = m.agregarAtomos(Elemento.C, 6);
        for (int i = 0; i < 6; i++)
            m.enlazar(c.get(i), c.get((i + 1) % 6), aromatico && i % 2 == 0 ? TipoEnlace.DOBLE : TipoEnlace.SIMPLE);
        return m;
    }

    public static Molecula benceno() { return anillo6("Benceno", true); }

    public static Molecula ciclohexano() { return anillo6("Ciclohexano", false); }

    /** Benceno + O unido a C1. */
    public static Molecula fenol() {
        Molecula m = anillo6("Fenol", true);
        m.enlazar(m.buscarAtomo(1), m.agregarAtomo(Elemento.O));
        return m;
    }

    /** Decalina: dos anillos de 6 fusionados (10 C, 11 enlaces). */
    public static Molecula decalina() {
        Molecula m = new Molecula("Decalina");
        List<Atomo> c = m.agregarAtomos(Elemento.C, 10);
        for (int i = 0; i < 9; i++) m.enlazar(c.get(i), c.get(i + 1));
        m.enlazar(c.get(9), c.get(0));
        m.enlazar(c.get(0), c.get(5));
        return m;
    }

    /** C1 central unido a 3 C. */
    public static Molecula isobutano() {
        Molecula m = new Molecula("Isobutano");
        Atomo centro = m.agregarAtomo(Elemento.C);
        for (Atomo c : m.agregarAtomos(Elemento.C, 3)) m.enlazar(centro, c);
        return m;
    }

    /** C1 central unido a 4 C. */
    public static Molecula neopentano() {
        Molecula m = new Molecula("Neopentano");
        Atomo centro = m.agregarAtomo(Elemento.C);
        for (Atomo c : m.agregarAtomos(Elemento.C, 4)) m.enlazar(centro, c);
        return m;
    }

    /** C1-C2(-C6)-C3-C4-C5 */
    public static Molecula metilpentano() {
        Molecula m = new Molecula("2-Metilpentano");
        List<Atomo> c = m.agregarAtomos(Elemento.C, 5);
        m.enlazarEnCadena(c);
        m.enlazar(c.get(1), m.agregarAtomo(Elemento.C));
        return m;
    }

    public static Molecula unAtomo(String nombre, Elemento e) {
        Molecula m = new Molecula(nombre);
        m.agregarAtomo(e);
        return m;
    }
}
