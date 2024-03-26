package com.fullcycle.codeflix.subscription.infrastructure.rest.models.res;

import com.fullcycle.codeflix.subscription.application.subscription.CreateSubscription;
import com.fullcycle.codeflix.subscription.domain.AssertionConcern;

public record CreateSubscriptionResponse(String subscriptionId) implements AssertionConcern {

    public CreateSubscriptionResponse {
        this.assertArgumentNotEmpty(subscriptionId, "'subscriptionId' should not be empty");
    }

    public CreateSubscriptionResponse(final CreateSubscription.Output out) {
        this(out.subscriptionId());
    }
}
