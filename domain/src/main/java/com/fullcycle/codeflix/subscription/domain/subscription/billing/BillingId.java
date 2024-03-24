package com.fullcycle.codeflix.subscription.domain.subscription.billing;

import com.fullcycle.codeflix.subscription.domain.Identifier;

import java.util.Objects;

public record BillingId(String value) implements Identifier {

    public BillingId {
        Objects.requireNonNull(value);
    }
}
