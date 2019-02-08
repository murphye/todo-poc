package mx.reactive.web;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableWebConfig.class)
@JsonDeserialize(as = ImmutableWebConfig.class)
public interface WebConfig {
    Integer getPort();
}
