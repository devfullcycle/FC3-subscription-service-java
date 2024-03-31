package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.utils.InstantUtils;

import java.time.Instant;
import java.time.LocalDate;

public record SubscriptionCanceled(
        String subscriptionId,
        String accountId,
        String planId,
        LocalDate dueDate,
        Instant occurredOn
) implements SubscriptionEvent {

    public SubscriptionCanceled {
        this.assertArgumentNotNull(subscriptionId, "'subscriptionId' can't be null");
        this.assertArgumentNotNull(accountId, "'accountId' can't be null");
        this.assertArgumentNotNull(dueDate, "'dueDate' can't be null");
        this.assertArgumentNotNull(occurredOn, "'occurredOn' can't be null");
    }

    public SubscriptionCanceled(final Subscription subscription) {
        this(
                subscription.id().value(),
                subscription.accountId().value(),
                subscription.planId().value(),
                subscription.dueDate(),
                InstantUtils.now()
        );
    }
}
