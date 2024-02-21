package com.fullcycle.codeflix.subscription.domain.plan;

import com.fullcycle.codeflix.subscription.domain.Identifier;

import java.util.Objects;

public record PlanId(String value) implements Identifier {

    public PlanId {
        Objects.requireNonNull(value);
    }
}
