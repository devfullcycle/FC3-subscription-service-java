package com.fullcycle.codeflix.subscription.domain.user;

import com.fullcycle.codeflix.subscription.domain.exceptions.NotificationException;
import com.fullcycle.codeflix.subscription.domain.validation.Error;
import com.fullcycle.codeflix.subscription.domain.validation.handler.Notification;

public record Address(
        String street,
        String number,
        String complement,
        String neighborhood,
        String zipcode,
        String city,
        String state,
        String country
) {

    public Address {
        var n = Notification.create();

        if (street == null || street.isBlank()) {
            n.append(new Error("'street' should not be empty"));
        }

        if (number == null || number.isBlank()) {
            n.append(new Error("'number' should not be empty"));
        }

        if (complement == null || complement.isBlank()) {
            n.append(new Error("'complement' should not be empty"));
        }

        if (neighborhood == null || neighborhood.isBlank()) {
            n.append(new Error("'neighborhood' should not be empty"));
        }

        if (zipcode == null || zipcode.isBlank()) {
            n.append(new Error("'zipcode' should not be empty"));
        }

        if (city == null || city.isBlank()) {
            n.append(new Error("'city' should not be empty"));
        }

        if (state == null || state.isBlank()) {
            n.append(new Error("'state' should not be empty"));
        }

        if (country == null || country.isBlank()) {
            n.append(new Error("'country' should not be empty"));
        }

        if (n.hasError()) {
            throw NotificationException.with("Invalid address", n);
        }
    }
}
