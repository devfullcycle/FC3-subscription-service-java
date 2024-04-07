package com.fullcycle.subscription.domain.subscription.status;

import com.fullcycle.subscription.domain.exceptions.DomainException;
import com.fullcycle.subscription.domain.subscription.Subscription;

public record CanceledSubscriptionStatus(Subscription subscription) implements SubscriptionStatus {

    @Override
    public void trailing() {
        throw DomainException.with("Subscription with status canceled can´t transit to trailing");
    }

    @Override
    public void incomplete() {
        throw DomainException.with("Subscription with status canceled can´t transit to incomplete");
    }

    @Override
    public void active() {
        throw DomainException.with("Subscription with status canceled can´t transit to active");
    }

    @Override
    public void cancel() {
        // do nothing
    }
}
