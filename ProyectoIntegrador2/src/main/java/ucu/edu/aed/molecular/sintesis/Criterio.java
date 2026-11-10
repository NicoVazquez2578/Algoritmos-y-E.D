package ucu.edu.aed.molecular.sintesis;

/*
  Cómo se convierte una reacción en un número (peso) para buscar la mejor ruta. 
  Responde a la pregunta guía "¿cómo se combinan costo y riesgo?":
 */
@FunctionalInterface
public interface Criterio {

    double peso(Reaccion r);

    Criterio COSTO = Reaccion::getCosto;

    Criterio RIESGO = Reaccion::getRiesgo;

    static Criterio combinado(double alfa, double costoReferencia) {
        if (alfa < 0 || alfa > 1) throw new IllegalArgumentException("alfa debe estar entre 0 y 1");
        if (costoReferencia <= 0) throw new IllegalArgumentException("El costo de referencia debe ser positivo");
        return r -> alfa * (r.getCosto() / costoReferencia)
                + (1 - alfa) * (r.getRiesgo() / Reaccion.RIESGO_MAXIMO);
    }
}
