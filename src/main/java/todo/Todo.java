package todo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableTodo.Builder.class)
public interface Todo {
    UUID getId();
    String getTitle();
    int getOrder();
    boolean getCompleted();
}
