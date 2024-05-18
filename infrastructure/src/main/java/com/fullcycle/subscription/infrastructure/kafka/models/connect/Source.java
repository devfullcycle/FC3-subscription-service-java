package com.fullcycle.subscription.infrastructure.kafka.models.connect;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Source(
        @JsonProperty("name") String name,
        @JsonProperty("db") String database,
        @JsonProperty("table") String table
) {
}
