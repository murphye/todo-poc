package todo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.UUID;

@Value.Immutable
@JsonSerialize(as = ImmutableTodo.class)
@JsonDeserialize(as = ImmutableTodo.class)
public interface Todo {
    UUID getId();
    String getTitle();
    int getOrder();
    boolean getCompleted();
}
