package com.fullcycle.codeflix.subscription.application.plan;

import com.fullcycle.codeflix.subscription.application.UseCase;
import com.fullcycle.codeflix.subscription.domain.plan.Plan;
import com.fullcycle.codeflix.subscription.domain.plan.PlanGateway;
import com.fullcycle.codeflix.subscription.domain.plan.PlanId;

import java.util.Objects;

public class CreatePlan extends UseCase<CreatePlan.Command, CreatePlan.Output> {

    private final PlanGateway planGateway;

    public CreatePlan(final PlanGateway planGateway) {
        this.planGateway = Objects.requireNonNull(planGateway);
    }

    @Override
    public Output execute(final Command cmd) {
        final var aPlan = Plan.newPlan(cmd.name(), cmd.description(), cmd.active());
        this.planGateway.save(aPlan);
        return new Output(aPlan.id());
    }

    public interface Command {
        String name();
        String description();
        boolean active();
    }

    public record Output(String planId) {
        public Output(PlanId id) {
            this(id.value());
        }
    }
}
