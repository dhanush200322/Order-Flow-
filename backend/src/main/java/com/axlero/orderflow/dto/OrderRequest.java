package com.axlero.orderflow.dto;

import com.axlero.orderflow.model.OrderSide;
import com.axlero.orderflow.model.OrderType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Data Transfer Object representing an incoming REST order ingestion request.
 */
public record OrderRequest(
    @NotBlank(message = "accountId cannot be null or empty")
    String accountId,

    @NotNull(message = "side cannot be null")
    OrderSide side,

    @NotNull(message = "orderType cannot be null")
    OrderType orderType,

    @Min(value = 0, message = "price cannot be negative")
    double price,

    @Positive(message = "quantity must be positive")
    long quantity
) {
}
