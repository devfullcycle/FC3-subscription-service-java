package com.fullcycle.subscription;

import com.fullcycle.subscription.domain.account.AccountGateway;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;

public class ControllerTestConfiguration {

    @Bean
    public AccountGateway accountGateway() {
        return Mockito.mock(AccountGateway.class);
    }
}
