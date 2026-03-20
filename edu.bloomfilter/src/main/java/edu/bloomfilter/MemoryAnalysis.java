package edu.bloomfilter;

import java.util.*;

public class MemoryAnalysis {
    static final int N = 1_000_000;
    static final double EPSILON = 0.01;

    public static void main(String[] args) {
        Random rng = new Random(0);
        List<String> data = new ArrayList<>(N);
        for (int i = 0; i < N; i++)
            data.add("element-" + rng.nextLong());

        // ── Memoria BloomFilter ───────────────────────────────────────────
        System.gc();
        long beforeBloom = Runtime.getRuntime().totalMemory()
                         - Runtime.getRuntime().freeMemory();

        BloomFilter<String> bloom = new BloomFilter<>(N, EPSILON, String::hashCode);
        data.forEach(bloom::add);

        long afterBloom = Runtime.getRuntime().totalMemory()
                        - Runtime.getRuntime().freeMemory();

        long bloomMemory = afterBloom - beforeBloom;

        // ── Memoria HashSet ───────────────────────────────────────────────
        System.gc();
        long beforeHash = Runtime.getRuntime().totalMemory()
                        - Runtime.getRuntime().freeMemory();

        HashSet<String> hashSet = new HashSet<>(data);

        long afterHash = Runtime.getRuntime().totalMemory()
                       - Runtime.getRuntime().freeMemory();

        long hashMemory = afterHash - beforeHash;

        // ── Memoria teórica ───────────────────────────────────────────────
        long bloomTheoretical = bloom.memoryBytes();
        long hashTheoretical  = (long) N * 32; // ~32 bytes por entrada

        // ── Tasa de FP ────────────────────────────────────────────────────
        double fpRate = BloomFilterValidator.measureFPRate(
                new BloomFilter<>(N, EPSILON, String::hashCode),
                new ArrayList<>(data),
                10_000);

        // ── Resultados ────────────────────────────────────────────────────
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║         ANÁLISIS DE MEMORIA — RESULTADOS         ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.printf ("║  BloomFilter memoria real:      %,15d bytes ║%n", bloomMemory);
        System.out.printf ("║  BloomFilter memoria teórica:   %,15d bytes ║%n", bloomTheoretical);
        System.out.printf ("║  HashSet    memoria real:       %,15d bytes ║%n", hashMemory);
        System.out.printf ("║  HashSet    memoria teórica:    %,15d bytes ║%n", hashTheoretical);
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.printf ("║  Tasa FP real:    %.4f                          ║%n", fpRate);
        System.out.printf ("║  Tasa FP teórica: %.4f (ε=1%%)                  ║%n", EPSILON);
        System.out.println("╚══════════════════════════════════════════════════╝");
    }
}