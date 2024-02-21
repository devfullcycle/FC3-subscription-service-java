package com.fullcycle.codeflix.subscription.domain.person;

import com.fullcycle.codeflix.subscription.domain.ValueObject;

public record Address(String zipcode, String number, String complement, String country) implements ValueObject {

    public Address {
        this.assertArgumentNotEmpty(zipcode, "'zipcode' is required");
        this.assertArgumentNotEmpty(number, "'number' is required");
        this.assertArgumentNotEmpty(country, "'country' is required");
    }
}
