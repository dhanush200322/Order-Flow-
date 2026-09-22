package com.axlero.orderflow.controller;

import com.axlero.orderflow.dto.OrderRequest;
import com.axlero.orderflow.dto.OrderResponse;
import com.axlero.orderflow.service.OrderIngestionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller exposing order ingestion endpoints.
 */
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderIngestionService orderIngestionService;

    public OrderController(OrderIngestionService orderIngestionService) {
        this.orderIngestionService = orderIngestionService;
    }

    /**
     * Endpoint for submitting new orders into the OrderFlow platform.
     *
     * @param request validated order ingestion request
     * @return HTTP 201 Created with ingestion details
     */
    @PostMapping
    public ResponseEntity<OrderResponse> submitOrder(@Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderIngestionService.ingestOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
