package com.fullcycle.codeflix.subscription.infrastructure.configuration.usecases;

import com.fullcycle.codeflix.subscription.application.subscription.*;
import com.fullcycle.codeflix.subscription.application.subscription.impl.*;
import com.fullcycle.codeflix.subscription.domain.iam.IdentityGateway;
import com.fullcycle.codeflix.subscription.domain.payment.PaymentGateway;
import com.fullcycle.codeflix.subscription.domain.plan.PlanGateway;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionGateway;
import com.fullcycle.codeflix.subscription.domain.user.UserGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SubscriptionUseCases {

    @Bean
    CancelSubscription cancelSubscription(final SubscriptionGateway subscriptionGateway) {
        return new DefaultCancelSubscription(subscriptionGateway);
    }

    @Bean
    ChargeSubscription chargeSubscription(
            final PaymentGateway paymentGateway,
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway,
            final UserGateway userGateway
    ) {
        return new DefaultChargeSubscription(paymentGateway, planGateway, subscriptionGateway, userGateway);
    }

    @Bean
    CreateSubscription createSubscription(
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway,
            final UserGateway userGateway
    ) {
        return new DefaultCreateSubscription(planGateway, subscriptionGateway, userGateway);
    }

    @Bean
    IncompleteSubscription incompleteSubscription(final SubscriptionGateway subscriptionGateway) {
        return new DefaultIncompleteSubscription(subscriptionGateway);
    }

    @Bean
    RenewedSubscription renewedSubscription(
            final IdentityGateway identityGateway,
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        return new DefaultRenewedSubscription(identityGateway, planGateway, subscriptionGateway);
    }
}
