package com.fullcycle.subscription.infrastructure.rest.models.res;

import com.fullcycle.subscription.application.plan.CreatePlan;
import com.fullcycle.subscription.domain.AssertionConcern;

public record CreatePlanResponse(Long planId) implements AssertionConcern {

    public CreatePlanResponse {
        this.assertArgumentNotNull(planId, "CreatePlanResponse 'planId' should not be null");
    }

    public CreatePlanResponse(CreatePlan.Output out) {
        this(out.planId().value());
    }
}
