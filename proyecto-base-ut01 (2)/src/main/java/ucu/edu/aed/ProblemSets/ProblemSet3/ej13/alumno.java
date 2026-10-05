import java.util.Objects;

public class Alumno {
    private int id;
    private String fullName;
    private String email;

    public Alumno(int id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }

    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }

    // Dos alumnos son el mismo si tienen el mismo id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alumno otro = (Alumno) o;
        return id == otro.id;
    }

    // Usa el mismo atributo que equals
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return fullName + " (id " + id + ")";
    }
}