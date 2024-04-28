package com.fullcycle.subscription.infrastructure.rest.models.res;

import com.fullcycle.subscription.application.subscription.CreateSubscription;
import com.fullcycle.subscription.domain.AssertionConcern;

public record CreateSubscriptionResponse(String subscriptionId) implements AssertionConcern {

    public CreateSubscriptionResponse {
        this.assertArgumentNotEmpty(subscriptionId, "CreateSubscriptionResponse 'subscriptionId' should not be empty");
    }

    public CreateSubscriptionResponse(CreateSubscription.Output out) {
        this(out.subscriptionId().value());
    }
}
