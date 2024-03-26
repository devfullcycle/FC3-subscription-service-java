package com.fullcycle.codeflix.subscription.infrastructure.rest.models.req;

import com.fullcycle.codeflix.subscription.application.plan.CreatePlan;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreatePlanRequest(
        @NotEmpty String name,
        @NotEmpty String description,
        @NotNull Double price,
        @NotEmpty String currency,
        @NotEmpty String groupId,
        Boolean active
) implements CreatePlan.Input {

    @Override
    public Boolean active() {
        return active != null ? active : false;
    }
}
