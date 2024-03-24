package com.fullcycle.codeflix.subscription.domain.subscription.billing;

import com.fullcycle.codeflix.subscription.domain.Entity;
import com.fullcycle.codeflix.subscription.domain.plan.BillingCycle;

import java.time.Instant;

public class BillingRecord extends Entity<BillingRecordId> {

    private String transactionId;
    private Instant invoiceDate;
    private BillingCycle billingCycle;
    private Double amount;

    protected BillingRecord(BillingRecordId billingRecordId) {
        super(billingRecordId);
    }
}
