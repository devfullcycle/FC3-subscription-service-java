package com.fullcycle.subscription.infrastructure.gateway.client;

import com.fullcycle.subscription.domain.payment.Payment;
import com.fullcycle.subscription.domain.payment.PaymentGateway;
import com.fullcycle.subscription.domain.payment.Transaction;
import com.fullcycle.subscription.domain.utils.IdUtils;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class PagarmeClient implements PaymentGateway {

    @Override
    public Transaction processPayment(Payment payment) {
        if (LocalTime.now().getMinute() %2 == 0) {
            return Transaction.success(IdUtils.uniqueId());
        } else {
            return Transaction.failure(IdUtils.uniqueId(), "Not enough funds");
        }
    }
}
