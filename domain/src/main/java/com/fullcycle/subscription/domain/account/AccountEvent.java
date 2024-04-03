package com.fullcycle.subscription.domain.account;

import com.fullcycle.subscription.domain.DomainEvent;
import com.fullcycle.subscription.domain.utils.InstantUtils;

import java.time.Instant;

public sealed interface AccountEvent extends DomainEvent {

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

    record AccountCreated(String accountId, String email, String fullname, Instant occurredOn) implements AccountEvent {

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
}
