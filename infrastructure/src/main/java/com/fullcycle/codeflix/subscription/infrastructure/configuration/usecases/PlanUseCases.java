package com.fullcycle.codeflix.subscription.infrastructure.configuration.usecases;

import com.fullcycle.codeflix.subscription.application.plan.CreatePlan;
import com.fullcycle.codeflix.subscription.application.plan.impl.DefaultCreatePlan;
import com.fullcycle.codeflix.subscription.domain.plan.PlanGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class PlanUseCases {

    @Bean
    CreatePlan createPlan(final PlanGateway planGateway) {
        return new DefaultCreatePlan(planGateway);
    }
}
