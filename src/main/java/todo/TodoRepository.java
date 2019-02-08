package todo;

import mx.reactive.data.pgclient.ReactivePgCrudRepository;
import io.reactiverse.reactivex.pgclient.*;
import javax.inject.Inject;
import java.util.UUID;

public class TodoRepository extends ReactivePgCrudRepository<Todo, UUID> {

    @Inject
    public TodoRepository(PgPool pool) {
        super(pool, Todo.class);
    }
}
