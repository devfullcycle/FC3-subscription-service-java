package com.fullcycle.codeflix.subscription.infrastructure.subscription.controllers;

import com.fullcycle.codeflix.subscription.application.subscription.ActivateSubscription;
import com.fullcycle.codeflix.subscription.infrastructure.subscription.SubscriptionRestAPI;
import com.fullcycle.codeflix.subscription.infrastructure.subscription.commands.CreateSubscriptionInput;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class SubscriptionRestController implements SubscriptionRestAPI {

    private final ActivateSubscription activateSubscription;

    public SubscriptionRestController(final ActivateSubscription activateSubscription) {
        this.activateSubscription = Objects.requireNonNull(activateSubscription);
    }

    @PostMapping
    public ResponseEntity<ActivateSubscription.Output> createSubscription(@RequestBody @Valid CreateSubscriptionInput cmd) {
        final var output = this.activateSubscription.execute(cmd);
        return ResponseEntity.created(URI.create("/subscriptions/%s".formatted(output.subscriptionId()))).body(output);
    }
}
