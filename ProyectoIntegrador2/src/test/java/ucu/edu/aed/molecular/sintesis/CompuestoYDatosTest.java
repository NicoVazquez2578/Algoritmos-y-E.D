package ucu.edu.aed.molecular.sintesis;

import org.junit.jupiter.api.Test;
import ucu.edu.aed.molecular.MoleculasDePrueba;


import static org.junit.jupiter.api.Assertions.*;

class CompuestoYDatosTest {

    // ───── Compuesto ─────

    @Test
    void formulaYMasa() {
        Compuesto etanol = new Compuesto("Etanol", MoleculasDePrueba.etanol(), null);
        assertEquals("C2H6O", etanol.formula());
        assertEquals(46.069, etanol.masaMolar(), 0.01);
        assertTrue(etanol.tieneEstructura());
    }

    @Test
    void sinEstructura() {
        Compuesto co = new Compuesto("Monoxido de carbono", null, null);
        assertEquals("?", co.formula());
        assertTrue(Double.isNaN(co.masaMolar()));
        assertFalse(co.tieneEstructura());
    }

    @Test
    void igualdad() {
        Compuesto a = new Compuesto("Etanol", null, null);
        Compuesto b = new Compuesto("ETANOL", null, null, true);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, new Compuesto("Metanol", null, null));
    }

    // ───── DatosCientificos ─────

    @Test
    void escalaNfpa() {
        assertThrows(IllegalArgumentException.class, () -> new DatosCientificos(0, 0, -1, 0, null));
        assertThrows(IllegalArgumentException.class, () -> new DatosCientificos(0, 0, 5, 0, null));
        assertThrows(IllegalArgumentException.class, () -> new DatosCientificos(0, 0, 0, 5, null));
        assertEquals(4, new DatosCientificos(0, 0, 4, 4, null).getToxicidad());
    }

    @Test
    void estado() {
        assertEquals("sólido", new DatosCientificos(300, 100, 0, 0, null).estadoA25Grados());
        assertEquals("líquido", new DatosCientificos(78.4, -114, 0, 0, null).estadoA25Grados());
        assertEquals("gas", new DatosCientificos(-161, -182, 0, 0, null).estadoA25Grados());
        assertEquals("desconocido", new DatosCientificos(Double.NaN, 233, 0, 0, null).estadoA25Grados());
    }

}
