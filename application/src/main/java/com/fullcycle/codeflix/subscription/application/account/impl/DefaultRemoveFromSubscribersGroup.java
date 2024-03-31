package com.fullcycle.codeflix.subscription.application.account.impl;

import com.fullcycle.codeflix.subscription.application.account.RemoveFromSubscribersGroup;
import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.Identifier;
import com.fullcycle.codeflix.subscription.domain.account.Account;
import com.fullcycle.codeflix.subscription.domain.account.AccountGateway;
import com.fullcycle.codeflix.subscription.domain.account.AccountId;
import com.fullcycle.codeflix.subscription.domain.account.iam.GroupId;
import com.fullcycle.codeflix.subscription.domain.account.iam.IdentityGateway;
import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.plan.Plan;
import com.fullcycle.codeflix.subscription.domain.plan.PlanGateway;
import com.fullcycle.codeflix.subscription.domain.subscription.Subscription;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionGateway;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionId;
import com.fullcycle.codeflix.subscription.domain.validation.ValidationError;

import java.util.Objects;

import static java.time.LocalDate.now;

public class DefaultRemoveFromSubscribersGroup extends RemoveFromSubscribersGroup {

    private final AccountGateway accountGateway;
    private final IdentityGateway identityGateway;
    private final PlanGateway planGateway;
    private final SubscriptionGateway subscriptionGateway;

    public DefaultRemoveFromSubscribersGroup(
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

        if (isRemovableStatus(subscription) && subscription.dueDate().isBefore(now())) {
            final var userId = this.accountGateway.accountOfId(accountId)
                    .orElseThrow(() -> notFound(Account.class, accountId))
                    .userId();

            final var aPlan = this.planGateway.planOfId(subscription.planId())
                    .orElseThrow(() -> notFound(Plan.class, subscription.planId()));

            this.identityGateway.addUserToGroup(userId, new GroupId(aPlan.groupId()));
        }

        return new StdOutput(subscription.id().value());
    }

    private static boolean isRemovableStatus(Subscription subscription) {
        return subscription.isCanceled() || subscription.isIncomplete();
    }

    private DomainException notFound(Class<? extends AggregateRoot<?>> clazz, Identifier id) {
        return DomainException.with(new ValidationError("%s with id %s was not found".formatted(clazz.getCanonicalName(), id.value())));
    }

    record StdOutput(String subscriptionId) implements Output {

    }
}
