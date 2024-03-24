package com.fullcycle.codeflix.subscription.domain.subscription.billing;

import com.fullcycle.codeflix.subscription.domain.Identifier;

import java.util.Objects;

public record BillingDetailsId(String value) implements Identifier {

    public BillingDetailsId {
        Objects.requireNonNull(value);
    }
}
