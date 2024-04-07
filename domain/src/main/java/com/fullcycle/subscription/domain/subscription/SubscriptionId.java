package com.fullcycle.subscription.domain.subscription;

import com.fullcycle.subscription.domain.Identifier;

public record SubscriptionId(String value) implements Identifier<String> {

    public SubscriptionId {
        this.assertArgumentNotEmpty(value, "'subscriptionId' should not be empty");
    }
}
