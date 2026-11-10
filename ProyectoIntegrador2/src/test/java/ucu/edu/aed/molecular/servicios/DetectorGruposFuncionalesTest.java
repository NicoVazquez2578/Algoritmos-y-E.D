package ucu.edu.aed.molecular.servicios;

import org.junit.jupiter.api.Test;
import ucu.edu.aed.molecular.MoleculasDePrueba;
import ucu.edu.aed.molecular.molecula.Coincidencia;
import ucu.edu.aed.molecular.molecula.Elemento;
import ucu.edu.aed.molecular.molecula.Molecula;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ucu.edu.aed.molecular.servicios.DetectorGruposFuncionales.*;

class DetectorGruposFuncionalesTest {

    private final DetectorGruposFuncionales det = new DetectorGruposFuncionales();

    @Test
    void alcohol() {
        Molecula etanol = MoleculasDePrueba.etanol();
        assertEquals(Set.of(ALCOHOL), det.gruposPresentes(etanol));
        assertEquals(Set.of(ALCOHOL), det.gruposPresentes(MoleculasDePrueba.metanol()));
        Coincidencia c = det.detectar(etanol).get(0);
        assertEquals(List.of(etanol.buscarAtomo(2), etanol.buscarAtomo(3)), c.getAtomos());
    }

    @Test
    void acido() {
        assertEquals(Set.of(ACIDO), det.gruposPresentes(MoleculasDePrueba.acidoAcetico()));
    }

    @Test
    void amina() {
        assertEquals(Set.of(AMINA), det.gruposPresentes(MoleculasDePrueba.etilamina()));
        assertFalse(det.contiene(MoleculasDePrueba.acetamida(), AMINA));
    }

    @Test
    void variosGrupos() {
        assertEquals(Set.of(AMINA, ACIDO), det.gruposPresentes(MoleculasDePrueba.glicina()));
    }

    @Test
    void carbonilo() {
        assertEquals(Set.of(CARBONILO), det.gruposPresentes(MoleculasDePrueba.acetaldehido()));
        assertEquals(Set.of(CARBONILO), det.gruposPresentes(MoleculasDePrueba.acetona()));
    }

    @Test
    void anillos() {
        assertEquals(Set.of(ANILLO), det.gruposPresentes(MoleculasDePrueba.benceno()));
        assertEquals(Set.of(ANILLO), det.gruposPresentes(MoleculasDePrueba.ciclohexano()));
        assertEquals(1, det.contarAnillos(MoleculasDePrueba.benceno()));
        assertEquals(2, det.contarAnillos(MoleculasDePrueba.decalina()));
        assertEquals(0, det.contarAnillos(MoleculasDePrueba.cadena("Hexano", 6)));
    }

    @Test
    void fenol() {
        Molecula fenol = MoleculasDePrueba.fenol();
        assertEquals(Set.of(ANILLO), det.gruposPresentes(fenol));
        Coincidencia anillo = det.detectar(fenol).get(0);
        assertEquals(6, anillo.getAtomos().size());
        assertTrue(anillo.getAtomos().stream().noneMatch(a -> a.es(Elemento.O)));
    }

    @Test
    void sinGrupos() {
        assertTrue(det.detectar(MoleculasDePrueba.cadena("Butano", 4)).isEmpty());
        assertTrue(det.detectar(MoleculasDePrueba.isobutano()).isEmpty());
        assertTrue(det.detectar(MoleculasDePrueba.acetatoDeEtilo()).isEmpty());
        assertTrue(det.detectar(MoleculasDePrueba.unAtomo("Agua", Elemento.O)).isEmpty());
        assertTrue(det.detectar(new Molecula("Vacia")).isEmpty());
        assertEquals(0, det.contarAnillos(new Molecula("Vacia")));
    }

}
