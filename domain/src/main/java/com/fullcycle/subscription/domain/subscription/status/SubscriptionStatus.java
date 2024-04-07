package com.fullcycle.subscription.domain.subscription.status;

import com.fullcycle.subscription.domain.exceptions.DomainException;
import com.fullcycle.subscription.domain.subscription.Subscription;

public sealed interface SubscriptionStatus permits
        AbstractSubscriptionStatus,
        CanceledSubscriptionStatus,
        IncompleteSubscriptionStatus,
        TrailingSubscriptionStatus {

    String TRAILING = "trailing";
    String INCOMPLETE = "incomplete";
    String ACTIVE = "active";
    String CANCELED = "canceled";

    void trailing();
    void incomplete();
    void active();
    void cancel();

    default String value() {
        return switch (this) {
            case ActiveSubscriptionStatus s -> ACTIVE;
            case CanceledSubscriptionStatus s -> CANCELED;
            case IncompleteSubscriptionStatus s -> INCOMPLETE;
            case TrailingSubscriptionStatus s -> TRAILING;
        };
    }

    static SubscriptionStatus create(final String status, final Subscription aSubscription) {
        if (aSubscription == null) {
            throw DomainException.with("'subscription' should not be null");
        }

        if (status == null) {
            throw DomainException.with("'status' should not be null");
        }

        return switch (status) {
            case ACTIVE -> new ActiveSubscriptionStatus(aSubscription);
            case CANCELED -> new CanceledSubscriptionStatus(aSubscription);
            case INCOMPLETE -> new IncompleteSubscriptionStatus(aSubscription);
            case TRAILING -> new TrailingSubscriptionStatus(aSubscription);
            default -> throw DomainException.with("Invalid status: %s".formatted(status));
        };
    }
}
