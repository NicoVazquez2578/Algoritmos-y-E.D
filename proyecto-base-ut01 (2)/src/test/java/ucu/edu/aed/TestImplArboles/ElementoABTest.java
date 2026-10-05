package ucu.edu.aed.TestImplArboles;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import ucu.edu.aed.tda.Arboles.Impl.ElementoAB;
import ucu.edu.aed.tda.Arboles.TDAElemento;

public class ElementoABTest extends TestCase {

    private TDAElemento<Integer> raiz;

    protected void setUp() {
        raiz = new ElementoAB<>(50);
    }

    private List<Integer> inorden(TDAElemento<Integer> nodo) {
        List<Integer> resultado = new ArrayList<>();
        nodo.inOrder(n -> resultado.add(n.getDato()));
        return resultado;
    }

    public void testNodoUnicoEsHoja() {
        assertTrue(raiz.esHoja());
        assertEquals(1, raiz.cantidadNodos());
        assertEquals(1, raiz.cantidadHojas());
        assertEquals(0, raiz.cantidadNodosInternos());
        assertEquals(1, raiz.altura());
    }

    public void testInsertarMenorVaAIzquierda() {
        raiz.insertar(30);
        assertEquals(30, raiz.getHijoIzquierdo().getDato().intValue());
        assertNull(raiz.getHijoDerecho());
    }

    public void testInsertarMayorVaADerecha() {
        raiz.insertar(70);
        assertEquals(70, raiz.getHijoDerecho().getDato().intValue());
        assertNull(raiz.getHijoIzquierdo());
    }

    public void testInsertarDuplicadoNoModificaElArbol() {
        raiz.insertar(30);
        raiz.insertar(70);
        int nodosAntes = raiz.cantidadNodos();
        assertTrue(raiz.insertar(50)); // duplicado de la raíz: se ignora pero igual devuelve true
        assertEquals(nodosAntes, raiz.cantidadNodos());
    }

    public void testInsertarVariosYRecorrerInorden() {
        for (int v : new int[]{30, 70, 20, 40, 60, 80}) raiz.insertar(v);
        assertEquals(List.of(20, 30, 40, 50, 60, 70, 80), inorden(raiz));
    }

    public void testBuscarExistenteYNoExistente() {
        raiz.insertar(30);
        raiz.insertar(70);
        raiz.insertar(20);

        assertSame(raiz, raiz.buscar(50));
        assertEquals(20, raiz.buscar(20).getDato().intValue());
        assertNull(raiz.buscar(999));
    }

    public void testCantidadNodosHojasEInternos() {
        for (int v : new int[]{30, 70, 20, 40}) raiz.insertar(v);
        // 50 y 30 son internos; 70, 20, 40 son hojas
        assertEquals(5, raiz.cantidadNodos());
        assertEquals(3, raiz.cantidadHojas());
        assertEquals(2, raiz.cantidadNodosInternos());
    }

    public void testAltura() {
        raiz.insertar(30);
        raiz.insertar(20); // 50 -> 30 -> 20
        assertEquals(3, raiz.altura());
    }

    public void testObtenerNivel() {
        raiz.insertar(30);
        raiz.insertar(70);
        raiz.insertar(20);

        assertEquals(0, raiz.obtenerNivel(50));
        assertEquals(1, raiz.obtenerNivel(30));
        assertEquals(2, raiz.obtenerNivel(20));
        assertEquals(-1, raiz.obtenerNivel(999));
    }

    public void testEliminarHoja() {
        raiz.insertar(30);
        raiz.insertar(70);
        TDAElemento<Integer> resultado = raiz.eliminar(30);
        assertSame(raiz, resultado);
        assertNull(raiz.getHijoIzquierdo());
        assertEquals(List.of(50, 70), inorden(raiz));
    }

    public void testEliminarNodoConUnHijo() {
        raiz.insertar(30);
        raiz.insertar(20); // 30 queda con un solo hijo (izquierdo)
        raiz = raiz.eliminar(30);
        assertEquals(50, raiz.getDato().intValue());
        assertEquals(20, raiz.getHijoIzquierdo().getDato().intValue());
    }

    public void testEliminarNodoConDosHijos_usaPredecesor() {
        for (int v : new int[]{30, 70, 20, 40}) raiz.insertar(v);
        raiz = raiz.eliminar(30);

        // El predecesor de 30 (mayor de su subárbol izquierdo) es 20
        assertEquals(20, raiz.getHijoIzquierdo().getDato().intValue());
        assertEquals(40, raiz.getHijoIzquierdo().getHijoDerecho().getDato().intValue());
        assertNull(raiz.getHijoIzquierdo().getHijoIzquierdo());
        assertEquals(List.of(20, 40, 50, 70), inorden(raiz));
    }

    public void testEliminarRaizConDosHijos() {
        raiz.insertar(30);
        raiz.insertar(70);
        raiz.insertar(20);
        raiz.insertar(40);
        raiz = raiz.eliminar(50); // elimina la raíz misma

        assertEquals(40, raiz.getDato().intValue()); // predecesor de 50
        assertEquals(List.of(20, 30, 40, 70), inorden(raiz));
    }

    public void testEliminarInexistenteNoModificaElArbol() {
        raiz.insertar(30);
        raiz.insertar(70);
        assertSame(raiz, raiz.eliminar(999));
        assertEquals(List.of(30, 50, 70), inorden(raiz));
    }

    public void testPreOrderYPostOrder() {
        for (int v : new int[]{30, 70, 20, 40}) raiz.insertar(v);

        List<Integer> pre = new ArrayList<>();
        raiz.preOrder(n -> pre.add(n.getDato()));
        assertEquals(List.of(50, 30, 20, 40, 70), pre);

        List<Integer> post = new ArrayList<>();
        raiz.postOrder(n -> post.add(n.getDato()));
        assertEquals(List.of(20, 40, 30, 70, 50), post);
    }
}