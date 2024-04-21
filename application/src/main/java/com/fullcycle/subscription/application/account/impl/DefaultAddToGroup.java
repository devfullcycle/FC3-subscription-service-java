package com.fullcycle.subscription.application.account.impl;

import com.fullcycle.subscription.application.account.AddToGroup;
import com.fullcycle.subscription.domain.AggregateRoot;
import com.fullcycle.subscription.domain.Identifier;
import com.fullcycle.subscription.domain.account.Account;
import com.fullcycle.subscription.domain.account.AccountGateway;
import com.fullcycle.subscription.domain.account.AccountId;
import com.fullcycle.subscription.domain.account.idp.GroupId;
import com.fullcycle.subscription.domain.account.idp.IdentityProviderGateway;
import com.fullcycle.subscription.domain.exceptions.DomainException;
import com.fullcycle.subscription.domain.subscription.Subscription;
import com.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import com.fullcycle.subscription.domain.subscription.SubscriptionId;

import java.util.Objects;

public class DefaultAddToGroup extends AddToGroup {

    private final AccountGateway accountGateway;
    private final IdentityProviderGateway identityProviderGateway;
    private final SubscriptionGateway subscriptionGateway;

    public DefaultAddToGroup(
            final AccountGateway accountGateway,
            final IdentityProviderGateway identityProviderGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.identityProviderGateway = Objects.requireNonNull(identityProviderGateway);
        this.subscriptionGateway = Objects.requireNonNull(subscriptionGateway);
    }

    @Override
    public Output execute(final AddToGroup.Input in) {
        if (in == null) {
            throw new IllegalArgumentException("Input to DefaultAddToGroup cannot be null");
        }

        final var anAccountId = new AccountId(in.accountId());
        final var aSubscriptionId = new SubscriptionId(in.subscriptionId());

        final var aSubscription = subscriptionGateway.subscriptionOfId(aSubscriptionId)
                .filter(it -> it.accountId().equals(anAccountId))
                .orElseThrow(() -> notFound(Subscription.class, aSubscriptionId));

        if (aSubscription.isTrail() || aSubscription.isActive()) {
            final var userId = this.accountGateway.accountOfId(anAccountId)
                    .orElseThrow(() -> notFound(Account.class, anAccountId))
                    .userId();

            this.identityProviderGateway.addUserToGroup(userId, new GroupId(in.groupId()));
        }

        return new StdOutput(aSubscriptionId);
    }

    private RuntimeException notFound(Class<? extends AggregateRoot<?>> aggClass, Identifier id) {
        return DomainException.with("%s with id %s was not found".formatted(aggClass.getCanonicalName(), id.value()));
    }

    record StdOutput(SubscriptionId subscriptionId) implements Output {

    }
}
