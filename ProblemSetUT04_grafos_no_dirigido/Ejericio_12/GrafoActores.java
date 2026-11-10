import java.util.*;

public class GrafoActores {
    private final UndirectedGraph<String> grafo;

    public GrafoActores() {
        this.grafo = new UndirectedGraph<>();
    }

    public void agregarActor(String actor) {
        grafo.agregarVertice(actor);
    }

    public void agregarRelacion(String actor1, String actor2) {
        grafo.agregarArista(actor1, actor2);
    }

    public int numBacon(String actorNombre) {
        String origen = "Kevin_Bacon";

        if (!grafo.vertices().contains(origen) || !grafo.vertices().contains(actorNombre)) {
            return -1;
        }
        if (origen.equals(actorNombre)) {
            return 0;
        }

        UndirectedGraphAlgorithms.BeaResultado<String> resultadoBea = UndirectedGraphAlgorithms.bea(grafo, origen);
        Map<String, String> padre = resultadoBea.padre;

        if (!padre.containsKey(actorNombre)) {
            return -1;
        }

        int distancia = 0;
        String actual = actorNombre;
        while (actual != null && !actual.equals(origen)) {
            actual = padre.get(actual);
            distancia++;
        }

        return actual != null ? distancia : -1;
    }
}