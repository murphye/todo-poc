package mx.reactive.data.pgclient;

import io.reactiverse.reactivex.pgclient.PgPool;
import io.reactiverse.reactivex.pgclient.PgStream;
import io.reactiverse.reactivex.pgclient.Row;
import io.reactiverse.reactivex.pgclient.Tuple;
import io.reactivex.Completable;
import io.reactivex.Flowable;

public abstract class ReactivePgAbstractRepository {

    // TODO change to static?
    final String SELECT_STAR = "SELECT *";
    final String SELECT_COUNT = "SELECT count(*)";
    final String FROM = " FROM ";
    final String INSERT_INTO = "INSERT INTO ";
    final String UPDATE = "UPDATE ";
    final String DELETE_FROM = "DELETE" + FROM;
    final String WHERE = " WHERE ";
    final String AND = " AND ";
    final String OR = " OR ";
    final String VALUES = " VALUES ";
    final String SET = " SET ";

    static final String COMPARE(String column, String operator, int position) {
        return String.format(" \"%s\" %s $%d ", column, operator, position);
    }

    private PgPool pool;

    protected ReactivePgAbstractRepository(PgPool pool) {
        this.pool = pool;
    }

    protected Flowable<Row> executeRow(String sql, Tuple args) {
        return pool
                .rxBegin()  // Cursors require a transaction
                .flatMapPublisher(tx -> tx.rxPrepare(sql)
                        .flatMapPublisher(preparedQuery -> {
                            PgStream<Row> stream = preparedQuery.createStream(50, args);
                            return stream.toFlowable();
                        })
                        // Commit the transaction after usage
                        .doAfterTerminate(tx::commit));
    }


    protected Completable executeCompletable(String sql) {
        return pool
                .rxBegin()
                .flatMapCompletable(tx -> tx
                        .rxQuery(sql)
                        .flatMapCompletable(result -> tx.rxCommit())
                );
    }
}
