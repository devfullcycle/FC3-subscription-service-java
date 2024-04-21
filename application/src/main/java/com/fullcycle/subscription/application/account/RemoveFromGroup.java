package com.fullcycle.subscription.application.account;

import com.fullcycle.subscription.application.UseCase;
import com.fullcycle.subscription.domain.subscription.SubscriptionId;

public abstract class RemoveFromGroup extends UseCase<RemoveFromGroup.Input, RemoveFromGroup.Output> {

    public interface Input {
        String accountId();
        String subscriptionId();
        String groupId();
    }

    public interface Output {
        SubscriptionId subscriptionId();
    }
}
