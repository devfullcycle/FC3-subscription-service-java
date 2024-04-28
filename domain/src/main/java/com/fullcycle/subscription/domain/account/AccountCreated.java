package com.fullcycle.subscription.domain.account;

import com.fullcycle.subscription.domain.utils.InstantUtils;

import java.time.Instant;

public record AccountCreated(String accountId, String email, String fullname, Instant occurredOn) implements AccountEvent {

    public AccountCreated {
        this.assertArgumentNotEmpty(accountId, "'accountId' should not be empty");
        this.assertArgumentNotEmpty(email, "'email' should not be empty");
        this.assertArgumentNotEmpty(fullname, "'fullname' should not be empty");
        this.assertArgumentNotNull(occurredOn, "'occurredOn' should not be null");
    }

    public AccountCreated(final Account anAccount) {
        this(
                anAccount.id().value(),
                anAccount.email().value(),
                anAccount.name().fullname(),
                InstantUtils.now()
        );
    }
}