package com.fullcycle.subscription.infrastructure.configuration.usecases;

import com.fullcycle.subscription.application.plan.ChangePlan;
import com.fullcycle.subscription.application.plan.CreatePlan;
import com.fullcycle.subscription.application.plan.impl.DefaultChangePlan;
import com.fullcycle.subscription.application.plan.impl.DefaultCreatePlan;
import com.fullcycle.subscription.domain.plan.PlanGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class PlanUseCaseConfig {

    @Bean
    CreatePlan createPlan(PlanGateway planGateway) {
        return new DefaultCreatePlan(planGateway);
    }

    @Bean
    ChangePlan changePlan(PlanGateway planGateway) {
        return new DefaultChangePlan(planGateway);
    }
}
