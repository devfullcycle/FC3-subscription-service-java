package com.fullcycle.subscription.infrastructure.configuration.usecases;

import com.fullcycle.subscription.application.subscription.CancelSubscription;
import com.fullcycle.subscription.application.subscription.ChargeSubscription;
import com.fullcycle.subscription.application.subscription.CreateSubscription;
import com.fullcycle.subscription.application.subscription.impl.DefaultCancelSubscription;
import com.fullcycle.subscription.application.subscription.impl.DefaultChargeSubscription;
import com.fullcycle.subscription.application.subscription.impl.DefaultCreateSubscription;
import com.fullcycle.subscription.domain.account.AccountGateway;
import com.fullcycle.subscription.domain.payment.PaymentGateway;
import com.fullcycle.subscription.domain.plan.PlanGateway;
import com.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration(proxyBeanMethods = false)
public class SubscriptionUseCaseConfig {

    @Bean
    CreateSubscription createSubscription(
            final AccountGateway accountGateway,
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        return new DefaultCreateSubscription(accountGateway, planGateway, subscriptionGateway);
    }

    @Bean
    CancelSubscription cancelSubscription(
            final SubscriptionGateway subscriptionGateway
    ) {
        return new DefaultCancelSubscription(subscriptionGateway);
    }

    @Bean
    ChargeSubscription chargeSubscription(
            final Clock clock,
            final PaymentGateway paymentGateway,
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        return new DefaultChargeSubscription(clock, paymentGateway, planGateway, subscriptionGateway);
    }
}
