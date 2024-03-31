package com.fullcycle.codeflix.subscription.infrastructure.gateways.clients;

import com.fullcycle.codeflix.subscription.domain.payment.Payment;
import com.fullcycle.codeflix.subscription.domain.payment.PaymentGateway;
import com.fullcycle.codeflix.subscription.domain.payment.Transaction;
import com.fullcycle.codeflix.subscription.domain.utils.IdUtils;
import com.fullcycle.codeflix.subscription.domain.utils.InstantUtils;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoField;

@Component
public class StripePaymentGateway implements PaymentGateway {

    @Override
    public Transaction processPayment(final Payment payment) {
        final var minute = InstantUtils.now().get(ChronoField.MINUTE_OF_HOUR);
        if (minute / 2 == 0) {
            return Transaction.success(IdUtils.uniqueId());
        } else {
            return Transaction.failure(IdUtils.uniqueId(), "Nenhum saldo restante");
        }
    }
}
