package ucu.edu.aed.ProyectoPrimerHito;

import ucu.edu.aed.tda.TDALista;

/*
  Administra los vehiculos que estan esperando un repuesto.
 
  A diferencia de la cola normal del taller (FIFO), aca hace falta poder
  sacar un vehiculo puntual apenas llega su repuesto, sin importar el
  orden de llegada. Por eso el metodo clave es quitarPorPatente().
 
  Es una interfaz porque el Desafio 3 pide comparar dos formas de buscar
  por patente: GestorEsperaRepuestosV1 recorre la lista (O(n)), y una
  futura GestorEsperaRepuestosV2 podria usar un indice auxiliar para
  ubicarla mas rapido (O(1)).
 */
public interface IGestorEsperaRepuestos {

    // Agrega un vehiculo a la espera de repuesto. 
    void agregar(Vehiculo vehiculo);

    /*
      Busca y retorna el vehiculo con esa patente sin removerlo de la espera.
      Si no se encuentra, devuelve null.
     */
    Vehiculo buscarPorPatente(String patente);

    /*
      Busca el vehiculo con esa patente, lo saca de la espera y lo
      devuelve. Si no lo encuentra, devuelve null.
     */
    Vehiculo quitarPorPatente(String patente);

    // Devuelve los vehiculos que estan esperando repuesto. 
    TDALista<Vehiculo> listar();
}