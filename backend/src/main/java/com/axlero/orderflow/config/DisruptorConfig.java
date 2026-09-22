package com.axlero.orderflow.config;

import com.axlero.orderflow.disruptor.MatchingEngineBoundary;
import com.axlero.orderflow.disruptor.OrderDisruptorEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Configuration managing the lifecycle of the existing OrderDisruptorEngine.
 */
@Configuration
public class DisruptorConfig {

    private static final Logger log = LoggerFactory.getLogger(DisruptorConfig.class);

    @Bean
    @ConditionalOnMissingBean
    public MatchingEngineBoundary matchingEngineBoundary() {
        return (order, sequence) -> {
            log.info("[DISRUPTOR BOUNDARY] Ingested Order delivered | Sequence: {} | OrderId: {} | Account: {} | Side: {} | Type: {} | Price: {} | Qty: {}",
                sequence, order.orderId(), order.accountId(), order.side(), order.orderType(), order.price(), order.quantity());
        };
    }

    @Bean(destroyMethod = "shutdown")
    public OrderDisruptorEngine orderDisruptorEngine(MatchingEngineBoundary boundary) {
        OrderDisruptorEngine engine = new OrderDisruptorEngine(boundary);
        engine.start();
        log.info("[SPRING DISRUPTOR] OrderDisruptorEngine initialized and started (Buffer Size: {})", engine.getBufferSize());
        return engine;
    }
}
