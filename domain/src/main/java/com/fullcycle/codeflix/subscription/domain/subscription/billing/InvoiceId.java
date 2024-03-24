package com.fullcycle.codeflix.subscription.domain.subscription.billing;

import com.fullcycle.codeflix.subscription.domain.Identifier;

import java.util.Objects;

public record InvoiceId(String value) implements Identifier {

    public InvoiceId {
        Objects.requireNonNull(value);
    }
}
