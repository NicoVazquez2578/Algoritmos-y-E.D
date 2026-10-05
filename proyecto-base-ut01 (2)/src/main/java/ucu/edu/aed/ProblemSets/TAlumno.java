package ucu.edu.aed.ProblemSets;

import java.util.Comparator;

/**
 * Ejercicio 24 - Conjuntos.
 *
 * <p>Representa un alumno con cédula (identificador de exactamente 4
 * dígitos), nombre y apellido. La cédula es el dato que identifica a
 * la persona: dos {@code TAlumno} se consideran "el mismo alumno" si
 * tienen la misma cédula, aunque el resto de los datos sea distinto.</p>
 *
 * <p>Por eso se sobreescriben {@link #equals} y {@link #hashCode}
 * comparando solo por cédula. Esto no es un detalle menor: la clase
 * {@code Conjunto} (y {@code ListaArray} por dentro) usan
 * {@code equals()} para decidir si un alumno "ya está" en un conjunto
 * — sin esta redefinición, Java compararía por identidad de objeto
 * (¿son el mismo objeto en memoria?) en vez de comparar por cédula, y
 * dos instancias distintas que representan al mismo alumno real
 * quedarían tratadas como alumnos diferentes.</p>
 */
public class TAlumno {

    /**
     * Orden por el que se van a comparar los alumnos al armar un
     * Conjunto&lt;TAlumno&gt; (se ordenan de menor a mayor cédula).
     * Se define acá, como una constante, para no tener que repetir la
     * misma comparación cada vez que se crea un Conjunto de alumnos.
     */
    public static final Comparator<TAlumno> POR_CEDULA =
            Comparator.comparingInt(TAlumno::getCedula);

    private final int cedula;
    private final String nombre;
    private final String apellido;

    public TAlumno(int cedula, String nombre, String apellido) {
        if (cedula < 1000 || cedula > 9999) {
            throw new IllegalArgumentException(
                    "La cédula debe tener exactamente 4 dígitos (1000-9999): " + cedula);
        }
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public int getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TAlumno)) {
            return false;
        }
        return this.cedula == ((TAlumno) obj).cedula;
    }

    @Override
    public int hashCode() {
        // Debe ser consistente con equals(): si dos TAlumno son
        // "iguales" (misma cédula), tienen que devolver el mismo
        // hashCode. Como equals() compara solo por cédula, alcanza con
        // basar el hash únicamente en la cédula.
        return Integer.hashCode(cedula);
    }

    @Override
    public String toString() {
        return cedula + " - " + nombre + " " + apellido;
    }
}
