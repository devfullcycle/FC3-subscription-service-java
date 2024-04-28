package com.fullcycle.subscription.infrastructure.rest.controllers;

import com.fullcycle.subscription.application.plan.ChangePlan;
import com.fullcycle.subscription.application.plan.CreatePlan;
import com.fullcycle.subscription.domain.exceptions.DomainException;
import com.fullcycle.subscription.infrastructure.rest.PlanRestApi;
import com.fullcycle.subscription.infrastructure.rest.models.req.ChangePlanRequest;
import com.fullcycle.subscription.infrastructure.rest.models.req.CreatePlanRequest;
import com.fullcycle.subscription.infrastructure.rest.models.res.ChangePlanResponse;
import com.fullcycle.subscription.infrastructure.rest.models.res.CreatePlanResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class PlanRestController implements PlanRestApi {

    private final CreatePlan createPlan;
    private final ChangePlan changePlan;

    public PlanRestController(final CreatePlan createPlan, final ChangePlan changePlan) {
        this.createPlan = Objects.requireNonNull(createPlan);
        this.changePlan = Objects.requireNonNull(changePlan);
    }

    @Override
    public ResponseEntity<CreatePlanResponse> createPlan(final CreatePlanRequest req) {
        final var res = this.createPlan.execute(req, CreatePlanResponse::new);
        return ResponseEntity.created(URI.create("/plans/" + res.planId())).body(res);
    }

    @Override
    public ResponseEntity<ChangePlanResponse> changePlan(final Long planId, final ChangePlanRequest req) {
        if (!Objects.equals(req.planId(), planId)) {
            throw DomainException.with("Plan identifier doesn't matches");
        }
        return ResponseEntity.ok().body(this.changePlan.execute(req, ChangePlanResponse::new));
    }
}
