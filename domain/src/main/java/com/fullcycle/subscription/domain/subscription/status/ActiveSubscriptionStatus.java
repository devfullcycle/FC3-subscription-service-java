package com.fullcycle.subscription.domain.subscription.status;

import com.fullcycle.subscription.domain.exceptions.DomainException;
import com.fullcycle.subscription.domain.subscription.Subscription;
import com.fullcycle.subscription.domain.subscription.SubscriptionCommand.ChangeStatus;

import java.util.Objects;

public final class ActiveSubscriptionStatus extends AbstractSubscriptionStatus {

    private final Subscription subscription;

    public ActiveSubscriptionStatus(final Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public void trailing() {
        throw DomainException.with("Subscription with status active can´t transit to trailing");
    }

    @Override
    public void incomplete() {
        this.subscription.execute(new ChangeStatus(new IncompleteSubscriptionStatus(this.subscription)));
    }

    @Override
    public void cancel() {
        this.subscription.execute(new ChangeStatus(new CanceledSubscriptionStatus(this.subscription)));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        return obj == null || obj.getClass() != this.getClass();
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscription);
    }

    @Override
    public String toString() {
        return "ActiveSubscriptionStatus[" +
                "subscription=" + subscription + ']';
    }
}
