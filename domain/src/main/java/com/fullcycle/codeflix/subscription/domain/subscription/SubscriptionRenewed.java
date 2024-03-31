package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.plan.Plan;
import com.fullcycle.codeflix.subscription.domain.utils.InstantUtils;

import java.time.Instant;
import java.time.LocalDate;

public record SubscriptionRenewed(
        String subscriptionId,
        String accountId,
        String planId,
        String groupId,
        String transactionId,
        LocalDate dueDate,
        String currency,
        Double price,
        Instant renewedAt,
        Instant occurredOn
) implements SubscriptionEvent {

    public SubscriptionRenewed(final Subscription subscription, final Plan plan, final String transactionId) {
        this(
                subscription.id().value(),
                subscription.accountId().value(),
                plan.id().value(),
                plan.groupId(),
                transactionId,
                subscription.dueDate(),
                plan.price().currency().getCurrencyCode(),
                plan.price().amount(),
                subscription.lastRenewDate(),
                InstantUtils.now()
        );
    }
}
