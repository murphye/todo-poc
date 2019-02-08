package mx.reactive.core;

import dagger.BindsInstance;

public interface MatrixComponent<I> {
    I instance();

    interface Builder {
        @BindsInstance Builder context(String context);
        MatrixComponent build();
    }
}
