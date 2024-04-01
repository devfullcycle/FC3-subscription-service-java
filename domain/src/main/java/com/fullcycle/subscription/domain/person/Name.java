package com.fullcycle.subscription.domain.person;

import com.fullcycle.subscription.domain.ValueObject;

public record Name(String firstname, String lastname) implements ValueObject {

    public Name {
        this.assertArgumentNotEmpty(firstname, "'firstname' should not be empty");
        this.assertArgumentNotEmpty(lastname, "'lastname' should not be empty");
    }
}
