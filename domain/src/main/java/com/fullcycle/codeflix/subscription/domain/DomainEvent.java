package com.fullcycle.codeflix.subscription.domain;

import com.fullcycle.codeflix.subscription.domain.utils.IdUtils;
import com.fullcycle.codeflix.subscription.domain.utils.InstantUtils;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

public abstract class DomainEvent {

    private final String eventId;
    private final int eventVersion;
    private final String aggregateId;
    private final Instant occurredOn;

    public DomainEvent(final int eventVersion, final String aggregateId) {
        this.eventId = IdUtils.uniqueId();
        this.eventVersion = eventVersion;
        this.aggregateId = aggregateId;
        this.occurredOn = InstantUtils.now();
    }

    public DomainEvent(
            final String eventId,
            final int eventVersion,
            final String aggregateId,
            final Instant occurredOn
    ) {
        this.eventId = eventId;
        this.eventVersion = eventVersion;
        this.aggregateId = aggregateId;
        this.occurredOn = occurredOn;
    }

    public abstract String type();

    public abstract Map<String, Serializable> payload();

}
