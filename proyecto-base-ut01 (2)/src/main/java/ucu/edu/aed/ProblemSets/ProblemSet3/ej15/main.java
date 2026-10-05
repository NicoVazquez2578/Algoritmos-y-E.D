import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        // Dos objetos distintos que representan el mismo libro
        Libro a = new Libro("978-0-13-468599-1", "Effective Java", "Joshua Bloch", 2018);
        Libro b = new Libro("978-0-13-468599-1", "Effective Java 3ra edicion", "J. Bloch", 2018);
        Libro c = new Libro("978-0-13-235088-4", "Clean Code", "Robert Martin", 2008);

        System.out.println("a == b: " + (a == b));
        System.out.println("a.equals(b): " + a.equals(b));

        Set<Libro> libros = new HashSet<>();
        System.out.println("add(a): " + libros.add(a));
        System.out.println("add(b): " + libros.add(b));
        System.out.println("add(c): " + libros.add(c));
        System.out.println("Tamaño del set: " + libros.size());
        System.out.println("Contenido: " + libros);

        // Casos de prueba del contrato
        System.out.println();
        System.out.println("Reflexiva a.equals(a): " + a.equals(a));
        System.out.println("Simétrica a.equals(b) y b.equals(a): " + (a.equals(b) == b.equals(a)));
        System.out.println("equals con null: " + a.equals(null));
        System.out.println("equals con otra clase: " + a.equals("hola"));
        System.out.println("ISBN distinto: " + a.equals(c));
        System.out.println("Iguales tienen mismo hashCode: " + (a.hashCode() == b.hashCode()));
        System.out.println("hashCode consistente: " + (a.hashCode() == a.hashCode()));
        System.out.println("contains(b) en el set: " + libros.contains(b));
    }
}