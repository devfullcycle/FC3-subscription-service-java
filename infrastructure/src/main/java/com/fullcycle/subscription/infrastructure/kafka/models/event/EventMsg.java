package com.fullcycle.subscription.infrastructure.kafka.models.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EventMsg(@JsonProperty("event_id") Long eventId) {
}
