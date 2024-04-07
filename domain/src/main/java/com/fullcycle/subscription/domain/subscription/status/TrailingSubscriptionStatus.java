package com.fullcycle.subscription.domain.subscription.status;

import com.fullcycle.subscription.domain.subscription.Subscription;

public record TrailingSubscriptionStatus(Subscription subscription) implements SubscriptionStatus {

    @Override
    public String value() {
        return TRAILING;
    }

    @Override
    public void trailing() {
        // Do nothing
    }

    @Override
    public void incomplete() {

    }

    @Override
    public void active() {

    }

    @Override
    public void cancel() {

    }
}
