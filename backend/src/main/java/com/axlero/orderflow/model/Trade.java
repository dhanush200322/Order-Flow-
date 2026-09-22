package com.axlero.orderflow.model;

/**
 * Immutable domain model representing an executed trade in the OrderFlow system.
 */
public record Trade(
    long tradeId,
    long buyOrderId,
    long sellOrderId,
    String buyAccountId,
    String sellAccountId,
    double price,
    long quantity,
    long timestamp
) {
    public Trade {
        if (tradeId <= 0) {
            throw new IllegalArgumentException("tradeId must be positive");
        }
        if (buyOrderId <= 0) {
            throw new IllegalArgumentException("buyOrderId must be positive");
        }
        if (sellOrderId <= 0) {
            throw new IllegalArgumentException("sellOrderId must be positive");
        }
        if (buyAccountId == null || buyAccountId.isBlank()) {
            throw new IllegalArgumentException("buyAccountId cannot be null or empty");
        }
        if (sellAccountId == null || sellAccountId.isBlank()) {
            throw new IllegalArgumentException("sellAccountId cannot be null or empty");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("price must be positive");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }
}
