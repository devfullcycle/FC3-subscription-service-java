package com.fullcycle.subscription.domain.account;

import com.fullcycle.subscription.domain.DomainEvent;

public sealed interface AccountEvent extends DomainEvent permits AccountCreated {

    String TYPE = "Account";

    String accountId();

    @Override
    default String aggregateId() {
        return accountId();
    }

    @Override
    default String aggregateType() {
        return TYPE;
    }

}
