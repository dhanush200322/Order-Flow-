package com.axlero.orderflow.disruptor;

import com.axlero.orderflow.model.Order;
import com.axlero.orderflow.model.OrderSide;
import com.axlero.orderflow.model.OrderType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class DisruptorPipelineTest {

    @Test
    @DisplayName("Should publish order, enter Ring Buffer, and consume correctly through EventHandler")
    void testSingleOrderPipeline() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Order> receivedOrder = new AtomicReference<>();

        MatchingEngineBoundary consumer = (order, seq) -> {
            receivedOrder.set(order);
            latch.countDown();
        };

        try (OrderDisruptorEngine engine = new OrderDisruptorEngine(consumer)) {
            engine.start();
            assertTrue(engine.isStarted());

            Order order = new Order(101L, "TRADER-1", OrderSide.BUY, OrderType.LIMIT, 100.0, 50L, System.currentTimeMillis(), 1L);
            long seq = engine.publishOrder(order);

            assertTrue(seq >= 0);
            boolean completed = latch.await(2, TimeUnit.SECONDS);
            assertTrue(completed, "Event consumer did not process event in time");

            assertNotNull(receivedOrder.get());
            assertEquals(101L, receivedOrder.get().orderId());
            assertEquals("TRADER-1", receivedOrder.get().accountId());
            assertEquals(OrderSide.BUY, receivedOrder.get().side());
            assertEquals(OrderType.LIMIT, receivedOrder.get().orderType());
            assertEquals(100.0, receivedOrder.get().price());
            assertEquals(50L, receivedOrder.get().quantity());
        }
    }

    @Test
    @DisplayName("Should publish and consume multiple orders in order through the Ring Buffer")
    void testMultipleOrdersPipeline() throws InterruptedException {
        int orderCount = 50;
        CountDownLatch latch = new CountDownLatch(orderCount);
        List<Order> receivedOrders = Collections.synchronizedList(new ArrayList<>());

        MatchingEngineBoundary consumer = (order, seq) -> {
            receivedOrders.add(order);
            latch.countDown();
        };

        try (OrderDisruptorEngine engine = new OrderDisruptorEngine(consumer)) {
            engine.start();

            for (int i = 1; i <= orderCount; i++) {
                Order order = new Order((long) i, "ACC-" + i, OrderSide.BUY, OrderType.LIMIT, 100.0 + i, 10L * i, System.currentTimeMillis(), (long) i);
                engine.publishOrder(order);
            }

            boolean completed = latch.await(3, TimeUnit.SECONDS);
            assertTrue(completed, "All orders should be consumed by event handler");
            assertEquals(orderCount, receivedOrders.size());

            for (int i = 0; i < orderCount; i++) {
                assertEquals((long) (i + 1), receivedOrders.get(i).orderId());
                assertEquals("ACC-" + (i + 1), receivedOrders.get(i).accountId());
            }
        }
    }

    @Test
    @DisplayName("Should throw exception if publishing to unstarted engine")
    void testPublishingToUnstartedEngine() {
        MatchingEngineBoundary consumer = (order, seq) -> {};
        try (OrderDisruptorEngine engine = new OrderDisruptorEngine(consumer)) {
            assertFalse(engine.isStarted());
            Order order = new Order(1L, "ACC-1", OrderSide.BUY, OrderType.LIMIT, 10.0, 1L, System.currentTimeMillis(), 1L);
            assertThrows(IllegalStateException.class, () -> engine.publishOrder(order));
        }
    }
}
