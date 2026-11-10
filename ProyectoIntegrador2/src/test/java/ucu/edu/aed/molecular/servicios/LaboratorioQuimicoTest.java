package ucu.edu.aed.molecular.servicios;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucu.edu.aed.molecular.MoleculasDePrueba;
import ucu.edu.aed.molecular.app.DatosEjemplo;
import ucu.edu.aed.molecular.molecula.Atomo;
import ucu.edu.aed.molecular.molecula.Elemento;
import ucu.edu.aed.molecular.molecula.Molecula;
import ucu.edu.aed.molecular.sintesis.Compuesto;
import ucu.edu.aed.molecular.sintesis.Reaccion;
import ucu.edu.aed.molecular.sintesis.RutaSintesis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/* Prueba la fachada y las consultas propias sobre los datos de ejemplo del sistema. */
class LaboratorioQuimicoTest {

    private LaboratorioQuimico lab;

    @BeforeEach
    void cargar() { lab = DatosEjemplo.crearLaboratorio(); }

    private static List<String> nombres(List<Compuesto> cs) {
        List<String> res = new ArrayList<>();
        for (Compuesto c : cs) res.add(c.getNombre());
        return res;
    }

    @Test
    void datosDeEjemplo() {
        assertEquals(22, lab.getMoleculas().size());
        assertEquals(18, lab.getRed().getCompuestos().size());
        assertEquals(9, lab.getRed().getReacciones().size());
        assertTrue(lab.getMoleculas().stream().allMatch(Molecula::esValida));
    }

    @Test
    void registrarInvalida() {
        assertThrows(IllegalArgumentException.class, () -> lab.registrarMolecula(new Molecula("Vacia")));
        Molecula sueltos = new Molecula("Sueltos");
        sueltos.agregarAtomos(Elemento.C, 2);
        assertThrows(IllegalArgumentException.class, () -> lab.registrarMolecula(sueltos));
    }

    @Test
    void consulta1() {
        assertEquals(List.of("Etanol", "1-Propanol"),
                nombres(lab.compuestosConGrupoYToxicidadMaxima(DetectorGruposFuncionales.ALCOHOL, 2)));
        assertTrue(lab.compuestosConGrupoYToxicidadMaxima(DetectorGruposFuncionales.ALCOHOL, 1).isEmpty());
        assertTrue(nombres(lab.compuestosConGrupoYToxicidadMaxima(DetectorGruposFuncionales.ANILLO, 4))
                .containsAll(List.of("Benceno", "Ciclohexano", "Fenol")));
    }

    @Test
    void consulta2() {
        Molecula iso = MoleculasDePrueba.isobutano();
        assertEquals(Set.of(iso.buscarAtomo(1)), lab.atomosPuente(iso));
        assertTrue(lab.atomosPuente(MoleculasDePrueba.benceno()).isEmpty());
        assertTrue(lab.atomosPuente(MoleculasDePrueba.unAtomo("Metano", Elemento.C)).isEmpty());
        Molecula fenol = MoleculasDePrueba.fenol();
        assertEquals(Set.of(fenol.buscarAtomo(1)), lab.atomosPuente(fenol));
    }

    @Test
    void consulta3() {
        assertEquals(1, lab.contarAnillos(MoleculasDePrueba.benceno()));
        assertEquals(2, lab.contarAnillos(MoleculasDePrueba.decalina()));
        assertEquals(0, lab.contarAnillos(MoleculasDePrueba.etanol()));
    }

    @Test
    void consulta4() {
        RutaSintesis r300 = lab.rutaMasSeguraConPresupuesto("Acido acetico", 300);
        assertEquals(1, r300.cantidadPasos());
        assertEquals("R5", r300.getPasos().get(0).getId());
        RutaSintesis r500 = lab.rutaMasSeguraConPresupuesto("Acido acetico", 500);
        List<String> ids = new ArrayList<>();
        for (Reaccion r : r500.getPasos()) ids.add(r.getId());
        assertEquals(List.of("R1", "R2"), ids);
        assertNull(lab.rutaMasSeguraConPresupuesto("Acido acetico", 100));
    }

    @Test
    void consulta5() {
        Molecula butano = MoleculasDePrueba.cadena("Butano", 4);
        Atomo centro = lab.atomoCentral(butano);
        assertTrue(centro.getId() == 2 || centro.getId() == 3);
        assertEquals(2.0, lab.excentricidad(butano, centro), 1e-9);
        assertEquals(3.0, lab.excentricidad(butano, butano.buscarAtomo(1)), 1e-9);
        assertEquals(1, lab.atomoCentral(MoleculasDePrueba.neopentano()).getId());
        assertNull(lab.atomoCentral(new Molecula("Vacia")));
    }
}
