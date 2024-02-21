package com.fullcycle.codeflix.subscription.domain.plan;

import java.util.Arrays;
import java.util.Optional;

public enum BillingCycle {
    ANNUALLY, MONTHLY;

    public static Optional<BillingCycle> of(String billingCycle) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(billingCycle))
                .findFirst();
    }
}
