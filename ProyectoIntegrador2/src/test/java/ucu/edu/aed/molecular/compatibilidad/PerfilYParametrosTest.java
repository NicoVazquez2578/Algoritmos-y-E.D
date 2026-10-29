package ucu.edu.aed.molecular.compatibilidad;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PerfilYParametrosTest {

    // ───── PerfilEstructural ─────

    @Test
    void completaTipos() {
        PerfilEstructural p = new PerfilEstructural("Isobutano", 3, Map.of(TipoCarbono.PRIMARIO, 3, TipoCarbono.TERCIARIO, 1), 9);
        assertEquals(4, p.getCarbonos().size());
        assertEquals(0, p.getCarbonos(TipoCarbono.SECUNDARIO));
        assertEquals(4, p.totalCarbonos());
        assertThrows(UnsupportedOperationException.class, () -> p.getCarbonos().put(TipoCarbono.SECUNDARIO, 1));
    }

    // ───── ParametrosObjetivo ─────

    @Test
    void cantidadCriterios() {
        assertEquals(0, new ParametrosObjetivo().cantidadCriterios());
        ParametrosObjetivo p = new ParametrosObjetivo()
                .conCadena(new Rango(4, 6))
                .conWiener(new Rango(10, 40))
                .conCarbonos(TipoCarbono.TERCIARIO, new Rango(0, 1))
                .conCarbonos(TipoCarbono.CUATERNARIO, new Rango(0, 0));
        assertEquals(4, p.cantidadCriterios());
        assertNotNull(p.getCadena());
        assertThrows(UnsupportedOperationException.class, () -> p.getCarbonos().clear());
    }

    // ───── ResultadoCompatibilidad ─────

    private static ResultadoCompatibilidad resultado(String nombre, double puntaje, int cumplidos) {
        return new ResultadoCompatibilidad(new PerfilEstructural(nombre, 1, Map.of(), 0), puntaje, cumplidos, 3);
    }

    @Test
    void puntajeInvalido() {
        assertThrows(IllegalArgumentException.class, () -> resultado("X", -0.1, 0));
        assertThrows(IllegalArgumentException.class, () -> resultado("X", 1.1, 0));
    }

    @Test
    void orden() {
        ResultadoCompatibilidad alto = resultado("Zeta", 0.9, 1);
        ResultadoCompatibilidad bajo = resultado("Alfa", 0.2, 3);
        ResultadoCompatibilidad empateMasCumple = resultado("Beta", 0.5, 2);
        ResultadoCompatibilidad empateMenosCumple = resultado("Alfa", 0.5, 1);
        ResultadoCompatibilidad empateTotalA = resultado("Gamma", 0.5, 1);

        List<ResultadoCompatibilidad> lista = new ArrayList<>(List.of(bajo, empateTotalA, empateMenosCumple, alto, empateMasCumple));
        Collections.sort(lista);
        assertEquals(List.of(alto, empateMasCumple, empateMenosCumple, empateTotalA, bajo), lista);
    }

}
