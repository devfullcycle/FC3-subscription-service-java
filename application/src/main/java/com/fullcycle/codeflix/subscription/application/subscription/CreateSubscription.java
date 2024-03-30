package com.fullcycle.codeflix.subscription.application.subscription;

import com.fullcycle.codeflix.subscription.application.UseCase;

public abstract class CreateSubscription
        extends UseCase<CreateSubscription.Input, CreateSubscription.Output> {

    public interface Input {
        String userId();
        String planId();
    }

    public interface Output {
        String subscriptionId();
    }
}
