package ucu.edu.aed.ProblemSets.ProblemSet3.ej16;

import java.util.ArrayList;
import java.util.List;

public class TArbolGenealogico {
    private TNodoArbolGenealogico raiz;

    public TArbolGenealogico(TNodoArbolGenealogico raiz) {
        this.raiz = raiz;
    }

    public TNodoArbolGenealogico getRaiz() {
        return raiz;
    }

    // Auxiliar: Busca un nodo por el nombre de la persona
    public TNodoArbolGenealogico buscarNodo(String nombre, TNodoArbolGenealogico actual) {
        if (actual == null) actual = this.raiz;
        if (actual == null) return null;

        if (actual.getPersona().getNombre().equalsIgnoreCase(nombre)) {
            return actual;
        }

        for (TNodoArbolGenealogico hijo : actual.getHijos()) {
            TNodoArbolGenealogico hallado = buscarNodo(nombre, hijo);
            if (hallado != null) return hallado;
        }
        return null;
    }

    // 1. Listar todos los descendientes de una persona dada
    public List<Persona> listarDescendientes(String nombrePersona) {
        List<Persona> descendientes = new ArrayList<>();
        TNodoArbolGenealogico nodo = buscarNodo(nombrePersona, raiz);
        if (nodo != null) {
            recolectarDescendientes(nodo, descendientes);
        }
        return descendientes;
    }

    private void recolectarDescendientes(TNodoArbolGenealogico nodo, List<Persona> lista) {
        for (TNodoArbolGenealogico hijo : nodo.getHijos()) {
            lista.add(hijo.getPersona());
            recolectarDescendientes(hijo, lista);
        }
    }

    // 2. Calcular la altura del árbol
    public int calcularAltura(TNodoArbolGenealogico nodo) {
        if (nodo == null) nodo = this.raiz;
        if (nodo == null || nodo.getHijos().isEmpty()) return 0;

        int maxAlturaHijos = 0;
        for (TNodoArbolGenealogico hijo : nodo.getHijos()) {
            int alt = calcularAltura(hijo);
            if (alt > maxAlturaHijos) {
                maxAlturaHijos = alt;
            }
        }
        return 1 + maxAlturaHijos;
    }

    // 3. Contar la cantidad total de personas
    public int contarPersonas(TNodoArbolGenealogico nodo) {
        if (nodo == null) nodo = this.raiz;
        if (nodo == null) return 0;

        int total = 1;
        for (TNodoArbolGenealogico hijo : nodo.getHijos()) {
            total += contarPersonas(hijo);
        }
        return total;
    }

    // 4. Obtener todas las personas de una generación dada
    public List<Persona> obtenerPersonasPorGeneracion(int generacionObjetivo) {
        List<Persona> resultado = new ArrayList<>();
        obtenerGeneracionRec(raiz, generacionObjetivo, 0, resultado);
        return resultado;
    }

    private void obtenerGeneracionRec(TNodoArbolGenealogico nodo, int genObjetivo, int nivelActual, List<Persona> lista) {
        if (nodo == null) return;
        if (nivelActual == genObjetivo) {
            lista.add(nodo.getPersona());
            return;
        }
        for (TNodoArbolGenealogico hijo : nodo.getHijos()) {
            obtenerGeneracionRec(hijo, genObjetivo, nivelActual + 1, lista);
        }
    }

    // Auxiliar: Obtiene el camino de nodos desde la raíz hasta la persona dada
    private boolean obtenerCamino(TNodoArbolGenealogico nodo, String nombre, List<TNodoArbolGenealogico> camino) {
        if (nodo == null) return false;

        camino.add(nodo);
        if (nodo.getPersona().getNombre().equalsIgnoreCase(nombre)) {
            return true;
        }

        for (TNodoArbolGenealogico hijo : nodo.getHijos()) {
            if (obtenerCamino(hijo, nombre, camino)) {
                return true;
            }
        }

        camino.remove(camino.size() - 1);
        return false;
    }

    // 5. Encontrar el ancestro común más cercano entre dos personas
    public Persona ancestroComunMasCercano(String nombre1, String nombre2) {
        List<TNodoArbolGenealogico> camino1 = new ArrayList<>();
        List<TNodoArbolGenealogico> camino2 = new ArrayList<>();

        if (!obtenerCamino(raiz, nombre1, camino1) || !obtenerCamino(raiz, nombre2, camino2)) {
            return null;
        }

        Persona ancestro = null;
        int i = 0;
        while (i < camino1.size() && i < camino2.size()) {
            if (camino1.get(i) == camino2.get(i)) {
                ancestro = camino1.get(i).getPersona();
            } else {
                break;
            }
            i++;
        }
        return ancestro;
    }

    // 6. Determinar si una persona es descendiente de otra
    public boolean esDescendiente(String nombreAncestro, String nombreDescendiente) {
        List<TNodoArbolGenealogico> camino = new ArrayList<>();
        if (!obtenerCamino(raiz, nombreDescendiente, camino)) {
            return false;
        }

        for (int i = 0; i < camino.size() - 1; i++) {
            if (camino.get(i).getPersona().getNombre().equalsIgnoreCase(nombreAncestro)) {
                return true;
            }
        }
        return false;
    }
}