package com.fullcycle.codeflix.subscription.domain.subscription.status;

import com.fullcycle.codeflix.subscription.domain.subscription.Subscription;

public final class SubscriptionStatusFactory {

    private SubscriptionStatusFactory() {}

    public static SubscriptionStatus create(final Subscription subscription, final String value) {
        return switch (value) {
            case SubscriptionStatus.TRAILING -> new TrailingSubscriptionStatus(subscription);
            case SubscriptionStatus.INCOMPLETE -> new IncompleteSubscriptionStatus(subscription);
            case SubscriptionStatus.ACTIVE -> new ActiveSubscriptionStatus(subscription);
            case SubscriptionStatus.CANCELED -> new CanceledSubscriptionStatus(subscription);
            default -> throw new IllegalArgumentException();
        };
    }
}
