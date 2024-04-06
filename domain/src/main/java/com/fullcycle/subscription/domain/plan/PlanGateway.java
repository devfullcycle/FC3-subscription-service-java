package com.fullcycle.subscription.domain.plan;

import java.util.List;
import java.util.Optional;

public interface PlanGateway {

    PlanId nextId();

    Optional<Plan> planOfId(final PlanId anId);

    List<Plan> allPlans();

    boolean existsPlanOfId(final PlanId anId);

    Plan save(Plan plan);
}
