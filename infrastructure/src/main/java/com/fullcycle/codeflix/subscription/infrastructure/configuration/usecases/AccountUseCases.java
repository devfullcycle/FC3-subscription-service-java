package com.fullcycle.codeflix.subscription.infrastructure.configuration.usecases;

import com.fullcycle.codeflix.subscription.application.account.AddToSubscribersGroup;
import com.fullcycle.codeflix.subscription.application.account.CreateAccount;
import com.fullcycle.codeflix.subscription.application.account.CreateIamUser;
import com.fullcycle.codeflix.subscription.application.account.RemoveFromSubscribersGroup;
import com.fullcycle.codeflix.subscription.application.account.impl.DefaultAddToSubscribersGroup;
import com.fullcycle.codeflix.subscription.application.account.impl.DefaultCreateAccount;
import com.fullcycle.codeflix.subscription.application.account.impl.DefaultCreateIamUser;
import com.fullcycle.codeflix.subscription.application.account.impl.DefaultRemoveFromSubscribersGroup;
import com.fullcycle.codeflix.subscription.domain.account.AccountGateway;
import com.fullcycle.codeflix.subscription.domain.account.iam.IdentityGateway;
import com.fullcycle.codeflix.subscription.domain.plan.PlanGateway;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AccountUseCases {

    @Bean
    CreateAccount createAccount(final AccountGateway accountGateway) {
        return new DefaultCreateAccount(accountGateway);
    }

    @Bean
    CreateIamUser createIamUser(final IdentityGateway identityGateway) {
        return new DefaultCreateIamUser(identityGateway);
    }

    @Bean
    AddToSubscribersGroup addToSubscribersGroup(
            final AccountGateway accountGateway,
            final IdentityGateway identityGateway,
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        return new DefaultAddToSubscribersGroup(accountGateway, identityGateway, planGateway, subscriptionGateway);
    }

    @Bean
    RemoveFromSubscribersGroup removeFromSubscribersGroup(
            final AccountGateway accountGateway,
            final IdentityGateway identityGateway,
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        return new DefaultRemoveFromSubscribersGroup(accountGateway, identityGateway, planGateway, subscriptionGateway);
    }
}
