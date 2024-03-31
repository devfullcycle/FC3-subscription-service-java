package com.fullcycle.codeflix.subscription.application.account.impl;

import com.fullcycle.codeflix.subscription.application.account.AddToSubscribersGroup;
import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.Identifier;
import com.fullcycle.codeflix.subscription.domain.account.Account;
import com.fullcycle.codeflix.subscription.domain.account.AccountGateway;
import com.fullcycle.codeflix.subscription.domain.account.AccountId;
import com.fullcycle.codeflix.subscription.domain.account.iam.GroupId;
import com.fullcycle.codeflix.subscription.domain.account.iam.IdentityGateway;
import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.plan.PlanGateway;
import com.fullcycle.codeflix.subscription.domain.subscription.Subscription;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionGateway;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionId;
import com.fullcycle.codeflix.subscription.domain.validation.ValidationError;

import java.util.Objects;

public class DefaultAddToSubscribersGroup extends AddToSubscribersGroup {

    private final AccountGateway accountGateway;
    private final IdentityGateway identityGateway;
    private final PlanGateway planGateway;
    private final SubscriptionGateway subscriptionGateway;

    public DefaultAddToSubscribersGroup(
            final AccountGateway accountGateway,
            final IdentityGateway identityGateway,
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.identityGateway = Objects.requireNonNull(identityGateway);
        this.planGateway = Objects.requireNonNull(planGateway);
        this.subscriptionGateway = Objects.requireNonNull(subscriptionGateway);
    }

    @Override
    public Output execute(final Input in) {
        final var accountId = new AccountId(in.accountId());
        final var subscriptionId = new SubscriptionId(in.subscriptionId());

        final var subscription =
                subscriptionGateway.subscriptionOfId(subscriptionId)
                        .filter(it -> it.accountId().equals(accountId))
                        .orElseThrow(() -> notFound(Subscription.class, subscriptionId));

        if (subscription.isTrail() || subscription.isActive()) {
            final var userId = this.accountGateway.accountOfId(accountId)
                    .orElseThrow(() -> notFound(Account.class, accountId))
                    .userId();

            this.identityGateway.addUserToGroup(userId, new GroupId(in.groupId()));
        }

        return new StdOutput(subscription.id().value());
    }

    private DomainException notFound(Class<? extends AggregateRoot<?>> clazz, Identifier id) {
        return DomainException.with(new ValidationError("%s with id %s was not found".formatted(clazz.getCanonicalName(), id.value())));
    }

    record StdOutput(String subscriptionId) implements Output {

    }
}
