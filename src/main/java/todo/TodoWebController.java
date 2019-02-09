package todo;

import mx.reactive.web.WebController;
import mx.reactive.web.MimeTypes;
import io.reactivex.*;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.eventbus.EventBus;
import io.vertx.reactivex.core.eventbus.Message;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import static mx.reactive.web.WebController.*;

@Singleton
public class TodoWebController implements WebController {

    private static Logger logger = LoggerFactory.getLogger(TodoWebController.class);

    private final TodoService todoService;
    private final EventBus eventBus;

    @Inject
    public TodoWebController(TodoService todoService, EventBus eventBus) {
        this.todoService = todoService;
        this.eventBus = eventBus;
    }

    @Override
    @Inject
    public void configure(Vertx vertx, Router router) {
        Router todoRouter = subRouter(vertx, router, "/todo");

        todoRouter.put()
                    .handler(this::addTodo)
                    .consumes(MimeTypes.json.type)
                    .produces(MimeTypes.json.type);

        todoRouter.get("/hello").handler(rc -> rc.response().end("hello"));

        todoRouter.get("/:id")
                .handler(this::getTodo)
                .produces(MimeTypes.json.type);

        todoRouter.get()
                    .handler(this::getTodos)
                    .produces(MimeTypes.json.type);

        todoRouter.delete("/:id")
                    .handler(this::deleteTodo);
    }

    private void addTodo(RoutingContext rc) {
        // Demo sending body to Verticle which handles the save independently (just for fun)
        Single<Message<JsonObject>> response = sendBody(rc, eventBus, "add-todo");

        response.subscribe(message -> {
            logger.info("Message received");

            Todo todo = message.body().mapTo(Todo.class);

            end(rc, todo);
        });
    }

    private void getTodo(RoutingContext rc) {
        UUID todoId = paramUUID(rc, "id");
        Maybe<Todo> todo = todoService.getTodo(todoId);
        end(rc, todo);
    }

    private void getTodos(RoutingContext rc) {

        System.out.println(">>>>>>>>> " + Thread.currentThread().getName());

        Flowable<Todo> todos = todoService.getTodos();
        end(rc, todos);
    }

    private void deleteTodo(RoutingContext rc) {
        Todo todo = bodyAs(rc, Todo.class);
        Completable completable = todoService.deleteTodo(todo);

        // TODO Publish the delete event


        end(rc, completable);
    }
}