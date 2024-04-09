package com.fullcycle.subscription.domain.money;

import com.fullcycle.subscription.domain.ValueObject;

import java.util.Currency;

public record Money(Currency currency, Double amount) implements ValueObject {

    public Money {
        this.assertArgumentNotNull(currency, "'currency' should not be null");
        this.assertArgumentNotNull(amount, "'amount' should not be null");
    }

    public Money(String currency, Double amount) {
        this(Currency.getInstance(currency), amount);
    }
}
