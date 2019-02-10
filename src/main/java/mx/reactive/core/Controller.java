package mx.reactive.core;

import io.vertx.reactivex.core.Vertx;

public interface Controller<O> {
    void consume(Vertx vertx, O object);
}
