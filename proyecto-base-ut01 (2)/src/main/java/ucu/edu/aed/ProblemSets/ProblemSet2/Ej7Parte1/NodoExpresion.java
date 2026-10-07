package ucu.edu.aed.ProblemSets.ProblemSet2.Ej7Parte1;

import ucu.edu.aed.tda.TDALista;
 
/*
  Nodo de un árbol binario que representa una expresión aritmética.
 
  Hay tres tipos de nodo:
   - OPERADOR: nodo interno con dos hijos (+, -, *, /).
   - CONSTANTE: hoja con un valor numérico.
   - VARIABLE: hoja con el nombre de una variable.
 */
public class NodoExpresion {
 
    public static final int OPERADOR = 1;
    public static final int CONSTANTE = 2;
    public static final int VARIABLE = 3;
 
    private int tipo;
    private char operador;   
    private double valor;    
    private String nombre;   
    private NodoExpresion hijoIzquierdo;
    private NodoExpresion hijoDerecho;
 
    /** Crea un nodo operador con sus dos operandos. */
    public NodoExpresion(char operador, NodoExpresion hijoIzquierdo, NodoExpresion hijoDerecho) {
        if (operador != '+' && operador != '-' && operador != '*' && operador != '/') {
            throw new IllegalArgumentException("Operador no válido: " + operador);
        }
        if (hijoIzquierdo == null || hijoDerecho == null) {
            throw new IllegalArgumentException("Un operador binario necesita dos operandos");
        }
        this.tipo = OPERADOR;
        this.operador = operador;
        this.hijoIzquierdo = hijoIzquierdo;
        this.hijoDerecho = hijoDerecho;
    }
 
    /** Crea una hoja constante. */
    public NodoExpresion(double valor) {
        this.tipo = CONSTANTE;
        this.valor = valor;
    }
 
    /** Crea una hoja variable. */
    public NodoExpresion(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("La variable necesita un nombre");
        }
        this.tipo = VARIABLE;
        this.nombre = nombre;
    }
 
    public int getTipo() {
        return tipo;
    }
 
    public NodoExpresion getHijoIzquierdo() {
        return hijoIzquierdo;
    }
 
    public NodoExpresion getHijoDerecho() {
        return hijoDerecho;
    }
 
    public boolean esHoja() {
        return hijoIzquierdo == null && hijoDerecho == null;
    }
 
    /*
      Sustituye las variables del subárbol por los valores de la lista.
     
      Precondición: asignaciones no es nula (puede estar vacía).
      Postcondición: cada hoja variable cuyo nombre aparece en asignaciones
                     pasa a ser una constante con el valor asignado.
                     Las variables que no aparecen en la lista quedan igual.
     
      Orden del tiempo de ejecución: O(n).
     */
    public void sustituir(TDALista<Asignacion> asignaciones) {
        if (esHoja()) {
            if (tipo == VARIABLE) {
                final String nombreVariable = this.nombre;
                Asignacion asignacion = asignaciones.buscar(a -> a.getNombre().equals(nombreVariable));
                if (asignacion != null) {
                    this.tipo = CONSTANTE;
                    this.valor = asignacion.getValor();
                    this.nombre = null;
                }
            }
        } else {
            hijoIzquierdo.sustituir(asignaciones);
            hijoDerecho.sustituir(asignaciones);
        }
    }
 
    /*
      Evalúa la expresión del subárbol (recorrido en postorden: primero se
      evalúan los dos hijos y después se aplica el operador).
     
      Precondición: el subárbol no tiene variables sin sustituir.
      
      Postcondición: devuelve el valor de la expresión. El árbol no cambia.
     
      Orden del tiempo de ejecución: O(n).
     */
    public double evaluar() {
        if (tipo == CONSTANTE) {
            return valor;
        }
        if (tipo == VARIABLE) {
            throw new IllegalStateException("La variable " + nombre + " no tiene valor asignado");
        }
 
        double a = hijoIzquierdo.evaluar();
        double b = hijoDerecho.evaluar();
 
        if (operador == '+') {
            return a + b;
        }
        if (operador == '-') {
            return a - b;
        }
        if (operador == '*') {
            return a * b;
        }
        // solo queda '/'
        if (b == 0) {
            throw new ArithmeticException("División por cero");
        }
        return a / b;
    }
 
    /** Devuelve la expresión en notación infija, con paréntesis. */
    @Override
    public String toString() {
        if (tipo == CONSTANTE) {
            if (valor == Math.floor(valor)) {
                return String.valueOf((long) valor);
            }
            return String.valueOf(valor);
        }
        if (tipo == VARIABLE) {
            return nombre;
        }
        return "(" + hijoIzquierdo + " " + operador + " " + hijoDerecho + ")";
    }
}