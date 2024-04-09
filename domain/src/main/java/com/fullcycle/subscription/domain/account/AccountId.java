package com.fullcycle.subscription.domain.account;

import com.fullcycle.subscription.domain.Identifier;

public record AccountId(String value) implements Identifier<String> {

    public AccountId {
        this.assertArgumentNotEmpty(value, "'accountId' should not be empty");
    }
}
