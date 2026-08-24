package ucu.edu.aed.ProblemSets;

import junit.framework.TestCase;
import ucu.edu.aed.impl.Conjunto;
import ucu.edu.aed.tda.TDAConjunto;

/**
 * Casos de prueba para el TDA Conjunto (Ejercicio 24).
 *
 * <p>Cada método de acá abajo corresponde a uno de los 8 casos de la
 * sección "Especificación de casos de prueba" de
 * Ejercicio24_Parte1.txt (vacíos, disjuntos, idénticos, superposición
 * parcial, subconjunto, elemento común en cada extremo, un solo
 * elemento) — conviene mirar ese archivo si no queda claro por qué se
 * eligió justamente ese caso. Al final hay algunos tests extra que no
 * son parte de esa lista, pero validan reglas básicas del TDA: que un
 * conjunto no admite alumnos repetidos, que union/interseccion no
 * modifican los conjuntos originales, y esSubconjuntoDe.</p>
 */
public class Ejercicio24Test extends TestCase {

    private TAlumno ana;
    private TAlumno bruno;
    private TAlumno carla;
    private TAlumno diego;
    private TAlumno elena;

    @Override
    protected void setUp() {
        ana = new TAlumno(1001, "Ana", "Pérez");
        bruno = new TAlumno(1002, "Bruno", "Gómez");
        carla = new TAlumno(1003, "Carla", "Díaz");
        diego = new TAlumno(1004, "Diego", "Suárez");
        elena = new TAlumno(1005, "Elena", "Fernández");
    }

    private TDAConjunto<TAlumno> nuevoConjunto() {
        return new Conjunto<>(TAlumno.POR_CEDULA);
    }

    // --- Caso 1: ambos conjuntos vacíos ---

    public void testUnionDeDosConjuntosVacios() {
        TDAConjunto<TAlumno> a = nuevoConjunto();
        TDAConjunto<TAlumno> b = nuevoConjunto();
        assertTrue(a.union(b).esVacio());
    }

    public void testInterseccionDeDosConjuntosVacios() {
        TDAConjunto<TAlumno> a = nuevoConjunto();
        TDAConjunto<TAlumno> b = nuevoConjunto();
        assertTrue(a.interseccion(b).esVacio());
    }

    // --- Caso 2: un conjunto vacío, el otro no ---

    public void testUnionConUnConjuntoVacioDaElOtroConjunto() {
        TDAConjunto<TAlumno> a = nuevoConjunto();
        a.agregar(ana);
        a.agregar(bruno);
        TDAConjunto<TAlumno> vacio = nuevoConjunto();

        TDAConjunto<TAlumno> resultado = a.union(vacio);
        assertEquals(2, resultado.tamaño());
        assertTrue(resultado.contiene(ana));
        assertTrue(resultado.contiene(bruno));
    }

    public void testInterseccionConUnConjuntoVacioEsVacia() {
        TDAConjunto<TAlumno> a = nuevoConjunto();
        a.agregar(ana);
        TDAConjunto<TAlumno> vacio = nuevoConjunto();

        assertTrue(a.interseccion(vacio).esVacio());
    }

    // --- Caso 3: conjuntos disjuntos ---

    public void testUnionDeConjuntosDisjuntos() {
        TDAConjunto<TAlumno> aed1 = nuevoConjunto();
        aed1.agregar(ana);
        aed1.agregar(bruno);

        TDAConjunto<TAlumno> pf = nuevoConjunto();
        pf.agregar(diego);
        pf.agregar(elena);

        TDAConjunto<TAlumno> union = aed1.union(pf);
        assertEquals(4, union.tamaño());
        assertTrue(union.contiene(ana));
        assertTrue(union.contiene(bruno));
        assertTrue(union.contiene(diego));
        assertTrue(union.contiene(elena));
    }

