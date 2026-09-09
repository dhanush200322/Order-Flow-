package com.axlero.orderflow.disruptor;

import com.axlero.orderflow.model.Order;

/**
 * Boundary interface for consuming order events processed by the Ring Buffer pipeline.
 * Represents the entry point for the future Matching Engine (Week 2).
 */
@FunctionalInterface
public interface MatchingEngineBoundary {
    void onOrderReceived(Order order, long sequence);
}
