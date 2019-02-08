package mx.reactive.data.pgclient;

import dagger.Module;
import dagger.Provides;
import io.reactiverse.pgclient.PgPoolOptions;
import io.reactiverse.reactivex.pgclient.PgClient;
import io.reactiverse.reactivex.pgclient.PgPool;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.Vertx;

import javax.inject.Singleton;

@Module
public class MatrixReactivePgModule {

    private static Logger logger = LoggerFactory.getLogger(MatrixReactivePgModule.class);

    @Provides
    @Singleton
    public PgPool pgPool(Vertx vertx, ConfigRetriever configRetriever) {
        PgPoolOptions options = new PgPoolOptions();
        options.setDatabase("todos");
        options.setHost("localhost");
        options.setPort(5432);
        options.setUser("postgres");
        options.setPassword("");
        options.setCachePreparedStatements(true);
        options.setMaxSize(10);
        return PgClient.pool(vertx, new PgPoolOptions(options));
    }

}