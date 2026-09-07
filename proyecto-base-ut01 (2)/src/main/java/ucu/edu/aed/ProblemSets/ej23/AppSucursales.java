package ucu.edu.aed.ProblemSets.ej23;

/**
 * Punto de entrada para el sistema de sucursales (Ejercicio 23 — Tareas 1 a 4).
 * Modifica las tareas comentadas abajo según la actividad del taller.
 */
public class AppSucursales {

    public static void main(String[] args) throws Exception {
        DirectorioSucursales directorio = new DirectorioSucursales();

        // Tarea 1 — Cargar suc1.txt y mostrar cantidad de sucursales
        directorio.cargarDesdeArchivo("suc1.txt");
        System.out.println("=== Tarea 1: sucursales en suc1.txt ===");
        directorio.listar();
        System.out.println("Total: " + directorio.cantidad());

        // Tarea 2 — Quitar Chicago
        System.out.println("\n=== Tarea 2: quitar Chicago ===");
        boolean quitado = directorio.quitar("Chicago");
        System.out.println("Chicago quitado: " + quitado);
        System.out.println("Total tras quitar: " + directorio.cantidad());

        // Tarea 3 — Cargar suc2.txt y mostrar total
        directorio.cargarDesdeArchivo("suc2.txt");
        System.out.println("\n=== Tarea 3: después de cargar suc2.txt ===");
        System.out.println("Total: " + directorio.cantidad());

        // Tarea 4 — Cargar suc3.txt e imprimir con separador ";"
        directorio.cargarDesdeArchivo("suc3.txt");
        System.out.println("\n=== Tarea 4: Imprimir con separador ';' ===");
        directorio.imprimir(";");
    }
}
