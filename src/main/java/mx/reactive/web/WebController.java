package mx.reactive.web;

import mx.reactive.core.Controller;
import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.DisposableSubscriber;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.eventbus.EventBus;
import io.vertx.reactivex.core.eventbus.Message;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public interface WebController extends Controller<Router> {

    static Router subRouter(Vertx vertx, Router router, String mountPoint) {
        Router subRouter = Router.router(vertx);
        router.mountSubRouter(mountPoint, subRouter);
        return subRouter;
    }

    static String param(RoutingContext rc, String paramName) {
        return rc.request().getParam(paramName);
    }

    static Short paramShort(RoutingContext rc, String paramName) {
        return Short.parseShort(param(rc, paramName));
    }

    static Integer paramInteger(RoutingContext rc, String paramName) {
        return Integer.parseInt(param(rc, paramName));
    }

    static Long paramLong(RoutingContext rc, String paramName) {
        return Long.parseLong(param(rc, paramName));
    }

    static Float paramFloat(RoutingContext rc, String paramName) {
        return Float.parseFloat(param(rc, paramName));
    }

    static Double paramDouble(RoutingContext rc, String paramName) {
        return Double.parseDouble(param(rc, paramName));
    }

    static Boolean paramBoolean(RoutingContext rc, String paramName) {
        return Boolean.parseBoolean(param(rc, paramName));
    }

    static LocalDate paramLocalDate(RoutingContext rc, String paramName, DateTimeFormatter formatter) {
        return LocalDate.parse(param(rc, paramName), formatter);
    }

    static LocalTime paramLocalTime(RoutingContext rc, String paramName) {
        return LocalTime.parse(param(rc, paramName));
    }

    static LocalDateTime paramLocalDateTime(RoutingContext rc, String paramName) {
        return LocalDateTime.parse(param(rc, paramName));
    }

    static ZonedDateTime paramZonedDateTime(RoutingContext rc, String paramName) {
        return ZonedDateTime.parse(param(rc, paramName));
    }

    static UUID paramUUID(RoutingContext rc, String paramName) {
        return UUID.fromString(param(rc, paramName));
    }

    static String header(RoutingContext rc, String headerName) {
        return rc.request().getHeader(headerName);
    }

    static <T> T bodyAs(RoutingContext rc, Class<? extends T> type) {
        return rc.getBodyAsJson().mapTo(type);
    }

    static <T> void end(RoutingContext rc, T obj) {
        rc.response().end(JsonObject.mapFrom(obj).encodePrettily());
    }

    static <T> void end(RoutingContext rc, Iterable<T> objs) {
        JsonArray jsonArray = new JsonArray();

        for(Object obj : objs) {
            jsonArray.add(JsonObject.mapFrom(obj));
        }

        rc.response().end(jsonArray.encodePrettily());
    }

    static <T> void end(RoutingContext rc, Completable obj) {
        obj.subscribeWith(new WebHandlersCompletableObserver(rc));
    }

    static <T> void end(RoutingContext rc, Maybe<T> obj) {
        obj.subscribeWith(new WebHandlersMaybeObserver<>(rc));
    }

    static <T> void end(RoutingContext rc, Single<T> obj) {
        obj.subscribeWith(new WebHandlersSingleObserver<>(rc));
    }

    static <T> void end(RoutingContext rc, Flowable<T> obj) {
        rc.response().setChunked(true);
        obj.subscribeWith(new WebHandlersDisposableSubscriber<>(rc));
    }

    static HttpServerResponse produces(RoutingContext rc, String type) {
        return rc.response().putHeader("content-type", type);
    }

    static <T> Single<Message<T>> sendBody(RoutingContext rc, EventBus eventBus, String address) {
        return eventBus.<T>rxSend(address, rc.getBodyAsJson());
    }

    class WebHandlersCompletableObserver implements CompletableObserver {
        private RoutingContext rc;

        public WebHandlersCompletableObserver(RoutingContext rc) {
            this.rc = rc;
        }

        @Override public void onSubscribe(Disposable d) {}

        @Override public void onError(Throwable err) {
            err.printStackTrace();
            rc.fail(err);
        }

        @Override public void onComplete() { rc.response().end(); }
    }

    class WebHandlersMaybeObserver<T> implements MaybeObserver<T> {
        private RoutingContext rc;

        public WebHandlersMaybeObserver(RoutingContext rc) {
            this.rc = rc;
        }

        @Override public void onSubscribe(Disposable d) {}

        @Override public void onSuccess(T object) {
            rc.response().end(JsonObject.mapFrom(object).encodePrettily());
        }

        @Override public void onError(Throwable err) {
            err.printStackTrace();
            rc.fail(err);
        }

        @Override public void onComplete() {}
    }

    class WebHandlersSingleObserver<T> implements SingleObserver<T> {
        private RoutingContext rc;

        public WebHandlersSingleObserver(RoutingContext rc) {
            this.rc = rc;
        }

        @Override public void onSubscribe(Disposable d) {}

        @Override public void onSuccess(T object) {
            rc.response().end(JsonObject.mapFrom(object).encodePrettily());
        }

        @Override public void onError(Throwable err) {
            err.printStackTrace();
            rc.fail(err);
        }
    }

    class WebHandlersDisposableSubscriber<T> extends DisposableSubscriber<T> {
        private RoutingContext rc;

        public WebHandlersDisposableSubscriber(RoutingContext rc) {
            this.rc = rc;
        }

        @Override public void onNext(T object) {
            // TODO: "java.lang.IllegalStateException: Response is closed" when running Apache Bench
            try {
                rc.response().write(JsonObject.mapFrom(object).encodePrettily());
            }
            catch (IllegalStateException e) {
                e.printStackTrace();
            }

        }

        @Override public void onError(Throwable err) {
            err.printStackTrace();
            rc.fail(err);
        }

        @Override public void onComplete() {
            rc.response().end();
        }
    }
}
