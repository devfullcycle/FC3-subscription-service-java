package com.fullcycle.codeflix.subscription.application.subscription.impl;

import com.fullcycle.codeflix.subscription.application.subscription.CancelSubscription;
import com.fullcycle.codeflix.subscription.application.subscription.RenewedSubscription;
import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.Identifier;
import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.subscription.Subscription;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionGateway;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionId;
import com.fullcycle.codeflix.subscription.domain.user.UserId;
import com.fullcycle.codeflix.subscription.domain.validation.ValidationError;

import java.util.Objects;

public class DefaultCancelSubscription extends CancelSubscription {

    private final SubscriptionGateway subscriptionGateway;

    public DefaultCancelSubscription(
            final SubscriptionGateway subscriptionGateway
    ) {
        this.subscriptionGateway = Objects.requireNonNull(subscriptionGateway);
    }

    @Override
    public Output execute(final Input in) {
        final var subscriptionId = new SubscriptionId(in.subscriptionId());

        final var subscription =
                subscriptionGateway.subscriptionOfId(subscriptionId)
                        .filter(it -> it.userId().equals(new UserId(in.userId())))
                        .orElseThrow(() -> notFound(Subscription.class, subscriptionId));

        subscription.cancel();

        subscriptionGateway.save(subscription);
        return new StdOutput(subscription.id().value(), subscription.status().value());
    }

    private DomainException notFound(Class<? extends AggregateRoot<?>> clazz, Identifier id) {
        return DomainException.with(new ValidationError("%s with id %s was not found".formatted(clazz.getCanonicalName(), id.value())));
    }

    record StdOutput(String subscriptionId, String subscriptionStatus) implements Output {

    }
}
