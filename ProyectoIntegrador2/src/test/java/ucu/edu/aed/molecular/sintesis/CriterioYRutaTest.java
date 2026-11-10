package ucu.edu.aed.molecular.sintesis;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CriterioYRutaTest {

    private final Compuesto x = new Compuesto("X", null, null, true);
    private final Compuesto y = new Compuesto("Y", null, null);
    private final Compuesto z = new Compuesto("Z", null, null);
    private final Reaccion r1 = new Reaccion("R1", "", List.of(x), List.of(y), 120, 6);
    private final Reaccion r2 = new Reaccion("R2", "", List.of(y), List.of(z), 80, 2);

    // ───── Criterio ─────

    @Test
    void criterioCombinado() {
        assertEquals(120.0 / 200, Criterio.combinado(1, 200).peso(r1), 1e-9);
        assertEquals(6.0 / 10, Criterio.combinado(0, 200).peso(r1), 1e-9);
        assertEquals(0.5 * 0.6 + 0.5 * 0.6, Criterio.combinado(0.5, 200).peso(r1), 1e-9);
    }

    @Test
    void criterioCombinadoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> Criterio.combinado(-0.1, 100));
        assertThrows(IllegalArgumentException.class, () -> Criterio.combinado(1.1, 100));
        assertThrows(IllegalArgumentException.class, () -> Criterio.combinado(0.5, 0));
    }

    // ───── PasoSintesis ─────

    @Test
    void pasoSintesis() {
        PasoSintesis entrada = PasoSintesis.entrada(r1, Criterio.RIESGO);
        PasoSintesis salida = PasoSintesis.salida(r1);
        assertEquals(6, entrada.getWeight(), 1e-9);
        assertTrue(entrada.esEntrada());
        assertEquals(0, salida.getWeight(), 1e-9);
        assertFalse(salida.esEntrada());
        assertEquals(r1, salida.getReaccion());
    }

    // ───── RutaSintesis ─────

    @Test
    void rutaVacia() {
        RutaSintesis r = new RutaSintesis(x, List.of());
        assertTrue(r.esVacia());
        assertEquals(0, r.costoTotal(), 1e-9);
        assertEquals(0, r.riesgoTotal(), 1e-9);
        assertEquals(0, r.riesgoMaximo(), 1e-9);
    }

    @Test
    void totales() {
        RutaSintesis r = new RutaSintesis(z, List.of(r1, r2));
        assertEquals(2, r.cantidadPasos());
        assertEquals(200, r.costoTotal(), 1e-9);
        assertEquals(8, r.riesgoTotal(), 1e-9);
        assertEquals(6, r.riesgoMaximo(), 1e-9);
        assertEquals(8, r.pesoTotal(Criterio.RIESGO), 1e-9);
        assertEquals(z, r.getObjetivo());
    }

    @Test
    void orden() {
        Reaccion barata = new Reaccion("B", "", List.of(x), List.of(z), 100, 9);
        Reaccion segura = new Reaccion("S", "", List.of(x), List.of(z), 100, 1);
        Reaccion mitad1 = new Reaccion("M1", "", List.of(x), List.of(y), 50, 0.5);
        Reaccion mitad2 = new Reaccion("M2", "", List.of(y), List.of(z), 50, 0.5);
        RutaSintesis caraRiesgosa = new RutaSintesis(z, List.of(barata));
        RutaSintesis caraSegura = new RutaSintesis(z, List.of(segura));
        RutaSintesis dosPasos = new RutaSintesis(z, List.of(mitad1, mitad2));
        RutaSintesis unPaso = new RutaSintesis(z, List.of(segura));
        RutaSintesis masBarata = new RutaSintesis(z, List.of(r2));

        List<RutaSintesis> rutas = new ArrayList<>(List.of(caraRiesgosa, caraSegura, masBarata));
        Collections.sort(rutas);
        assertEquals(List.of(masBarata, caraSegura, caraRiesgosa), rutas);
        assertTrue(unPaso.compareTo(dosPasos) < 0);   // mismo costo (100) y riesgo (1): gana la de menos pasos
    }

}
