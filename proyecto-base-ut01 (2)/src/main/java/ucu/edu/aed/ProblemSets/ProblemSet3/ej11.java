import java.io.*;
import java.util.*;

public class FrecuenciaPalabras {

    public static void main(String[] args) throws IOException {
        String archivo = "libro.txt";
        if (args.length > 0) {
            archivo = args[0];
        }

        // clave = palabra, valor = cantidad de veces que aparece
        Map<String, Integer> frecuencias = new HashMap<>();

        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivo), "UTF-8"));
        String linea;
        while ((linea = br.readLine()) != null) {
            // separo por todo lo que no sea una letra
            String[] palabras = linea.toLowerCase().split("[^\\p{L}]+");
            for (String palabra : palabras) {
                if (!palabra.isEmpty()) {
                    frecuencias.put(palabra, frecuencias.getOrDefault(palabra, 0) + 1);
                }
            }
        }
        br.close();

        // paso las entradas a una lista para poder ordenarlas por frecuencia
        List<Map.Entry<String, Integer>> lista = new ArrayList<>(frecuencias.entrySet());
        lista.sort((a, b) -> b.getValue() - a.getValue());

        System.out.println("Palabras distintas: " + frecuencias.size());
        System.out.println("Las 10 que más ocurren:");

        PrintWriter csv = new PrintWriter("top10.csv", "UTF-8");
        csv.println("palabra,frecuencia");
        for (int i = 0; i < 10 && i < lista.size(); i++) {
            String palabra = lista.get(i).getKey();
            int veces = lista.get(i).getValue();
            System.out.println((i + 1) + ". " + palabra + " -> " + veces);
            csv.println(palabra + "," + veces);
        }
        csv.close();
    }
}