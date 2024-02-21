package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.DomainEvent;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

public class SubscriptionActivated extends DomainEvent {

    private static final int VERSION = 0;

    private final String userId;
    private final String subscriptionId;

    public SubscriptionActivated(final Subscription subscription) {
        super(VERSION, subscription.id().value());
        this.userId = subscription.userId().value();
        this.subscriptionId = subscription.id().value();
    }

    private SubscriptionActivated(
            final String eventId,
            final int eventVersion,
            final String aggregateId,
            final Instant occurredOn,
            final String userId,
            final String subscriptionId
    ) {
        super(eventId, eventVersion, aggregateId, occurredOn);
        this.userId = userId;
        this.subscriptionId = subscriptionId;
    }

    public static SubscriptionActivated restore(
            final String eventId,
            final int eventVersion,
            final String aggregateId,
            final Instant occurredOn,
            final Map<String, Serializable> payload
    ) {
        final var userId = (String) payload.get("userId");
        final var subscriptionId = (String) payload.get("subscriptionId");
        return new SubscriptionActivated(eventId, eventVersion, aggregateId, occurredOn, userId, subscriptionId);
    }

    @Override
    public String type() {
        return "subscription.activated";
    }

    @Override
    public Map<String, Serializable> payload() {
        return Map.of(
                "subscriptionId", subscriptionId,
                "userId", userId
        );
    }
}
