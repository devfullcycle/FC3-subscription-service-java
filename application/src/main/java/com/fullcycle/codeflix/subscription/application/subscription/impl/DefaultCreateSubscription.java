package com.fullcycle.codeflix.subscription.application.subscription.impl;

import com.fullcycle.codeflix.subscription.application.subscription.CreateSubscription;
import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.Identifier;
import com.fullcycle.codeflix.subscription.domain.account.Account;
import com.fullcycle.codeflix.subscription.domain.account.AccountGateway;
import com.fullcycle.codeflix.subscription.domain.account.AccountId;
import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.plan.Plan;
import com.fullcycle.codeflix.subscription.domain.plan.PlanGateway;
import com.fullcycle.codeflix.subscription.domain.plan.PlanId;
import com.fullcycle.codeflix.subscription.domain.plan.PlanNotFoundException;
import com.fullcycle.codeflix.subscription.domain.subscription.ActiveSubscriptionException;
import com.fullcycle.codeflix.subscription.domain.subscription.Subscription;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionGateway;
import com.fullcycle.codeflix.subscription.domain.validation.ValidationError;

import java.util.Objects;

public class DefaultCreateSubscription extends CreateSubscription {

    private final PlanGateway planGateway;
    private final SubscriptionGateway subscriptionGateway;
    private final AccountGateway accountGateway;

    public DefaultCreateSubscription(
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway,
            final AccountGateway accountGateway
    ) {
        this.planGateway = Objects.requireNonNull(planGateway);
        this.subscriptionGateway = Objects.requireNonNull(subscriptionGateway);
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public Output execute(final Input in) {
        final var userId = new AccountId(in.userId());
        final var planId = new PlanId(in.planId());

        validateSubscriptionExistence(userId);

        final var plan = this.planGateway.planOfId(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));

        final var account = accountGateway.accountOfId(userId)
                .orElseThrow(() -> notFound(Account.class, userId));

        final var subscription = newSubscriptionWith(account, plan);
        subscriptionGateway.save(subscription);

        return new StdOutput(subscription.id().value());
    }

    private void validatePlanExistence(final PlanId planId) {
        if (!this.planGateway.existsPlanOfId(planId)) {
            throw new PlanNotFoundException(planId);
        }
    }

    private void validateSubscriptionExistence(final AccountId accountId) {
        subscriptionGateway.latestSubscriptionOfUser(accountId).ifPresent(s -> {
            if (!s.isCanceled()) {
                throw new ActiveSubscriptionException(accountId, s.id());
            }
        });
    }

    private Subscription newSubscriptionWith(final Account user, final Plan plan) {
        return Subscription.newSubscription(
                this.subscriptionGateway.nextId(),
                user.id(),
                plan
        );
    }

    private DomainException notFound(Class<? extends AggregateRoot<?>> clazz, Identifier id) {
        return DomainException.with(new ValidationError("%s with id %s was not found".formatted(clazz.getCanonicalName(), id.value())));
    }

    public record StdOutput(String subscriptionId) implements Output {

    }
}
