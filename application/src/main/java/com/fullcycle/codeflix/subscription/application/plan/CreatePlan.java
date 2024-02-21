package com.fullcycle.codeflix.subscription.application.plan;

import com.fullcycle.codeflix.subscription.application.UseCase;

public class CreatePlan extends UseCase<CreatePlan.Input, CreatePlan.Output> {

    @Override
    public Output execute(final Input input) {
        return null;
    }

    public record Input() {}

    public record Output() {}
}
