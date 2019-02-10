package mx.reactive.web;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableWebConfig.Builder.class)
public interface WebConfig {
    Integer getPort();
}
