package com.fullcycle.codeflix.subscription.domain.payment;

import com.fullcycle.codeflix.subscription.domain.ValueObject;

public record BillingAddress(String zipcode, String number, String complement, String country) implements ValueObject {

    public BillingAddress {
        this.assertArgumentNotEmpty(zipcode, "'zipcode' is required");
        this.assertArgumentNotEmpty(number, "'number' is required");
        this.assertArgumentNotEmpty(complement, "'complement' is required");
        this.assertArgumentNotEmpty(country, "'country' is required");
    }
}
