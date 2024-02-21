package com.fullcycle.codeflix.subscription.application.subscription;

import com.fullcycle.codeflix.subscription.application.UseCase;
import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.AssertionConcern;
import com.fullcycle.codeflix.subscription.domain.Identifier;
import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.payment.Payment;
import com.fullcycle.codeflix.subscription.domain.payment.PaymentGateway;
import com.fullcycle.codeflix.subscription.domain.subscription.Subscription;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionGateway;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionId;
import com.fullcycle.codeflix.subscription.domain.user.User;
import com.fullcycle.codeflix.subscription.domain.user.UserGateway;
import com.fullcycle.codeflix.subscription.domain.user.UserId;
import com.fullcycle.codeflix.subscription.domain.utils.IdUtils;
import com.fullcycle.codeflix.subscription.domain.validation.ValidationError;

import java.util.Objects;

public class RenewSubscription
        extends UseCase<RenewSubscription.Input, RenewSubscription.Output>
        implements AssertionConcern {

    private final PaymentGateway paymentGateway;
    private final SubscriptionGateway subscriptionGateway;
    private final UserGateway userGateway;

    public RenewSubscription(
            final PaymentGateway paymentGateway,
            final SubscriptionGateway subscriptionGateway,
            final UserGateway userGateway
    ) {
        this.paymentGateway = Objects.requireNonNull(paymentGateway);
        this.subscriptionGateway = Objects.requireNonNull(subscriptionGateway);
        this.userGateway = Objects.requireNonNull(userGateway);
    }

    @Override
    public Output execute(final Input input) {
        final var userId = new UserId(input.userId);
        final var subscriptionId = new SubscriptionId(input.subscriptionId);

        final var subscription =
                subscriptionGateway.subscriptionOfId(subscriptionId)
                        .filter(it -> it.userId().equals(userId))
                        .orElseThrow(() -> notFound(Subscription.class, subscriptionId));

        final var user = this.userGateway.userOfId(userId)
                .orElseThrow(() -> notFound(User.class, userId));

        final var transaction =
                this.paymentGateway.processPayment(Payment.create(
                        input.paymentType,
                        subscription.price(),
                        IdUtils.uniqueId(),
                        user.address().zipcode(),
                        user.address().number(),
                        user.address().complement(),
                        user.address().country()
                ));

        subscription.renewed(transaction.transactionId());

        subscriptionGateway.save(subscription);

        return new Output(subscription.id().value());
    }

    private DomainException notFound(Class<? extends AggregateRoot<?>> clazz, Identifier id) {
        return DomainException.with(new ValidationError("%s with id %s was not found".formatted(clazz.getCanonicalName(), id.value())));
    }

    public record Input(String userId, String subscriptionId, String paymentType) {

    }

    public record Output(String subscriptionId) {

    }
}
