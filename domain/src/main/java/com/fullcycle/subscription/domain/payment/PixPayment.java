package com.fullcycle.subscription.domain.payment;

public record PixPayment(Double amount, String orderId, BillingAddress address) implements Payment {

    public PixPayment {
        this.assertArgumentNotNull(amount, "Payment 'amount' should not be null");
        this.assertArgumentNotEmpty(orderId, "Payment 'orderId' should not be empty");
        this.assertArgumentNotNull(address, "Payment 'address' should not be null");
    }
}
