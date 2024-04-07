package com.fullcycle.subscription.domain.subscription.status;

import com.fullcycle.subscription.domain.exceptions.DomainException;
import com.fullcycle.subscription.domain.subscription.Subscription;

public sealed interface SubscriptionStatus permits TrailingSubscriptionStatus {

    String TRAILING = "trailing";
    String INCOMPLETE = "incomplete";
    String ACTIVE = "active";
    String CANCELED = "canceled";

    String value();

    void trailing();
    void incomplete();
    void active();
    void cancel();

    static SubscriptionStatus create(final String status, final Subscription aSubscription) {
        return switch (status) {
            case TRAILING -> new TrailingSubscriptionStatus(aSubscription);
            default -> throw DomainException.with("Invalid status: %s".formatted(status));
        };
    }
}
