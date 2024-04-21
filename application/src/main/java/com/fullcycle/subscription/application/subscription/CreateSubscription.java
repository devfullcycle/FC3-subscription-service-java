package com.fullcycle.subscription.application.subscription;

import com.fullcycle.subscription.application.UseCase;
import com.fullcycle.subscription.domain.subscription.SubscriptionId;

public abstract class CreateSubscription extends UseCase<CreateSubscription.Input, CreateSubscription.Output> {

    public interface Input {
        String accountId();
        Long planId();
    }

    public interface Output {
        SubscriptionId subscriptionId();
    }
}
