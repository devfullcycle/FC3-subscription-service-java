package com.fullcycle.subscription.application.subscription;

import com.fullcycle.subscription.application.UseCase;
import com.fullcycle.subscription.domain.payment.Transaction;
import com.fullcycle.subscription.domain.subscription.SubscriptionId;

import java.time.LocalDate;

public abstract class ChargeSubscription extends UseCase<ChargeSubscription.Input, ChargeSubscription.Output> {

    public interface Input {
        String accountId();
        String subscriptionId();
        String paymentType();
        String creditCardToken();
    }

    public interface Output {
        SubscriptionId subscriptionId();
        String subscriptionStatus();
        LocalDate subscriptionDueDate();
        Transaction paymentTransaction();
    }
}
