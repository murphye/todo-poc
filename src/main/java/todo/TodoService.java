package todo;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.inject.Inject;
import java.util.UUID;

public class TodoService {

    private TodoRepository todoRepository;

    @Inject
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Single<Todo> addTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public Maybe<Todo> getTodo(UUID todoId) {
        return todoRepository.findById(todoId);
    }

    public Flowable<Todo> getTodos() {
        return todoRepository.findAll();
    }

    public Completable deleteTodo(Todo todo) {
        //TODO return todoRepository.delete(todo);
        return null;
    }

}
