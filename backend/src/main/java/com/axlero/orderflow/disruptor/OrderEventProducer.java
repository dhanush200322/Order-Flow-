package com.axlero.orderflow.disruptor;

import com.axlero.orderflow.event.OrderEvent;
import com.axlero.orderflow.model.Order;
import com.lmax.disruptor.RingBuffer;

/**
 * High-performance producer for publishing Order objects into the Ring Buffer.
 */
public class OrderEventProducer {

    private final RingBuffer<OrderEvent> ringBuffer;

    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * Publishes an incoming Order into pre-allocated Ring Buffer slot.
     *
     * @param order the order to publish
     * @return sequence number allocated for the published event
     */
    public long publishOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Cannot publish null order");
        }
        long nextSeq = ringBuffer.next();
        try {
            OrderEvent event = ringBuffer.get(nextSeq);
            event.setOrder(order);
            event.setSequence(nextSeq);
            event.setTimestamp(System.currentTimeMillis());
            return nextSeq;
        } finally {
            ringBuffer.publish(nextSeq);
        }
    }
}
