package todo;

import dagger.Component;
import mx.reactive.core.MatrixApplication;
import mx.reactive.core.MatrixComponent;
import mx.reactive.core.MatrixModule;
import mx.reactive.data.pgclient.MatrixReactivePgModule;
import mx.reactive.web.MatrixWebModule;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Interface that has the required boilerplate needed for Dagger and to load the modules.
 */
@Singleton
@Component(modules = {MatrixModule.class, MatrixWebModule.class, MatrixReactivePgModule.class}) // Matrix Dagger modules this application needs to operate.
interface TodoComponent extends MatrixComponent<TodoApplication> {
    @Component.Builder interface Builder extends MatrixComponent.Builder {}
}
public class TodoApplication extends MatrixApplication {

    @Inject
    public TodoApplication(TodoWebController todoWebController,      // Top level objects in the dependency injection graph.
                           TodoEventController todoEventController,  // Needed to trigger dependency injection for required objects.
                           TodoInitDatabase todoInitDatabase) {}     // Add new objects here that require dependency injection.

    public TodoApplication() { // This instantiates the main verticle
        super (DaggerTodoComponent.builder()); // Builds the dependency injection graph as the first step in starting an application
    }

    /**
     * Allows for execution without the Vert.x Maven plugin (optional)
     */
    public static void main(String[] args) {
        new TodoApplication();
    }
}