package com.fullcycle.subscription.infrastructure.rest.models.res;

import com.fullcycle.subscription.application.subscription.ChargeSubscription;

public record ChargeSubscriptionResponse(
        String subscriptionId,
        String subscriptionStatus,
        String subscriptionDueDate,
        String paymentTransactionId,
        String paymentTransactionError
) {

    public ChargeSubscriptionResponse(ChargeSubscription.Output out) {
        this(
                out.subscriptionId().value(),
                out.subscriptionStatus(),
                out.subscriptionDueDate().toString(),
                out.paymentTransaction().transactionId(),
                out.paymentTransaction().errorMessage()
        );
    }
}
