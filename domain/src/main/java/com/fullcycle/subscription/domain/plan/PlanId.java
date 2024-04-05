package com.fullcycle.subscription.domain.plan;

import com.fullcycle.subscription.domain.Identifier;

public record PlanId(String value) implements Identifier {

    public PlanId {
        this.assertArgumentNotEmpty(value, "'planId' should not be empty");
    }
}
