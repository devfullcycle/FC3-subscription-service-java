package com.fullcycle.codeflix.subscription.domain.payment;

import com.fullcycle.codeflix.subscription.domain.exceptions.NotificationException;
import com.fullcycle.codeflix.subscription.domain.validation.Error;
import com.fullcycle.codeflix.subscription.domain.validation.handler.Notification;

public record BillingAddress(
        String zipcode,
        String number,
        String complement,
        String country
) {

    public BillingAddress {
        var n = Notification.create();

        if (zipcode == null || zipcode.isBlank()) {
            n.append(new Error("'zipcode' should not be empty"));
        }

        if (number == null || number.isBlank()) {
            n.append(new Error("'number' should not be empty"));
        }

        if (complement == null || complement.isBlank()) {
            n.append(new Error("'complement' should not be empty"));
        }

        if (country == null || country.isBlank()) {
            n.append(new Error("'country' should not be empty"));
        }

        if (n.hasError()) {
            throw NotificationException.with("Invalid address", n);
        }
    }
}
