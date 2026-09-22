package com.axlero.orderflow.controller;

import com.axlero.orderflow.dto.OrderRequest;
import com.axlero.orderflow.dto.OrderResponse;
import com.axlero.orderflow.model.OrderSide;
import com.axlero.orderflow.model.OrderType;
import com.axlero.orderflow.service.OrderIngestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderIngestionService orderIngestionService;

    @Test
    @DisplayName("POST /api/orders with valid request should return HTTP 201 Created and response payload")
    void testSubmitValidOrder() throws Exception {
        OrderRequest request = new OrderRequest("ACC001", OrderSide.BUY, OrderType.LIMIT, 100.50, 10L);
        OrderResponse mockResponse = new OrderResponse(1001L, "ACC001", OrderSide.BUY, OrderType.LIMIT, 100.50, 10L, "SUBMITTED", System.currentTimeMillis(), 0L);

        when(orderIngestionService.ingestOrder(any(OrderRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.orderId").value(1001))
            .andExpect(jsonPath("$.accountId").value("ACC001"))
            .andExpect(jsonPath("$.side").value("BUY"))
            .andExpect(jsonPath("$.orderType").value("LIMIT"))
            .andExpect(jsonPath("$.price").value(100.50))
            .andExpect(jsonPath("$.quantity").value(10))
            .andExpect(jsonPath("$.status").value("SUBMITTED"));
    }

    @Test
    @DisplayName("POST /api/orders with invalid fields should return HTTP 400 Bad Request")
    void testSubmitInvalidOrder() throws Exception {
        // Blank accountId, negative price, zero quantity
        OrderRequest invalidRequest = new OrderRequest("", OrderSide.BUY, OrderType.LIMIT, -50.0, 0L);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/orders with null side or orderType should return HTTP 400 Bad Request")
    void testSubmitNullSideOrType() throws Exception {
        String invalidJson = """
            {
              "accountId": "ACC001",
              "side": null,
              "orderType": "LIMIT",
              "price": 100.0,
              "quantity": 5
            }
            """;

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
            .andExpect(status().isBadRequest());
    }
}
