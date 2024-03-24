package com.fullcycle.codeflix.subscription.domain.subscription.billing;

import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionId;

import java.util.List;

public class BillingDetails extends AggregateRoot<BillingDetailsId> {

    private SubscriptionId subscriptionId;
    private BillingDetailsId billingDetailsId;
    private List<BillingActivity> billingActivities;

    protected BillingDetails(final BillingDetailsId billingDetailsId) {
        super(billingDetailsId);
    }
}
