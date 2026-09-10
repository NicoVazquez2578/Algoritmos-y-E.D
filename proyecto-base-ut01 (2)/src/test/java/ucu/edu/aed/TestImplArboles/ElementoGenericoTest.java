package ucu.edu.aed.TestImplArboles;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import ucu.edu.aed.tda.Arboles.Impl.ElementoGenerico;
import ucu.edu.aed.tda.Arboles.TDAElementoGenerico;

public class ElementoGenericoTest extends TestCase {

    private ElementoGenerico<Integer> raiz;

    protected void setUp() {
        raiz = new ElementoGenerico<>(1);
        raiz.insertarHijo(2, 1); // hijo de 1
        raiz.insertarHijo(3, 1); // hijo de 1
        raiz.insertarHijo(4, 2); // hijo de 2 (nieto de 1)
        // Árbol: 1 -> [2 -> [4], 3]
    }

    public void testConstructorSinHijos() {
        ElementoGenerico<Integer> solo = new ElementoGenerico<>(99);
        assertTrue(solo.getHijos().esVacio());
        assertTrue(solo.esHoja());
    }

    public void testInsertarHijoEnRaiz() {
        assertEquals(2, raiz.getHijos().tamaño());
        assertEquals(2, raiz.getHijos().obtener(0).getDato().intValue());
        assertEquals(3, raiz.getHijos().obtener(1).getDato().intValue());
    }

    public void testInsertarHijoAnidado() {
        TDAElementoGenerico<Integer> nodo2 = raiz.getHijos().obtener(0);
        assertEquals(1, nodo2.getHijos().tamaño());
        assertEquals(4, nodo2.getHijos().obtener(0).getDato().intValue());
    }

    public void testInsertarHijoConPadreInexistente() {
        assertFalse(raiz.insertarHijo(50, 999));
        assertEquals(4, raiz.cantidadNodos()); // no cambió nada
    }

    public void testBuscarRaiz() {
        assertSame(raiz, raiz.buscar(1));
    }

    public void testBuscarDescendiente() {
        assertEquals(4, raiz.buscar(4).getDato().intValue());
    }

    public void testBuscarInexistente() {
        assertNull(raiz.buscar(999));
    }

    public void testEliminarLaRaizDevuelveNull() {
        assertNull(raiz.eliminar(1));
    }

    public void testEliminarHijoDirectoSeLlevaElSubarbol() {
        assertSame(raiz, raiz.eliminar(2)); // se lleva a 2 y a su hijo 4
        assertEquals(1, raiz.getHijos().tamaño());
        assertEquals(3, raiz.getHijos().obtener(0).getDato().intValue());
        assertNull(raiz.buscar(4));
        assertEquals(2, raiz.cantidadNodos());
    }

    public void testEliminarNietoFuncionaAunqueElRetornoIntermedioSeDescarte() {
        raiz.eliminar(4);
        TDAElementoGenerico<Integer> nodo2 = raiz.getHijos().obtener(0);
        assertTrue(nodo2.getHijos().esVacio());
        assertNull(raiz.buscar(4));
        assertEquals(3, raiz.cantidadNodos());
    }

    public void testEliminarInexistenteNoModificaElArbol() {
        assertSame(raiz, raiz.eliminar(999));
        assertEquals(4, raiz.cantidadNodos());
    }

    public void testPreOrder() {
        List<Integer> resultado = new ArrayList<>();
        raiz.preOrder(n -> resultado.add(n.getDato()));
        assertEquals(List.of(1, 2, 4, 3), resultado);
    }

    public void testPostOrder() {
        List<Integer> resultado = new ArrayList<>();
        raiz.postOrder(n -> resultado.add(n.getDato()));
        assertEquals(List.of(4, 2, 3, 1), resultado);
    }

    public void testEsHoja() {
        assertFalse(raiz.esHoja());
        assertFalse(raiz.getHijos().obtener(0).esHoja()); // nodo 2, tiene hijo 4
        assertTrue(raiz.getHijos().obtener(1).esHoja());  // nodo 3
    }

    public void testCantidadNodosHojasEInternos() {
        assertEquals(4, raiz.cantidadNodos());
        assertEquals(2, raiz.cantidadHojas());
        assertEquals(2, raiz.cantidadNodosInternos());
    }

    public void testAltura() {
        assertEquals(1, raiz.getHijos().obtener(1).altura()); // nodo 3, hoja
        assertEquals(2, raiz.getHijos().obtener(0).altura()); // nodo 2, con hijo 4
        assertEquals(3, raiz.altura());                       // 1 -> 2 -> 4
    }

    public void testObtenerNivel() {
        assertEquals(0, raiz.obtenerNivel(1));
        assertEquals(1, raiz.obtenerNivel(2));
        assertEquals(2, raiz.obtenerNivel(4));
        assertEquals(-1, raiz.obtenerNivel(999));
    }
}
