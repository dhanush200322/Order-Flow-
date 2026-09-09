package com.axlero.orderflow.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    @DisplayName("Should create an Order with all required fields")
    void testOrderCreation() {
        long now = System.currentTimeMillis();
        Order order = new Order(1001L, "ACC-01", OrderSide.BUY, OrderType.LIMIT, 150.50, 100L, now, 1L);

        assertEquals(1001L, order.orderId());
        assertEquals("ACC-01", order.accountId());
        assertEquals(OrderSide.BUY, order.side());
        assertEquals(OrderType.LIMIT, order.orderType());
        assertEquals(150.50, order.price());
        assertEquals(100L, order.quantity());
        assertEquals(now, order.timestamp());
        assertEquals(1L, order.sequence());
    }

    @Test
    @DisplayName("Should correctly represent BUY and SELL order sides")
    void testOrderSideValues() {
        assertEquals(2, OrderSide.values().length);
        assertEquals(OrderSide.BUY, OrderSide.valueOf("BUY"));
        assertEquals(OrderSide.SELL, OrderSide.valueOf("SELL"));
    }

    @Test
    @DisplayName("Should correctly represent LIMIT and MARKET order types")
    void testOrderTypeValues() {
        assertEquals(2, OrderType.values().length);
        assertEquals(OrderType.LIMIT, OrderType.valueOf("LIMIT"));
        assertEquals(OrderType.MARKET, OrderType.valueOf("MARKET"));
    }

    @Test
    @DisplayName("Should reject invalid order arguments")
    void testInvalidOrderArguments() {
        long now = System.currentTimeMillis();
        assertThrows(IllegalArgumentException.class, () ->
            new Order(0L, "ACC-01", OrderSide.BUY, OrderType.LIMIT, 100.0, 10L, now, 1L));

        assertThrows(IllegalArgumentException.class, () ->
            new Order(1L, "", OrderSide.BUY, OrderType.LIMIT, 100.0, 10L, now, 1L));

        assertThrows(IllegalArgumentException.class, () ->
            new Order(1L, "ACC-01", null, OrderType.LIMIT, 100.0, 10L, now, 1L));

        assertThrows(IllegalArgumentException.class, () ->
            new Order(1L, "ACC-01", OrderSide.BUY, null, 100.0, 10L, now, 1L));

        assertThrows(IllegalArgumentException.class, () ->
            new Order(1L, "ACC-01", OrderSide.BUY, OrderType.LIMIT, -10.0, 10L, now, 1L));

        assertThrows(IllegalArgumentException.class, () ->
            new Order(1L, "ACC-01", OrderSide.BUY, OrderType.LIMIT, 100.0, 0L, now, 1L));
    }
}
