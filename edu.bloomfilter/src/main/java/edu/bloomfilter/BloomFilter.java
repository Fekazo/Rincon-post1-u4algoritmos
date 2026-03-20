package edu.bloomfilter;

import java.util.BitSet;
import java.util.function.ToIntFunction;

/**
 * Bloom filter parametrizable con k funciones hash double-hashing.
 * 
 * @param <T> tipo de elementos a insertar
 */
public class BloomFilter<T> {
    private final BitSet bits;
    private final int m; // tamaño del arreglo de bits
    private final int k; // número de funciones hash
    private final ToIntFunction<T> hashFn; // función hash base

    /**
     * Construye un Bloom filter óptimo para n elementos y tasa de FP epsilon.
     * m = ceil(-n * ln(eps) / (ln(2)^2))
     * k = round((m / n) * ln(2))
     */
    public BloomFilter(int n, double epsilon, ToIntFunction<T> hashFn) {
        this.m = (int) Math.ceil(-n * Math.log(epsilon) / (Math.log(2) *
                Math.log(2)));
        this.k = (int) Math.max(1, Math.round((double) m / n * Math.log(2)));
        this.bits = new BitSet(m);
        this.hashFn = hashFn;
    }

    // Double hashing: h_i(x) = (h1(x) + i * h2(x)) mod m
    private int hash(int h1, int h2, int i) {
        return Math.floorMod(h1 + i * h2, m);
    }

/** Inserta el elemento en el filtro. */ 
public void add(T element) { 
int h1 = hashFn.applyAsInt(element); 
int h2 = Integer.reverse(h1) | 1; // h2 impar para cubrir todas las posiciones 
for (int i = 0; i < k; i++) bits.set(hash(h1, h2, i)); 
}

    /** Retorna true si el elemento posiblemente está en el conjunto. */
    public boolean mightContain(T element) {
        int h1 = hashFn.applyAsInt(element);
        int h2 = Integer.reverse(h1) | 1;
        for (int i = 0; i < k; i++) {
            if (!bits.get(hash(h1, h2, i)))
                return false;
        }
        return true;
    }

    public int getBitCount() {
        return m;
    }

    public int getHashCount() {
        return k;
    }

    public long memoryBytes() {
        return (long) Math.ceil(m / 8.0);
    }
}