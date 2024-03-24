package com.fullcycle.codeflix.subscription.domain.subscription.billing;

import com.fullcycle.codeflix.subscription.domain.Identifier;

import java.util.Objects;

public record BillingRecordId(String value) implements Identifier {

    public BillingRecordId {
        Objects.requireNonNull(value);
    }
}
