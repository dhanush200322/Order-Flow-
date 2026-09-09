package com.axlero.orderflow.disruptor;

import com.axlero.orderflow.model.Order;
import com.axlero.orderflow.model.OrderSide;
import com.axlero.orderflow.model.OrderType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

class EventDeliveryVerificationTest {

    private void runDeliveryVerification(int eventCount) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(eventCount);
        AtomicLong consumedCounter = new AtomicLong(0);

        MatchingEngineBoundary consumer = (order, sequence) -> {
            consumedCounter.incrementAndGet();
            latch.countDown();
        };

        try (OrderDisruptorEngine engine = new OrderDisruptorEngine(consumer, 8192,
            com.lmax.disruptor.dsl.ProducerType.SINGLE,
            new com.lmax.disruptor.YieldingWaitStrategy(),
            r -> { Thread t = new Thread(r, "delivery-verify"); t.setDaemon(true); return t; })) {

            engine.start();

            for (int i = 1; i <= eventCount; i++) {
                Order order = new Order(i, "ACC", OrderSide.BUY, OrderType.LIMIT, 100.0, 10, System.currentTimeMillis(), i);
                engine.publishOrder(order);
            }

            boolean completed = latch.await(15, TimeUnit.SECONDS);
            assertTrue(completed, "Event delivery timed out for batch size " + eventCount);
            assertEquals(eventCount, consumedCounter.get(), "Consumed events count must equal submitted events");
        }
    }

    @Test
    @DisplayName("Verify delivery of 1 event")
    void testDelivery1Event() throws InterruptedException {
        runDeliveryVerification(1);
    }

    @Test
    @DisplayName("Verify delivery of 10 events (small batch)")
    void testDelivery10Events() throws InterruptedException {
        runDeliveryVerification(10);
    }

    @Test
    @DisplayName("Verify delivery of 1,000 events (larger batch)")
    void testDelivery1000Events() throws InterruptedException {
        runDeliveryVerification(1_000);
    }

    @Test
    @DisplayName("Verify delivery of 10,000 events")
    void testDelivery10000Events() throws InterruptedException {
        runDeliveryVerification(10_000);
    }

    @Test
    @DisplayName("Verify delivery of 100,000 events")
    void testDelivery100000Events() throws InterruptedException {
        runDeliveryVerification(100_000);
    }
}
