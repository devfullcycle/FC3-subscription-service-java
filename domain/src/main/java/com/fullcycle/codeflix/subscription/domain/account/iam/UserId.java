package com.fullcycle.codeflix.subscription.domain.account.iam;

import com.fullcycle.codeflix.subscription.domain.ValueObject;

public record UserId(String value) implements ValueObject {
    public UserId {
        this.assertArgumentNotNull(value, "'iam user id' should not be null");
    }
}
