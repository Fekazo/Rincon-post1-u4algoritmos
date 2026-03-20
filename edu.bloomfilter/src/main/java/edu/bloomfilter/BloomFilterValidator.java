package edu.bloomfilter;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class BloomFilterValidator {
    public static double measureFPRate(BloomFilter<String> filter,
            List<String> inserted,
            int queryCount) {
        // Insertar todos los elementos
        inserted.forEach(filter::add);
        // Generar queries que definitivamente NO están en el filtro
        Set<String> insertedSet = new HashSet<>(inserted);
        long fpCount = 0, total = 0;
        Random rng = new Random(42);
        while (total < queryCount) {
            String query = "query_" + rng.nextLong();
            if (!insertedSet.contains(query)) {
                if (filter.mightContain(query))
                    fpCount++;
                total++;
            }
        }
        return (double) fpCount / total;
    }
}