package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.validation.ValidationError;

import java.util.List;

public class SubscriptionNotActiveException extends DomainException {

    private final String subscriptionId;

    private static String message(final Subscription subscription) {
        return "Invalid subscription [subscriptionId:%s]".formatted(subscription.id());
    }

    public SubscriptionNotActiveException(final Subscription subscription) {
        this(message(subscription), subscription.id().value());
    }

    protected SubscriptionNotActiveException(final String message, final String subscriptionId) {
        super(message, List.of(new ValidationError(message)));
        this.subscriptionId = subscriptionId;
    }

    public String subscriptionId() {
        return subscriptionId;
    }
}
