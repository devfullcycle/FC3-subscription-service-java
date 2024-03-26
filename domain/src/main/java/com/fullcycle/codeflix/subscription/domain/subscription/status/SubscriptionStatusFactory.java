package com.fullcycle.codeflix.subscription.domain.subscription.status;

import com.fullcycle.codeflix.subscription.domain.subscription.Subscription;

public final class SubscriptionStatusFactory {

    private SubscriptionStatusFactory() {}

    public static SubscriptionStatus create(final Subscription subscription, final String value) {
        return switch (value) {
            case "trailing" -> new TrailingSubscriptionStatus(subscription);
            case "incomplete" -> new IncompleteSubscriptionStatus(subscription);
            case "active" -> new ActiveSubscriptionStatus(subscription);
            case "canceled" -> new CanceledSubscriptionStatus(subscription);
            default -> throw new IllegalArgumentException();
        };
    }
}
