package com.fullcycle.codeflix.subscription.domain.payment;

public interface Payment {

    double amount();

    String orderId();

    BillingAddress address();

    static Payment create(
            final String paymentType,
            final Double amount,
            final String orderId,
            final BillingAddress billingAddress
    ) {
        return PaymentFactory.create(paymentType, amount, orderId, billingAddress);
    }
}
