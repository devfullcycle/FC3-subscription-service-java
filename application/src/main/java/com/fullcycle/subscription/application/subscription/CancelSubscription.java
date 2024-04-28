package com.fullcycle.subscription.application.subscription;

import com.fullcycle.subscription.application.UseCase;
import com.fullcycle.subscription.domain.subscription.SubscriptionId;

public abstract class CancelSubscription extends UseCase<CancelSubscription.Input, CancelSubscription.Output> {

    public interface Input {
        String accountId();
    }

    public interface Output {
        String subscriptionStatus();
        SubscriptionId subscriptionId();
    }
}
