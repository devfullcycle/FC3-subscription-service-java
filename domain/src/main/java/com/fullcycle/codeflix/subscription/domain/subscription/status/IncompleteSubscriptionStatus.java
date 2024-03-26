package com.fullcycle.codeflix.subscription.domain.subscription.status;

import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.subscription.Subscription;

public record IncompleteSubscriptionStatus(Subscription subscription) implements SubscriptionStatus {

    private static final String VALUE = "incomplete";

    @Override
    public String value() {
        return VALUE;
    }

    @Override
    public void trailing() {
        throw DomainException.with("status", "Subscription with status incomplete can't go back to trailing");
    }

    @Override
    public void incomplete() {
        // Do nothing
    }

    @Override
    public void active() {
        this.subscription.setStatus(new ActiveSubscriptionStatus(subscription));
    }

    @Override
    public void canceled() {
        this.subscription.setStatus(new CanceledSubscriptionStatus(subscription));
    }
}
