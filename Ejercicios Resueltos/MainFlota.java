package ucu.edu.aed.flota;

import ucu.edu.aed.tda.TDALista;

/** Programa de prueba del Ejercicio 13 - Registro de la Federación Intergaláctica. */
public class MainFlota {

    public static void main(String[] args) {
        RegistroFlota registro = new RegistroFlota();

        // Se insertan en el orden dado en el enunciado
        registro.registrarNave(10, "Explorador", 0);
        registro.registrarNave(20, "Destructor", 90);
        registro.registrarNave(30, "Médica", 100);
        registro.registrarNave(40, "Explorador", 50);
        registro.registrarNave(50, "Carguero", 20);
        registro.registrarNave(60, "Destructor", 28);
        registro.registrarNave(70, "Explorador", 14);
        registro.registrarNave(80, "Médica", 7);
        registro.registrarNave(90, "Carguero", 23);
        registro.registrarNave(100, "Explorador", 26);

        // Esperado: [10, 40, 70, 100]
        TDALista<Integer> codigos = registro.codigosExploradoras();
        System.out.print("Exploradoras (" + codigos.tamaño() + "):");
        for (int i = 0; i < codigos.tamaño(); i++) { // es solo una prueba, la lista es chica
            System.out.print(" " + codigos.obtener(i));
        }
        System.out.println();

        // Esperado: (0 + 50 + 14 + 26) / 4 = 22.5
        System.out.println("Combustible promedio: " + registro.promedioCombustibleExploradoras());

        // Código repetido: no se registra
        System.out.println("Registrar código 40 de nuevo: " + registro.registrarNave(40, "Carguero", 5));

        // Registro vacío
        RegistroFlota vacio = new RegistroFlota();
        System.out.println("Vacío -> exploradoras: " + vacio.codigosExploradoras().tamaño()
                + ", promedio: " + vacio.promedioCombustibleExploradoras());

        // Sin exploradoras
        RegistroFlota sinExploradoras = new RegistroFlota();
        sinExploradoras.registrarNave(1, "Carguero", 10);
        sinExploradoras.registrarNave(2, "Médica", 20);
        System.out.println("Sin exploradoras -> exploradoras: " + sinExploradoras.codigosExploradoras().tamaño()
                + ", promedio: " + sinExploradoras.promedioCombustibleExploradoras());

        // Una sola exploradora
        RegistroFlota una = new RegistroFlota();
        una.registrarNave(5, "Explorador", 33);
        System.out.println("Una exploradora -> promedio: " + una.promedioCombustibleExploradoras());

        // División real: promedio de 1 y 2 tiene que ser 1.5 (no 1)
        RegistroFlota dos = new RegistroFlota();
        dos.registrarNave(1, "Explorador", 1);
        dos.registrarNave(2, "Explorador", 2);
        System.out.println("Promedio de 1 y 2 -> " + dos.promedioCombustibleExploradoras());
    }
}
