package mx.reactive.core;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.AbstractVerticle;

public class MatrixApplication extends AbstractVerticle {

    private static Logger logger = LoggerFactory.getLogger(MatrixApplication.class);

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

    public static void run(MatrixComponent... matrixComponents) {
        System.out.println(
                "\n" +
                        "   ██╗   ██╗███████╗██████╗ ████████╗██╗  ██╗\n" +
                        "   ██║   ██║██╔════╝██╔══██╗╚══██╔══╝╚██╗██╔╝\n" +
                        "   ██║   ██║█████╗  ██████╔╝   ██║    ╚███╔╝ \n" +
                        "   ╚██╗ ██╔╝██╔══╝  ██╔══██╗   ██║    ██╔██╗ \n" +
                        "    ╚████╔╝ ███████╗██║  ██║   ██║██╗██╔╝ ██╗\n" +
                        "     ╚═══╝  ╚══════╝╚═╝  ╚═╝   ╚═╝╚═╝╚═╝  ╚═╝\n");

        long begin = System.currentTimeMillis();
        for(MatrixComponent matrixComponent : matrixComponents) {
            Object instance = matrixComponent.instance();

            long end = System.currentTimeMillis();
            logger.info(instance.getClass().getSimpleName() + " started in " + (end - begin) + " ms");
        }
    }
}