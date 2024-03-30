package com.fullcycle.codeflix.subscription.domain.person;

import com.fullcycle.codeflix.subscription.domain.ValueObject;

public record Nickname(String value) implements ValueObject {

    public Nickname {
        this.assertArgumentNotEmpty(value, "'nickname' should not be empty");
    }
}
