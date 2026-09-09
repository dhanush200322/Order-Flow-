# OrderFlow - Week 1 Baseline Performance Benchmark

## 1. Overview
This document records the baseline performance benchmark for the Axlero Solutions OrderFlow Ring Buffer event pipeline (Week 1 foundation).

## 2. Measurement Boundary
The benchmark strictly measures the event delivery path across the LMAX Disruptor infrastructure:

```text
Producer (publishOrder)
   │
   ▼
Ring Buffer (OrderEvent slot)
   │
   ▼
OrderEventHandler (onEvent)
   │
   ▼
MatchingEngineBoundary (onOrderReceived)
```

- **Inclusions:** Event translation, Ring Buffer publishing, thread context handoff, event handler consumption, payload extraction.
- **Exclusions:** JVM startup overhead, console logging, matching engine logic (Week 2), network I/O, database/persistence.

## 3. Methodology & Workload Configuration
- **Warm-up Phase:** Executes pre-benchmark events (default 50,000 events) to trigger JVM JIT compilation, class loading, and branch prediction before measurement begins.
- **Measured Workload:** Publishes 200,000 order events through a 8,192-slot Ring Buffer using `YieldingWaitStrategy`.
- **Latency Tracking:** Measures nanosecond delta (`System.nanoTime()`) from order publication to boundary handler arrival.
- **Correctness Verification:** Enforces `submittedEvents == consumedEvents` to guarantee zero event loss.

## 4. Benchmark Result Status

> [!NOTE]
> **Status:** BASELINE OBSERVATION
> The numbers recorded here establish the initial baseline for event transport. The final project targets (100,000 ops/sec, p99 < 100 µs) apply to the full Matching Engine in Week 2.

### Comparison Table

| Metric | Axlero Review Target (Week 2) | Observed Baseline (Week 1 Foundation) | Status |
| :--- | :--- | :--- | :--- |
| **Throughput** | $\ge 100,000$ ops/sec | Measured via `PipelineBenchmarkRunner` | BASELINE |
| **p99 Latency** | $< 100.00$ µs | Measured via `PipelineBenchmarkRunner` | BASELINE |
| **Correctness** | 100% (0 events lost) | 100% (0 events lost) | PASSED |

## 5. Execution Instructions
To execute the benchmark manually and generate an updated report:

```bash
cd backend
mvn exec:java -Dexec.mainClass="com.axlero.orderflow.benchmark.PipelineBenchmarkRunner"
```
