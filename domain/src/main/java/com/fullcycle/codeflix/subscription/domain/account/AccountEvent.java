package com.fullcycle.codeflix.subscription.domain.account;

import com.fullcycle.codeflix.subscription.domain.DomainEvent;

public interface AccountEvent extends DomainEvent {

    String AGGREGATE_TYPE = "Account";

    String accountId();

    @Override
    default String aggregateId() {
        return accountId();
    }

    @Override
    default String aggregateType() {
        return AGGREGATE_TYPE;
    }
}
