package ucu.edu.aed.TestImplArboles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import ucu.edu.aed.tda.Arboles.Impl.ArbolBinarioBusqueda;
import ucu.edu.aed.tda.Arboles.TDAElemento;

public class ArbolBinarioBusquedaTest extends TestCase {

    private ArbolBinarioBusqueda<Integer> arbol;

    protected void setUp() {
        arbol = new ArbolBinarioBusqueda<>();
    }

    public void testArbolVacio_esVacioDevuelveTrue() {
        assertTrue(arbol.esVacio());
    }

    public void testArbolVacio_cantidadNodosEsCero() {
        assertEquals(0, arbol.cantidadNodos());
    }

    public void testArbolVacio_buscarDevuelveNull() {
        assertNull(arbol.buscar(5));
    }

    public void testArbolVacio_eliminarDevuelveFalse() {
        assertFalse(arbol.eliminar(5));
    }

    public void testUnicoNodo_seInsertaComoRaiz() {
        assertTrue(arbol.insertar(10));
        assertFalse(arbol.esVacio());
        assertEquals(1, arbol.cantidadNodos());
        assertEquals(1, arbol.cantidadHojas());
        assertEquals(0, arbol.cantidadNodosInternos());
        assertEquals(Integer.valueOf(10), arbol.buscar(10));
    }

    public void testUnicoNodo_eliminarloDejaElArbolVacio() {
        arbol.insertar(10);
        assertTrue(arbol.eliminar(10));
        assertTrue(arbol.esVacio());
        assertNull(arbol.buscar(10));
    }

    public void testInsertar_noPermiteDuplicados() {
        assertTrue(arbol.insertar(10));
        assertFalse(arbol.insertar(10));
        assertEquals(1, arbol.cantidadNodos());
    }

    public void testInsertar_rechazaNull() {
        assertFalse(arbol.insertar(null));
        assertTrue(arbol.esVacio());
    }

    public void testInsertar_respetaElOrden() {
        arbol.insertar(50);
        arbol.insertar(30);
        arbol.insertar(70);
        arbol.insertar(20);
        arbol.insertar(40);

        assertEquals(Integer.valueOf(30), arbol.obtenerRaiz().getHijoIzquierdo().getDato());
        assertEquals(Integer.valueOf(70), arbol.obtenerRaiz().getHijoDerecho().getDato());
        assertEquals(Integer.valueOf(20), arbol.obtenerRaiz().getHijoIzquierdo().getHijoIzquierdo().getDato());
        assertEquals(Integer.valueOf(40), arbol.obtenerRaiz().getHijoIzquierdo().getHijoDerecho().getDato());
    }

    public void testArbolDegenerado_alInsertarEnOrdenCrecienteQuedaComoLista() {
        for (int i = 1; i <= 6; i++) {
            arbol.insertar(i);
        }
        TDAElemento<Integer> actual = arbol.obtenerRaiz();
        int altura = 1;
        while (actual.getHijoDerecho() != null) {
            assertNull(actual.getHijoIzquierdo());
            actual = actual.getHijoDerecho();
            altura++;
        }
        assertEquals(6, altura);
        assertEquals(1, arbol.cantidadHojas());
    }

    public void testBuscar_datoExistente() {
        arbol.insertar(50);
        arbol.insertar(30);
        arbol.insertar(70);
        assertEquals(Integer.valueOf(30), arbol.buscar(30));
    }

    public void testBuscar_datoInexistente() {
        arbol.insertar(50);
        assertNull(arbol.buscar(99));
    }

    public void testEliminar_nodoHoja() {
        arbol.insertar(50);
        arbol.insertar(30);
        arbol.insertar(70);
        assertTrue(arbol.eliminar(30));
        assertNull(arbol.buscar(30));
        assertEquals(2, arbol.cantidadNodos());
        assertNull(arbol.obtenerRaiz().getHijoIzquierdo());
    }

    public void testEliminar_nodoConUnHijo() {
        arbol.insertar(50);
        arbol.insertar(30);
        arbol.insertar(20);
        assertTrue(arbol.eliminar(30));
        assertNull(arbol.buscar(30));
        assertEquals(Integer.valueOf(20), arbol.obtenerRaiz().getHijoIzquierdo().getDato());
    }

    public void testEliminar_nodoConDosHijos() {
        arbol.insertar(50);
        arbol.insertar(30);
        arbol.insertar(70);
        arbol.insertar(20);
        arbol.insertar(40);
        assertTrue(arbol.eliminar(30));
        assertNull(arbol.buscar(30));
        // quitarNodo() reemplaza con el predecesor in-order (el mayor del subárbol izquierdo): 20
        assertEquals(Integer.valueOf(20), arbol.obtenerRaiz().getHijoIzquierdo().getDato());
        assertEquals(Integer.valueOf(40), arbol.obtenerRaiz().getHijoIzquierdo().getHijoDerecho().getDato());
    }

    public void testEliminar_raizConDosHijos() {
        arbol.insertar(50);
        arbol.insertar(30);
        arbol.insertar(70);
        assertTrue(arbol.eliminar(50));
        assertNull(arbol.buscar(50));
        assertEquals(2, arbol.cantidadNodos());
    }

    public void testEliminar_datoInexistente() {
        arbol.insertar(50);
        assertFalse(arbol.eliminar(99));
        assertEquals(1, arbol.cantidadNodos());
    }

    public void testInOrder_devuelveLosDatosOrdenados() {
        int[] valores = {50, 30, 70, 20, 40, 60, 80};
        for (int v : valores) arbol.insertar(v);

        List<Integer> visitados = new ArrayList<>();
        arbol.inOrder(visitados::add);

        assertEquals(Arrays.asList(20, 30, 40, 50, 60, 70, 80), visitados);
    }

    public void testPreOrder_visitaLaRaizPrimero() {
        arbol.insertar(50);
        arbol.insertar(30);
        arbol.insertar(70);

        List<Integer> visitados = new ArrayList<>();
        arbol.preOrder(visitados::add);

        assertEquals(Arrays.asList(50, 30, 70), visitados);
    }

    public void testPostOrder_visitaLaRaizAlFinal() {
        arbol.insertar(50);
        arbol.insertar(30);
        arbol.insertar(70);

        List<Integer> visitados = new ArrayList<>();
        arbol.postOrder(visitados::add);

        assertEquals(Arrays.asList(30, 70, 50), visitados);
    }

    public void testRecorridoPorNiveles_visitaNivelPorNivel() {
        arbol.insertar(50);
        arbol.insertar(30);
        arbol.insertar(70);
        arbol.insertar(20);

        List<Integer> visitados = new ArrayList<>();
        arbol.recorridoPorNiveles(visitados::add);

        assertEquals(Arrays.asList(50, 30, 70, 20), visitados);
    }

    public void testCantidadHojasYNodosInternos() {
        arbol.insertar(50);
        arbol.insertar(30);
        arbol.insertar(70);
        assertEquals(2, arbol.cantidadHojas());
        assertEquals(1, arbol.cantidadNodosInternos());
    }
}