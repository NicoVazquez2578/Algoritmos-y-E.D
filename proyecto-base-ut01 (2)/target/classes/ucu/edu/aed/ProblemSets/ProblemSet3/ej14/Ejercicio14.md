# Ejercicio 14: Resolución de Colisiones

Dado un mismo conjunto de claves enteras, se insertan las claves en tablas hash usando tres estrategias de resolución de colisiones: **sondeo lineal**, **sondeo cuadrático** y **encadenamiento separado**.

**Claves (en el orden dado):** 45, 12, 37, 82, 29, 54, 31, 76, 18, 93, 11, 68

---

## 1. Tamaño de tabla y justificación

- **Tamaño elegido:** M = 17
- **Justificación:** Hay 12 claves, por lo que hacen falta al menos 12 posiciones. La unidad recomienda dimensionar la tabla ~10% más grande de lo necesario (12 x 1.1 = 13.2). También se recomienda un M primo para que el módulo distribuya de forma uniforme. El número primo siguiente a 13.2 es **17**, lo que resulta en un factor de carga λ = 12 / 17 ≈ 0.71.

---

## 2. Función Hash

La función hash seleccionada es: **h(k) = k mod 17**

### Resultados del cálculo de h(k) para cada clave:

| Clave  | h(k) | Explicación / Cálculo |
| :----: | :--: | :-------------------- |
| **45** |  11  | 45 = 2 x 17 + 11      |
| **12** |  12  | 12 = 0 x 17 + 12      |
| **37** |  3   | 37 = 2 x 17 + 3       |
| **82** |  14  | 82 = 4 x 17 + 14      |
| **29** |  12  | 29 = 1 x 17 + 12      |
| **54** |  3   | 54 = 3 x 17 + 3       |
| **31** |  14  | 31 = 1 x 17 + 14      |
| **76** |  8   | 76 = 4 x 17 + 8       |
| **18** |  1   | 18 = 1 x 17 + 1       |
| **93** |  8   | 93 = 5 x 17 + 8       |
| **11** |  11  | 11 = 0 x 17 + 11      |
| **68** |  0   | 68 = 4 x 17 + 0       |

---

## 3. Inserciones y Estado Final de las Tablas

### Pasos de inserción por cada clave:

| Clave  | h(k) | Sondeo Lineal                          | Sondeo Cuadrático                 | Encadenamiento Separado |
| :----: | :--: | :------------------------------------- | :-------------------------------- | :---------------------- |
| **45** |  11  | 11 → libre                             | 11 → libre                        | 1.º en la cadena 11     |
| **12** |  12  | 12 → libre                             | 12 → libre                        | 1.º en la cadena 12     |
| **37** |  3   | 3 → libre                              | 3 → libre                         | 1.º en la cadena 3      |
| **82** |  14  | 14 → libre                             | 14 → libre                        | 1.º en la cadena 14     |
| **29** |  12  | 12 ocupada → 13 libre                  | 12 ocupada → 13 libre             | 2.º en la cadena 12     |
| **54** |  3   | 3 ocupada → 4 libre                    | 3 ocupada → 4 libre               | 2.º en la cadena 3      |
| **31** |  14  | 14 ocupada → 15 libre                  | 14 ocupada → 15 libre             | 2.º en la cadena 14     |
| **76** |  8   | 8 → libre                              | 8 → libre                         | 1.º en la cadena 8      |
| **18** |  1   | 1 → libre                              | 1 → libre                         | 1.º en la cadena 1      |
| **93** |  8   | 8 ocupada → 9 libre                    | 8 ocupada → 9 libre               | 2.º en la cadena 8      |
| **11** |  11  | 11, 12, 13, 14, 15 ocupadas → 16 libre | 11, 12, 15, 3 ocupadas → 10 libre | 2.º en la cadena 11     |
| **68** |  0   | 0 → libre                              | 0 → libre                         | 1.º en la cadena 0      |

---

### Estado final de las tablas:

| Posición | Sondeo Lineal | Sondeo Cuadrático | Encadenamiento Separado |
| :------: | :-----------: | :---------------: | :---------------------: |
|  **0**   |      68       |        68         |           68            |
|  **1**   |      18       |        18         |           18            |
|  **2**   |       –       |         –         |            –            |
|  **3**   |      37       |        37         |         37 → 54         |
|  **4**   |      54       |        54         |            –            |
|  **5**   |       –       |         –         |            –            |
|  **6**   |       –       |         –         |            –            |
|  **7**   |       –       |         –         |            –            |
|  **8**   |      76       |        76         |         76 → 93         |
|  **9**   |      93       |        93         |            –            |
|  **10**  |       –       |        11         |            –            |
|  **11**  |      45       |        45         |         45 → 11         |
|  **12**  |      12       |        12         |         12 → 29         |
|  **13**  |      29       |        29         |            –            |
|  **14**  |      82       |        82         |         82 → 31         |
|  **15**  |      31       |        31         |            –            |
|  **16**  |      11       |         –         |            –            |

---

## 4. Cantidad Total de Colisiones

