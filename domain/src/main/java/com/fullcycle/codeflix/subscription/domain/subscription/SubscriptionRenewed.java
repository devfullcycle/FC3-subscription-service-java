package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.DomainEvent;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

public class SubscriptionRenewed extends DomainEvent {

    private static final int VERSION = 0;
    private final String subscriptionId;
    private final String userId;
    private final String transactionId;
    private final String billingCycle;
    private final Double price;

    public SubscriptionRenewed(final Subscription subscription, final BillingHistory record) {
        super(VERSION, subscription.id().value());
        this.subscriptionId = subscription.id().value();
        this.userId = subscription.userId().value();
        this.transactionId = record.transactionId();
        this.billingCycle = record.billingCycle().name();
        this.price = record.amount();
    }

    private SubscriptionRenewed(
            final String eventId,
            final int eventVersion,
            final String aggregateId,
            final Instant occurredOn,
            final String subscriptionId,
            final String userId,
            final String transactionId,
            final String billingCycle,
            final Double price
    ) {
        super(eventId, eventVersion, aggregateId, occurredOn);
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.transactionId = transactionId;
        this.billingCycle = billingCycle;
        this.price = price;
    }

    public static SubscriptionRenewed restore(
            final String eventId,
            final int eventVersion,
            final String aggregateId,
            final Instant occurredOn,
            final Map<String, Serializable> payload
    ) {
        final var userId = (String) payload.get("userId");
        final var subscriptionId = (String) payload.get("subscriptionId");
        final var transactionId = (String) payload.get("transactionId");
        final var billingCycle = (String) payload.get("billingCycle");
        final var price = (Double) payload.get("price");
        return new SubscriptionRenewed(eventId, eventVersion, aggregateId, occurredOn, userId, subscriptionId, transactionId, billingCycle, price);
    }

    @Override
    public String type() {
        return "subscription.renewed";
    }

    @Override
    public Map<String, Serializable> payload() {
        return Map.of(
                "subscriptionId", subscriptionId,
                "userId", userId,
                "transactionId", transactionId,
                "billingCycle", billingCycle,
                "price", price
        );
    }
}
