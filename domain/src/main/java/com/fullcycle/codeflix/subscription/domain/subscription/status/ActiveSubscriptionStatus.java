package com.fullcycle.codeflix.subscription.domain.subscription.status;

import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.subscription.Subscription;

public record ActiveSubscriptionStatus(Subscription subscription) implements SubscriptionStatus {

    private static final String VALUE = "active";

    @Override
    public String value() {
        return VALUE;
    }

    @Override
    public void trailing() {
        throw DomainException.with("status", "Subscription with status active can't go back to trailing");
    }

    @Override
    public void incomplete() {
        this.subscription.setStatus(new IncompleteSubscriptionStatus(subscription));
    }

    @Override
    public void active() {
        // Do nothing
    }

    @Override
    public void canceled() {
        this.subscription.setStatus(new CanceledSubscriptionStatus(subscription));
    }
}
