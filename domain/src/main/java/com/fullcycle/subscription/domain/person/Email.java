package com.fullcycle.subscription.domain.person;

import com.fullcycle.subscription.domain.ValueObject;

public record Email(String value) implements ValueObject {

    public Email {
        this.assertArgumentNotEmpty(value, "'email' should not be empty");
    }
}
