package com.fullcycle.subscription.domain.account;

import com.fullcycle.subscription.domain.Identifier;

public record AccountId(String value) implements Identifier {

    public AccountId {
        this.assertArgumentNotNull(value, "'accountId' should not be null");
    }
}
