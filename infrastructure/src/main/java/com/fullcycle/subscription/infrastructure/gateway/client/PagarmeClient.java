package com.fullcycle.subscription.infrastructure.gateway.client;

import com.fullcycle.subscription.domain.payment.Payment;
import com.fullcycle.subscription.domain.payment.PaymentGateway;
import com.fullcycle.subscription.domain.payment.Transaction;
import org.springframework.stereotype.Component;

@Component
public class PagarmeClient implements PaymentGateway {

    @Override
    public Transaction processPayment(Payment payment) {
        return null;
    }
}
