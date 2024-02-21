package com.fullcycle.codeflix.subscription.domain.payment;

public interface PaymentGateway {
    Transaction processPayment(Payment payment);
}
