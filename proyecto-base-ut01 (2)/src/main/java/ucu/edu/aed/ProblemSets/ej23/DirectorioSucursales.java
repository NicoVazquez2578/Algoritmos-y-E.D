package ucu.edu.aed.ProblemSets.ej23;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import ucu.edu.aed.impl.ListaArray;
import ucu.edu.aed.tda.TDALista;

/**
 * Gestiona el directorio de sucursales de la empresa AED (Ejercicio 23).
 * Usa ListaArray que implementa TDA Lista, cumpliendo el contrato del ejercicio
 * ("basado en TDA Lista Simplemente Enlazada con genéricos").
 */
public class DirectorioSucursales {

    // Programa contra la interfaz para que la implementación sea intercambiable
    private final TDALista<String> sucursales;

    public DirectorioSucursales() {
        sucursales = new ListaArray<>();
    }

    public void agregar(String ciudad) {
        sucursales.agregar(ciudad);
    }

    public boolean buscar(String ciudad) {
        return sucursales.contiene(ciudad);
    }

    public boolean quitar(String ciudad) {
        return sucursales.remover(ciudad);
    }

    public void listar() {
        for (int i = 0; i < sucursales.tamaño(); i++) {
            System.out.println(sucursales.obtener(i));
        }
    }

    // Imprime todas las sucursales separadas por el delimitador indicado
    public void imprimir(String separador) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sucursales.tamaño(); i++) {
            if (i > 0) sb.append(separador);
            sb.append(sucursales.obtener(i));
        }
        System.out.println(sb);
    }

    public int cantidad() { return sucursales.tamaño(); }
    public boolean esVacio() { return sucursales.esVacio(); }

    // Lee un archivo de texto con una sucursal por línea desde los recursos del classpath
    public void cargarDesdeArchivo(String nombreArchivo) throws Exception {
        InputStream stream = DirectorioSucursales.class.getClassLoader()
                .getResourceAsStream(nombreArchivo);
        if (stream == null) {
            System.out.println("Archivo no encontrado: " + nombreArchivo);
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String linea;
        while ((linea = reader.readLine()) != null) {
            linea = linea.trim();
            if (!linea.isEmpty()) agregar(linea);
        }
        reader.close();
    }
}
