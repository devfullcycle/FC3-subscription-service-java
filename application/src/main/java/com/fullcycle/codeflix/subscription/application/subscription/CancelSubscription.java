package com.fullcycle.codeflix.subscription.application.subscription;

import com.fullcycle.codeflix.subscription.application.UseCase;
import com.fullcycle.codeflix.subscription.domain.AssertionConcern;

public abstract class CancelSubscription
        extends UseCase<CancelSubscription.Input, CancelSubscription.Output>
        implements AssertionConcern {

    public interface Input {
        String userId();
        String subscriptionId();
    }

    public interface Output {
        String subscriptionId();
        String subscriptionStatus();
    }
}
