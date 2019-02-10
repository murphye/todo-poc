package todo;

import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.core.RxHelper;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.HttpRequest;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.codec.BodyCodec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test runs the entire application and depends on PostgreSQL to be running.
 */
@DisplayName("👀 A MatrixApplication test")
@ExtendWith(VertxExtension.class)
class TodoApplicationTest {

    @BeforeEach
    void prepare(Vertx vertx, VertxTestContext testContext) {
        RxHelper.deployVerticle(vertx, new TodoApplication())
                .subscribe(id -> testContext.completeNow(), testContext::failNow);
    }

    @Test
    @DisplayName("🚀 Start a server and perform requests")
    void server_test(Vertx vertx, VertxTestContext testContext) {
        final int numberOfPasses = 5;

        Checkpoint checkpoints = testContext.checkpoint(numberOfPasses);

        HttpRequest<Todo> request = WebClient
                .create(vertx)
                .get(8080, "localhost", "/todo/31569142-70e4-4820-b138-3cc0d095a2f8")
                .as(BodyCodec.json(Todo.class));

        request
                .rxSend()
                .repeat(numberOfPasses)
                .subscribe(
                        response -> testContext.verify(() -> {
                            assertThat(response.body()).isNotNull();
                            checkpoints.flag();
                        }),
                        testContext::failNow);
    }

}
