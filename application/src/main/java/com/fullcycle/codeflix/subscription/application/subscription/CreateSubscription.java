package com.fullcycle.codeflix.subscription.application.subscription;

import com.fullcycle.codeflix.subscription.application.UseCase;
import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.AssertionConcern;
import com.fullcycle.codeflix.subscription.domain.Identifier;
import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.plan.BillingCycle;
import com.fullcycle.codeflix.subscription.domain.plan.PlanGateway;
import com.fullcycle.codeflix.subscription.domain.plan.PlanId;
import com.fullcycle.codeflix.subscription.domain.subscription.Subscription;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionGateway;
import com.fullcycle.codeflix.subscription.domain.user.User;
import com.fullcycle.codeflix.subscription.domain.user.UserGateway;
import com.fullcycle.codeflix.subscription.domain.user.UserId;
import com.fullcycle.codeflix.subscription.domain.validation.Error;

import java.util.Objects;

public class CreateSubscription
        extends UseCase<CreateSubscription.Input, CreateSubscription.Output>
        implements AssertionConcern {

    private final PlanGateway planGateway;
    private final SubscriptionGateway subscriptionGateway;
    private final UserGateway userGateway;

    public CreateSubscription(
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway,
            final UserGateway userGateway
    ) {
        this.planGateway = Objects.requireNonNull(planGateway);
        this.subscriptionGateway = Objects.requireNonNull(subscriptionGateway);
        this.userGateway = Objects.requireNonNull(userGateway);
    }

    @Override
    public Output execute(final Input input) {
        final var userId = new UserId(input.userId);
        final var price = assertArgumentNotNull(input.price, "'price' should not be null");
        final var billingCycle = BillingCycle.of(input.billingCycle)
                .orElseThrow(() -> DomainException.with(new Error("Invalid billing cycle")));

        validatePlanOption(new PlanId(input.planId), billingCycle, price);

        final var user = userGateway.userOfId(userId)
                .orElseThrow(() -> notFound(User.class, userId));

        final var subscription =
                subscriptionGateway.subscriptionOfUser(userId)
                        .orElseGet(() -> newSubscriptionWith(user, billingCycle, price));

        subscription.activateSubscription();

        subscriptionGateway.save(subscription);

        return new Output(subscription.id().value());
    }

    private void validatePlanOption(final PlanId planId, final BillingCycle billingCycle, final Double price) {
        planGateway.planOfId(planId)
                .filter(it -> it.hasOption(billingCycle, price))
                .orElseThrow(() -> DomainException.with(new Error("Invalid plan or selected plan option")));
    }

    private Subscription newSubscriptionWith(final User user, final BillingCycle billingCycle, final Double price) {
        return Subscription.newSubscription(
                this.subscriptionGateway.nextId(),
                user.id(),
                billingCycle,
                price,
                true
        );
    }

    private DomainException notFound(Class<? extends AggregateRoot<?>> clazz, Identifier id) {
        return DomainException.with(new Error("%s with id %s was not found".formatted(clazz.getCanonicalName(), id.value())));
    }

    public record Input(
            String userId,
            String planId,
            String billingCycle,
            Double price
    ) {

    }

    public record Output(String subscriptionId) {

    }
}
