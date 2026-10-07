package ucu.edu.aed.ProblemSets.ProblemSet2.Ej12;

import ucu.edu.aed.impl.Lista;
import ucu.edu.aed.tda.Arboles.Impl.ArbolBinarioBusqueda;
import ucu.edu.aed.tda.Arboles.TDAArbolBinario;
import ucu.edu.aed.tda.TDALista;
 
/*
  Grimorio del Archimago Aldric: guarda los hechizos en un árbol binario de
  búsqueda, usando el ID del hechizo como clave.
 */
public class Grimorio {
 
    private final TDAArbolBinario<Hechizo> hechizos;
 
    public Grimorio() {
        this.hechizos = new ArbolBinarioBusqueda<>();
    }
 
    public boolean agregarHechizo(int id, String nombre) {
        if (id <= 0 || nombre == null || nombre.isEmpty()) {
            return false;
        }
        return hechizos.insertar(new Hechizo(id, nombre));
    }
 
    public TDALista<Hechizo> hechizosProhibidos() {
        TDALista<Hechizo> prohibidos = new Lista<>();
        hechizos.inOrder(hechizo -> {
            if (hechizo.esProhibido()) {
                prohibidos.agregar(hechizo);
            }
        });
        return prohibidos;
    }
 
    public String generarCantico() {
        StringBuilder cantico = new StringBuilder();
        hechizos.inOrder(hechizo -> {
            if (hechizo.esProhibido()) {
                if (cantico.length() > 0) {
                    cantico.append(" - ");
                }
                cantico.append(hechizo.getNombre());
            }
        });
        return cantico.toString();
    }
}
 
