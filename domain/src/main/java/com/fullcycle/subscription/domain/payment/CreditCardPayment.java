package com.fullcycle.subscription.domain.payment;

public record CreditCardPayment(Double amount, String orderId, String token, BillingAddress address) implements Payment {

    public CreditCardPayment {
        this.assertArgumentNotNull(amount, "Payment 'amount' should not be null");
        this.assertArgumentNotEmpty(orderId, "Payment 'orderId' should not be empty");
        this.assertArgumentNotNull(address, "Payment 'address' should not be null");
        this.assertArgumentNotEmpty(token, "Payment 'token' should not be empty");
    }
}
