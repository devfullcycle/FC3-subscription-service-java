package com.fullcycle.subscription.infrastructure.rest.models.res;

import com.fullcycle.subscription.application.plan.ChangePlan;
import com.fullcycle.subscription.domain.AssertionConcern;

public record ChangePlanResponse(Long planId) implements AssertionConcern {

    public ChangePlanResponse {
        this.assertArgumentNotNull(planId, "ChangePlanResponse 'planId' should not be null");
    }

    public ChangePlanResponse(ChangePlan.Output out) {
        this(out.planId().value());
    }
}
