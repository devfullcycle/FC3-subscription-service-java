package com.fullcycle.codeflix.subscription.infrastructure.rest.controllers;

import com.fullcycle.codeflix.subscription.application.subscription.CreateSubscription;
import com.fullcycle.codeflix.subscription.infrastructure.rest.SubscriptionRestAPI;
import com.fullcycle.codeflix.subscription.infrastructure.rest.models.req.CreateSubscriptionRequest;
import com.fullcycle.codeflix.subscription.infrastructure.rest.models.res.CreateSubscriptionResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class SubscriptionRestController implements SubscriptionRestAPI {

    private final CreateSubscription createSubscription;

    public SubscriptionRestController(final CreateSubscription createSubscription) {
        this.createSubscription = Objects.requireNonNull(createSubscription);
    }

    @PostMapping
    public ResponseEntity<CreateSubscriptionResponse> createSubscription(@RequestBody @Valid CreateSubscriptionRequest cmd) {
        final var output = this.createSubscription.execute(cmd, CreateSubscriptionResponse::new);
        return ResponseEntity.created(URI.create("/subscriptions/%s".formatted(output.subscriptionId()))).body(output);
    }
}
