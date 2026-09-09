package com.axlero.orderflow.event;

import com.axlero.orderflow.model.Order;

/**
 * Event container designed for high-throughput event processing (e.g., LMAX Disruptor Ring Buffer).
 * Mutable container supporting zero-allocation object reuse.
 */
public class OrderEvent {

    private Order order;
    private long sequence;
    private long timestamp;

    public OrderEvent() {
    }

    public OrderEvent(Order order, long sequence, long timestamp) {
        this.order = order;
        this.sequence = sequence;
        this.timestamp = timestamp;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void clear() {
        this.order = null;
        this.sequence = 0L;
        this.timestamp = 0L;
    }
}
