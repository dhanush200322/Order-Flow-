package com.axlero.orderflow.integration;

import com.axlero.orderflow.disruptor.MatchingEngineBoundary;
import com.axlero.orderflow.dto.OrderRequest;
import com.axlero.orderflow.model.Order;
import com.axlero.orderflow.model.OrderSide;
import com.axlero.orderflow.model.OrderType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderIngestionIntegrationTest {

    private static final CountDownLatch latch = new CountDownLatch(1);
    private static final AtomicReference<Order> capturedOrder = new AtomicReference<>();
    private static final AtomicReference<Long> capturedSequence = new AtomicReference<>();

    @TestConfiguration
    static class TestBoundaryConfig {
        @Bean
        @Primary
        public MatchingEngineBoundary testMatchingEngineBoundary() {
            return (order, sequence) -> {
                capturedOrder.set(order);
                capturedSequence.set(sequence);
                latch.countDown();
            };
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("End-to-End Test: REST POST /api/orders -> Request -> Service -> Order -> Disruptor -> MatchingEngineBoundary")
    void testEndToEndOrderIngestionPath() throws Exception {
        OrderRequest request = new OrderRequest("TEST-ACC-99", OrderSide.BUY, OrderType.LIMIT, 250.75, 15L);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.orderId").exists())
            .andExpect(jsonPath("$.accountId").value("TEST-ACC-99"))
            .andExpect(jsonPath("$.side").value("BUY"))
            .andExpect(jsonPath("$.orderType").value("LIMIT"))
            .andExpect(jsonPath("$.price").value(250.75))
            .andExpect(jsonPath("$.quantity").value(15))
            .andExpect(jsonPath("$.status").value("SUBMITTED"));

        // Wait for Disruptor event consumer thread to process the event
        boolean delivered = latch.await(3, TimeUnit.SECONDS);
        assertTrue(delivered, "Submitted REST order should reach the MatchingEngineBoundary through the Disruptor Ring Buffer");

        Order deliveredOrder = capturedOrder.get();
        assertNotNull(deliveredOrder);
        assertEquals("TEST-ACC-99", deliveredOrder.accountId());
        assertEquals(OrderSide.BUY, deliveredOrder.side());
        assertEquals(OrderType.LIMIT, deliveredOrder.orderType());
        assertEquals(250.75, deliveredOrder.price());
        assertEquals(15L, deliveredOrder.quantity());
        assertNotNull(capturedSequence.get(), "Disruptor sequence must be populated");
    }
}
