import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GrafoActoresTest {

    @Test
    public void testKevinBaconEsCero() {
        GrafoActores g = new GrafoActores();
        g.agregarActor("Kevin_Bacon");
        assertEquals(0, g.numBacon("Kevin_Bacon"));
    }

    @Test
    public void testBaconNumeroDirecto() {
        GrafoActores g = new GrafoActores();
        g.agregarActor("Kevin_Bacon");
        g.agregarActor("Tom_Hanks");
        g.agregarRelacion("Kevin_Bacon", "Tom_Hanks");

        assertEquals(1, g.numBacon("Tom_Hanks"));
    }

    @Test
    public void testBaconNumeroDistanciaDos() {
        GrafoActores g = new GrafoActores();
        g.agregarActor("Kevin_Bacon");
        g.agregarActor("Tom_Hanks");
        g.agregarActor("Sally_Field");

        g.agregarRelacion("Kevin_Bacon", "Tom_Hanks");
        g.agregarRelacion("Tom_Hanks", "Sally_Field");

        assertEquals(2, g.numBacon("Sally_Field"));
    }

    @Test
    public void testActorNoConectado() {
        GrafoActores g = new GrafoActores();
        g.agregarActor("Kevin_Bacon");
        g.agregarActor("Actor_Aislado");

        assertEquals(-1, g.numBacon("Actor_Aislado"));
    }

    @Test
    public void testActorInexistente() {
        GrafoActores g = new GrafoActores();
        g.agregarActor("Kevin_Bacon");

        assertEquals(-1, g.numBacon("Actor_No_Registrado"));
    }
}