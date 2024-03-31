package com.fullcycle.codeflix.subscription.infrastructure.configuration.usecases;

import com.fullcycle.codeflix.subscription.application.subscription.CancelSubscription;
import com.fullcycle.codeflix.subscription.application.subscription.ChargeSubscription;
import com.fullcycle.codeflix.subscription.application.subscription.CreateSubscription;
import com.fullcycle.codeflix.subscription.application.subscription.impl.DefaultCancelSubscription;
import com.fullcycle.codeflix.subscription.application.subscription.impl.DefaultChargeSubscription;
import com.fullcycle.codeflix.subscription.application.subscription.impl.DefaultCreateSubscription;
import com.fullcycle.codeflix.subscription.domain.account.AccountGateway;
import com.fullcycle.codeflix.subscription.domain.payment.PaymentGateway;
import com.fullcycle.codeflix.subscription.domain.plan.PlanGateway;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration(proxyBeanMethods = false)
public class SubscriptionUseCases {

    @Bean
    CancelSubscription cancelSubscription(final SubscriptionGateway subscriptionGateway) {
        return new DefaultCancelSubscription(subscriptionGateway);
    }

    @Bean
    ChargeSubscription chargeSubscription(
            final AccountGateway accountGateway,
            final Clock clock,
            final PaymentGateway paymentGateway,
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        return new DefaultChargeSubscription(accountGateway, clock, paymentGateway, planGateway, subscriptionGateway);
    }

    @Bean
    CreateSubscription createSubscription(
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway,
            final AccountGateway accountGateway
    ) {
        return new DefaultCreateSubscription(planGateway, subscriptionGateway, accountGateway);
    }
}
