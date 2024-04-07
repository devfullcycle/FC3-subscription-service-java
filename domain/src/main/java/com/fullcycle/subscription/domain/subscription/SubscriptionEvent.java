package com.fullcycle.subscription.domain.subscription;

import com.fullcycle.subscription.domain.DomainEvent;
import com.fullcycle.subscription.domain.plan.Plan;
import com.fullcycle.subscription.domain.utils.InstantUtils;

import java.time.Instant;
import java.time.LocalDate;

public sealed interface SubscriptionEvent extends DomainEvent
        permits SubscriptionCanceled, SubscriptionCreated, SubscriptionRenewed, SubscriptionIncomplete {

    String TYPE = "Subscription";

    String subscriptionId();

    @Override
    default String aggregateId() {
        return subscriptionId();
    }

    @Override
    default String aggregateType() {
        return TYPE;
    }
}
