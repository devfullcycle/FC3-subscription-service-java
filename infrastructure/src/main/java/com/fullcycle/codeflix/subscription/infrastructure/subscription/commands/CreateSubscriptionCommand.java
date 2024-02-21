package com.fullcycle.codeflix.subscription.infrastructure.subscription.commands;

import com.fullcycle.codeflix.subscription.application.subscription.CreateSubscription;
import com.fullcycle.codeflix.subscription.domain.plan.BillingCycle;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateSubscriptionCommand(
        @NotEmpty String userId,
        @NotEmpty String planId,
        @NotNull BillingCycle billingCycle,
        @NotNull Double price
) implements CreateSubscription.Command {

}
