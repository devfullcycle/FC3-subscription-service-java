package com.fullcycle.codeflix.subscription.domain.iam;

import com.fullcycle.codeflix.subscription.domain.ValueObject;

public record GroupId(String value) implements ValueObject {
    public GroupId {
        this.assertArgumentNotNull(value, "'group id' should not be null");
    }
}
