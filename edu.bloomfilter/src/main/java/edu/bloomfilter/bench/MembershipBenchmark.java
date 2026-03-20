package edu.bloomfilter.bench;

import org.openjdk.jmh.annotations.*;

import edu.bloomfilter.BloomFilter;

import java.util.*;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 2)
@Fork(1)
public class MembershipBenchmark {
    static final int N = 1_000_000;
    List<String> data;
    BloomFilter<String> bloom;
    HashSet<String> hashSet;
    String[] queries;

    @Setup(Level.Trial)
    public void setup() {
        data = new ArrayList<>(N);
        Random rng = new Random(0);
        for (int i = 0; i < N; i++)
            data.add("element-" + rng.nextLong());
        // Bloom filter: epsilon = 1%
        bloom = new BloomFilter<>(N, 0.01, s -> s.hashCode());
        data.forEach(bloom::add);
        hashSet = new HashSet<>(data);
        // Queries: mitad presentes, mitad ausentes
        queries = new String[1000];
        for (int i = 0; i < 500; i++)
            queries[i] = data.get(i);
        for (int i = 500; i < 1000; i++)
            queries[i] = "absent-" + rng.nextLong();
    }

    @Benchmark
    public boolean bloomQuery() {
        return bloom.mightContain(queries[(int) (System.nanoTime() % 1000)]);
    }

    @Benchmark
    public boolean hashSetQuery() {
        return hashSet.contains(queries[(int) (System.nanoTime() % 1000)]);
    }
}