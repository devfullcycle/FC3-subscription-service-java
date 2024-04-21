package com.fullcycle.subscription.domain.subscription;

import com.fullcycle.subscription.domain.utils.InstantUtils;

import java.time.Instant;
import java.time.LocalDate;

record SubscriptionCanceled(
        String subscriptionId,
        String accountId,
        Long planId,
        LocalDate dueDate,
        Instant occurredOn
) implements SubscriptionEvent {

    public SubscriptionCanceled {
        this.assertArgumentNotEmpty(subscriptionId, "'subscriptionId' should not be empty");
        this.assertArgumentNotEmpty(accountId, "'accountId' should not be empty");
        this.assertArgumentNotNull(planId, "'planId' should not be null");
        this.assertArgumentNotNull(dueDate, "'dueDate' should not be null");
        this.assertArgumentNotNull(occurredOn, "'occurredOn' should not be null");
    }

    public SubscriptionCanceled(final Subscription aSubscription) {
        this(
                aSubscription.id().value(),
                aSubscription.accountId().value(),
                aSubscription.planId().value(),
                aSubscription.dueDate(),
                InstantUtils.now()
        );
    }
}