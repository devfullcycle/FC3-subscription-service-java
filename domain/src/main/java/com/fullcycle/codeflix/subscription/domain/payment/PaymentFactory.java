package com.fullcycle.codeflix.subscription.domain.payment;

public final class PaymentFactory {

    public static Payment create(
            final String paymentType,
            final Double amount,
            final String orderId,
            final BillingAddress billingAddress
    ) {
        return switch (paymentType) {
            case "pix" -> new PixPayment(amount, orderId, billingAddress);
            case "credit_card" -> new CreditCardPayment(amount, orderId, billingAddress);
            default -> throw new IllegalArgumentException("");
        };
    }
}
