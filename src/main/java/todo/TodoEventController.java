package todo;

import mx.reactive.core.Controller;
import io.reactivex.Flowable;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.EventBus;
import io.vertx.reactivex.core.Vertx;

import javax.inject.Inject;

public class TodoEventController extends AbstractVerticle implements Controller<EventBus> {

    private static Logger logger = LoggerFactory.getLogger(TodoEventController.class);
    private final TodoRepository todoRepository;

    @Inject
    public TodoEventController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Inject
    @Override
    public void consume(Vertx vertx, EventBus eventBus) {

        // Create a Flowable of Todo objects which originate from the EventBus
        Flowable<Todo> todos =
                eventBus.<JsonObject>consumer("add-todo")
                        .toFlowable()
                        // Echo back the message body to confirm receipt (for demo purposes)
                        .doOnNext(message -> message.reply(message.body()))
                        .map(message -> message.body().mapTo(Todo.class));

        this.todoRepository.saveAll(todos); // saveAll subscribes to the todos Flowable and saves each emitted Todo

        vertx.rxDeployVerticle(this).subscribe();
    }
}
