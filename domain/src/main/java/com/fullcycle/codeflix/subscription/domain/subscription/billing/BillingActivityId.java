package com.fullcycle.codeflix.subscription.domain.subscription.billing;

import com.fullcycle.codeflix.subscription.domain.Identifier;

import java.util.Objects;

public record BillingActivityId(String value) implements Identifier {

    public BillingActivityId {
        Objects.requireNonNull(value);
    }
}
