package com.fullcycle.codeflix.subscription.domain.subscription.status;

public sealed interface SubscriptionStatus permits
        IncompleteSubscriptionStatus,
        TrailingSubscriptionStatus,
        ActiveSubscriptionStatus,
        CanceledSubscriptionStatus {
    //    PENDING_PAYMENT, PAID, ACTIVE, EXPIRED, SUSPENDED;

    String value();

    void trailing();

    void incomplete();

    void active();

    void canceled();

}
