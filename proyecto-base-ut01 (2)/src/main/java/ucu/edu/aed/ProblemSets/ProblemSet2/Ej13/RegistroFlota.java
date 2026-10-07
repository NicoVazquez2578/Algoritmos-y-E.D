package ucu.edu.aed.ProblemSets.ProblemSet2.Ej13;

import ucu.edu.aed.impl.Lista;
import ucu.edu.aed.tda.Arboles.Impl.AVL;
import ucu.edu.aed.tda.Arboles.TDAArbolBinario;
import ucu.edu.aed.tda.TDALista;
 
/**
 * Registro centralizado de la flota. Las naves se guardan en un árbol AVL
 * indexado por código de nave, para que las búsquedas sean O(log n) aunque
 * lleguen incorporaciones masivas e impredecibles.
 */
public class RegistroFlota {
 
    private final TDAArbolBinario<Nave> naves;
 
    public RegistroFlota() {
        this.naves = new AVL<>();
    }
 
    public boolean registrarNave(int codigo, String clase, int combustible) {
        if (codigo <= 0 || clase == null || clase.isEmpty() || combustible < 0) {
            return false;
        }
        return naves.insertar(new Nave(codigo, clase, combustible));
    }
 
    public TDALista<Integer> codigosExploradoras() {
        TDALista<Integer> codigos = new Lista<>();
        naves.inOrder(nave -> {
            if (nave.esExploradora()) {
                codigos.agregar(nave.getCodigo());
            }
        });
        return codigos;
    }
 
    public double promedioCombustibleExploradoras() {
        Acumulador acum = new Acumulador();
        naves.inOrder(nave -> {
            if (nave.esExploradora()) {
                acum.suma += nave.getCombustible();
                acum.cantidad++;
            }
        });
        if (acum.cantidad == 0) {
            return 0;
        }
        return (double) acum.suma / acum.cantidad; // división real, no entera
    }
 
    // Guarda la suma y la cantidad mientras se recorre el árbol. 
    private static class Acumulador {
        long suma = 0;
        int cantidad = 0;
    }
}
