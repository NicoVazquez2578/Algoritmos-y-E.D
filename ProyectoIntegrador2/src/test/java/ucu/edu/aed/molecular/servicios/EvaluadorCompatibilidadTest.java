package ucu.edu.aed.molecular.servicios;

import org.junit.jupiter.api.Test;
import ucu.edu.aed.molecular.MoleculasDePrueba;
import ucu.edu.aed.molecular.compatibilidad.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EvaluadorCompatibilidadTest {

    private final EvaluadorCompatibilidad ev = new EvaluadorCompatibilidad(new AnalizadorEstructural());

    private static PerfilEstructural perfil(int cadena, long wiener, int terciarios) {
        return new PerfilEstructural("P", cadena, Map.of(TipoCarbono.TERCIARIO, terciarios), wiener);
    }

    private static ParametrosObjetivo objetivo() {
        return new ParametrosObjetivo().conCadena(new Rango(5, 6)).conWiener(new Rango(30, 40))
                .conCarbonos(TipoCarbono.TERCIARIO, new Rango(0, 0));
    }

    @Test
    void cumpleTodo() {
        ResultadoCompatibilidad r = ev.evaluar(perfil(6, 35, 0), objetivo());
        assertEquals(1.0, r.getPuntaje(), 1e-9);
        assertEquals(3, r.getCriteriosCumplidos());
        assertTrue(r.cumpleTodo());
    }

    @Test
    void puntajeConDesvios() {
        // cadena 4 en [5,6]: desvío 1 ; wiener 35 ok ; 1 terciario en [0,0]: desvío 1  -> 1/(1+2)
        ResultadoCompatibilidad r = ev.evaluar(perfil(4, 35, 1), objetivo());
        assertEquals(1.0 / 3, r.getPuntaje(), 1e-9);
        assertEquals(1, r.getCriteriosCumplidos());
        assertEquals(3, r.getCriteriosTotales());
    }

    @Test
    void ordenar() {
        List<ResultadoCompatibilidad> ranking = ev.ordenar(List.of(
                MoleculasDePrueba.cadena("Butano", 4),
                MoleculasDePrueba.neopentano(),
                MoleculasDePrueba.cadena("Hexano", 6),
                MoleculasDePrueba.metilpentano()), objetivo());
        assertEquals(4, ranking.size());
        assertEquals("Hexano", ranking.get(0).getNombreMolecula());
        assertEquals("2-Metilpentano", ranking.get(1).getNombreMolecula());
        assertEquals("Neopentano", ranking.get(3).getNombreMolecula());
        for (int i = 0; i + 1 < ranking.size(); i++)
            assertTrue(ranking.get(i).getPuntaje() >= ranking.get(i + 1).getPuntaje());
    }

    @Test
    void listaVacia() {
        assertTrue(ev.ordenar(List.of(), objetivo()).isEmpty());
    }
}
