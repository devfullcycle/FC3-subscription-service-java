package com.fullcycle.codeflix.subscription.domain.subscription.billing;

import com.fullcycle.codeflix.subscription.domain.Entity;

import java.time.Instant;

public class BillingActivity extends Entity<BillingActivityId> {

    private BillingActivityId billingActivityId;
    private String description;
    private String paymentMethod;
    private double grossAmount;
    private Instant billingDate;
    private Period servicePeriod;
    private InvoiceId invoiceId;

    private BillingActivity(BillingActivityId billingActivityId) {
        super(billingActivityId);
    }
}
