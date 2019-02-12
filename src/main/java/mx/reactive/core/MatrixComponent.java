package mx.reactive.core;

import dagger.BindsInstance;

/**
 * Improves the API for creating Dagger components to be specific to Matrix.
 * @param <Application> Class for to be instantiated which loads the Matrix application.
 */
public interface MatrixComponent<Application> {
    Application instance();

    interface Builder {
        @BindsInstance Builder context(String context);
        MatrixComponent build();
    }
}
