package com.fullcycle.codeflix.subscription.domain.monetary;

import java.util.Currency;

public record MonetaryAmount(Double amount, Currency currency) {

    public MonetaryAmount(Double amount, String currency) {
        this(amount, Currency.getInstance(currency));
    }
}
