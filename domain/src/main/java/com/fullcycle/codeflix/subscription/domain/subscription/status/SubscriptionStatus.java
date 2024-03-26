package com.fullcycle.codeflix.subscription.domain.subscription.status;

public sealed interface SubscriptionStatus permits
        IncompleteSubscriptionStatus,
        TrailingSubscriptionStatus,
        ActiveSubscriptionStatus,
        CanceledSubscriptionStatus {

    String TRAILING = "trailing";
    String INCOMPLETE = "incomplete";
    String ACTIVE = "active";
    String CANCELED = "canceled";

    String value();

    void trailing();

    void incomplete();

    void active();

    void cancel();

}
