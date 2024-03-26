package com.fullcycle.codeflix.subscription.infrastructure.configuration.usecases;

import com.fullcycle.codeflix.subscription.application.iam.IamSignUp;
import com.fullcycle.codeflix.subscription.application.iam.impl.DefaultIamSignUp;
import com.fullcycle.codeflix.subscription.domain.iam.IdentityGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class IamUseCases {

    @Bean
    IamSignUp iamSignUp(final IdentityGateway identityGateway) {
        return new DefaultIamSignUp(identityGateway);
    }
}
