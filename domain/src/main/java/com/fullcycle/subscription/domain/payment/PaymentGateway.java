package com.fullcycle.subscription.domain.payment;

public interface PaymentGateway {
    Transaction processPayment(Payment payment);
}
