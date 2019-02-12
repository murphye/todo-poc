package mx.reactive.core;

import io.vertx.reactivex.core.Vertx;

/**
 * Controls consumption of data by routing data to handlers for further processing by another object, such as a Service.
 * @param <E>
 */
public interface Controller<T> {

    /**
     * Setup consumption of transmitted data, such as HTTP requests, events, or messages. Route data to the appropriate
     * handler methods.
     * @param vertx The Vertx instance
     * @param transport Object that directly or indirectly transfers data that must be consumed and routed to handlers.
     *                  Examples would be the EventBus, Router, or KafkaConsumer.
     */
    void consume(Vertx vertx, T transport);
}