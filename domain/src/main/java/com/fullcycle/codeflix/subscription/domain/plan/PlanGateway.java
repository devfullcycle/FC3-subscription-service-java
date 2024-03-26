package com.fullcycle.codeflix.subscription.domain.plan;

import java.util.Collection;
import java.util.Optional;

public interface PlanGateway {

    PlanId nextId();

    Collection<Plan> allPlans();

    Optional<Plan> planOfId(PlanId planId);

    Plan save(Plan subscription);

}
