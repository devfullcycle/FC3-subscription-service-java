package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.plan.Plan;
import com.fullcycle.codeflix.subscription.domain.utils.InstantUtils;

import java.time.Instant;

public record SubscriptionCreated(
        String subscriptionId,
        String accountId,
        String planId,
        String groupId,
        Instant occurredOn
) implements SubscriptionEvent {

    public SubscriptionCreated(final Subscription subscription, final Plan plan) {
        this(
                subscription.id().value(),
                subscription.accountId().value(),
                plan.id().value(),
                plan.groupId(),
                InstantUtils.now()
        );
    }
}
