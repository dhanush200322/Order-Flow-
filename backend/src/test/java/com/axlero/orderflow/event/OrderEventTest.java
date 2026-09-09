package com.axlero.orderflow.event;

import com.axlero.orderflow.model.Order;
import com.axlero.orderflow.model.OrderSide;
import com.axlero.orderflow.model.OrderType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderEventTest {

    @Test
    @DisplayName("Should encapsulate and carry Order information correctly")
    void testOrderEventCarriesOrder() {
        long timestamp = System.currentTimeMillis();
        Order order = new Order(2001L, "TRADER-99", OrderSide.SELL, OrderType.MARKET, 0.0, 50L, timestamp, 42L);

        OrderEvent event = new OrderEvent();
        event.setOrder(order);
        event.setSequence(42L);
        event.setTimestamp(timestamp);

        assertNotNull(event.getOrder());
        assertEquals(order, event.getOrder());
        assertEquals(2001L, event.getOrder().orderId());
        assertEquals("TRADER-99", event.getOrder().accountId());
        assertEquals(OrderSide.SELL, event.getOrder().side());
        assertEquals(OrderType.MARKET, event.getOrder().orderType());
        assertEquals(42L, event.getSequence());
        assertEquals(timestamp, event.getTimestamp());
    }

    @Test
    @DisplayName("Should clear event state for Ring Buffer reusability")
    void testClearEvent() {
        Order order = new Order(2002L, "TRADER-100", OrderSide.BUY, OrderType.LIMIT, 99.0, 10L, 1000L, 1L);
        OrderEvent event = new OrderEvent(order, 1L, 1000L);

        event.clear();

        assertNull(event.getOrder());
        assertEquals(0L, event.getSequence());
        assertEquals(0L, event.getTimestamp());
    }
}
