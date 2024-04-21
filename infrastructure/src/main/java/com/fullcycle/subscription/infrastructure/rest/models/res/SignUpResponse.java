package com.fullcycle.subscription.infrastructure.rest.models.res;

import com.fullcycle.subscription.application.account.CreateAccount;
import com.fullcycle.subscription.domain.AssertionConcern;

public record SignUpResponse(String accountId) implements AssertionConcern {

    public SignUpResponse {
        this.assertArgumentNotEmpty(accountId, "'accountId' must not be empty");
    }

    public SignUpResponse(final CreateAccount.Output out) {
        this(out.accountId().value());
    }
}
