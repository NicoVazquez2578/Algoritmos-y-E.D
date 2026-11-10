package ucu.edu.aed.molecular.servicios;

import ucu.edu.aed.molecular.compatibilidad.*;
import ucu.edu.aed.molecular.molecula.Molecula;

import java.util.*;

/*
  Ordena moléculas candidatas de mayor a menor adecuación a unos parámetros objetivo.

  Criterio (justificación para el informe):
   1. Para cada propiedad con rango se calcula la desviación normalizada (Rango.desviacion):
      0 si está dentro; si no, distancia al borde / ancho del rango. Normalizar permite
      sumar propiedades de escalas distintas (cadena ~5, Wiener ~100).
   2. puntaje = 1 / (1 + suma de desviaciones): vale 1 si cumple todo y baja hacia 0.
   3. Empates: más rangos cumplidos primero; después, por nombre (ResultadoCompatibilidad.compareTo).
  Costo: n moléculas × costo del perfil (dominado por Wiener, O(V²)) + O(n log n) para ordenar.
*/
public class EvaluadorCompatibilidad {

    private final AnalizadorEstructural analizador;

    public EvaluadorCompatibilidad(AnalizadorEstructural analizador) { this.analizador = analizador; }

    public ResultadoCompatibilidad evaluar(PerfilEstructural p, ParametrosObjetivo obj) {
        double desvio = 0;
        int cumplidos = 0;
        if (obj.getCadena() != null) {
            double d = obj.getCadena().desviacion(p.getLongitudCadena());
            desvio += d; if (d == 0) cumplidos++;
        }
        if (obj.getWiener() != null) {
            double d = obj.getWiener().desviacion(p.getIndiceWiener());
            desvio += d; if (d == 0) cumplidos++;
        }
        for (Map.Entry<TipoCarbono, Rango> e : obj.getCarbonos().entrySet()) {
            double d = e.getValue().desviacion(p.getCarbonos(e.getKey()));
            desvio += d; if (d == 0) cumplidos++;
        }
        return new ResultadoCompatibilidad(p, 1.0 / (1.0 + desvio), cumplidos, obj.cantidadCriterios());
    }

    public List<ResultadoCompatibilidad> ordenar(List<Molecula> candidatas, ParametrosObjetivo obj) {
        List<ResultadoCompatibilidad> res = new ArrayList<>();
        for (Molecula m : candidatas) res.add(evaluar(analizador.perfil(m), obj));
        Collections.sort(res);
        return res;
    }
}
