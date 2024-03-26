package com.fullcycle.codeflix.subscription.domain.subscription.status;

import com.fullcycle.codeflix.subscription.domain.subscription.Subscription;

public record TrailingSubscriptionStatus(Subscription subscription) implements SubscriptionStatus {

    private static final String VALUE = "trailing";

    @Override
    public String value() {
        return VALUE;
    }

    @Override
    public void trailing() {
        // Do nothing
    }

    @Override
    public void incomplete() {
        this.subscription.setStatus(new IncompleteSubscriptionStatus(subscription));
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
