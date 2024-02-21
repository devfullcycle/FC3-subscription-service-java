package com.fullcycle.codeflix.subscription.domain.payment;

public final class PaymentFactory {

    public static Payment create(
            final String paymentType,
            final Double amount,
            final String orderId,
            final String zipcode,
            final String number,
            final String complement,
            final String country
    ) {
        return switch (paymentType) {
            case "pix" -> new PixPayment(amount, orderId, zipcode, number, complement, country);
            case "credit_card" -> new CreditCardPayment(amount, orderId, zipcode, number, complement, country);
            default -> throw new IllegalArgumentException("");
        };
    }
}
