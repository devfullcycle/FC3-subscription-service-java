package com.fullcycle.subscription.application.plan.impl;

import com.fullcycle.subscription.application.plan.ChangePlan;
import com.fullcycle.subscription.domain.exceptions.DomainException;
import com.fullcycle.subscription.domain.money.Money;
import com.fullcycle.subscription.domain.plan.PlanCommand;
import com.fullcycle.subscription.domain.plan.PlanGateway;
import com.fullcycle.subscription.domain.plan.PlanId;

import java.util.Objects;

public class DefaultChangePlan extends ChangePlan {

    private final PlanGateway planGateway;

    public DefaultChangePlan(final PlanGateway planGateway) {
        this.planGateway = Objects.requireNonNull(planGateway);
    }

    @Override
    public ChangePlan.Output execute(final ChangePlan.Input in) {
        final var aPlan = this.planGateway.planOfId(new PlanId(in.planId()))
                .orElseThrow(() -> DomainException.with("Plan with id %s could not be found".formatted(in.planId())));

        aPlan.execute(new PlanCommand.ChangePlan(in.name(), in.description(), new Money(in.currency(), in.price()), in.active()));
        this.planGateway.save(aPlan);
        return new StdOutput(aPlan.id());
    }

    record StdOutput(PlanId planId) implements Output {}
}
