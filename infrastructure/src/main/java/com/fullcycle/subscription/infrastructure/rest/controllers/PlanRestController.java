package com.fullcycle.subscription.infrastructure.rest.controllers;

import com.fullcycle.subscription.application.plan.CreatePlan;
import com.fullcycle.subscription.infrastructure.rest.PlanRestApi;
import com.fullcycle.subscription.infrastructure.rest.models.req.CreatePlanRequest;
import com.fullcycle.subscription.infrastructure.rest.models.res.CreatePlanResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class PlanRestController implements PlanRestApi {

    private final CreatePlan createPlan;

    public PlanRestController(final CreatePlan createPlan) {
        this.createPlan = Objects.requireNonNull(createPlan);
    }

    @Override
    public ResponseEntity<CreatePlanResponse> createPlan(final CreatePlanRequest req) {
        final var res = this.createPlan.execute(req, CreatePlanResponse::new);
        return ResponseEntity.created(URI.create("/plans/" + res.planId())).body(res);
    }
}
