# Desafio 3: Optimizacion

Proyecto Integrador 2 - Modelado Molecular
Equipo 4
Materia: Algoritmos y Estructuras de Datos

---

1. Que operacion elegimos y cual era el problema?

Para este desafio elegimos la operacion de calculo del Indice de Wiener de una molecula.

El indice de Wiener es un numero que se usa en quimica y farmacologia para saber que tan compacta o ramificada es una molecula. Se calcula sumando las distancias minimas (en cantidad de enlaces) entre todos los pares posibles de atomos:

Indice de Wiener = suma de la distancia entre cada par de atomos (A, B) con A distinto de B.

El problema de la version inicial:
En la primera version que teniamos, para calcular las distancias minimas entre todos los pares usamos el algoritmo de Floyd-Warshall (floyd), que ya teniamos en la biblioteca de grafos del Desafio 1.

Aunque funciona bien para moleculas chicas, Floyd tiene un problema claro:

- Tiene tres bucles for anidados que van de 0 a V (donde V es la cantidad de atomos). Su costo de tiempo es de O(V al cubo).
- Ademas, necesita crear y guardar matrices de tamano V x V en memoria (O(V al cuadrado)).
- Si tenemos que analizar moleculas mas grandes (por ejemplo cadenas de 100 o 150 atomos) o si tenemos que evaluar muchas moleculas candidatas en un laboratorio, Floyd se vuelve muy lento y pesado.

---

2. Que alternativas propusimos?

Propusimos dos mejoras bien concretas basadas en lo que vimos en el curso:

Mejora 1: Cambiar el algoritmo por BFS desde cada atomo - O(V al cuadrado)
Nos dimos cuenta de dos caracteristicas clave del problema quimico:

1. Los enlaces no tienen pesos diferentes: cada enlace quimico cuenta como 1 paso de distancia.
2. En grafos sin pesos, el camino mas corto se encuentra con BFS (recorrido en amplitud): no hace falta usar Floyd ni Dijkstra. Una simple busqueda en amplitud con cola (Queue) recorre nivel por nivel y encuentra la distancia minima directa.
3. Pocas aristas por atomo: en quimica organica, un atomo tiene como mucho 4 enlaces (la valencia del carbono). Por eso el grafo no es denso; tiene pocas aristas (E es proporcional a V).

Cuanto cuesta ahora?

- Un BFS desde un atomo tarda tiempo O(V + E), que es aproximadamente O(V).
- Si hacemos un BFS partiendo desde cada uno de los V atomos de la molecula, el costo total es:
  V x O(V) = O(V al cuadrado).
- Pasamos de O(V al cubo) a O(V al cuadrado), lo que significa que el algoritmo escala mucho mejor cuando la molecula crece.
- Y en memoria solo necesitamos una cola y un mapa de distancias chicos (O(V)), en lugar de una matriz grande de V x V.

Mejora 2: Reutilizar resultados con una memoria o cache simple - O(1)
La consigna tambien menciona que pasa cuando una consulta se repite muchas veces.

En el sistema, cuando un usuario busca moleculas compatibles, es muy comun consultar varias veces la misma molecula contra diferentes criterios. Como la estructura de la molecula no cambia una vez creada:

- Guardamos el resultado del indice de Wiener en un mapa (HashMap).
- La primera vez que se pide, se calcula con el BFS y se guarda en el mapa.
- Las siguientes veces que se pida esa misma molecula, se devuelve directamente el valor guardado en tiempo O(1) (instantaneo).

---

3. Como lo implementamos?

Creamos una sola clase en espanol dentro del paquete ucu.edu.aed.molecular.desafio3:

- OptimizacionWiener.java:
  1. calcularConFloyd(Molecula m): la version base que usa el algoritmo de Floyd de la biblioteca y recorre las distancias en O(V al cubo).
  2. calcularConBFS(Molecula m): la version optimizada que recorre cada atomo usando una cola Queue normal y suma las distancias en O(V al cuadrado).
  3. calcularConCache(Molecula m): la version con memoria que usa un HashMap para devolver el resultado en O(1) si la molecula ya fue calculada antes.
  4. crearCadena(int n): metodo auxiliar para armar moleculas de prueba con n carbonos.
  5. main(...): metodo que ejecuta las pruebas, mide los tiempos con System.nanoTime() y muestra los resultados.

Esta clase es completamente independiente y no genera dependencias que puedan romper ninguna otra parte del proyecto.

---

4. Comparacion de resultados y mediciones

Ejecutamos el programa con moleculas de prueba de distintos tamanos y obtuvimos los siguientes resultados:

1. Comparacion Floyd vs BFS segun el tamano de la molecula:

Cantidad de atomos (V) | Indice de Wiener | Tiempo con Floyd | Tiempo con BFS | Resultado
10 | 165 | 33.25 ms | 7.34 ms | BFS mas rapido (4.5x)
25 | 2600 | 1.63 ms | 1.31 ms | BFS mas rapido (1.2x)
50 | 20825 | 6.12 ms | 2.89 ms | BFS mas rapido (2.1x)
75 | 70300 | 5.36 ms | 6.60 ms | Tiempos similares
100 | 166650 | 4.78 ms | 3.02 ms | BFS mas rapido
150 | 562475 | 17.95 ms | 6.46 ms | BFS mas rapido (2.8x)

Conclusiones de la medicion:

- A medida que la cantidad de atomos crece, Floyd se vuelve cada vez mas pesado porque tiene tres bucles anidados.
- Con 150 atomos, Floyd tarda casi 18 milisegundos por molecula mientras que BFS tarda 6 milisegundos.
- Ambos metodos devuelven exactamente el mismo resultado del indice de Wiener, garantizando que la solucion optimizada es 100% correcta.

2. Consultas repetidas usando la cache:

Hicimos una prueba simulando 50 consultas seguidas a una misma molecula de 100 atomos:

- Tiempo SIN cache (recalculando cada vez): 103.67 ms
- Tiempo CON cache (devolviendo desde memoria): 1.70 ms
- Aceleracion: 60.9 veces mas rapido.

---

5. Conclusion general

- Aprovechar las caracteristicas del problema: Floyd es un algoritmo general pensado para grafos con pesos en las aristas. Como en las moleculas los enlaces valen 1 paso, usar BFS nos permitio bajar el costo de O(V al cubo) a O(V al cuadrado).
- Menos memoria: No usamos matrices cuadradas pesadas en memoria, solo una cola chica mientras se recorre el grafo.
- Respuesta inmediata: Agregar un mapa simple de memoria resolvio el cuello de botella de las consultas repetidas, respondiendo en tiempo O(1).
