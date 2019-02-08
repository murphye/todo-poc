package todo;

import dagger.Component;
import mx.reactive.core.MatrixApplication;
import mx.reactive.core.MatrixComponent;
import mx.reactive.core.MatrixModule;
import mx.reactive.data.pgclient.MatrixReactivePgModule;
import mx.reactive.web.MatrixWebModule;

import javax.inject.Inject;
import javax.inject.Singleton;

//////////

public class TodoApplication extends MatrixApplication {

    public TodoApplication() {
        super (DaggerTodoComponent.builder());
    }

    public static void main(String[] args) {
        new TodoApplication();
    }
}

//////////

@Singleton
@Component(modules = { // Add Modules to extend application functionality
        MatrixModule.class,
        MatrixWebModule.class,
        MatrixReactivePgModule.class
})
interface TodoComponent extends MatrixComponent<TodoContext> {
    @Component.Builder interface Builder extends MatrixComponent.Builder {}
}

//////////

@Singleton
class TodoContext {
    @Inject // Add Controllers to create objects with dependency injection
    public TodoContext(TodoWebController todoWebController,
                       TodoEventController todoEventController) {}
}

