package com.axlero.orderflow.disruptor;

import com.axlero.orderflow.model.Order;
import com.axlero.orderflow.model.OrderSide;
import com.axlero.orderflow.model.OrderType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PipelineVerificationTest {

    @Test
    @DisplayName("Manual Pipeline Verification: End-to-End Order Event Flow")
    void verifyPipelineFlow() throws InterruptedException {
        System.out.println("=== OrderFlow Ring Buffer Pipeline Manual Verification ===");

        CountDownLatch latch = new CountDownLatch(3);

        MatchingEngineBoundary verificationConsumer = (order, sequence) -> {
            System.out.printf("[CONSUMED EVENT] Seq: %d | OrderId: %d | Account: %s | Side: %s | Type: %s | Price: %.2f | Qty: %d%n",
                sequence, order.orderId(), order.accountId(), order.side(), order.orderType(), order.price(), order.quantity());
            latch.countDown();
        };

        try (OrderDisruptorEngine engine = new OrderDisruptorEngine(verificationConsumer)) {
            engine.start();
            System.out.println("[ENGINE] Disruptor Ring Buffer started (Buffer Size: " + engine.getBufferSize() + ")");

            Order o1 = new Order(5001L, "DHANUSH-ACC", OrderSide.BUY, OrderType.LIMIT, 250.75, 100L, System.currentTimeMillis(), 1L);
            Order o2 = new Order(5002L, "DHANUSH-ACC", OrderSide.SELL, OrderType.LIMIT, 251.00, 50L, System.currentTimeMillis(), 2L);
            Order o3 = new Order(5003L, "DHANUSH-ACC", OrderSide.BUY, OrderType.MARKET, 0.0, 25L, System.currentTimeMillis(), 3L);

            System.out.println("[PRODUCER] Publishing Order #5001 (BUY LIMIT)...");
            engine.publishOrder(o1);

            System.out.println("[PRODUCER] Publishing Order #5002 (SELL LIMIT)...");
            engine.publishOrder(o2);

            System.out.println("[PRODUCER] Publishing Order #5003 (BUY MARKET)...");
            engine.publishOrder(o3);

            boolean success = latch.await(2, TimeUnit.SECONDS);
            assertTrue(success, "Pipeline failed to process all published orders");
            System.out.println("[ENGINE] Pipeline verification completed successfully.");
        }
    }
}
