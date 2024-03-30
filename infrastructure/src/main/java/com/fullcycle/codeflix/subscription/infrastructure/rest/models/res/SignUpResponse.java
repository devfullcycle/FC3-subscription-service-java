package com.fullcycle.codeflix.subscription.infrastructure.rest.models.res;

import com.fullcycle.codeflix.subscription.application.account.CreateAccount;
import com.fullcycle.codeflix.subscription.domain.AssertionConcern;

public record SignUpResponse(String userId) implements AssertionConcern {

    public SignUpResponse {
        this.assertArgumentNotEmpty(userId, "'userId' should not be empty");
    }

    public SignUpResponse(final CreateAccount.Output out) {
        this(out.accountId());
    }
}
