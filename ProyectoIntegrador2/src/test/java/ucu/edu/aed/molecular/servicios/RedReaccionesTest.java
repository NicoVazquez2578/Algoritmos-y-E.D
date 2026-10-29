package ucu.edu.aed.molecular.servicios;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucu.edu.aed.molecular.sintesis.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/*
  Red de prueba (en stock: Etileno, Agua, Oxigeno, Metanol, CO, Hidrogeno):
    R1  Etileno + Agua        -> Etanol          costo 120 riesgo 6
    R2  Etanol + Oxigeno      -> Acido           costo 300 riesgo 2
    R3  Etanol + Oxigeno      -> Acetaldehido    costo  50 riesgo 5
    R4  Acetaldehido + Oxig.  -> Acido           costo  60 riesgo 4
    R5  Metanol + CO          -> Acido           costo 260 riesgo 9
    R6  Acido + Etanol        -> Acetato + Agua  costo  80 riesgo 3
    R8  Benceno + Hidrogeno   -> Ciclohexano     costo 200 riesgo 6   (Benceno NO está en stock)
  Rutas al ácido: R1-R3-R4 (230, riesgo 15) | R5 (260, riesgo 9) | R1-R2 (420, riesgo 8)
*/
class RedReaccionesTest {

    private RedReacciones red;
    private Compuesto etileno, agua, oxigeno, metanol, co, hidrogeno;
    private Compuesto etanol, acetaldehido, acido, acetato, benceno, ciclohexano;
    private Reaccion r1, r2, r3, r4, r5, r6, r8;

    @BeforeEach
    void armarRed() {
        red = new RedReacciones();
        etileno = enStock("Etileno"); agua = enStock("Agua"); oxigeno = enStock("Oxigeno");
        metanol = enStock("Metanol"); co = enStock("CO"); hidrogeno = enStock("Hidrogeno");
        etanol = nuevo("Etanol"); acetaldehido = nuevo("Acetaldehido"); acido = nuevo("Acido acetico");
        acetato = nuevo("Acetato de etilo"); benceno = nuevo("Benceno"); ciclohexano = nuevo("Ciclohexano");

        r1 = reaccion("R1", List.of(etileno, agua), List.of(etanol), 120, 6);
        r2 = reaccion("R2", List.of(etanol, oxigeno), List.of(acido), 300, 2);
        r3 = reaccion("R3", List.of(etanol, oxigeno), List.of(acetaldehido), 50, 5);
        r4 = reaccion("R4", List.of(acetaldehido, oxigeno), List.of(acido), 60, 4);
        r5 = reaccion("R5", List.of(metanol, co), List.of(acido), 260, 9);
        r6 = reaccion("R6", List.of(acido, etanol), List.of(acetato, agua), 80, 3);
        r8 = reaccion("R8", List.of(benceno, hidrogeno), List.of(ciclohexano), 200, 6);
    }

    private static Compuesto enStock(String n) { return new Compuesto(n, null, null, true); }
    private static Compuesto nuevo(String n) { return new Compuesto(n, null, null); }

    private Reaccion reaccion(String id, List<Compuesto> in, List<Compuesto> out, double costo, double riesgo) {
        Reaccion r = new Reaccion(id, id, in, out, costo, riesgo);
        red.registrarReaccion(r);
        return r;
    }

    private static List<String> ids(RutaSintesis ruta) {
        List<String> res = new ArrayList<>();
        for (Reaccion r : ruta.getPasos()) res.add(r.getId());
        return res;
    }

    // ───── Registro ─────

    @Test
    void idDuplicado() {
        assertThrows(IllegalArgumentException.class,
                () -> red.registrarReaccion(new Reaccion("R1", "", List.of(agua), List.of(etanol), 1, 1)));
    }

    // ───── Obtenibles (lógica Y) ─────

    @Test
    void obtenibles() {
        Set<Compuesto> obt = red.compuestosObtenibles();
        assertTrue(obt.containsAll(List.of(etileno, etanol, acetaldehido, acido, acetato)));
        assertFalse(obt.contains(benceno));
        assertFalse(obt.contains(ciclohexano));
    }

    @Test
    void logicaY() {
        assertTrue(red.esObtenible(hidrogeno));
        assertFalse(red.esObtenible(ciclohexano));
        assertFalse(red.reaccionesHabilitadas().contains(r8));
        assertTrue(red.reaccionesHabilitadas().contains(r6));
    }

