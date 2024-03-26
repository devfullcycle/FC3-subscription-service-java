package com.fullcycle.codeflix.subscription.application.subscription.impl;

import com.fullcycle.codeflix.subscription.application.subscription.IncompleteSubscription;
import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.Identifier;
import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.subscription.Subscription;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionGateway;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionId;
import com.fullcycle.codeflix.subscription.domain.user.UserId;
import com.fullcycle.codeflix.subscription.domain.validation.ValidationError;

import java.time.LocalDate;
import java.util.Objects;

public class DefaultIncompleteSubscription extends IncompleteSubscription {

    private final SubscriptionGateway subscriptionGateway;

    public DefaultIncompleteSubscription(final SubscriptionGateway subscriptionGateway) {
        this.subscriptionGateway = Objects.requireNonNull(subscriptionGateway);
    }

    @Override
    public Output execute(final Input in) {
        final var subscriptionId = new SubscriptionId(in.subscriptionId());

        final var subscription =
                subscriptionGateway.subscriptionOfId(subscriptionId)
                        .filter(it -> it.userId().equals(new UserId(in.userId())))
                        .orElseThrow(() -> notFound(Subscription.class, subscriptionId));

        if (subscription.dueDate().isAfter(LocalDate.now())) {
            subscription.incomplete(in.reason(), in.transactionId());
        } else {
            subscription.cancel();
        }

        subscriptionGateway.save(subscription);
        return new StdOutput(subscription.id().value());
    }

    private DomainException notFound(Class<? extends AggregateRoot<?>> clazz, Identifier id) {
        return DomainException.with(new ValidationError("%s with id %s was not found".formatted(clazz.getCanonicalName(), id.value())));
    }

    record StdOutput(String subscriptionId) implements Output {

    }
}
