package edu.bloomfilter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BloomFilterTest {

    // ── Función hash base reutilizada en todos los tests ──────────────────────
    private static BloomFilter<String> buildFilter(int n, double epsilon) {
        return new BloomFilter<>(n, epsilon, String::hashCode);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 1: Cero falsos negativos — todo elemento insertado debe ser hallado
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("add/mightContain: cero falsos negativos para 1 000 elementos")
    void testNoFalseNegatives() {
        int n = 1_000;
        double epsilon = 0.01;
        BloomFilter<String> filter = buildFilter(n, epsilon);

        List<String> elements = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            elements.add("element_" + i);
        }

        // Insertar
        elements.forEach(filter::add);

        // Verificar que TODOS son encontrados (no puede haber falsos negativos)
        for (String e : elements) {
            assertTrue(filter.mightContain(e),
                "Falso negativo detectado para: " + e);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 2: Tasa de FP empírica ≤ 2ε para n=100 000, ε=1%
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("Tasa de FP empírica no supera 2ε para n=100 000, ε=1%")
    void testFalsePositiveRate() {
        int n = 100_000;
        double epsilon = 0.01;
        BloomFilter<String> filter = buildFilter(n, epsilon);

        // Generar lista de elementos a insertar
        List<String> inserted = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            inserted.add("item_" + i);
        }

        // Medir tasa de FP con el validador
        double fpRate = BloomFilterValidator.measureFPRate(filter, inserted, 10_000);

        System.out.printf("Tasa de FP empírica: %.4f  |  Límite (2ε): %.4f%n",
                fpRate, 2 * epsilon);

        assertTrue(fpRate <= 2 * epsilon,
            String.format("FP rate %.4f supera el límite 2ε=%.4f", fpRate, 2 * epsilon));
    }
}