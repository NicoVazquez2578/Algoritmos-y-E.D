package ucu.edu.aed.molecular.sintesis;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReaccionTest {

    private final Compuesto a = new Compuesto("A", null, null, true);
    private final Compuesto b = new Compuesto("B", null, null, true);
    private final Compuesto c = new Compuesto("C", null, null);

    @Test
    void produceYConsume() {
        Reaccion r = new Reaccion("R1", "", List.of(a, b), List.of(c), 10, 1);
        assertTrue(r.consume(a));
        assertTrue(r.produce(c));
        assertFalse(r.produce(a));
        assertFalse(r.consume(c));
        assertTrue(r.produce(new Compuesto("c", null, null)));   // igualdad por nombre
    }

    @Test
    void validaciones() {
        assertThrows(IllegalArgumentException.class, () -> new Reaccion("", "", List.of(a), List.of(c), 1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Reaccion(null, "", List.of(a), List.of(c), 1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Reaccion("R", "", List.of(), List.of(c), 1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Reaccion("R", "", null, List.of(c), 1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Reaccion("R", "", List.of(a), List.of(), 1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Reaccion("R", "", List.of(a), List.of(c), -1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Reaccion("R", "", List.of(a), List.of(c), 1, -0.1));
        assertThrows(IllegalArgumentException.class, () -> new Reaccion("R", "", List.of(a), List.of(c), 1, 10.1));
        assertThrows(IllegalArgumentException.class, () -> new Reaccion("R", "", List.of(a), List.of(a), 1, 1));
    }

    @Test
    void igualdad() {
        Reaccion r1 = new Reaccion("R1", "x", List.of(a), List.of(c), 1, 1);
        Reaccion otra = new Reaccion("R1", "y", List.of(b), List.of(c), 9, 9);
        assertEquals(r1, otra);
        assertEquals(r1.hashCode(), otra.hashCode());
        assertNotEquals(r1, new Reaccion("R2", "x", List.of(a), List.of(c), 1, 1));
    }
}