    // ───── Mejor ruta (Dijkstra) ─────

    @Test
    void menorCosto() {
        RutaSintesis ruta = red.mejorRuta(acido, Criterio.COSTO);
        assertEquals(List.of("R1", "R3", "R4"), ids(ruta));
        assertEquals(230, ruta.costoTotal(), 1e-9);
    }

    @Test
    void menorRiesgo() {
        RutaSintesis ruta = red.mejorRuta(acido, Criterio.RIESGO);
        assertEquals(List.of("R1", "R2"), ids(ruta));
        assertEquals(8, ruta.riesgoTotal(), 1e-9);
    }

    @Test
    void combinado() {
        assertEquals(List.of("R5"), ids(red.mejorRuta(acido, Criterio.combinado(0.5, 300))));
    }

    @Test
    void sinRutasFalsas() {
        assertNotEquals(List.of("R4"), ids(red.mejorRuta(acido, Criterio.COSTO)));
        for (RutaSintesis r : red.rutasAlternativas(acido)) {
            assertNotEquals(List.of("R4"), ids(r));
            assertNotEquals(List.of("R2"), ids(r));
        }
    }

    @Test
    void mejorRutaBordes() {
        assertTrue(red.mejorRuta(agua, Criterio.COSTO).esVacia());
        assertNull(red.mejorRuta(ciclohexano, Criterio.COSTO));
        assertNull(red.mejorRuta(null, Criterio.COSTO));
    }

    @Test
    void sinRutaLineal() {
        RutaSintesis ruta = red.mejorRuta(acetato, Criterio.COSTO);
        assertNotNull(ruta);
        assertEquals("R6", ids(ruta).get(ids(ruta).size() - 1));
    }

    // ───── Rutas alternativas ─────

    @Test
    void alternativas() {
        List<RutaSintesis> rutas = red.rutasAlternativas(acido);
        assertEquals(3, rutas.size());
        assertEquals(List.of("R1", "R3", "R4"), ids(rutas.get(0)));
        assertEquals(List.of("R5"), ids(rutas.get(1)));
        assertEquals(List.of("R1", "R2"), ids(rutas.get(2)));
    }

    @Test
    void alternativasBordes() {
        assertTrue(red.rutasAlternativas(agua).isEmpty());
        assertTrue(red.rutasAlternativas(ciclohexano).isEmpty());
        assertTrue(red.rutasAlternativas(null).isEmpty());
    }

    @Test
    void empateDeCosto() {
        RedReacciones r = new RedReacciones();
        Compuesto x = enStock("X"), y = nuevo("Y");
        r.registrarReaccion(new Reaccion("Ra", "", List.of(x), List.of(y), 10, 1));
        r.registrarReaccion(new Reaccion("Rb", "", List.of(x), List.of(y), 10, 2));
        assertEquals(10, r.mejorRuta(y, Criterio.COSTO).costoTotal(), 1e-9);
        assertEquals(2, r.rutasAlternativas(y).size());
        assertEquals(List.of("Ra"), ids(r.mejorRuta(y, Criterio.RIESGO)));
    }

    // ───── Plan completo y orden ─────

    @Test
    void planAcetato() {
        RutaSintesis plan = red.planDeSintesis(acetato, Criterio.COSTO);
        List<String> pasos = ids(plan);
        assertEquals(Set.of("R1", "R3", "R4", "R6"), Set.copyOf(pasos));
        assertEquals("R1", pasos.get(0));
        assertEquals("R6", pasos.get(3));
        assertTrue(pasos.indexOf("R3") < pasos.indexOf("R4"));
        assertEquals(310, plan.costoTotal(), 1e-9);
    }

    @Test
    void planBordes() {
        assertNull(red.planDeSintesis(ciclohexano, Criterio.COSTO));
        assertTrue(red.planDeSintesis(agua, Criterio.COSTO).esVacia());
    }

    @Test
    void productoEnStockNoGeneraCiclo() {
        List<Reaccion> orden = red.ordenDeEjecucion(List.of(r6, r1, r3, r4));
        assertEquals(r1, orden.get(0));
        assertEquals(r6, orden.get(3));
    }
}
