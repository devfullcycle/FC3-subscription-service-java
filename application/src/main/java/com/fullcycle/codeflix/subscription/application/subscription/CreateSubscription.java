package com.fullcycle.codeflix.subscription.application.subscription;

import com.fullcycle.codeflix.subscription.application.UseCase;
import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.Identifier;
import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.plan.Plan;
import com.fullcycle.codeflix.subscription.domain.plan.PlanGateway;
import com.fullcycle.codeflix.subscription.domain.plan.PlanId;
import com.fullcycle.codeflix.subscription.domain.subscription.Subscription;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionGateway;
import com.fullcycle.codeflix.subscription.domain.user.User;
import com.fullcycle.codeflix.subscription.domain.user.UserGateway;
import com.fullcycle.codeflix.subscription.domain.user.UserId;
import com.fullcycle.codeflix.subscription.domain.validation.ValidationError;

import java.util.Objects;

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
