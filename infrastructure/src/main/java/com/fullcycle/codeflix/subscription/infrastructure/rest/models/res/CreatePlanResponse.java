package com.fullcycle.codeflix.subscription.infrastructure.rest.models.res;

import com.fullcycle.codeflix.subscription.application.plan.CreatePlan;
import com.fullcycle.codeflix.subscription.domain.AssertionConcern;

public record CreatePlanResponse(String planId) implements AssertionConcern {

    public CreatePlanResponse {
        this.assertArgumentNotEmpty(planId, "'planId' is required");
    }

    public CreatePlanResponse(final CreatePlan.Output out) {
        this(out.planId());
    }
}
