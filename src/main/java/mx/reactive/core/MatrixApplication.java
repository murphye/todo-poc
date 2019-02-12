package mx.reactive.core;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.AbstractVerticle;

/**
 * Helper to initialize and run a Matrix application.
 *
 * Supports running multiple Dagger components which allows the possibility of "modular monolith" applications with
 * separately running components within Vert.x. The same component can also be instantiated multiple times if better
 * performance across cores is needed. Generally speaking, an application developer is only going to have one component
 * per MatrixApplication, but the possibility is there for more for certain use cases.
 */
public class MatrixApplication extends AbstractVerticle {

    private static Logger logger = LoggerFactory.getLogger(MatrixApplication.class);

    /**
     * Constructor allows for multiple Dagger MatrixComponents to be loaded at application startup.
     * @param matrixComponents
     */
    public MatrixApplication(MatrixComponent... matrixComponents) {
        if(matrixComponents.length > 0) run(matrixComponents);
    }

    public MatrixApplication(MatrixComponent.Builder expressComponentBuilder) {
        this(build(expressComponentBuilder));
    }

    public static MatrixComponent build(MatrixComponent.Builder builder, String context) {
        return builder.context(context).build();
    }

    public static MatrixComponent build(MatrixComponent.Builder builder) {
        return builder.context("vertx").build();
    }

    private static void run(MatrixComponent... matrixComponents) {

        System.out.println(
                "  _  _ ____ ___ ____ _ _  _  \n" +
                "  |\\/| |__|  |  |__/ |  \\/  \n" +
                "  |  | |  |  |  |  \\ | _/\\_ \n");

        long begin = System.currentTimeMillis();

        // Startup all of the MatrixComponents passed into the constructor
        for(MatrixComponent matrixComponent : matrixComponents) {
            Object instance = matrixComponent.instance(); // Instantiate the Dagger dependency graph for each component

            long end = System.currentTimeMillis();
            logger.info(instance.getClass().getSimpleName() + " started in " + (end - begin) + " ms");
        }
    }
}