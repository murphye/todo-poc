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

        Flowable<Todo> todoFlowable =
                eventBus.<JsonObject>consumer("add-todo")
                        .toFlowable()
                        // Send back the message body to confirm receipt
                        .doOnNext(message -> message.reply(message.body()))
                        .map(message -> message.body().mapTo(Todo.class));

        // Demo 1 - Subscribing handler
        //jsonObjectFlowable.subscribe(this::handleTodoJson);

        // Demo 2 - Save Flowable of JsonObjects
        //TODO: this.todoRepository.saveAllJson(jsonObjectFlowable);

        // Demo 3 - Save Flowable of Todos
        this.todoRepository.saveAll(todoFlowable);

        vertx.rxDeployVerticle(this).subscribe();
    }

    private void handleTodoJson(JsonObject jsonObject) {
        logger.info(jsonObject.encodePrettily());
    }
}
