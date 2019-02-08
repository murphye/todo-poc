package mx.reactive.core;

import io.vertx.reactivex.core.Vertx;

public interface Controller<O> {
    void configure(Vertx vertx, O object);
}
