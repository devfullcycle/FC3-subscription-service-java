package com.fullcycle.codeflix.subscription.domain.subscription.status;

import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.subscription.Subscription;

public record CanceledSubscriptionStatus(Subscription subscription) implements SubscriptionStatus {

    private static final String VALUE = "canceled";

    @Override
    public String value() {
        return VALUE;
    }

    @Override
    public void trailing() {
        throw DomainException.with("status", "Subscription with status canceled can't go back to trailing");
    }

    @Override
    public void incomplete() {
        throw DomainException.with("status", "Subscription with status incomplete can't go back to incomplete");
    }

    @Override
    public void active() {
        throw DomainException.with("status", "Subscription with status canceled can't go back to active");
    }

    @Override
    public void canceled() {
        // Do nothing
    }
}
