package ucu.edu.aed.ProblemSets.ProblemSet2.Ej7Parte1;
import ucu.edu.aed.tda.TDALista;
 
/**
 * Árbol binario que representa una expresión aritmética.
 * Los métodos del árbol se apoyan en los métodos recursivos del nodo.
 */
public class ArbolExpresion {
 
    private NodoExpresion raiz;
 
    public ArbolExpresion() {
        this.raiz = null;
    }
 
    public ArbolExpresion(NodoExpresion raiz) {
        this.raiz = raiz;
    }
 
    public NodoExpresion getRaiz() {
        return raiz;
    }
 
    public boolean esVacio() {
        return raiz == null;
    }
 
    /*
      Sustituye las variables del árbol por los valores de la lista.
     
      Precondición: asignaciones no es nula.
      
      Postcondición: las variables con valor en la lista pasan a ser constantes.
        Si el árbol es vacío no pasa nada.
      
        Orden: O(n * v) (ver NodoExpresion.sustituir).
     */
    public void sustituir(TDALista<Asignacion> asignaciones) {
        if (raiz != null) {
            raiz.sustituir(asignaciones);
        }
    }
 
    /* 
     Evalúa la expresión del árbol.
     
      Precondición: el árbol no es vacío y no tiene variables sin sustituir.
      
      Postcondición: devuelve el valor de la expresión.
     
      Orden: O(n).
     */
    public double evaluar() {
        if (raiz == null) {
            throw new IllegalStateException("El árbol está vacío");
        }
        return raiz.evaluar();
    }
 
    /** Evalúa la expresión y emite el resultado por consola. */
    public void evaluarYEmitir() {
        double resultado = evaluar();
        System.out.println(this + " = " + resultado);
    }
 
    @Override
    public String toString() {
        if (raiz == null) {
            return "(árbol vacío)";
        }
        return raiz.toString();
    }
}