Se cuentan las posiciones ocupadas encontradas durante las inserciones:

| Estrategia                  | Colisiones por Clave                               | Total Colisiones |
| :-------------------------- | :------------------------------------------------- | :--------------: |
| **Sondeo Lineal**           | 29 → 1, 54 → 1, 31 → 1, 93 → 1, 11 → 5             |      **9**       |
| **Sondeo Cuadrático**       | 29 → 1, 54 → 1, 31 → 1, 93 → 1, 11 → 4             |      **8**       |
| **Encadenamiento Separado** | 29, 54, 31, 93 y 11 caen en lista no vacía (1 c/u) |      **5**       |

---

## 5. Búsquedas Exitosas y No Exitosas

### A. Promedio de comparaciones para búsquedas exitosas:

|        Clave        | Sondeo Lineal | Sondeo Cuadrático | Encadenamiento |
| :-----------------: | :-----------: | :---------------: | :------------: |
|       **45**        |       1       |         1         |       1        |
|       **12**        |       1       |         1         |       1        |
|       **37**        |       1       |         1         |       1        |
|       **82**        |       1       |         1         |       1        |
|       **29**        |       2       |         2         |       2        |
|       **54**        |       2       |         2         |       2        |
|       **31**        |       2       |         2         |       2        |
|       **76**        |       1       |         1         |       1        |
|       **18**        |       1       |         1         |       1        |
|       **93**        |       2       |         2         |       2        |
|       **11**        |       6       |         5         |       2        |
|       **68**        |       1       |         1         |       1        |
|      **Total**      |    **21**     |      **20**       |     **17**     |
| **Promedio (÷ 12)** |   **1.75**    |     **1.67**      |    **1.42**    |

---

### B. Promedio de comparaciones para búsquedas no exitosas:

Claves de prueba utilizadas: **100 a 116** (17 claves no pertenecientes al conjunto, asegurando evaluar cada posición de partida posible).

|        Clave        | h(k) | Sondeo Lineal | Sondeo Cuadrático | Encadenamiento |
| :-----------------: | :--: | :-----------: | :---------------: | :------------: |
|       **100**       |  15  |       4       |         1         |       0        |
|       **101**       |  16  |       3       |         0         |       0        |
|       **102**       |  0   |       2       |         4         |       1        |
|       **103**       |  1   |       1       |         1         |       1        |
|       **104**       |  2   |       0       |         0         |       0        |
|       **105**       |  3   |       2       |         2         |       2        |
|       **106**       |  4   |       1       |         1         |       0        |
|       **107**       |  5   |       0       |         0         |       0        |
|       **108**       |  6   |       0       |         0         |       0        |
|       **109**       |  7   |       0       |         0         |       0        |
|       **110**       |  8   |       2       |         4         |       2        |
|       **111**       |  9   |       1       |         7         |       0        |
|       **112**       |  10  |       0       |         3         |       0        |
|       **113**       |  11  |       8       |         5         |       2        |
|       **114**       |  12  |       7       |         2         |       2        |
|       **115**       |  13  |       6       |         3         |       0        |
|       **116**       |  14  |       5       |         3         |       2        |
|      **Total**      |      |    **42**     |      **36**       |     **12**     |
| **Promedio (÷ 17)** |      |   **2.47**    |     **2.12**      |    **0.71**    |

---

## 6. Comparación Resumen y Conclusión

| Métrica                       |     Sondeo Lineal      |   Sondeo Cuadrático    |     Encadenamiento Separado      |
| :---------------------------- | :--------------------: | :--------------------: | :------------------------------: |
| **Colisiones**                |           9            |           8            |              **5**               |
| **Comp. Búsqueda Exitosa**    |          1.75          |          1.67          |             **1.42**             |
| **Comp. Búsqueda No Exitosa** |          2.47          |          2.12          |             **0.71**             |
| **Peor Búsqueda Exitosa**     |      6 (clave 11)      |      5 (clave 11)      |              **2**               |
| **Uso de Memoria**            | Solo la tabla (17 pos) | Solo la tabla (17 pos) | Tabla de 17 cabeceras + 12 nodos |

### Conclusión final:

Para este conjunto de datos con M = 17, la estrategia de **Encadenamiento Separado funcionó mejor**, seguida por el **Sondeo Cuadrático** y por último el **Sondeo Lineal**.

- **Sondeo Lineal:** Sufre de _agrupamiento primario_. Las claves 45, 12, 29, 82 y 31 forman el bloque contiguo de la 11 a la 15, obligando a recorridos muy largos (hasta 8 comparaciones en búsquedas fallidas).
- **Sondeo Cuadrático:** Disminuye el agrupamiento al saltar con i², escapando del bloque inicial y mejorando los promedios, aunque no garantiza encontrar lugar libre si λ > 0.5.
- **Encadenamiento Separado:** Evita totalmente el agrupamiento entre posiciones distintas. Cada colisión solo impacta a la lista de su propia casilla, manteniendo un máximo de 2 recorridos por nodo y facilitando el proceso de eliminación.
