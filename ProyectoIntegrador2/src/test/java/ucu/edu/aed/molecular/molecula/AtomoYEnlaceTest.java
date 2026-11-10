package ucu.edu.aed.molecular.molecula;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AtomoYEnlaceTest {

    @Test
    void igualdadPorId() {
        Atomo c1 = new Atomo(1, Elemento.C);
        assertEquals(c1, new Atomo(1, Elemento.C));
        assertEquals(c1.hashCode(), new Atomo(1, Elemento.C).hashCode());
        assertNotEquals(c1, new Atomo(2, Elemento.C));
        assertNotEquals(c1, null);
        assertEquals("C1", c1.toString());
    }

    @Test
    void desdeSimbolo() {
        assertEquals(Elemento.CL, Elemento.desdeSimbolo("Cl"));
        assertEquals(Elemento.CL, Elemento.desdeSimbolo("cl"));
        assertEquals(Elemento.O, Elemento.desdeSimbolo("O"));
        assertThrows(IllegalArgumentException.class, () -> Elemento.desdeSimbolo("Xx"));
    }

    @Test
    void enlacePesoUno() {
        for (TipoEnlace t : TipoEnlace.values()) {
            Enlace e = new Enlace(t);
            assertEquals(1.0, e.getWeight(), 1e-9);
            assertEquals(t.getOrden(), e.getOrden());
        }
        assertEquals(1, TipoEnlace.SIMPLE.getOrden());
        assertEquals(2, TipoEnlace.DOBLE.getOrden());
        assertEquals(3, TipoEnlace.TRIPLE.getOrden());
    }

}
