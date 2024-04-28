package com.fullcycle.subscription.domain.subscription;

import com.fullcycle.subscription.domain.plan.Plan;
import com.fullcycle.subscription.domain.utils.InstantUtils;

import java.time.Instant;
import java.time.LocalDate;

public record SubscriptionRenewed(
        String subscriptionId,
        String accountId,
        Long planId,
        String transactionId,
        LocalDate dueDate,
        String currency,
        Double amount,
        Instant renewedAt,
        Instant occurredOn
) implements SubscriptionEvent {

    public SubscriptionRenewed {
        this.assertArgumentNotEmpty(subscriptionId, "'subscriptionId' should not be empty");
        this.assertArgumentNotEmpty(accountId, "'accountId' should not be empty");
        this.assertArgumentNotNull(planId, "'planId' should not be null");
        this.assertArgumentNotEmpty(transactionId, "'transactionId' should not be empty");
        this.assertArgumentNotNull(dueDate, "'dueDate' should not be null");
        this.assertArgumentNotEmpty(currency, "'currency' should not be empty");
        this.assertArgumentNotNull(amount, "'amount' should not be null");
        this.assertArgumentNotNull(renewedAt, "'renewedAt' should not be null");
        this.assertArgumentNotNull(occurredOn, "'occurredOn' should not be null");
    }

    public SubscriptionRenewed(final Subscription aSubscription, final Plan selectedPlan) {
        this(
                aSubscription.id().value(),
                aSubscription.accountId().value(),
                aSubscription.planId().value(),
                aSubscription.lastTransactionId(),
                aSubscription.dueDate(),
                selectedPlan.price().currency().getCurrencyCode(),
                selectedPlan.price().amount(),
                aSubscription.lastRenewDate(),
                InstantUtils.now()
        );
    }
}
