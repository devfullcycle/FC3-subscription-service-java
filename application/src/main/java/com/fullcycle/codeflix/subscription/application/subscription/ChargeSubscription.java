package com.fullcycle.codeflix.subscription.application.subscription;

import com.fullcycle.codeflix.subscription.application.UseCase;
import com.fullcycle.codeflix.subscription.domain.AssertionConcern;
import com.fullcycle.codeflix.subscription.domain.subscription.status.SubscriptionStatus;

public abstract class ChargeSubscription
        extends UseCase<ChargeSubscription.Input, ChargeSubscription.Output>
        implements AssertionConcern {

    public interface Input {
        String userId();
        String subscriptionId();
        String paymentType();
    }

    public interface Output {
        String subscriptionId();
        SubscriptionStatus subscriptionStatus();
    }
}
