package todo;

import dagger.Component;
import mx.reactive.core.MatrixApplication;
import mx.reactive.core.MatrixComponent;
import mx.reactive.core.MatrixModule;
import mx.reactive.data.pgclient.MatrixReactivePgModule;
import mx.reactive.web.MatrixWebModule;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Component(modules = {MatrixModule.class, MatrixWebModule.class, MatrixReactivePgModule.class})
interface TodoComponent extends MatrixComponent<TodoApplication> {
    @Component.Builder interface Builder extends MatrixComponent.Builder {}
}
public class TodoApplication extends MatrixApplication {

    @Inject
    public TodoApplication(TodoWebController todoWebController,
                           TodoEventController todoEventController,
                           TodoInitDatabase todoInitDatabase) {}

    public TodoApplication() {
        super (DaggerTodoComponent.builder());
    }

    public static void main(String[] args) {
        new TodoApplication();
    }
}