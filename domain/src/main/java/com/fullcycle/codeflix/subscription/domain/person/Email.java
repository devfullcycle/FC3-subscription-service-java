package com.fullcycle.codeflix.subscription.domain.person;

import com.fullcycle.codeflix.subscription.domain.ValueObject;

public record Email(String value) implements ValueObject {

    public Email {
        this.assertArgumentNotEmpty(value, "'email' should not be empty");
    }
}
