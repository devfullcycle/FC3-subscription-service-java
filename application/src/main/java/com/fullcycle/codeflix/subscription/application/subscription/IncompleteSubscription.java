package com.fullcycle.codeflix.subscription.application.subscription;

import com.fullcycle.codeflix.subscription.application.UseCase;
import com.fullcycle.codeflix.subscription.domain.AssertionConcern;

public abstract class IncompleteSubscription
        extends UseCase<IncompleteSubscription.Input, IncompleteSubscription.Output>
        implements AssertionConcern {

    public interface Input {
        String userId();
        String subscriptionId();
        String reason();
        String transactionId();
    }

    public interface Output {
        String subscriptionId();
    }
}
