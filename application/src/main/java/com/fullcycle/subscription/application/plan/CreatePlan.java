package com.fullcycle.subscription.application.plan;

import com.fullcycle.subscription.application.UseCase;
import com.fullcycle.subscription.domain.plan.PlanId;

public abstract class CreatePlan extends UseCase<CreatePlan.Input, CreatePlan.Output> {

    public interface Input {
        String name();
        String description();
        Double price();
        String currency();
        Boolean active();
    }

    public interface Output {
        PlanId planId();
    }
}
