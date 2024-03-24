package com.fullcycle.codeflix.subscription.application.subscription;

import com.fullcycle.codeflix.subscription.application.UseCase;
import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.Identifier;
import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.plan.BillingCycle;
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

public class ActivateSubscription
        extends UseCase<ActivateSubscription.Input, ActivateSubscription.Output> {

    private final PlanGateway planGateway;
    private final SubscriptionGateway subscriptionGateway;
    private final UserGateway userGateway;

    public ActivateSubscription(
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway,
            final UserGateway userGateway
    ) {
        this.planGateway = Objects.requireNonNull(planGateway);
        this.subscriptionGateway = Objects.requireNonNull(subscriptionGateway);
        this.userGateway = Objects.requireNonNull(userGateway);
    }

    @Override
    public Output execute(final Input in) {
        final var userId = new UserId(in.userId());
        final var planId = new PlanId(in.planId());

        this.planGateway.planOfId(planId)
                .orElseThrow(() -> notFound(Plan.class, planId));

        final var user = userGateway.userOfId(userId)
                .orElseThrow(() -> notFound(User.class, userId));

        final var subscription =
                subscriptionGateway.subscriptionOfUser(userId)
                        .orElseGet(() -> newSubscriptionWith(user, planId));

        subscription.activate();

        subscriptionGateway.save(subscription);

        return new Output(subscription.id().value());
    }

    private Subscription newSubscriptionWith(final User user, final PlanId planId) {
        return Subscription.newSubscription(
                this.subscriptionGateway.nextId(),
                user.id(),
                planId
        );
    }

    private DomainException notFound(Class<? extends AggregateRoot<?>> clazz, Identifier id) {
        return DomainException.with(new ValidationError("%s with id %s was not found".formatted(clazz.getCanonicalName(), id.value())));
    }

    public interface Input {
        String userId();
        String planId();
    }

    public record Output(String subscriptionId) {

    }
}
