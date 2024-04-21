package com.fullcycle.subscription.domain.payment;

import com.fullcycle.subscription.domain.AssertionConcern;
import com.fullcycle.subscription.domain.exceptions.DomainException;

public sealed interface Payment extends AssertionConcern permits CreditCardPayment, PixPayment {
    String PIX = "pix";
    String CREDIT_CARD = "credit_card";

    Double amount();
    String orderId();
    BillingAddress address();

    static Payment create(final String type, final String orderId, final Double amount, final BillingAddress address, final String token) {
        return switch (type) {
            case PIX -> new PixPayment(amount, orderId, address);
            case CREDIT_CARD -> new CreditCardPayment(amount, orderId, token, address);
            default -> throw DomainException.with("Invalid payment type: " + type);
        };
    }
}
