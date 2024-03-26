package com.fullcycle.codeflix.subscription.infrastructure.rest.models.req;

import com.fullcycle.codeflix.subscription.application.subscription.CreateSubscription;
import com.fullcycle.codeflix.subscription.domain.plan.BillingCycle;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateSubscriptionRequest(
        @NotEmpty String userId,
        @NotEmpty String planId,
        @NotNull BillingCycle billingCycle,
        @NotNull Double price
) implements CreateSubscription.Input {

}
