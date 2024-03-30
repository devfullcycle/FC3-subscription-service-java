package com.fullcycle.codeflix.subscription.domain.account.iam;

import com.fullcycle.codeflix.subscription.domain.ValueObject;

public record IAMUserId(String value) implements ValueObject {
    public IAMUserId {
        this.assertArgumentNotNull(value, "'iam user id' should not be null");
    }
}
