package com.fullcycle.codeflix.subscription.domain.subscription.billing;

import com.fullcycle.codeflix.subscription.domain.ValueObject;

import java.time.LocalDate;

public record Period(LocalDate start, LocalDate end) implements ValueObject {
    public Period {
        this.assertArgumentNotNull(start, "'start' should not be null");
        this.assertArgumentNotNull(start, "'end' should not be null");
    }
}