    public void testInterseccionDeConjuntosDisjuntosEsVacia() {
        TDAConjunto<TAlumno> aed1 = nuevoConjunto();
        aed1.agregar(ana);
        TDAConjunto<TAlumno> pf = nuevoConjunto();
        pf.agregar(diego);

        assertTrue(aed1.interseccion(pf).esVacio());
    }

    // --- Caso 4: conjuntos idénticos ---

    public void testUnionDeConjuntosIdenticosNoDuplicaElementos() {
        TDAConjunto<TAlumno> a = nuevoConjunto();
        a.agregar(ana);
        a.agregar(bruno);
        TDAConjunto<TAlumno> b = nuevoConjunto();
        b.agregar(ana);
        b.agregar(bruno);

        assertEquals(2, a.union(b).tamaño());
    }

    public void testInterseccionDeConjuntosIdenticosDaElMismoConjunto() {
        TDAConjunto<TAlumno> a = nuevoConjunto();
        a.agregar(ana);
        a.agregar(bruno);
        TDAConjunto<TAlumno> b = nuevoConjunto();
        b.agregar(ana);
        b.agregar(bruno);

        TDAConjunto<TAlumno> interseccion = a.interseccion(b);
        assertEquals(2, interseccion.tamaño());
        assertTrue(interseccion.contiene(ana));
        assertTrue(interseccion.contiene(bruno));
    }

    // --- Caso 5: superposición parcial (el caso de AED1 y PF: comparten a Carla) ---

    public void testUnionConSolapamientoParcialNoRepiteElElementoComun() {
        TDAConjunto<TAlumno> aed1 = nuevoConjunto();
        aed1.agregar(ana);
        aed1.agregar(bruno);
        aed1.agregar(carla);

        TDAConjunto<TAlumno> pf = nuevoConjunto();
        pf.agregar(carla);
        pf.agregar(diego);
        pf.agregar(elena);

        TDAConjunto<TAlumno> union = aed1.union(pf);
        assertEquals(5, union.tamaño()); // 3 + 3 - 1 en común
        assertTrue(union.contiene(ana));
        assertTrue(union.contiene(carla));
        assertTrue(union.contiene(elena));
    }

    public void testInterseccionConSolapamientoParcialDaSoloElComun() {
        TDAConjunto<TAlumno> aed1 = nuevoConjunto();
        aed1.agregar(ana);
        aed1.agregar(bruno);
        aed1.agregar(carla);

        TDAConjunto<TAlumno> pf = nuevoConjunto();
        pf.agregar(carla);
        pf.agregar(diego);
        pf.agregar(elena);

        TDAConjunto<TAlumno> interseccion = aed1.interseccion(pf);
        assertEquals(1, interseccion.tamaño());
        assertTrue(interseccion.contiene(carla));
    }

    // --- Caso 6: un conjunto es subconjunto del otro ---

    public void testUnionCuandoUnConjuntoEsSubconjuntoDelOtro() {
        TDAConjunto<TAlumno> grande = nuevoConjunto();
        grande.agregar(ana);
        grande.agregar(bruno);
        grande.agregar(carla);

        TDAConjunto<TAlumno> chico = nuevoConjunto();
        chico.agregar(bruno);

        assertEquals(3, grande.union(chico).tamaño());
    }

    public void testInterseccionCuandoUnConjuntoEsSubconjuntoDelOtroDaElChico() {
        TDAConjunto<TAlumno> grande = nuevoConjunto();
        grande.agregar(ana);
        grande.agregar(bruno);
        grande.agregar(carla);

        TDAConjunto<TAlumno> chico = nuevoConjunto();
        chico.agregar(bruno);

        TDAConjunto<TAlumno> interseccion = grande.interseccion(chico);
        assertEquals(1, interseccion.tamaño());
        assertTrue(interseccion.contiene(bruno));
    }

    // --- Caso 7: el elemento común está en un extremo (primero o último) ---

