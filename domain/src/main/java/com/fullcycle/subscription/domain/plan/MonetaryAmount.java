package com.fullcycle.subscription.domain.plan;

import com.fullcycle.subscription.domain.ValueObject;

import java.util.Currency;

public record MonetaryAmount(Currency currency, Double amount) implements ValueObject {

    public MonetaryAmount {
        this.assertArgumentNotNull(currency, "'currency' should not be null");
        this.assertArgumentNotNull(amount, "'amount' should not be null");
    }

    public MonetaryAmount(String currency, Double amount) {
        this(Currency.getInstance(currency), amount);
    }
}
