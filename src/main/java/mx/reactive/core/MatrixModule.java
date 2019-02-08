package mx.reactive.core;

import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import dagger.Module;
import dagger.Provides;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.eventbus.EventBus;

import javax.inject.Singleton;

@Module
public class MatrixModule {

    private static Vertx vertx;

    static {
        Json.mapper.registerModule(new AfterburnerModule());
        Json.prettyMapper.registerModule(new AfterburnerModule());
    }

    @Provides
    @Singleton
    public Vertx vertx() {

        if(vertx == null) {
            VertxOptions vertxOptions = new VertxOptions(); // TODO Make this configurable
            vertxOptions.setBlockedThreadCheckInterval(100); // 100ms instead of 5s
            vertx = Vertx.vertx(vertxOptions);
        }
        return vertx;
    }

    @Provides
    @Singleton
    public EventBus eventBus(Vertx vertx) {
        return vertx.eventBus();
    }

    @Provides
    @Singleton
    public ConfigRetriever configRetriever(Vertx vertx, ConfigRetrieverOptions options) {
        return ConfigRetriever.create(vertx, options);
    }

    @Provides
    @Singleton
    public ConfigRetrieverOptions configRetrieverOptions() {
        ConfigStoreOptions properties = new ConfigStoreOptions()
                .setType("file")
                .setFormat("properties")
                .setConfig(new JsonObject()
                        .put("path", "application.properties")
                        .put("hierarchical", true));

        ConfigStoreOptions env = new ConfigStoreOptions().setType("env");

        return new ConfigRetrieverOptions().addStore(properties).addStore(env);
    }

}
