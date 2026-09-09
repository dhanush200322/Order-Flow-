package com.axlero.orderflow.benchmark;

import com.axlero.orderflow.disruptor.MatchingEngineBoundary;
import com.axlero.orderflow.disruptor.OrderDisruptorEngine;
import com.axlero.orderflow.model.Order;
import com.axlero.orderflow.model.OrderSide;
import com.axlero.orderflow.model.OrderType;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Low-overhead benchmark harness for measuring Ring Buffer event throughput and latency.
 */
public class PipelineBenchmarkHarness {

    public BenchmarkResult runBenchmark(BenchmarkConfig config) throws InterruptedException {
        int warmup = config.warmupEvents();
        int measured = config.measuredEvents();
        int bufferSize = config.bufferSize();

        // 1. Warm-up Phase
        if (warmup > 0) {
            CountDownLatch warmupLatch = new CountDownLatch(warmup);
            MatchingEngineBoundary warmupConsumer = (order, sequence) -> warmupLatch.countDown();

            try (OrderDisruptorEngine warmupEngine = new OrderDisruptorEngine(
                warmupConsumer, bufferSize, ProducerType.SINGLE, new YieldingWaitStrategy(),
                r -> { Thread t = new Thread(r, "warmup-handler"); t.setDaemon(true); return t; }
            )) {
                warmupEngine.start();
                for (int i = 0; i < warmup; i++) {
                    Order order = new Order(i + 1, "WARMUP", OrderSide.BUY, OrderType.LIMIT, 100.0, 10, System.nanoTime(), i + 1);
                    warmupEngine.publishOrder(order);
                }
                boolean warmupFinished = warmupLatch.await(10, TimeUnit.SECONDS);
                if (!warmupFinished) {
                    throw new IllegalStateException("Warm-up phase timed out");
                }
            }
        }

        // 2. Measured Phase
        long[] latenciesNanos = new long[measured];
        AtomicLong consumedCounter = new AtomicLong(0);
        CountDownLatch measuredLatch = new CountDownLatch(measured);

        MatchingEngineBoundary measuredConsumer = (order, sequence) -> {
            long endNanos = System.nanoTime();
            long latency = endNanos - order.timestamp();
            long count = consumedCounter.getAndIncrement();
            if (count < measured) {
                latenciesNanos[(int) count] = latency;
            }
            measuredLatch.countDown();
        };

        long startTimeNanos;
        long endTimeNanos;

        try (OrderDisruptorEngine engine = new OrderDisruptorEngine(
            measuredConsumer, bufferSize, ProducerType.SINGLE, new YieldingWaitStrategy(),
            r -> { Thread t = new Thread(r, "benchmark-handler"); t.setDaemon(true); return t; }
        )) {
            engine.start();

            startTimeNanos = System.nanoTime();
            for (int i = 0; i < measured; i++) {
                Order order = new Order(i + 1, "ACC-BENCH", OrderSide.BUY, OrderType.LIMIT, 100.0 + (i % 10), 10, System.nanoTime(), i + 1);
                engine.publishOrder(order);
            }

            boolean finished = measuredLatch.await(30, TimeUnit.SECONDS);
            endTimeNanos = System.nanoTime();

            if (!finished) {
                throw new IllegalStateException("Benchmark timed out before processing all measured events");
            }
        }

        long durationNanos = endTimeNanos - startTimeNanos;
        long totalConsumed = consumedCounter.get();
        boolean correctness = (totalConsumed == measured);

        Arrays.sort(latenciesNanos);

        double p50Micros = latenciesNanos[(int) (measured * 0.50)] / 1000.0;
        double p95Micros = latenciesNanos[(int) (measured * 0.95)] / 1000.0;
        double p99Micros = latenciesNanos[(int) (measured * 0.99)] / 1000.0;
        double maxMicros = latenciesNanos[measured - 1] / 1000.0;

        double throughputOpsPerSec = (double) measured / (durationNanos / 1_000_000_000.0);

        Map<String, String> envInfo = collectEnvironmentInfo();

        return new BenchmarkResult(
            warmup,
            measured,
            totalConsumed,
            durationNanos,
            throughputOpsPerSec,
            p50Micros,
            p95Micros,
            p99Micros,
            maxMicros,
            correctness,
            envInfo
        );
    }

    private Map<String, String> collectEnvironmentInfo() {
        Map<String, String> env = new LinkedHashMap<>();
        env.put("Java Version", System.getProperty("java.version"));
        env.put("Java Vendor", System.getProperty("java.vendor"));
        env.put("JVM Name", System.getProperty("java.vm.name"));
        env.put("OS Name", System.getProperty("os.name"));
        env.put("OS Arch", System.getProperty("os.arch"));
        env.put("OS Version", System.getProperty("os.version"));
        env.put("Available Processors", String.valueOf(Runtime.getRuntime().availableProcessors()));
        return env;
    }
}
