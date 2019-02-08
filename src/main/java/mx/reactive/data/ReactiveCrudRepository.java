package mx.reactive.data;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.reactivestreams.Publisher;

public interface ReactiveCrudRepository<T, ID> {

    Single<Long> count();

    Completable deleteAll();

    Completable deleteById(ID id);

    Completable deleteById(Publisher<ID> id);

    Completable deleteById(Iterable<ID> id);

    Single<Boolean> existsById(ID id);

    Single<Boolean> existsById(Publisher<ID> id);

    Flowable<T> findAll();

    Flowable<T> findAllById(Iterable<ID> ids);

    Flowable<T> findAllById(Publisher<ID> idStream);

    Maybe<T> findById(ID id);

    Maybe<T> findById(Publisher<ID> id);

    <S extends T>Single<S> save(S entity);

    <S extends T>Flowable<S> saveAll(Iterable<S> entities);

    <S extends T> Flowable<S> saveAll(Publisher<S> entityStream);

    // Flowable<T> findByConditions(String conditions, List<String> params)

    // Flowable<T> findByConditions(String conditions, List<String> params, String orderBy)

    // SQL substatement to append after "FROM my_table_name "
    // Flowable<T> findBySubstatement(String substatement, List<String> params)
}
