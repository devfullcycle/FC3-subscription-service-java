package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.account.AccountId;
import com.fullcycle.codeflix.subscription.domain.monetary.MonetaryAmount;
import com.fullcycle.codeflix.subscription.domain.plan.Plan;
import com.fullcycle.codeflix.subscription.domain.plan.PlanId;
import com.fullcycle.codeflix.subscription.domain.utils.InstantUtils;

import java.time.Instant;
import java.time.LocalDate;

public record SubscriptionRenewed(
        SubscriptionId subscriptionId,
        AccountId accountId,
        PlanId planId,
        String transactionId,
        LocalDate dueDate,
        MonetaryAmount price,
        Instant renewedAt,
        Instant occurredOn
) implements SubscriptionEvent {

    public SubscriptionRenewed(final Subscription subscription, final Plan plan, final String transactionId) {
        this(
                subscription.id(),
                subscription.accountId(),
                plan.id(),
                transactionId,
                subscription.dueDate(),
                plan.price(),
                subscription.lastRenewDate(),
                InstantUtils.now()
        );
    }
}
