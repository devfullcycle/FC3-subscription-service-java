package com.fullcycle.codeflix.subscription.application.plan;

import com.fullcycle.codeflix.subscription.application.UseCase;

public abstract class CreatePlan extends UseCase<CreatePlan.Input, CreatePlan.Output> {

    public interface Input {
        String name();
        String description();
        Double price();
        String currency();
        String groupId();
        Boolean active();
    }

    public interface Output {
        String planId();
    }
}
