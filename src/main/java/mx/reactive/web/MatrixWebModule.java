package mx.reactive.web;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.Disposable;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.RxHelper;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

import javax.inject.Singleton;

@Module
public class MatrixWebModule extends AbstractVerticle {

    private static Logger logger = LoggerFactory.getLogger(MatrixWebModule.class);

    private Router router;
    private HttpServer httpServer;
    private int port;

    @Override
    public void start() {
        httpServer = vertx.createHttpServer();
        httpServer.requestHandler(router::accept).listen(port);
        logger.info("HTTP server now listening on port " + httpServer.actualPort() + " running on " + Thread.currentThread().getName());
    }

    @Override
    public void stop() {
        logger.info("HTTP Server is now stopped");
        httpServer.close();
    }

    @Provides
    @Singleton
    public Router router(Vertx vertx, ConfigRetriever configRetriever, String context) {
        router = Router.router(vertx);
        router.route().handler(BodyHandler.create()); // Needed for getBodyAsJson

        long now = System.currentTimeMillis();

        Disposable result = configRetriever.rxGetConfig().doOnSuccess(config -> {

            WebConfig webConfig =
                    config.getJsonObject(context)
                            .getJsonObject("web")
                            .mapTo(ImmutableWebConfig.class);

            port = webConfig.getPort();
            RxHelper.deployVerticle(vertx, this).doOnSuccess(onSuccess -> {
                logger.info("HttpServer deployed in " + (System.currentTimeMillis() - now) + " ms from context " + context);
            }).subscribe();
        }).subscribe();

        return router;
    }
}
