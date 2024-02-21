package com.fullcycle.codeflix.subscription.domain.payment;

import com.fullcycle.codeflix.subscription.domain.AssertionConcern;

public class CreditCardPayment implements Payment, AssertionConcern {

    private final double amount;
    private final String orderId;
    private final BillingAddress address;

    public CreditCardPayment(
            final Double amount,
            final String orderId,
            final String zipcode,
            final String number,
            final String complement,
            final String country
    ) {
        this.amount = amount;
        this.orderId = orderId;
        this.address = new BillingAddress(zipcode, number, complement, country);
    }

    @Override
    public double amount() {
        return amount;
    }

    @Override
    public String orderId() {
        return orderId;
    }

    @Override
    public BillingAddress address() {
        return address;
    }
}
