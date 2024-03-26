package com.fullcycle.codeflix.subscription.infrastructure.rest.controllers;

import com.fullcycle.codeflix.subscription.application.plan.CreatePlan;
import com.fullcycle.codeflix.subscription.infrastructure.rest.PlanRestAPI;
import com.fullcycle.codeflix.subscription.infrastructure.rest.models.req.CreatePlanRequest;
import com.fullcycle.codeflix.subscription.infrastructure.rest.models.res.CreatePlanResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class PlanRestController implements PlanRestAPI {

    private final CreatePlan createPlan;

    public PlanRestController(final CreatePlan createPlan) {
        this.createPlan = Objects.requireNonNull(createPlan);
    }

    @PostMapping
    public ResponseEntity<CreatePlanResponse> createPlan(@RequestBody @Valid CreatePlanRequest cmd) {
        final var output = this.createPlan.execute(cmd, CreatePlanResponse::new);
        return ResponseEntity.created(URI.create("/plans/%s".formatted(output.planId()))).body(output);
    }
}
