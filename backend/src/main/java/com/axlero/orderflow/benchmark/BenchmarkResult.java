package com.axlero.orderflow.benchmark;

import java.util.Map;

/**
 * Data carrier encapsulating baseline benchmark output metrics and environment context.
 */
public record BenchmarkResult(
    int warmupEvents,
    int measuredEvents,
    long consumedEvents,
    long durationNanos,
    double throughputOpsPerSec,
    double p50LatencyMicros,
    double p95LatencyMicros,
    double p99LatencyMicros,
    double maxLatencyMicros,
    boolean correctnessVerified,
    Map<String, String> environmentInfo
) {
}
