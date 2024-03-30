package com.fullcycle.codeflix.subscription.application.subscription.impl;

import com.fullcycle.codeflix.subscription.application.subscription.RenewedSubscription;
import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.Identifier;
import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.account.iam.GroupId;
import com.fullcycle.codeflix.subscription.domain.account.iam.IAMUserId;
import com.fullcycle.codeflix.subscription.domain.account.iam.IdentityGateway;
import com.fullcycle.codeflix.subscription.domain.plan.Plan;
import com.fullcycle.codeflix.subscription.domain.plan.PlanGateway;
import com.fullcycle.codeflix.subscription.domain.subscription.Subscription;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionGateway;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionId;
import com.fullcycle.codeflix.subscription.domain.account.AccountId;
import com.fullcycle.codeflix.subscription.domain.validation.ValidationError;

import java.util.Objects;

public class DefaultRenewedSubscription extends RenewedSubscription {

    private final IdentityGateway identityGateway;
    private final PlanGateway planGateway;
    private final SubscriptionGateway subscriptionGateway;

    public DefaultRenewedSubscription(
            final IdentityGateway identityGateway,
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        this.identityGateway = Objects.requireNonNull(identityGateway);
        this.planGateway = Objects.requireNonNull(planGateway);
        this.subscriptionGateway = Objects.requireNonNull(subscriptionGateway);
    }

    @Override
    public Output execute(final Input in) {
        final var subscriptionId = new SubscriptionId(in.subscriptionId());

        final var subscription =
                subscriptionGateway.subscriptionOfId(subscriptionId)
                        .filter(it -> it.accountId().equals(new AccountId(in.userId())))
                        .orElseThrow(() -> notFound(Subscription.class, subscriptionId));

        final var aPlan = this.planGateway.planOfId(subscription.planId())
                .orElseThrow(() -> notFound(Plan.class, subscription.planId()));

        this.identityGateway.addUserToGroup(new IAMUserId(in.subscriptionId()), new GroupId(aPlan.groupId()));

        return new StdOutput(subscription.id().value());
    }

    private DomainException notFound(Class<? extends AggregateRoot<?>> clazz, Identifier id) {
        return DomainException.with(new ValidationError("%s with id %s was not found".formatted(clazz.getCanonicalName(), id.value())));
    }

    record StdOutput(String subscriptionId) implements Output {

    }
}
