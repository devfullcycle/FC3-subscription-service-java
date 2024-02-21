package com.fullcycle.codeflix.subscription.domain.user;

import com.fullcycle.codeflix.subscription.domain.Identifier;

import java.util.Objects;

public record UserId(String value) implements Identifier {

    public UserId {
        Objects.requireNonNull(value);
    }
}
