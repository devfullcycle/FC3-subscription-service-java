package com.fullcycle.codeflix.subscription.domain.user;

import com.fullcycle.codeflix.subscription.domain.ValueObject;

public record Address(
        String street,
        String number,
        String complement,
        String neighborhood,
        String zipcode,
        String city,
        String state,
        String country
) implements ValueObject {

    public Address {
        this.assertArgumentNotEmpty(street, "'street' should not be empty");
        this.assertArgumentNotEmpty(street, "'number' should not be empty");
        this.assertArgumentNotEmpty(street, "'complement' should not be empty");
        this.assertArgumentNotEmpty(street, "'neighborhood' should not be empty");
        this.assertArgumentNotEmpty(street, "'zipcode' should not be empty");
        this.assertArgumentNotEmpty(street, "'city' should not be empty");
        this.assertArgumentNotEmpty(street, "'state' should not be empty");
        this.assertArgumentNotEmpty(street, "'country' should not be empty");
    }
}
