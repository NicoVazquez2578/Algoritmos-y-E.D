package ucu.edu.aed.TestImplArboles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import ucu.edu.aed.tda.Arboles.Impl.ArbolGenerico;

public class ArbolGenericoTest extends TestCase {

    private ArbolGenerico<Integer> arbol;

    protected void setUp() {
        arbol = new ArbolGenerico<>();
    }

    public void testArbolVacio() {
        assertTrue(arbol.esVacio());
        assertEquals(0, arbol.cantidadNodos());
        assertNull(arbol.buscar(1));
        assertFalse(arbol.eliminar(1));
        assertNull(arbol.obtenerRaiz());
    }

    public void testUnicoNodo() {
        assertTrue(arbol.insertar(1));
        assertEquals(1, arbol.cantidadNodos());
        assertEquals(1, arbol.cantidadHojas());
        assertEquals(0, arbol.cantidadNodosInternos());
    }

    public void testInsertar_soloFuncionaSiElArbolEstaVacio() {
        arbol.insertar(1);
        assertFalse(arbol.insertar(2));
        assertEquals(1, arbol.cantidadNodos());
    }

    public void testInsertarHijo_agregaVariosHijosAlMismoPadre() {
        arbol.insertar(1);
        assertTrue(arbol.insertarHijo(2, 1));
        assertTrue(arbol.insertarHijo(3, 1));
        assertTrue(arbol.insertarHijo(4, 1));

        assertEquals(4, arbol.cantidadNodos());
        assertEquals(3, arbol.obtenerRaiz().getHijos().tamaño());
    }

    public void testInsertarHijo_seRamificaAVariosNiveles() {
        // simula sistema -> subsistema -> pieza
        arbol.insertar(1);
        arbol.insertarHijo(2, 1);
        arbol.insertarHijo(3, 2);
        assertEquals(3, arbol.cantidadNodos());
        assertEquals(2, arbol.obtenerRaiz().obtenerNivel(3));
    }

    public void testInsertarHijo_padreInexistente() {
        arbol.insertar(1);
        assertFalse(arbol.insertarHijo(2, 99));
        assertEquals(1, arbol.cantidadNodos());
    }

    public void testInsertarHijo_arbolVacio() {
        assertFalse(arbol.insertarHijo(1, 1));
        assertTrue(arbol.esVacio());
    }

    public void testBuscar_raizYDescendientes() {
        arbol.insertar(1);
        arbol.insertarHijo(2, 1);
        arbol.insertarHijo(3, 2);

        assertEquals(Integer.valueOf(1), arbol.buscar(1));
        assertEquals(Integer.valueOf(3), arbol.buscar(3));
        assertNull(arbol.buscar(99));
    }

    public void testEliminar_hojaSinDescendientes() {
        arbol.insertar(1);
        arbol.insertarHijo(2, 1);
        arbol.insertarHijo(3, 1);

        assertTrue(arbol.eliminar(3));
        assertEquals(2, arbol.cantidadNodos());
        assertNull(arbol.buscar(3));
    }

    public void testEliminar_seLlevaTodoElSubarbol() {
        arbol.insertar(1);
        arbol.insertarHijo(2, 1);
        arbol.insertarHijo(3, 2);
        arbol.insertarHijo(4, 2);

        assertTrue(arbol.eliminar(2));

        assertEquals(1, arbol.cantidadNodos());
        assertNull(arbol.buscar(2));
        assertNull(arbol.buscar(3));
        assertNull(arbol.buscar(4));
    }

    public void testEliminar_laRaiz() {
        arbol.insertar(1);
        arbol.insertarHijo(2, 1);
        assertTrue(arbol.eliminar(1));
        assertTrue(arbol.esVacio());
    }

    public void testEliminar_datoInexistente() {
        arbol.insertar(1);
        assertFalse(arbol.eliminar(99));
        assertEquals(1, arbol.cantidadNodos());
    }

    public void testPreOrder_visitaElPadreAntesDeLosHijos() {
        arbol.insertar(1);
        arbol.insertarHijo(2, 1);
        arbol.insertarHijo(3, 1);
        arbol.insertarHijo(4, 2);

        List<Integer> visitados = new ArrayList<>();
        arbol.preOrder(visitados::add);

        assertEquals(Arrays.asList(1, 2, 4, 3), visitados);
    }

    public void testPostOrder_visitaLosHijosAntesQueElPadre() {
        arbol.insertar(1);
        arbol.insertarHijo(2, 1);
        arbol.insertarHijo(3, 1);

        List<Integer> visitados = new ArrayList<>();
        arbol.postOrder(visitados::add);

        assertEquals(Arrays.asList(2, 3, 1), visitados);
    }

    public void testRecorridoPorNiveles() {
        arbol.insertar(1);
        arbol.insertarHijo(2, 1);
        arbol.insertarHijo(3, 1);
        arbol.insertarHijo(4, 2);

        List<Integer> visitados = new ArrayList<>();
        arbol.recorridoPorNiveles(visitados::add);

        assertEquals(Arrays.asList(1, 2, 3, 4), visitados);
    }

    public void testCantidadHojasYNodosInternos() {
        arbol.insertar(1);
        arbol.insertarHijo(2, 1);
        arbol.insertarHijo(3, 1);
        arbol.insertarHijo(4, 2);
        assertEquals(2, arbol.cantidadHojas());
        assertEquals(2, arbol.cantidadNodosInternos());
    }
}