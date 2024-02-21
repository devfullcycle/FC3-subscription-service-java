package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.Identifier;

import java.util.Objects;

public record SubscriptionId(String value) implements Identifier {

    public SubscriptionId {
        Objects.requireNonNull(value);
    }
}
