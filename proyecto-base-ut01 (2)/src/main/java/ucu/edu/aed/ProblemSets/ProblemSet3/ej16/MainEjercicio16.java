package ucu.edu.aed.ProblemSets.ProblemSet3.ej16;

public class MainEjercicio16 {
    public static void main(String[] args) {
        //Parte 1: Construcción manual
        TNodoArbolGenealogico raiz = new TNodoArbolGenealogico(new Persona("Ana", 1940));

        TNodoArbolGenealogico hijo1 = new TNodoArbolGenealogico(new Persona("Carlos", 1965));
        TNodoArbolGenealogico hijo2 = new TNodoArbolGenealogico(new Persona("Beatriz", 1968));
        TNodoArbolGenealogico hijo3 = new TNodoArbolGenealogico(new Persona("David", 1972));

        raiz.agregarHijo(hijo1);
        raiz.agregarHijo(hijo2);
        raiz.agregarHijo(hijo3);

        TNodoArbolGenealogico nieto1 = new TNodoArbolGenealogico(new Persona("Elena", 1990));
        TNodoArbolGenealogico nieto2 = new TNodoArbolGenealogico(new Persona("Fernando", 1993));
        hijo1.agregarHijo(nieto1);
        hijo1.agregarHijo(nieto2);

        TNodoArbolGenealogico nieto3 = new TNodoArbolGenealogico(new Persona("Gabriel", 1995));
        hijo2.agregarHijo(nieto3);

        TNodoArbolGenealogico nieto4 = new TNodoArbolGenealogico(new Persona("Hugo", 1998));
        TNodoArbolGenealogico nieto5 = new TNodoArbolGenealogico(new Persona("Ines", 2001));
        TNodoArbolGenealogico nieto6 = new TNodoArbolGenealogico(new Persona("Javier", 2005));
        hijo3.agregarHijo(nieto4);
        hijo3.agregarHijo(nieto5);
        hijo3.agregarHijo(nieto6);

        TArbolGenealogico arbol = new TArbolGenealogico(raiz);

        //Parte 2: Pruebas de los métodos+
        System.out.println("1. Descendientes de Carlos: " + arbol.listarDescendientes("Carlos"));
        System.out.println("2. Altura del árbol: " + arbol.calcularAltura(arbol.getRaiz()));
        System.out.println("3. Cantidad total de personas: " + arbol.contarPersonas(arbol.getRaiz()));
        System.out.println("4. Personas en Generación 2: " + arbol.obtenerPersonasPorGeneracion(2));
        System.out.println("5. Ancestro común más cercano:");
        System.out.println("   - Elena y Fernando: " + arbol.ancestroComunMasCercano("Elena", "Fernando"));
        System.out.println("   - Elena y Hugo: " + arbol.ancestroComunMasCercano("Elena", "Hugo"));
        System.out.println("6. ¿Elena es descendiente de Carlos?: " + arbol.esDescendiente("Carlos", "Elena"));
        System.out.println("   ¿Hugo es descendiente de Beatriz?: " + arbol.esDescendiente("Beatriz", "Hugo"));
    }
}