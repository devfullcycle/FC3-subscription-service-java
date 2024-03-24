package com.fullcycle.codeflix.subscription.infrastructure.subscription.commands;

import com.fullcycle.codeflix.subscription.application.subscription.ActivateSubscription;
import com.fullcycle.codeflix.subscription.domain.plan.BillingCycle;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateSubscriptionInput(
        @NotEmpty String userId,
        @NotEmpty String planId,
        @NotNull BillingCycle billingCycle,
        @NotNull Double price
) implements ActivateSubscription.Input {

}
