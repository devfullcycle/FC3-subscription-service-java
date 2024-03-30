package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.DomainEvent;

public interface SubscriptionEvent extends DomainEvent {

    String AGGREGATE_TYPE = "Subscription";

    SubscriptionId subscriptionId();

    @Override
    default String aggregateId() {
        return subscriptionId().value();
    }
    
    @Override
    default String aggregateType() {
        return AGGREGATE_TYPE;
    }
}
