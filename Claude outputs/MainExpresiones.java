package ucu.edu.aed.expresiones;

import ucu.edu.aed.impl.Lista;
import ucu.edu.aed.tda.TDALista;

/** Programa de prueba del Ejercicio 7 - Parte 1. */
public class MainExpresiones {

    public static void main(String[] args) {
        // Expresión: a - b * (c + 4 / d)
        NodoExpresion division = new NodoExpresion('/', new NodoExpresion(4), new NodoExpresion("d"));
        NodoExpresion suma = new NodoExpresion('+', new NodoExpresion("c"), division);
        NodoExpresion producto = new NodoExpresion('*', new NodoExpresion("b"), suma);
        NodoExpresion resta = new NodoExpresion('-', new NodoExpresion("a"), producto);
        ArbolExpresion arbol = new ArbolExpresion(resta);

        System.out.println("Expresión original: " + arbol);

        TDALista<Asignacion> valores = new Lista<>();
        valores.agregar(new Asignacion("a", 10));
        valores.agregar(new Asignacion("b", 3));
        valores.agregar(new Asignacion("c", 2));
        valores.agregar(new Asignacion("d", 2));

        arbol.sustituir(valores);
        System.out.println("Tras sustituir:     " + arbol);
        arbol.evaluarYEmitir(); // esperado: -2.0

        // Caso: variable sin valor asignado
        ArbolExpresion sinValor = new ArbolExpresion(
                new NodoExpresion('+', new NodoExpresion("x"), new NodoExpresion(1)));
        sinValor.sustituir(new Lista<Asignacion>());
        probar("variable sin valor", sinValor);

        // Caso: división por cero, (y - y) vale 0
        ArbolExpresion divCero = new ArbolExpresion(
                new NodoExpresion('/', new NodoExpresion(5),
                        new NodoExpresion('-', new NodoExpresion("y"), new NodoExpresion("y"))));
        TDALista<Asignacion> soloY = new Lista<>();
        soloY.agregar(new Asignacion("y", 3));
        divCero.sustituir(soloY);
        probar("división por cero", divCero);

        // Caso: árbol vacío
        probar("árbol vacío", new ArbolExpresion());

        // Caso: una sola constante
        new ArbolExpresion(new NodoExpresion(7)).evaluarYEmitir();
    }

    private static void probar(String caso, ArbolExpresion arbol) {
        try {
            arbol.evaluarYEmitir();
            System.out.println("[" + caso + "] ERROR: tenía que fallar");
        } catch (IllegalStateException e) {
            System.out.println("[" + caso + "] falla como se espera: " + e.getMessage());
        } catch (ArithmeticException e) {
            System.out.println("[" + caso + "] falla como se espera: " + e.getMessage());
        }
    }
}
