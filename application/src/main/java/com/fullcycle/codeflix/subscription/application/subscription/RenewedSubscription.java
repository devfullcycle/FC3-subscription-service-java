package com.fullcycle.codeflix.subscription.application.subscription;

import com.fullcycle.codeflix.subscription.application.UseCase;
import com.fullcycle.codeflix.subscription.domain.AssertionConcern;

public abstract class RenewedSubscription
        extends UseCase<RenewedSubscription.Input, RenewedSubscription.Output>
        implements AssertionConcern {

    public interface Input {
        String userId();
        String subscriptionId();
        String transactionId();
    }

    public interface Output {
        String subscriptionId();
    }
}
