package ucu.edu.aed.ProblemSets.ProblemSet2.Ej13;

/**
 * EJERCICIO 13 — Federación Intergaláctica
 *
 * Construye el AVL con las 10 naves del enunciado y demuestra las
 * dos operaciones principales.
 */
public class MainFederacion {

    public static void main(String[] args) {
        System.out.println("=== EJERCICIO 13: Federación Intergaláctica ===\n");

        RegistroNaves registro = new RegistroNaves();

        Nave[] naves = {
            new Nave( 10, "Explorador",  0),
            new Nave( 20, "Destructor", 90),
            new Nave( 30, "Médica",    100),
            new Nave( 40, "Explorador", 50),
            new Nave( 50, "Carguero",   20),
            new Nave( 60, "Destructor", 28),
            new Nave( 70, "Explorador", 14),
            new Nave( 80, "Médica",      7),
            new Nave( 90, "Carguero",   23),
            new Nave(100, "Explorador", 26)
        };

        System.out.println("Insertando naves en el AVL (con balance automático):");
        for (Nave n : naves) {
            registro.insertar(n);
            System.out.println("  " + n + " — altura AVL: " + registro.alturaArbol());
        }

        System.out.println("\nRegistro en inorden (orden por código):");
        for (Nave n : registro.inorden()) {
            System.out.println("  " + n);
        }

        System.out.println("\n--- Naves exploradoras ---");
        System.out.println("Códigos: " + registro.identificarNavesExploradoras());

        System.out.println("\n--- Combustible promedio de exploradoras ---");
        System.out.printf("Promedio: %.2f litros%n", registro.calcularCombustiblePromedio());
        // (0 + 50 + 14 + 26) / 4 = 90 / 4 = 22.50
    }
}
