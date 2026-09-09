package com.axlero.orderflow.disruptor;

import com.axlero.orderflow.event.OrderEvent;
import com.lmax.disruptor.EventHandler;

/**
 * Event handler consuming OrderEvent instances from the Ring Buffer and passing them to the boundary.
 */
public class OrderEventHandler implements EventHandler<OrderEvent> {

    private final MatchingEngineBoundary consumer;

    public OrderEventHandler(MatchingEngineBoundary consumer) {
        this.consumer = consumer;
    }

    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
        if (event != null && event.getOrder() != null) {
            if (consumer != null) {
                consumer.onOrderReceived(event.getOrder(), sequence);
            }
            // Recycle event state after consumption to release references
            event.clear();
        }
    }
}
