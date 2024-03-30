package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.account.AccountId;
import com.fullcycle.codeflix.subscription.domain.utils.InstantUtils;

import java.time.Instant;

public record SubscriptionActivated(
        AccountId accountId,
        SubscriptionId subscriptionId,
        Instant occurredOn
) implements SubscriptionEvent {

    public SubscriptionActivated(final Subscription subscription) {
        this(subscription.accountId(), subscription.id(), InstantUtils.now());
    }
}
