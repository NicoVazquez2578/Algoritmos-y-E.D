package ucu.edu.aed.molecular.molecula;

import org.junit.jupiter.api.Test;
import ucu.edu.aed.molecular.MoleculasDePrueba;
import ucu.edu.aed.molecular.grafo.DirectedGraph;
import ucu.edu.aed.molecular.grafo.UndirectedGraph;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoleculaTest {

    // ───── Construcción ─────

    @Test
    void constructorRechazaNombreInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new Molecula(""));
        assertThrows(IllegalArgumentException.class, () -> new Molecula("   "));
        assertThrows(IllegalArgumentException.class, () -> new Molecula(null));
    }

    // ───── Caso borde: molécula vacía y de un átomo ─────

    @Test
    void moleculaVacia() {
        Molecula m = new Molecula("Vacia");
        assertEquals(0, m.cantidadAtomos());
        assertEquals(0, m.cantidadEnlaces());
        assertEquals("", m.formulaMolecular());
        assertEquals(0.0, m.masaMolar(), 1e-9);
        assertFalse(m.esValida());
        assertTrue(m.validar().get(0).contains("no tiene"));
        assertFalse(m.tieneAnillos());
    }

    @Test
    void unicoAtomo() {
        Molecula metano = MoleculasDePrueba.unAtomo("Metano", Elemento.C);
        assertEquals("CH4", metano.formulaMolecular());
        assertEquals(16.043, metano.masaMolar(), 0.01);
        assertTrue(metano.esValida());
        assertTrue(metano.vecinos(metano.buscarAtomo(1)).isEmpty());
    }

    // ───── Enlaces y validación de valencia ─────

    @Test
    void oxigenoNoSuperaValencia() {
        Molecula m = new Molecula("M");
        Atomo o = m.agregarAtomo(Elemento.O);
        m.enlazar(o, m.agregarAtomo(Elemento.C));
        m.enlazar(o, m.agregarAtomo(Elemento.C));
        Atomo c3 = m.agregarAtomo(Elemento.C);
        assertThrows(IllegalArgumentException.class, () -> m.enlazar(o, c3));
        assertEquals(2, m.cantidadEnlaces());
        assertEquals(2, m.valenciaUsada(o));
    }

    @Test
    void carbonoNoSuperaValencia() {
        Molecula m = MoleculasDePrueba.neopentano();
        Atomo centro = m.buscarAtomo(1);
        Atomo extra = m.agregarAtomo(Elemento.C);
        assertThrows(IllegalArgumentException.class, () -> m.enlazar(centro, extra));
    }

    @Test
    void dobleEnlaceConsumeDos() {
        Molecula m = MoleculasDePrueba.acidoAcetico();
        Atomo c2 = m.buscarAtomo(2);
        assertEquals(4, m.valenciaUsada(c2));
        assertEquals(0, m.hidrogenosImplicitos(c2));
        assertThrows(IllegalArgumentException.class, () -> m.enlazar(c2, m.agregarAtomo(Elemento.H)));
    }

    @Test
    void enlaceTriple() {
        Molecula m = new Molecula("Acetileno");
        m.enlazar(m.agregarAtomo(Elemento.C), m.agregarAtomo(Elemento.C), TipoEnlace.TRIPLE);
        assertEquals("C2H2", m.formulaMolecular());
        assertEquals(3, m.obtenerEnlace(m.buscarAtomo(1), m.buscarAtomo(2)).getOrden());
    }

    @Test
    void enlacesInvalidos() {
        Molecula m = MoleculasDePrueba.etanol();
        Atomo c1 = m.buscarAtomo(1), c2 = m.buscarAtomo(2);
        assertThrows(IllegalArgumentException.class, () -> m.enlazar(c1, c1));
        assertThrows(IllegalArgumentException.class, () -> m.enlazar(c1, c2));
        assertThrows(IllegalArgumentException.class, () -> m.enlazar(c2, c1));
        assertThrows(IllegalArgumentException.class, () -> m.enlazar(c1, new Atomo(99, Elemento.C)));
        assertThrows(IllegalArgumentException.class, () -> m.enlazar(c1, null));
        assertThrows(IllegalArgumentException.class, () -> m.enlazar(c1, m.agregarAtomo(Elemento.C), null));
    }

    // ───── Consultas sobre átomos ─────

    @Test
    void consultasDeVecinos() {
        Molecula m = MoleculasDePrueba.etanol();
        Atomo c1 = m.buscarAtomo(1), c2 = m.buscarAtomo(2), o = m.buscarAtomo(3);
        assertEquals(List.of(c1, o), m.vecinos(c2));
        assertEquals(List.of(o), m.vecinosDe(c2, Elemento.O));
        assertEquals(2, m.grado(c2));
        assertNotNull(m.obtenerEnlace(c2, o));
        assertNotNull(m.obtenerEnlace(o, c2));
        assertNull(m.obtenerEnlace(c1, o));
        assertTrue(m.estanEnlazados(o, c2));
        assertFalse(m.estanEnlazados(c1, o));
    }

    @Test
    void hidrogenos() {
        Molecula etanol = MoleculasDePrueba.etanol();
        assertEquals(3, etanol.hidrogenosImplicitos(etanol.buscarAtomo(1)));
        assertEquals(1, etanol.hidrogenosTotales(etanol.buscarAtomo(3)));

        Molecula agua = new Molecula("Agua");
        Atomo o = agua.agregarAtomo(Elemento.O);
        Atomo h = agua.agregarAtomo(Elemento.H);
        agua.enlazar(o, h);
        assertEquals(1, agua.hidrogenosImplicitos(o));
        assertEquals(2, agua.hidrogenosTotales(o));
        assertEquals(0, agua.hidrogenosImplicitos(h));
        assertEquals("H2O", agua.formulaMolecular());
    }

    @Test
    void buscarAtomoInexistente() {
        Molecula m = MoleculasDePrueba.etanol();
        assertEquals(Elemento.O, m.buscarAtomo(3).getElemento());
        assertNull(m.buscarAtomo(42));
        assertFalse(m.contiene(new Atomo(42, Elemento.C)));
        assertFalse(m.contiene(null));
    }

    // ───── Validación general ─────

    @Test
    void noConexaEsInvalida() {
        Molecula m = new Molecula("Fragmentos");
        m.agregarAtomos(Elemento.C, 2);
        assertFalse(m.esValida());
        assertTrue(m.validar().get(0).contains("conexa"));
    }

    // ───── Datos derivados ─────

    @Test
    void formulaHill() {
        assertEquals("C2H6O", MoleculasDePrueba.etanol().formulaMolecular());
        assertEquals("C2H4O2", MoleculasDePrueba.acidoAcetico().formulaMolecular());
        assertEquals("C2H5NO2", MoleculasDePrueba.glicina().formulaMolecular());
        assertEquals("C6H6", MoleculasDePrueba.benceno().formulaMolecular());
        assertEquals("C6H12", MoleculasDePrueba.ciclohexano().formulaMolecular());
        assertEquals("H3N", MoleculasDePrueba.unAtomo("Amoniaco", Elemento.N).formulaMolecular());
        assertEquals("H2O", MoleculasDePrueba.unAtomo("Agua", Elemento.O).formulaMolecular());

        Molecula clorometano = new Molecula("Clorometano");
        clorometano.enlazar(clorometano.agregarAtomo(Elemento.C), clorometano.agregarAtomo(Elemento.CL));
        assertEquals("CH3Cl", clorometano.formulaMolecular());
    }

    @Test
    void masaMolar() {
        assertEquals(46.069, MoleculasDePrueba.etanol().masaMolar(), 0.01);
        assertEquals(60.052, MoleculasDePrueba.acidoAcetico().masaMolar(), 0.01);
    }

    @Test
    void tieneAnillos() {
        assertTrue(MoleculasDePrueba.benceno().tieneAnillos());
        assertTrue(MoleculasDePrueba.decalina().tieneAnillos());
        assertFalse(MoleculasDePrueba.cadena("Butano", 4).tieneAnillos());
        assertFalse(MoleculasDePrueba.isobutano().tieneAnillos());
    }

    // ───── Vistas para los algoritmos ─────

    @Test
    void comoGrafoDirigido() {
        Molecula m = MoleculasDePrueba.etanol();
        DirectedGraph<Atomo, Enlace> d = m.comoGrafoDirigido();
        assertEquals(3, d.cantidadDeVertices());
        assertEquals(4, d.cantidadDeAristas());
        assertTrue(d.existeArista(d.construirComparable(m.buscarAtomo(2)), d.construirComparable(m.buscarAtomo(1))));
        assertTrue(d.existeArista(d.construirComparable(m.buscarAtomo(1)), d.construirComparable(m.buscarAtomo(2))));
    }

    @Test
    void subgrafoCarbonos() {
        UndirectedGraph<Atomo, Enlace> esq = MoleculasDePrueba.acidoAcetico().subgrafoCarbonos();
        assertEquals(2, esq.cantidadDeVertices());
        assertEquals(1, esq.cantidadDeAristas());
        assertEquals(0, MoleculasDePrueba.unAtomo("Agua", Elemento.O).subgrafoCarbonos().cantidadDeVertices());
    }

}
