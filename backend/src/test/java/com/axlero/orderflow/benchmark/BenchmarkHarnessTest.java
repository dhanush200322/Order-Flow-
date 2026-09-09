package com.axlero.orderflow.benchmark;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BenchmarkHarnessTest {

    @Test
    @DisplayName("Benchmark harness should execute successfully with 100% correctness")
    void testBenchmarkExecution() throws InterruptedException {
        BenchmarkConfig config = new BenchmarkConfig(1_000, 5_000, 1024);
        PipelineBenchmarkHarness harness = new PipelineBenchmarkHarness();

        BenchmarkResult result = harness.runBenchmark(config);

        assertNotNull(result);
        assertEquals(1_000, result.warmupEvents());
        assertEquals(5_000, result.measuredEvents());
        assertEquals(5_000, result.consumedEvents());
        assertTrue(result.correctnessVerified(), "Submitted events must equal consumed events");
        assertTrue(result.throughputOpsPerSec() > 0, "Throughput must be positive");
        assertTrue(result.p50LatencyMicros() >= 0, "p50 latency must be non-negative");
        assertTrue(result.p99LatencyMicros() >= result.p50LatencyMicros(), "p99 latency must be >= p50 latency");
    }

    @Test
    @DisplayName("BenchmarkConfig validation should reject invalid arguments")
    void testInvalidBenchmarkConfig() {
        assertThrows(IllegalArgumentException.class, () -> new BenchmarkConfig(-1, 100, 1024));
        assertThrows(IllegalArgumentException.class, () -> new BenchmarkConfig(100, 0, 1024));
        assertThrows(IllegalArgumentException.class, () -> new BenchmarkConfig(100, 100, 1000)); // not power of 2
    }
}
