package com.fullcycle.subscription.domain.plan;

public sealed interface PlanCommand {

    record ChangePlan(String name, String description, Boolean active) implements PlanCommand {

    }

    record InactivatePlan() implements PlanCommand {

    }

    record ActivatePlan() implements PlanCommand {

    }
}
