package com.fullcycle.codeflix.subscription.infrastructure.configuration.usecases;

import com.fullcycle.codeflix.subscription.application.user.SignUp;
import com.fullcycle.codeflix.subscription.application.user.impl.DefaultSignUp;
import com.fullcycle.codeflix.subscription.domain.user.UserGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class UserUseCases {

    @Bean
    SignUp signUp(final UserGateway userGateway) {
        return new DefaultSignUp(userGateway);
    }
}
