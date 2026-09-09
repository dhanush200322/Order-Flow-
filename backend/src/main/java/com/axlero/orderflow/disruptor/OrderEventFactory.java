package com.axlero.orderflow.disruptor;

import com.axlero.orderflow.event.OrderEvent;
import com.lmax.disruptor.EventFactory;

/**
 * Factory for pre-allocating OrderEvent instances in the Ring Buffer.
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {

    @Override
    public OrderEvent newInstance() {
        return new OrderEvent();
    }
}
