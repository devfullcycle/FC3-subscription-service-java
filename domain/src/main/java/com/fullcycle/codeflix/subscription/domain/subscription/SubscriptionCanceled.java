package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.account.AccountId;
import com.fullcycle.codeflix.subscription.domain.utils.InstantUtils;

import java.time.Instant;
import java.time.LocalDate;

public record SubscriptionCanceled(
        SubscriptionId subscriptionId,
        AccountId accountId,
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
        this(subscription.id(), subscription.accountId(), subscription.dueDate(), InstantUtils.now());
    }
}
