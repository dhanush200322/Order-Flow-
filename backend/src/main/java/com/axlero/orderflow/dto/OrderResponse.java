package com.axlero.orderflow.dto;

import com.axlero.orderflow.model.OrderSide;
import com.axlero.orderflow.model.OrderType;

/**
 * Data Transfer Object representing a response for a successfully ingested order.
 */
public record OrderResponse(
    long orderId,
    String accountId,
    OrderSide side,
    OrderType orderType,
    double price,
    long quantity,
    String status,
    long timestamp,
    long sequence
) {
}
