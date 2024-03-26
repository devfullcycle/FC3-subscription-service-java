package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.DomainEvent;
import com.fullcycle.codeflix.subscription.domain.user.UserId;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

public class SubscriptionIncomplete extends DomainEvent {

    private static final int VERSION = 0;
    private final SubscriptionId subscriptionId;
    private final UserId userId;
    private final String reason;
    private final LocalDate dueDate;

    public SubscriptionIncomplete(final Subscription subscription, final String reason) {
        super(VERSION, subscription.id().value());
        this.subscriptionId = subscription.id();
        this.userId = subscription.userId();
        this.dueDate = subscription.dueDate();
        this.reason = reason;
    }

    private SubscriptionIncomplete(
            final String eventId,
            final int eventVersion,
            final String aggregateId,
            final Instant occurredOn,
            final SubscriptionId subscriptionId,
            final UserId userId,
            final LocalDate dueDate,
            final String reason
    ) {
        super(eventId, eventVersion, aggregateId, occurredOn);
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.dueDate = dueDate;
        this.reason = reason;
    }

    public static SubscriptionIncomplete restore(
            final String eventId,
            final int eventVersion,
            final String aggregateId,
            final Instant occurredOn,
            final Map<String, Serializable> payload
    ) {
        final var userId = (String) payload.get("userId");
        final var subscriptionId = (String) payload.get("subscriptionId");
        final var dueDate = (LocalDate) payload.get("dueDate");
        final var reason = (String) payload.get("reason");
        return new SubscriptionIncomplete(eventId, eventVersion, aggregateId, occurredOn, new SubscriptionId(subscriptionId), new UserId(userId), dueDate, reason);
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
                "dueDate", dueDate,
                "reason", reason
        );
    }
}
