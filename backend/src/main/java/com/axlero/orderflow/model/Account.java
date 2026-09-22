package com.axlero.orderflow.model;

/**
 * Immutable domain model representing a user account in the OrderFlow system.
 */
public record Account(
    String accountId,
    String accountName,
    double balance,
    long createdAt
) {
    public Account {
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("accountId cannot be null or empty");
        }
        if (accountName == null || accountName.isBlank()) {
            throw new IllegalArgumentException("accountName cannot be null or empty");
        }
        if (balance < 0) {
            throw new IllegalArgumentException("balance cannot be negative");
        }
    }
}
