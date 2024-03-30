package com.fullcycle.codeflix.subscription.infrastructure.configuration.usecases;

import com.fullcycle.codeflix.subscription.application.account.CreateAccount;
import com.fullcycle.codeflix.subscription.application.account.impl.DefaultCreateAccount;
import com.fullcycle.codeflix.subscription.domain.account.AccountGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class UserUseCases {

    @Bean
    CreateAccount signUp(final AccountGateway accountGateway) {
        return new DefaultCreateAccount(accountGateway);
    }
}
