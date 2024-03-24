package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.DomainEvent;
import com.fullcycle.codeflix.subscription.domain.plan.Plan;
import com.fullcycle.codeflix.subscription.domain.plan.PlanId;
import com.fullcycle.codeflix.subscription.domain.subscription.billing.BillingRecord;
import com.fullcycle.codeflix.subscription.domain.user.UserId;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

public class SubscriptionRenewed extends DomainEvent {

    private static final int VERSION = 0;
    private final SubscriptionId subscriptionId;
    private final UserId userId;
    private final PlanId planId;
    private final String transactionId;
    private final LocalDate dueDate;
    private final Double price;
    private final Instant renewedAt;

    public SubscriptionRenewed(final Subscription subscription, final Plan plan, final String transactionId) {
        super(VERSION, subscription.id().value());
        this.subscriptionId = subscription.id();
        this.userId = subscription.userId();
        this.planId = plan.id();
        this.price = plan.price();
        this.transactionId = transactionId;
        this.dueDate = subscription.dueDate();
        this.renewedAt = subscription.lastRenewDate();
    }

    private SubscriptionRenewed(
            final String eventId,
            final int eventVersion,
            final String aggregateId,
            final Instant occurredOn,
            final SubscriptionId subscriptionId,
            final UserId userId,
            final PlanId planId,
            final String transactionId,
            final LocalDate dueDate,
            final Double price,
            final Instant renewedAt
    ) {
        super(eventId, eventVersion, aggregateId, occurredOn);
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.transactionId = transactionId;
        this.planId = planId;
        this.price = price;
        this.dueDate = dueDate;
        this.renewedAt = renewedAt;
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
        final var planId = (String) payload.get("planId");
        final var dueDate = (LocalDate) payload.get("dueDate");
        final var price = (Double) payload.get("price");
        final var renewedAt = (Instant) payload.get("renewedAt");
        return new SubscriptionRenewed(eventId, eventVersion, aggregateId, occurredOn, new SubscriptionId(subscriptionId), new UserId(userId), new PlanId(planId), transactionId, dueDate, price, renewedAt);
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
                "planId", planId.value(),
                "transactionId", transactionId,
                "dueDate", dueDate,
                "price", price,
                "renewedAt", renewedAt
        );
    }
}
