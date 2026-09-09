package com.axlero.orderflow.model;

/**
 * Immutable domain model representing an incoming order in the OrderFlow system.
 */
public record Order(
    long orderId,
    String accountId,
    OrderSide side,
    OrderType orderType,
    double price,
    long quantity,
    long timestamp,
    long sequence
) {
    public Order {
        if (orderId <= 0) {
            throw new IllegalArgumentException("orderId must be positive");
        }
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("accountId cannot be null or empty");
        }
        if (side == null) {
            throw new IllegalArgumentException("side cannot be null");
        }
        if (orderType == null) {
            throw new IllegalArgumentException("orderType cannot be null");
        }
        if (price < 0) {
            throw new IllegalArgumentException("price cannot be negative");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }
}
