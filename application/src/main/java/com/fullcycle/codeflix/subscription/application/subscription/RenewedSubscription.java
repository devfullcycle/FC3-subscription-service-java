package com.fullcycle.codeflix.subscription.application.subscription;

import com.fullcycle.codeflix.subscription.application.UseCase;
import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.AssertionConcern;
import com.fullcycle.codeflix.subscription.domain.Identifier;
import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.iam.GroupId;
import com.fullcycle.codeflix.subscription.domain.iam.IAMUserId;
import com.fullcycle.codeflix.subscription.domain.iam.IdentityGateway;
import com.fullcycle.codeflix.subscription.domain.plan.Plan;
import com.fullcycle.codeflix.subscription.domain.plan.PlanGateway;
import com.fullcycle.codeflix.subscription.domain.subscription.Subscription;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionGateway;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionId;
import com.fullcycle.codeflix.subscription.domain.user.UserId;
import com.fullcycle.codeflix.subscription.domain.validation.ValidationError;

import java.util.Objects;

public class RenewedSubscription
        extends UseCase<RenewedSubscription.Input, RenewedSubscription.Output>
        implements AssertionConcern {

    private final IdentityGateway identityGateway;
    private final PlanGateway planGateway;
    private final SubscriptionGateway subscriptionGateway;

    public RenewedSubscription(
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
        final var subscriptionId = new SubscriptionId(in.subscriptionId);

        final var subscription =
                subscriptionGateway.subscriptionOfId(subscriptionId)
                        .filter(it -> it.userId().equals(new UserId(in.userId)))
                        .orElseThrow(() -> notFound(Subscription.class, subscriptionId));

        final var aPlan = this.planGateway.planOfId(subscription.planId())
                .orElseThrow(() -> notFound(Plan.class, subscription.planId()));

        this.identityGateway.addUserToGroup(new IAMUserId(in.subscriptionId), new GroupId(aPlan.groupId()));

        return new Output(subscription.id().value());
    }

    private DomainException notFound(Class<? extends AggregateRoot<?>> clazz, Identifier id) {
        return DomainException.with(new ValidationError("%s with id %s was not found".formatted(clazz.getCanonicalName(), id.value())));
    }

    public record Input(String userId, String subscriptionId, String transactionId) {

    }

    public record Output(String subscriptionId) {

    }
}
