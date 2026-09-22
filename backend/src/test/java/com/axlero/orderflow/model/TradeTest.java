package com.axlero.orderflow.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TradeTest {

    @Test
    @DisplayName("Should create a Trade with all required fields")
    void testTradeCreation() {
        long now = System.currentTimeMillis();
        Trade trade = new Trade(100L, 1001L, 1002L, "ACC-01", "ACC-02", 150.50, 50L, now);

        assertEquals(100L, trade.tradeId());
        assertEquals(1001L, trade.buyOrderId());
        assertEquals(1002L, trade.sellOrderId());
        assertEquals("ACC-01", trade.buyAccountId());
        assertEquals("ACC-02", trade.sellAccountId());
        assertEquals(150.50, trade.price());
        assertEquals(50L, trade.quantity());
        assertEquals(now, trade.timestamp());
    }

    @Test
    @DisplayName("Should reject invalid trade arguments")
    void testInvalidTradeArguments() {
        long now = System.currentTimeMillis();
        assertThrows(IllegalArgumentException.class, () ->
            new Trade(0L, 1001L, 1002L, "ACC-01", "ACC-02", 150.50, 50L, now));

        assertThrows(IllegalArgumentException.class, () ->
            new Trade(100L, 0L, 1002L, "ACC-01", "ACC-02", 150.50, 50L, now));

        assertThrows(IllegalArgumentException.class, () ->
            new Trade(100L, 1001L, 0L, "ACC-01", "ACC-02", 150.50, 50L, now));

        assertThrows(IllegalArgumentException.class, () ->
            new Trade(100L, 1001L, 1002L, "", "ACC-02", 150.50, 50L, now));

        assertThrows(IllegalArgumentException.class, () ->
            new Trade(100L, 1001L, 1002L, "ACC-01", null, 150.50, 50L, now));

        assertThrows(IllegalArgumentException.class, () ->
            new Trade(100L, 1001L, 1002L, "ACC-01", "ACC-02", 0.0, 50L, now));

        assertThrows(IllegalArgumentException.class, () ->
            new Trade(100L, 1001L, 1002L, "ACC-01", "ACC-02", 150.50, 0L, now));
    }
}
