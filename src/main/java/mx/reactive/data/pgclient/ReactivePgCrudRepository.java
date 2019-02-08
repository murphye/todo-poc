package mx.reactive.data.pgclient;

import io.reactiverse.reactivex.pgclient.PgPool;
import io.reactiverse.reactivex.pgclient.Row;
import io.reactiverse.reactivex.pgclient.Tuple;
import io.reactivex.*;
import io.vertx.core.json.JsonObject;
import mx.reactive.data.ReactiveDataUtil;
import org.reactivestreams.Publisher;

public abstract class ReactivePgCrudRepository<T, ID> extends ReactivePgAbstractRepository {

    private final String tableName;

    private final String FROM_TABLE;
    private final String INSERT_INTO_TABLE;
    private final String UPDATE_TABLE;
    private final String DELETE_FROM_TABLE;

    private Class<T> entityClass;

    /**
     *
     * @param pool
     * @param entityClass Needed because of Java type erasure on the generic T
     */
    protected ReactivePgCrudRepository(PgPool pool, Class<T> entityClass) {
        super(pool);
        this.entityClass = entityClass;
        this.tableName = ReactivePgDataUtil.toSnakeCase(entityClass.getSimpleName());
        this.FROM_TABLE = String.format(" %s %s ", FROM, tableName);
        this.INSERT_INTO_TABLE = String.format("%s %s ", INSERT_INTO, tableName);
        this.UPDATE_TABLE = String.format("%s %s ", UPDATE, tableName);
        this.DELETE_FROM_TABLE = String.format("%s %s ", DELETE_FROM, tableName);
    }

    public Single<Long> count() {
        return executeRow(SELECT_COUNT + FROM_TABLE, Tuple.tuple())
                .map(row -> row.getLong(0)).firstOrError();
    }

    public Completable deleteAll(){return null;}

    public Completable deleteById(ID id){return null;}

    public Completable deleteById(Publisher<ID> id){return null;}

    public Completable deleteById(Iterable<ID> id){return null;}

    public Single<Boolean> existsById(ID id){return null;}

    public Single<Boolean> existsById(Publisher<ID> id){return null;}

    public Flowable<T> findAll() {
        return executeEntity(SELECT_STAR + FROM_TABLE, Tuple.tuple());
    }

    public Flowable<T> findAllById(Iterable<ID> ids){
        return findAllById(Flowable.fromIterable(ids));
    }

    public Flowable<T> findAllById(Publisher<ID> idStream){return null;}

    public Maybe<T> findById(ID id){
        String sql = SELECT_STAR + FROM_TABLE + WHERE + COMPARE("id","=", 1);
        Tuple tuple = Tuple.of(id);
        System.out.println(sql);
        return executeEntity(sql, tuple).singleElement();
    }

    public Maybe<T> findById(Publisher<ID> id){return null;}

    public Single<T> save(T entity){
        JsonObject enityJsonObject = JsonObject.mapFrom(entity);

        // Create PostgreSQL Upsert
        String upsertSql = INSERT_INTO_TABLE + ReactiveDataUtil.insertColumnsFromJsonObject(enityJsonObject) +
                VALUES + ReactiveDataUtil.insertValuesFromJsonObject(enityJsonObject) +
                " ON CONFLICT (id) DO " +
                UPDATE + SET + ReactiveDataUtil.updateValuesFromJsonObject(enityJsonObject);

        return executeCompletable(upsertSql).toSingleDefault(entity);
    }

    public <S extends T>Flowable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    public <S extends T> Flowable<S> saveAll(Publisher<S> entityStream) {
        Flowable<S> entityFlowable = Flowable.fromPublisher(entityStream);
        entityFlowable.subscribe(entity -> this.save(entity).subscribe());
        return entityFlowable.publish();
    }

    // TODO: For Criteria use Collection and use ArrayTuple constructor
    // This will allow for a common constructor
    private Flowable<T> executeEntity(String sql, Tuple args) {
        // new ArrayTuple(args)
        return rowFlowableToEntityFlowable(executeRow(sql, args));
    }

    private Flowable<T> rowFlowableToEntityFlowable(Flowable<Row> stream) {
        return (Flowable<T>) ReactivePgDataUtil.rowFlowableToPojoFlowable(stream, entityClass);
    }
}