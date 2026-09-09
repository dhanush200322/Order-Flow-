package com.axlero.orderflow.benchmark;

/**
 * Executable benchmark runner for producing formatted baseline reports.
 */
public class PipelineBenchmarkRunner {

    public static void main(String[] args) {
        System.out.println("===============================================================================");
        System.out.println("            AXLERO SOLUTIONS ORDERFLOW - WEEK 1 BASELINE BENCHMARK           ");
        System.out.println("===============================================================================\n");

        BenchmarkConfig config = new BenchmarkConfig(50_000, 200_000, 8192);
        PipelineBenchmarkHarness harness = new PipelineBenchmarkHarness();

        try {
            System.out.println("Warming up JVM with " + config.warmupEvents() + " events...");
            System.out.println("Executing measured workload with " + config.measuredEvents() + " events...");
            BenchmarkResult result = harness.runBenchmark(config);

            printReport(result);
        } catch (Exception e) {
            System.err.println("Benchmark execution failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void printReport(BenchmarkResult result) {
        System.out.println("\n-------------------------------------------------------------------------------");
        System.out.println("                          ENVIRONMENT INFORMATION                              ");
        System.out.println("-------------------------------------------------------------------------------");
        result.environmentInfo().forEach((key, val) -> System.out.printf("  %-22s : %s%n", key, val));

        System.out.println("\n-------------------------------------------------------------------------------");
        System.out.println("                         WORKLOAD & CORRECTNESS                                ");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.printf("  %-22s : %,d%n", "Warm-up Events", result.warmupEvents());
        System.out.printf("  %-22s : %,d%n", "Submitted Events", result.measuredEvents());
        System.out.printf("  %-22s : %,d%n", "Consumed Events", result.consumedEvents());
        System.out.printf("  %-22s : %s%n", "Correctness Verified", result.correctnessVerified() ? "PASSED (0 events lost)" : "FAILED");
        System.out.printf("  %-22s : %.3f ms%n", "Elapsed Time", result.durationNanos() / 1_000_000.0);

        System.out.println("\n-------------------------------------------------------------------------------");
        System.out.println("                        OBSERVED BASELINE METRICS                              ");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.printf("  %-22s : %,.2f ops/sec%n", "Throughput", result.throughputOpsPerSec());
        System.out.printf("  %-22s : %.2f µs%n", "Latency p50 (Median)", result.p50LatencyMicros());
        System.out.printf("  %-22s : %.2f µs%n", "Latency p95", result.p95LatencyMicros());
        System.out.printf("  %-22s : %.2f µs%n", "Latency p99", result.p99LatencyMicros());
        System.out.printf("  %-22s : %.2f µs%n", "Latency Max", result.maxLatencyMicros());

        System.out.println("\n-------------------------------------------------------------------------------");
        System.out.println("                  BASELINE OBSERVATION vs PROJECT TARGETS                       ");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("  Status                : BASELINE OBSERVATION (Week 1 Foundation)");
        System.out.println("  Target (Week 2 Review): 100,000 ops/sec | p99 < 100.00 µs");
        System.out.printf("  Observed (Week 1)     : %,.2f ops/sec | p99 = %.2f µs%n",
            result.throughputOpsPerSec(), result.p99LatencyMicros());
        System.out.println("-------------------------------------------------------------------------------\n");
    }
}
