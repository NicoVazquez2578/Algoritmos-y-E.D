import java.io.*;

public class Main {
    public static void main(String[] args) {
        GrafoActores grafo = new GrafoActores();

        try (BufferedReader br = new BufferedReader(new FileReader("actores.csv"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String actor = linea.trim();
                if (!actor.isEmpty()) {
                    grafo.agregarActor(actor);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer actores.csv: " + e.getMessage());
        }

        try (BufferedReader br = new BufferedReader(new FileReader("en_películas.csv"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 2) {
                    String actor1 = datos[0].trim();
                    String actor2 = datos[1].trim();
                    grafo.agregarRelacion(actor1, actor2);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer en_películas.csv: " + e.getMessage());
        }

        String[] actoresConsulta = {
            "John_Goodman",
            "Tom_Cruise",
            "Jason_Statham",
            "Lukas_Haas",
            "Djimon_Hounsou"
        };

        try (PrintWriter writer = new PrintWriter(new FileWriter("salida.txt"))) {
            for (String actor : actoresConsulta) {
                int nroBacon = grafo.numBacon(actor);
                String resultado = "Número de Bacon para " + actor + ": " + (nroBacon != -1 ? nroBacon : "Sin conexión");
                
                System.out.println(resultado);
                writer.println(resultado);
            }
        } catch (IOException e) {
            System.err.println("Error al escribir salida.txt: " + e.getMessage());
        }
    }
}