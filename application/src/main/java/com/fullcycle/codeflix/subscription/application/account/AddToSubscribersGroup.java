package com.fullcycle.codeflix.subscription.application.account;

import com.fullcycle.codeflix.subscription.application.UseCase;
import com.fullcycle.codeflix.subscription.domain.AssertionConcern;

public abstract class AddToSubscribersGroup
        extends UseCase<AddToSubscribersGroup.Input, AddToSubscribersGroup.Output>
        implements AssertionConcern {

    public interface Input {
        String accountId();
        String groupId();
        String subscriptionId();
    }

    public interface Output {
        String subscriptionId();
    }
}
