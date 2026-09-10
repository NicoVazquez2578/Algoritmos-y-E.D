package ucu.edu.aed.TestImplArboles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import ucu.edu.aed.tda.Arboles.Impl.ArbolBinario;
import ucu.edu.aed.tda.Arboles.TDAElemento;

public class ArbolBinarioTest extends TestCase {

    private ArbolBinario<Integer> arbol;

    protected void setUp() {
        arbol = new ArbolBinario<>();
    }

    public void testArbolVacio() {
        assertTrue(arbol.esVacio());
        assertEquals(0, arbol.cantidadNodos());
        assertNull(arbol.buscar(1));
        assertFalse(arbol.eliminar(1));
    }

    public void testUnicoNodo() {
        assertTrue(arbol.insertar(10));
        assertEquals(1, arbol.cantidadNodos());
        assertEquals(1, arbol.cantidadHojas());
        assertEquals(Integer.valueOf(10), arbol.buscar(10));
    }

    public void testInsertar_rechazaNull() {
        assertFalse(arbol.insertar(null));
        assertTrue(arbol.esVacio());
    }

    public void testInsertar_rechazaDuplicados() {
        assertTrue(arbol.insertar(5));
        assertFalse(arbol.insertar(5));
        assertEquals(1, arbol.cantidadNodos());
    }

    public void testInsertar_ocupaSiemprePrimeraPosicionLibrePorNiveles() {
        arbol.insertar(1);
        arbol.insertar(2);
        arbol.insertar(3);
        arbol.insertar(4);

        TDAElemento<Integer> raiz = arbol.obtenerRaiz();
        assertEquals(Integer.valueOf(1), raiz.getDato());
        assertEquals(Integer.valueOf(2), raiz.getHijoIzquierdo().getDato());
        assertEquals(Integer.valueOf(3), raiz.getHijoDerecho().getDato());
        assertEquals(Integer.valueOf(4), raiz.getHijoIzquierdo().getHijoIzquierdo().getDato());
        assertNull(raiz.getHijoIzquierdo().getHijoDerecho());
        assertNull(raiz.getHijoDerecho().getHijoIzquierdo());
    }

    public void testInsertar_noQuedaDegeneradoAunEnOrdenCreciente() {
        for (int i = 1; i <= 7; i++) {
            arbol.insertar(i);
        }
        assertEquals(3, arbol.obtenerRaiz().altura());
    }

    public void testBuscar_datoExistenteYNoExistente() {
        arbol.insertar(1);
        arbol.insertar(2);
        arbol.insertar(3);
        assertEquals(Integer.valueOf(3), arbol.buscar(3));
        assertNull(arbol.buscar(99));
    }

    public void testEliminar_raizUnica() {
        arbol.insertar(1);
        assertTrue(arbol.eliminar(1));
        assertTrue(arbol.esVacio());
    }

    public void testEliminar_moviendoElUltimoNodoAlLugarDelEliminado() {
        arbol.insertar(1);
        arbol.insertar(2);
        arbol.insertar(3);
        arbol.insertar(4); // el último en orden de nivel es 4 (hijo izquierdo de 2)

        assertTrue(arbol.eliminar(2));

        TDAElemento<Integer> raiz = arbol.obtenerRaiz();
        assertEquals(Integer.valueOf(4), raiz.getHijoIzquierdo().getDato());
        assertNull(raiz.getHijoIzquierdo().getHijoIzquierdo());
        assertEquals(3, arbol.cantidadNodos());
    }

    public void testEliminar_elUltimoNodoEsElMismoQueSeElimina() {
        arbol.insertar(1);
        arbol.insertar(2);
        arbol.insertar(3); // 3 es el último en orden de nivel

        assertTrue(arbol.eliminar(3));
        assertNull(arbol.obtenerRaiz().getHijoDerecho());
        assertEquals(2, arbol.cantidadNodos());
    }

    public void testEliminar_datoInexistente() {
        arbol.insertar(1);
        assertFalse(arbol.eliminar(99));
        assertEquals(1, arbol.cantidadNodos());
    }

    public void testPreOrder() {
        arbol.insertar(1);
        arbol.insertar(2);
        arbol.insertar(3);

        List<Integer> visitados = new ArrayList<>();
        arbol.preOrder(visitados::add);

        assertEquals(3, visitados.size());
        assertEquals(Integer.valueOf(1), visitados.get(0));
    }

    public void testRecorridoPorNiveles_devuelveLosDatosEnElOrdenDeInsercion() {
        arbol.insertar(1);
        arbol.insertar(2);
        arbol.insertar(3);
        arbol.insertar(4);

        List<Integer> visitados = new ArrayList<>();
        arbol.recorridoPorNiveles(visitados::add);

        assertEquals(Arrays.asList(1, 2, 3, 4), visitados);
    }

    public void testCantidadHojasYNodosInternos() {
        arbol.insertar(1);
        arbol.insertar(2);
        arbol.insertar(3);
        arbol.insertar(4);
        // 4 cuelga de 2: hojas = {3, 4}, internos = {1, 2}
        assertEquals(2, arbol.cantidadHojas());
        assertEquals(2, arbol.cantidadNodosInternos());
    }
}
