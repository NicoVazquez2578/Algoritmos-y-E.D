package ucu.edu.aed.molecular.servicios;

import org.junit.jupiter.api.Test;
import ucu.edu.aed.molecular.MoleculasDePrueba;
import ucu.edu.aed.molecular.compatibilidad.PerfilEstructural;
import ucu.edu.aed.molecular.compatibilidad.TipoCarbono;
import ucu.edu.aed.molecular.desafio3.OptimizacionWiener;
import ucu.edu.aed.molecular.molecula.Elemento;
import ucu.edu.aed.molecular.molecula.Molecula;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnalizadorEstructuralTest {

    private final AnalizadorEstructural an = new AnalizadorEstructural();

    // ───── Cadena principal ─────

    @Test
    void cadenaLineal() {
        assertEquals(1, an.longitudCadenaPrincipal(MoleculasDePrueba.unAtomo("Metano", Elemento.C)));
        assertEquals(2, an.longitudCadenaPrincipal(MoleculasDePrueba.etanol()));
        assertEquals(6, an.longitudCadenaPrincipal(MoleculasDePrueba.cadena("Hexano", 6)));
    }

    @Test
    void cadenaRamificada() {
        assertEquals(5, an.longitudCadenaPrincipal(MoleculasDePrueba.metilpentano()));
        assertEquals(3, an.longitudCadenaPrincipal(MoleculasDePrueba.isobutano()));
        assertEquals(3, an.longitudCadenaPrincipal(MoleculasDePrueba.neopentano()));
    }

    @Test
    void cadenaConAnillos() {
        assertEquals(6, an.longitudCadenaPrincipal(MoleculasDePrueba.benceno()));
        assertEquals(10, an.longitudCadenaPrincipal(MoleculasDePrueba.decalina()));
    }

    @Test
    void cadenaSinCarbonos() {
        assertEquals(0, an.longitudCadenaPrincipal(MoleculasDePrueba.unAtomo("Agua", Elemento.O)));
        assertEquals(0, an.longitudCadenaPrincipal(new Molecula("Vacia")));
    }

    // ───── Tipos de carbono ─────

    @Test
    void tiposDeCarbono() {
        Map<TipoCarbono, Integer> propano = an.clasificarCarbonos(MoleculasDePrueba.cadena("Propano", 3));
        assertEquals(2, propano.get(TipoCarbono.PRIMARIO));
        assertEquals(1, propano.get(TipoCarbono.SECUNDARIO));

        Map<TipoCarbono, Integer> iso = an.clasificarCarbonos(MoleculasDePrueba.isobutano());
        assertEquals(3, iso.get(TipoCarbono.PRIMARIO));
        assertEquals(1, iso.get(TipoCarbono.TERCIARIO));

        Map<TipoCarbono, Integer> neo = an.clasificarCarbonos(MoleculasDePrueba.neopentano());
        assertEquals(4, neo.get(TipoCarbono.PRIMARIO));
        assertEquals(1, neo.get(TipoCarbono.CUATERNARIO));

        assertEquals(6, an.clasificarCarbonos(MoleculasDePrueba.benceno()).get(TipoCarbono.SECUNDARIO));
    }

    // ───── Índice de Wiener ─────

    @Test
    void wiener() {
        assertEquals(0, an.indiceWiener(MoleculasDePrueba.unAtomo("Metano", Elemento.C)));
        assertEquals(10, an.indiceWiener(MoleculasDePrueba.cadena("Butano", 4)));
        assertEquals(16, an.indiceWiener(MoleculasDePrueba.neopentano()));
        assertEquals(27, an.indiceWiener(MoleculasDePrueba.benceno()));
    }

    @Test
    void wienerFormulaYFloyd() {
        Molecula c10 = MoleculasDePrueba.cadena("Decano", 10);
        assertEquals(165, an.indiceWiener(c10));
        Molecula ramificada = MoleculasDePrueba.metilpentano();
        assertEquals(OptimizacionWiener.calcularConFloyd(ramificada), an.indiceWiener(ramificada));
    }

    // ───── Perfil ─────

    @Test
    void perfil() {
        PerfilEstructural p = an.perfil(MoleculasDePrueba.metilpentano());
        assertEquals("2-Metilpentano", p.getNombreMolecula());
        assertEquals(5, p.getLongitudCadena());
        assertEquals(1, p.getCarbonos(TipoCarbono.TERCIARIO));
        assertEquals(32, p.getIndiceWiener());
    }
}
