package mx.reactive.core;

import io.vertx.reactivex.core.Vertx;

/**
 * Controls consumption of data by routing data to handlers for further processing by another object, such as a Service.
 * @param <E>
 */
public interface Controller<E> {

    /**
     * Setup consumption of emitted data, such as HTTP Requests, event, or messages. Route data to the appropriate
     * handler methods.
     * @param vertx The Vertx instance
     * @param emitter Object that directly or indirectly emits data that must be consumed and routed to handlers
     */
    void consume(Vertx vertx, E emitter);
}