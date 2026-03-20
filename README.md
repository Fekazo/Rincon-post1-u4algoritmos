# Rincon-post1-u4algoritmos
Evidencias
Checkpoint 1
Checkpoint 1.jpg
Checkpoint 2
Checkpoint 2.jpg

LO DE ABAJO VA EN EL README
# Bloom Filter vs HashSet — Análisis Comparativo

## Parámetros
- n = 1 000 000 elementos
- ε = 1% (tasa de falsos positivos teórica)

## Tabla de resultados

| Métrica                 | BloomFilter           | HashSet               |
|-------------------------|-----------------------|-----------------------|
| Throughput (ops/ms)     | [pendiente JMH]       | [pendiente JMH]       |
| Memoria real (bytes)    | 1.198.152             | 47.340.624            |
| Memoria teórica (bytes) | 1.198.133             | 32.000.000            |
| Tasa de FP real         | 1.08%                 | 0%                    |
| Tasa de FP teórica (ε)  | 1.00%                 | 0%                    |

## Conclusiones de diseño

### ¿Cuándo usar BloomFilter?
- Cuando la memoria es un recurso crítico (~39x más eficiente que HashSet)
- Cuando se tolera una pequeña tasa de falsos positivos (~1%)
- Ejemplos: caché de URLs, detección de spam, bases de datos

### ¿Cuándo usar HashSet?
- Cuando se requiere exactitud absoluta (cero falsos positivos)
- Cuando la memoria no es una restricción
- Ejemplos: registros financieros, autenticación de usuarios
