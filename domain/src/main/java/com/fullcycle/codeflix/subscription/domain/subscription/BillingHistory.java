package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.plan.BillingCycle;

import java.time.Instant;

public record BillingHistory(
        String transactionId,
        Instant billingDate,
        BillingCycle billingCycle,
        Double amount
) {
}
