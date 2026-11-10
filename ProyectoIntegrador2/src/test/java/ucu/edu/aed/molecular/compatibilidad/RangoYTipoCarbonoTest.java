package ucu.edu.aed.molecular.compatibilidad;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RangoYTipoCarbonoTest {

    // ───── Rango ─────

    @Test
    void contiene() {
        Rango r = new Rango(4, 6);
        assertTrue(r.contiene(4));
        assertTrue(r.contiene(5));
        assertTrue(r.contiene(6));
        assertFalse(r.contiene(3.99));
        assertFalse(r.contiene(6.01));
    }

    @Test
    void desviacion() {
        Rango r = new Rango(4, 6);          // ancho 2
        assertEquals(0.0, r.desviacion(5), 1e-9);
        assertEquals(1.0, r.desviacion(8), 1e-9);   // (8-6)/2
        assertEquals(1.5, r.desviacion(1), 1e-9);   // (4-1)/2
    }

    @Test
    void anchoMinimo() {
        Rango r = Rango.exacto(3);
        assertEquals(1.0, r.ancho(), 1e-9);
        assertEquals(2.0, r.desviacion(5), 1e-9);
    }

    // ───── TipoCarbono ─────

    @Test
    void clasificacion() {
        assertEquals(TipoCarbono.PRIMARIO, TipoCarbono.segunVecinosCarbono(0));
        assertEquals(TipoCarbono.PRIMARIO, TipoCarbono.segunVecinosCarbono(1));
        assertEquals(TipoCarbono.SECUNDARIO, TipoCarbono.segunVecinosCarbono(2));
        assertEquals(TipoCarbono.TERCIARIO, TipoCarbono.segunVecinosCarbono(3));
        assertEquals(TipoCarbono.CUATERNARIO, TipoCarbono.segunVecinosCarbono(4));
    }

}
