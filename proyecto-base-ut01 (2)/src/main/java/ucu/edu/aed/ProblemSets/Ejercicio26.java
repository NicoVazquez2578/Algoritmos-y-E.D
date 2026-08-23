package ucu.edu.aed.ProblemSets;

import ucu.edu.aed.impl.Pila;
import ucu.edu.aed.tda.TDAPila;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


  //Ejercicio 26: Pilas.
 
public class Ejercicio26 {

    // PARTE A — Con TDA Pila 
        
    public static class Expresion {

        private final List<Character> listaDeEntrada;

        public Expresion(List<Character> listaDeEntrada) {
            this.listaDeEntrada = listaDeEntrada;
        }

        /**
          Recorre la lista de caracteres del código fuente y verifica
          que los símbolos de apertura/cierre estén correctamente
          balanceados y anidados.
         
          <p>Se programa contra la interfaz {@link TDAPila}, no contra
          la clase concreta: a este método no le importa cómo está
          implementada la pila por dentro.</p>
         
          <p>Cualquier caracter que no sea un símbolo de apertura o
          cierre (letras, números, operadores, espacios, etc.) se
          ignora — así se puede pasar código fuente real, no solo una
          secuencia "pura" de corchetes.</p>
         
          <p><b>Complejidad:</b> O(n) en tiempo — se recorre la lista
          una sola vez y cada apertura se apila y se desapila a lo
          sumo una vez. O(n) en espacio en el peor caso (una entrada
          formada solo por símbolos de apertura, ej. "((((((").</p>
         
          @param listaDeEntrada la secuencia de caracteres a validar
          @return {@code true} si los corchetes están correctamente
                  balanceados; {@code false} en caso contrario
         */
        public boolean controlCorchetes(List<Character> listaDeEntrada) {
            TDAPila<Character> pila = new Pila<>();

            for (Character c : listaDeEntrada) {
                if (esApertura(c)) {
                    pila.mete(c);
                } else if (esCierre(c)) {
                    if (pila.esVacio()) {
                        return false; // cierre sin apertura correspondiente
                    }
                    Character apertura = pila.saca();
                    if (!coincide(apertura, c)) {
                        return false; // el tipo de corchete no coincide
                    }
                }
                // cualquier otro caracter se ignora
            }
            return pila.esVacio(); // si algo quedó sin cerrar, está mal formada
        }

        private boolean esApertura(char c) {
            return c == '(' || c == '[' || c == '{';
        }

        private boolean esCierre(char c) {
            return c == ')' || c == ']' || c == '}';
        }

        private boolean coincide(char apertura, char cierre) {
            return (apertura == '(' && cierre == ')')
                    || (apertura == '[' && cierre == ']')
                    || (apertura == '{' && cierre == '}');
        }
    }

    // PARTE B — Mismo método, usando java.util.Stack de la librería
    // estándar de Java en lugar del TDA Pila propio.
    
    public static class ExpresionConStackJava {

        public boolean controlCorchetes(List<Character> listaDeEntrada) {
            Stack<Character> pila = new Stack<>();

            for (Character c : listaDeEntrada) {
                if (esApertura(c)) {
                    pila.push(c);
                } else if (esCierre(c)) {
                    if (pila.isEmpty()) {
                        return false;
                    }
                    Character apertura = pila.pop();
                    if (!coincide(apertura, c)) {
                        return false;
                    }
                }
            }
            return pila.isEmpty();
        }

        private boolean esApertura(char c) {
            return c == '(' || c == '[' || c == '{';
        }

        private boolean esCierre(char c) {
            return c == ')' || c == ']' || c == '}';
        }

        private boolean coincide(char apertura, char cierre) {
            return (apertura == '(' && cierre == ')')
                    || (apertura == '[' && cierre == ']')
                    || (apertura == '{' && cierre == '}');
        }
    }

    // PARTE C — Comparación
    // Tiempo de ejecución: no cambia. Ambas son O(n). mete/saca/tope
    // de la Pila propia son O(1) porque Pila extiende Lista e inserta
    // y remueve siempre en el índice 0 (cabeza de la lista enlazada,
    // sin recorrer nada). push/pop/peek de java.util.Stack también
    // son O(1) amortizado (Stack extiende Vector y opera al final del
    // arreglo interno). El análisis asintótico es idéntico.
    //
    // Memoria: la Pila propia reserva memoria nodo a nodo, a medida
    // que se necesita, con overhead de un puntero "siguiente" por
    // elemento. java.util.Stack guarda los datos en un arreglo
    // contiguo que suele reservar más capacidad de la que usa (se
    // duplica al llenarse), así que puede haber "hueco" reservado sin
    // usar; a cambio, no paga el overhead de punteros por elemento.
    //
    // Mantenibilidad y control interno: con la Pila propia, el equipo
    // controla exactamente qué operaciones existen (el TDA que
    // definieron) y puede seguir modificándola. java.util.Stack
    // hereda de Vector métodos que rompen el encapsulamiento de pila
    // (por ejemplo, get(int) o insertElementAt permiten acceder o
    // insertar en cualquier posición, no solo en el tope) — nada
    // impide que otro programador use la "pila" como si fuera una
    // lista. El TDA propio evita ese riesgo por diseño.
    
    private static List<Character> aListaDeCaracteres(String s) {
        List<Character> lista = new ArrayList<>();
        for (char c : s.toCharArray()) {
            lista.add(c);
        }
        return lista;
    }

    public static void main(String[] args) {
        String[] casos = {
                "{}{{}}",                        // bien formada (ejemplo del enunciado)
                "{{)}{{",                        // mal formada (ejemplo del enunciado)
                "",                               // caso borde: secuencia vacía
                "{",                              // caso borde: apertura sin cierre
                "}",                              // caso borde: cierre sin apertura
                "()[]{}",                         // varios tipos, todos bien anidados
                "([{}])",                         // anidamiento profundo bien formado
                "([)]",                           // mal formada: los tipos se cruzan
                "{ int x = (a+b)*[c-d]; }"        // simula código fuente con texto mezclado
        };

        System.out.println("=== PARTE A y B: comparación de resultados ===");
        for (String caso : casos) {
            List<Character> entrada = aListaDeCaracteres(caso);

            Expresion expresionPropia = new Expresion(entrada);
            ExpresionConStackJava expresionJava = new ExpresionConStackJava();

            boolean resultadoPropio = expresionPropia.controlCorchetes(entrada);
            boolean resultadoJava = expresionJava.controlCorchetes(entrada);
            String coincideTexto = (resultadoPropio == resultadoJava) ? "OK" : "DIFIEREN";

            System.out.printf(
                    "\"%s\" -> Pila propia: %-5s | Stack de Java: %-5s [%s]%n",
                    caso, resultadoPropio, resultadoJava, coincideTexto
            );
        }

        System.out.println();
        System.out.println("=== PARTE C ===");
        System.out.println("Ver el comentario arriba de esta clase con el analisis completo.");
        System.out.println("Resumen: mismo orden de tiempo (O(n) ambas); difiere el patron de");
        System.out.println("uso de memoria (nodos enlazados vs arreglo con capacidad extra); la");
        System.out.println("Pila propia da mas control porque expone solo las operaciones del TDA.");
    }
}
