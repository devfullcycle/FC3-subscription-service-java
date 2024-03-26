package com.fullcycle.codeflix.subscription.application.plan.impl;

import com.fullcycle.codeflix.subscription.application.plan.CreatePlan;
import com.fullcycle.codeflix.subscription.domain.plan.Plan;
import com.fullcycle.codeflix.subscription.domain.plan.PlanGateway;
import com.fullcycle.codeflix.subscription.domain.plan.PlanId;

import java.util.Objects;

public class DefaultCreatePlan extends CreatePlan {

    private final PlanGateway planGateway;

    public DefaultCreatePlan(final PlanGateway planGateway) {
        this.planGateway = Objects.requireNonNull(planGateway);
    }

    @Override
    public Output execute(final Input in) {
        final var aPlan = newPlanWith(in);
        this.planGateway.save(aPlan);
        return new StdOutput(aPlan.id());
    }

    private Plan newPlanWith(final Input in) {
        final var planId = this.planGateway.nextId();
        return Plan.with(
                planId,
                in.name(),
                in.description(),
                in.groupId(),
                in.active(),
                in.currency(),
                in.price()
        );
    }

    record StdOutput(String planId) implements Output {
        public StdOutput(PlanId id) {
            this(id.value());
        }
    }
}
