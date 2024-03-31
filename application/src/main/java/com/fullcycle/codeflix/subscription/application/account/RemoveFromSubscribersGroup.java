package com.fullcycle.codeflix.subscription.application.account;

import com.fullcycle.codeflix.subscription.application.UseCase;
import com.fullcycle.codeflix.subscription.domain.AssertionConcern;

public abstract class RemoveFromSubscribersGroup
        extends UseCase<RemoveFromSubscribersGroup.Input, RemoveFromSubscribersGroup.Output>
        implements AssertionConcern {

    public interface Input {
        String accountId();

        String subscriptionId();
    }

    public interface Output {
        String subscriptionId();
    }
}
