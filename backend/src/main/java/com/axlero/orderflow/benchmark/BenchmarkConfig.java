package com.axlero.orderflow.benchmark;

/**
 * Configuration holder for baseline benchmark execution.
 */
public record BenchmarkConfig(
    int warmupEvents,
    int measuredEvents,
    int bufferSize
) {
    public BenchmarkConfig {
        if (warmupEvents < 0) {
            throw new IllegalArgumentException("warmupEvents must be >= 0");
        }
        if (measuredEvents <= 0) {
            throw new IllegalArgumentException("measuredEvents must be > 0");
        }
        if (bufferSize <= 0 || (bufferSize & (bufferSize - 1)) != 0) {
            throw new IllegalArgumentException("bufferSize must be a power of 2");
        }
    }

    public static BenchmarkConfig defaultConfig() {
        return new BenchmarkConfig(10_000, 100_000, 8192);
    }
}