    public void testInterseccionConElementoComunEnElPrimerLugar() {
        TDAConjunto<TAlumno> a = nuevoConjunto();
        a.agregar(ana);   // 1001, la cédula más chica -> queda primera
        a.agregar(carla);
        TDAConjunto<TAlumno> b = nuevoConjunto();
        b.agregar(ana);
        b.agregar(elena);

        TDAConjunto<TAlumno> interseccion = a.interseccion(b);
        assertEquals(1, interseccion.tamaño());
        assertTrue(interseccion.contiene(ana));
    }

    public void testInterseccionConElementoComunEnElUltimoLugar() {
        TDAConjunto<TAlumno> a = nuevoConjunto();
        a.agregar(ana);
        a.agregar(elena);   // 1005, la cédula más grande -> queda última
        TDAConjunto<TAlumno> b = nuevoConjunto();
        b.agregar(diego);
        b.agregar(elena);

        TDAConjunto<TAlumno> interseccion = a.interseccion(b);
        assertEquals(1, interseccion.tamaño());
        assertTrue(interseccion.contiene(elena));
    }

    // --- Caso 8: conjuntos de un solo elemento ---

    public void testUnionDeConjuntosDeUnSoloElementoDistintos() {
        TDAConjunto<TAlumno> a = nuevoConjunto();
        a.agregar(ana);
        TDAConjunto<TAlumno> b = nuevoConjunto();
        b.agregar(bruno);

        assertEquals(2, a.union(b).tamaño());
    }

    public void testInterseccionDeConjuntosDeUnSoloElementoIgual() {
        TDAConjunto<TAlumno> a = nuevoConjunto();
        a.agregar(ana);
        TDAConjunto<TAlumno> b = nuevoConjunto();
        b.agregar(ana);

        TDAConjunto<TAlumno> interseccion = a.interseccion(b);
        assertEquals(1, interseccion.tamaño());
        assertTrue(interseccion.contiene(ana));
    }

    // --- No se admiten alumnos repetidos dentro de un mismo conjunto ---

    public void testAgregarElMismoAlumnoDosVecesNoLoDuplica() {
        TDAConjunto<TAlumno> aed1 = nuevoConjunto();
        aed1.agregar(ana);
        aed1.agregar(ana); // mismo alumno (misma cédula) otra vez
        assertEquals(1, aed1.tamaño());
    }

    // --- Union/interseccion no modifican los conjuntos originales ---

    public void testUnionEInterseccionNoModificanLosConjuntosOriginales() {
        TDAConjunto<TAlumno> aed1 = nuevoConjunto();
        aed1.agregar(ana);
        aed1.agregar(carla);
        TDAConjunto<TAlumno> pf = nuevoConjunto();
        pf.agregar(carla);
        pf.agregar(diego);

        aed1.union(pf);
        aed1.interseccion(pf);

        assertEquals(2, aed1.tamaño());
        assertEquals(2, pf.tamaño());
    }

    // --- esSubconjuntoDe ---

    public void testEsSubconjuntoDeCasoVerdadero() {
        TDAConjunto<TAlumno> chico = nuevoConjunto();
        chico.agregar(bruno);
        TDAConjunto<TAlumno> grande = nuevoConjunto();
        grande.agregar(ana);
        grande.agregar(bruno);
        grande.agregar(carla);

        assertTrue(chico.esSubconjuntoDe(grande));
    }

    public void testEsSubconjuntoDeCasoFalso() {
        TDAConjunto<TAlumno> a = nuevoConjunto();
        a.agregar(ana);
        a.agregar(diego); // diego no está en "grande"
        TDAConjunto<TAlumno> grande = nuevoConjunto();
        grande.agregar(ana);
        grande.agregar(bruno);
        grande.agregar(carla);

        assertFalse(a.esSubconjuntoDe(grande));
    }

    public void testConjuntoVacioEsSubconjuntoDeCualquiera() {
        TDAConjunto<TAlumno> vacio = nuevoConjunto();
        TDAConjunto<TAlumno> grande = nuevoConjunto();
        grande.agregar(ana);

        assertTrue(vacio.esSubconjuntoDe(grande));
    }
}
