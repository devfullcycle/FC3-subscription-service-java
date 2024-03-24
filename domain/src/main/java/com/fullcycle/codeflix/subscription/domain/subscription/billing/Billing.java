package com.fullcycle.codeflix.subscription.domain.subscription.billing;

import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionId;

import java.util.Set;

public class Billing extends AggregateRoot<BillingId> {

    private SubscriptionId subscriptionId;

    private Set<BillingRecord> billingRecords;

    private Billing(final BillingId billingId) {
        super(billingId);
    }
}
