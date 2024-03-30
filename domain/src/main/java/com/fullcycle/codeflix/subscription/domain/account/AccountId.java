package com.fullcycle.codeflix.subscription.domain.account;

import com.fullcycle.codeflix.subscription.domain.Identifier;

public record AccountId(String value) implements Identifier {

    public AccountId {
        this.assertArgumentNotNull(value, "'value' is required for user identifier");
    }
}
