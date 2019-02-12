package todo;

import io.reactiverse.reactivex.pgclient.PgPool;
import io.reactivex.Observable;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Simplistic database initializer that doesn't bother to check to see if DDL was already created or not.
 */
@Singleton
public class TodoInitDatabase {

    private static Logger logger = LoggerFactory.getLogger(TodoInitDatabase.class);

    @Inject
    public TodoInitDatabase(Vertx vertx, PgPool pool) {
        logger.info("Setting up the database...");

        pool.rxBegin().flatMapObservable(tx -> {
            return vertx.fileSystem().rxReadFile("todos.sql")
                    .flatMapObservable(buffer -> Observable.fromArray(buffer.toString().split(";")))
                    .flatMapSingle(tx::rxQuery)
                    .doOnTerminate(tx::commit);

        })
        .lastOrError()
        .subscribe(
                r-> logger.info("Database created successfully!"),
                e-> logger.error("You either have bad SQL or have already loaded your database!")
        );
    }
}