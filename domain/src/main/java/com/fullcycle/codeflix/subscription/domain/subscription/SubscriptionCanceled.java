package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.DomainEvent;
import com.fullcycle.codeflix.subscription.domain.user.UserId;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

public class SubscriptionCanceled extends DomainEvent {

    private static final int VERSION = 0;
    private final SubscriptionId subscriptionId;
    private final UserId userId;
    private final LocalDate dueDate;

    public SubscriptionCanceled(final Subscription subscription) {
        super(VERSION, subscription.id().value());
        this.subscriptionId = subscription.id();
        this.userId = subscription.userId();
        this.dueDate = subscription.dueDate();
    }

    private SubscriptionCanceled(
            final String eventId,
            final int eventVersion,
            final String aggregateId,
            final Instant occurredOn,
            final SubscriptionId subscriptionId,
            final UserId userId,
            final LocalDate dueDate
    ) {
        super(eventId, eventVersion, aggregateId, occurredOn);
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.dueDate = dueDate;
    }

    public static SubscriptionCanceled restore(
            final String eventId,
            final int eventVersion,
            final String aggregateId,
            final Instant occurredOn,
            final Map<String, Serializable> payload
    ) {
        final var userId = (String) payload.get("userId");
        final var subscriptionId = (String) payload.get("subscriptionId");
        final var dueDate = (LocalDate) payload.get("dueDate");
        return new SubscriptionCanceled(eventId, eventVersion, aggregateId, occurredOn, new SubscriptionId(subscriptionId), new UserId(userId), dueDate);
    }

    @Override
    public String type() {
        return "subscription.renewed";
    }

    @Override
    public Map<String, Serializable> payload() {
        return Map.of(
                "subscriptionId", subscriptionId.value(),
                "userId", userId.value(),
                "dueDate", dueDate
        );
    }
}
