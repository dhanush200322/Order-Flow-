package com.axlero.orderflow.disruptor;

import com.axlero.orderflow.event.OrderEvent;
import com.axlero.orderflow.model.Order;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;

/**
 * Encapsulates setup, configuration, startup, and clean shutdown of the LMAX Disruptor pipeline.
 */
public class OrderDisruptorEngine implements AutoCloseable {

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    private final Disruptor<OrderEvent> disruptor;
    private final RingBuffer<OrderEvent> ringBuffer;
    private final OrderEventProducer producer;
    private final int bufferSize;
    private boolean started = false;

    public OrderDisruptorEngine(MatchingEngineBoundary consumer) {
        this(consumer, DEFAULT_BUFFER_SIZE, ProducerType.MULTI, new BlockingWaitStrategy(),
            r -> {
                Thread thread = new Thread(r, "disruptor-order-handler");
                thread.setDaemon(true);
                return thread;
            });
    }

    public OrderDisruptorEngine(MatchingEngineBoundary consumer,
                                int bufferSize,
                                ProducerType producerType,
                                WaitStrategy waitStrategy,
                                ThreadFactory threadFactory) {
        this.bufferSize = bufferSize;
        this.disruptor = new Disruptor<>(
            new OrderEventFactory(),
            bufferSize,
            threadFactory,
            producerType,
            waitStrategy
        );

        this.disruptor.handleEventsWith(new OrderEventHandler(consumer));
        this.ringBuffer = disruptor.getRingBuffer();
        this.producer = new OrderEventProducer(ringBuffer);
    }

    public synchronized void start() {
        if (!started) {
            disruptor.start();
            started = true;
        }
    }

    public long publishOrder(Order order) {
        if (!started) {
            throw new IllegalStateException("Disruptor engine is not started");
        }
        return producer.publishOrder(order);
    }

    public RingBuffer<OrderEvent> getRingBuffer() {
        return ringBuffer;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public boolean isStarted() {
        return started;
    }

    public synchronized void shutdown() {
        if (started) {
            disruptor.shutdown();
            started = false;
        }
    }

    @Override
    public void close() {
        shutdown();
    }
}
