package com.fullcycle.codeflix.subscription.domain.payment;

public interface Payment {

    double amount();

    String orderId();

    BillingAddress address();

    static Payment create(
            final String paymentType,
            final Double amount,
            final String orderId,
            final String zipcode,
            final String number,
            final String complement,
            final String country
    ) {
        return PaymentFactory.create(paymentType, amount, orderId, zipcode, number, complement, country);
    }
}
