package ucu.edu.aed.TestImplArboles;

import junit.framework.TestCase;
import ucu.edu.aed.tda.Arboles.Impl.AVL;
import ucu.edu.aed.tda.Arboles.TDAElemento;
import java.util.ArrayList;
import java.util.List;
import ucu.edu.aed.tda.Arboles.Impl.ArbolBinarioBusqueda;

public class AVLTest extends TestCase {

    private AVL<Integer> avl;

    protected void setUp() {
        avl = new AVL<>();
    }

    private List<Integer> inorden() {
        List<Integer> resultado = new ArrayList<>();
        recorrer(avl.raiz, resultado);
        return resultado;
    }

    private void recorrer(TDAElemento<Integer> nodo, List<Integer> acumulador) {
        if (nodo == null) return;
        recorrer(nodo.getHijoIzquierdo(), acumulador);
        acumulador.add(nodo.getDato());
        recorrer(nodo.getHijoDerecho(), acumulador);
    }

    public void testArbolVacio() {
        assertNull(avl.raiz);
    }

    public void testRotacionLL() {
        avl.insertar(30);
        avl.insertar(20);
        avl.insertar(10); // fuerza LL

        assertEquals(20, avl.raiz.getDato().intValue());
        assertEquals(10, avl.raiz.getHijoIzquierdo().getDato().intValue());
        assertEquals(30, avl.raiz.getHijoDerecho().getDato().intValue());
        assertEquals(List.of(10, 20, 30), inorden());
    }

    public void testRotacionRR() {
        avl.insertar(10);
        avl.insertar(20);
        avl.insertar(30); // fuerza RR

        assertEquals(20, avl.raiz.getDato().intValue());
        assertEquals(10, avl.raiz.getHijoIzquierdo().getDato().intValue());
        assertEquals(30, avl.raiz.getHijoDerecho().getDato().intValue());
        assertEquals(List.of(10, 20, 30), inorden());
    }

    public void testRotacionLR() {
        avl.insertar(30);
        avl.insertar(10);
        avl.insertar(20); // desbalance a la izquierda, pero 20 > 10 -> LR

        assertEquals(20, avl.raiz.getDato().intValue());
        assertEquals(10, avl.raiz.getHijoIzquierdo().getDato().intValue());
        assertEquals(30, avl.raiz.getHijoDerecho().getDato().intValue());
        assertEquals(List.of(10, 20, 30), inorden());
    }

    public void testRotacionRL() {
        avl.insertar(10);
        avl.insertar(30);
        avl.insertar(20); // desbalance a la derecha, pero 20 < 30 -> RL

        assertEquals(20, avl.raiz.getDato().intValue());
        assertEquals(10, avl.raiz.getHijoIzquierdo().getDato().intValue());
        assertEquals(30, avl.raiz.getHijoDerecho().getDato().intValue());
        assertEquals(List.of(10, 20, 30), inorden());
    }

    public void testInsertarDuplicadoNoAgregaNodo() {
        avl.insertar(10);
        avl.insertar(20);
        int nodosAntes = avl.raiz.cantidadNodos();
        assertTrue(avl.insertar(20)); // el contrato siempre devuelve true, aunque sea duplicado
        assertEquals(nodosAntes, avl.raiz.cantidadNodos());
    }

    public void testInsercionSecuencial1a7QuedaPerfectamenteBalanceado() {
        for (int i = 1; i <= 7; i++) avl.insertar(i);

        assertEquals(4, avl.raiz.getDato().intValue());
        assertEquals(2, avl.raiz.getHijoIzquierdo().getDato().intValue());
        assertEquals(6, avl.raiz.getHijoDerecho().getDato().intValue());
        assertEquals(3, avl.raiz.altura());
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7), inorden());
    }

    public void testEliminarHoja() {
        avl.insertar(20);
        avl.insertar(10);
        avl.insertar(30);
        assertTrue(avl.eliminar(10));
        assertNull(avl.raiz.getHijoIzquierdo());
        assertEquals(List.of(20, 30), inorden());
    }

    public void testEliminarInexistente() {
        avl.insertar(20);
        avl.insertar(10);
        assertFalse(avl.eliminar(99));
        assertEquals(List.of(10, 20), inorden());
    }

}
