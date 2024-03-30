package com.fullcycle.codeflix.subscription.infrastructure.configuration.usecases;

import com.fullcycle.codeflix.subscription.application.account.CreateIamUser;
import com.fullcycle.codeflix.subscription.application.account.impl.DefaultCreateIamUser;
import com.fullcycle.codeflix.subscription.domain.account.iam.IdentityGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class IamUseCases {

    @Bean
    CreateIamUser iamSignUp(final IdentityGateway identityGateway) {
        return new DefaultCreateIamUser(identityGateway);
    }
}
