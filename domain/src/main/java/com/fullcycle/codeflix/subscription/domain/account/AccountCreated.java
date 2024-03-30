package com.fullcycle.codeflix.subscription.domain.account;

import com.fullcycle.codeflix.subscription.domain.utils.InstantUtils;

import java.time.Instant;

public record AccountCreated(
        String accountId,
        String email,
        String fullname,
        Instant occurredOn
) implements AccountEvent {

    public AccountCreated(final Account account) {
        this(
                account.id().value(),
                account.email().value(),
                account.name().fullname(),
                InstantUtils.now()
        );
    }
}
