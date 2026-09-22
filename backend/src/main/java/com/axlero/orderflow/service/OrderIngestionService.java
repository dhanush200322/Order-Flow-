package com.axlero.orderflow.service;

import com.axlero.orderflow.disruptor.OrderDisruptorEngine;
import com.axlero.orderflow.dto.OrderRequest;
import com.axlero.orderflow.dto.OrderResponse;
import com.axlero.orderflow.model.Order;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Core ingestion service bridging HTTP REST requests to the LMAX Disruptor Ring Buffer.
 */
@Service
public class OrderIngestionService {

    private final OrderDisruptorEngine disruptorEngine;
    private final AtomicLong orderIdGenerator = new AtomicLong(1000L);

    public OrderIngestionService(OrderDisruptorEngine disruptorEngine) {
        this.disruptorEngine = disruptorEngine;
    }

    /**
     * Ingests an incoming REST order request, maps it to the Order domain model,
     * and publishes it into the Ring Buffer pipeline.
     *
     * @param request validated order request DTO
     * @return OrderResponse containing ingestion confirmation details
     */
    public OrderResponse ingestOrder(OrderRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("OrderRequest cannot be null");
        }

        long orderId = orderIdGenerator.incrementAndGet();
        long timestamp = System.currentTimeMillis();

        Order domainOrder = new Order(
            orderId,
            request.accountId().trim(),
            request.side(),
            request.orderType(),
            request.price(),
            request.quantity(),
            timestamp,
            0L
        );

        long sequence = disruptorEngine.publishOrder(domainOrder);

        return new OrderResponse(
            orderId,
            domainOrder.accountId(),
            domainOrder.side(),
            domainOrder.orderType(),
            domainOrder.price(),
            domainOrder.quantity(),
            "SUBMITTED",
            timestamp,
            sequence
        );
    }
}
