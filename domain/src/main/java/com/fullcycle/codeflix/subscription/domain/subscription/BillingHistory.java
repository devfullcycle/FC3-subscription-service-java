package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.ValueObject;
import com.fullcycle.codeflix.subscription.domain.plan.BillingCycle;

import java.time.Instant;

public record BillingHistory(String transactionId, Instant billingDate, BillingCycle billingCycle, Double amount) implements ValueObject {

    public BillingHistory {
        this.assertArgumentNotNull(transactionId, "'transactionId' is required");
        this.assertArgumentNotNull(billingDate, "'billingDate' is required");
        this.assertArgumentNotNull(billingCycle, "'billingCycle' is required");
        this.assertArgumentNotNull(amount, "'amount' is required");
    }
}
