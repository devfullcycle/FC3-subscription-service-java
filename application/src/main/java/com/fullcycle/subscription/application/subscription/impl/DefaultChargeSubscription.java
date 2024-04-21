package com.fullcycle.subscription.application.subscription.impl;

import com.fullcycle.subscription.application.subscription.ChargeSubscription;
import com.fullcycle.subscription.domain.account.Account;
import com.fullcycle.subscription.domain.account.AccountGateway;
import com.fullcycle.subscription.domain.account.AccountId;
import com.fullcycle.subscription.domain.exceptions.DomainException;
import com.fullcycle.subscription.domain.payment.BillingAddress;
import com.fullcycle.subscription.domain.payment.Payment;
import com.fullcycle.subscription.domain.payment.PaymentGateway;
import com.fullcycle.subscription.domain.payment.Transaction;
import com.fullcycle.subscription.domain.plan.Plan;
import com.fullcycle.subscription.domain.plan.PlanGateway;
import com.fullcycle.subscription.domain.subscription.Subscription;
import com.fullcycle.subscription.domain.subscription.SubscriptionCommand;
import com.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import com.fullcycle.subscription.domain.subscription.SubscriptionId;
import com.fullcycle.subscription.domain.utils.IdUtils;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DefaultChargeSubscription extends ChargeSubscription {

    private static final int MAX_INCOMPLETE_DAYS = 2;

    private final AccountGateway accountGateway;
    private final Clock clock;
    private final PaymentGateway paymentGateway;
    private final PlanGateway planGateway;
    private final SubscriptionGateway subscriptionGateway;

    public DefaultChargeSubscription(
            final AccountGateway accountGateway,
            final Clock clock,
            final PaymentGateway paymentGateway,
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.clock = Objects.requireNonNull(clock);
        this.paymentGateway = Objects.requireNonNull(paymentGateway);
        this.planGateway = Objects.requireNonNull(planGateway);
        this.subscriptionGateway = Objects.requireNonNull(subscriptionGateway);
    }

    @Override
    public ChargeSubscription.Output execute(final ChargeSubscription.Input in) {
        if (in == null) {
            throw new IllegalArgumentException("Input of DefaultChargeSubscription cannot be null");
        }

        final var accountId = new AccountId(in.accountId());
        final var subscriptionId = new SubscriptionId(in.subscriptionId());
        final var now = clock.instant();

        final var aSubscription = subscriptionGateway.subscriptionOfId(subscriptionId)
                .filter(it -> it.accountId().equals(accountId))
                .orElseThrow(() -> DomainException.notFound(Subscription.class, subscriptionId));

        if (aSubscription.dueDate().isAfter(LocalDate.ofInstant(now, ZoneId.systemDefault()))) {
            return new StdOutput(subscriptionId, aSubscription.status().value(), aSubscription.dueDate(), null);
        }

        final var aPlan = this.planGateway.planOfId(aSubscription.planId())
                .orElseThrow(() -> DomainException.notFound(Plan.class, aSubscription.planId()));

        final var anUserAccount = this.accountGateway.accountOfId(accountId)
                .orElseThrow(() -> DomainException.notFound(Account.class, accountId));

        final var aPayment = this.newPaymentWith(in, aPlan, anUserAccount);
        final var actualTransaction = this.paymentGateway.processPayment(aPayment);

        if (actualTransaction.isSuccess()) {
            aSubscription.execute(new SubscriptionCommand.RenewSubscription(aPlan, actualTransaction.transactionId()));
        } else if (hasTolerableDays(aSubscription.dueDate(), now)) {
            aSubscription.execute(new SubscriptionCommand.IncompleteSubscription(actualTransaction.errorMessage(), actualTransaction.transactionId()));
        } else {
            aSubscription.execute(new SubscriptionCommand.CancelSubscription());
        }

        this.subscriptionGateway.save(aSubscription);
        return new StdOutput(subscriptionId, aSubscription.status().value(), aSubscription.dueDate(), actualTransaction);
    }

    private boolean hasTolerableDays(final LocalDate dueDate, final Instant now) {
        return ChronoUnit.DAYS.between(dueDate, LocalDate.ofInstant(now, ZoneOffset.UTC)) <= MAX_INCOMPLETE_DAYS;
    }

    private Payment newPaymentWith(final Input in, final Plan aPlan, final Account anUserAccount) {
        return Payment.create(
                in.paymentType(),
                IdUtils.uniqueId(),
                aPlan.price().amount(),
                new BillingAddress(
                        anUserAccount.billingAddress().zipcode(),
                        anUserAccount.billingAddress().number(),
                        anUserAccount.billingAddress().complement(),
                        anUserAccount.billingAddress().country()
                ),
                in.creditCardToken()
        );
    }

    record StdOutput(
            SubscriptionId subscriptionId,
            String subscriptionStatus,
            LocalDate subscriptionDueDate,
            Transaction paymentTransaction
    ) implements ChargeSubscription.Output {

    }
}
