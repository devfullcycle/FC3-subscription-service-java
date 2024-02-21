package com.fullcycle.codeflix.subscription.infrastructure.subscription.controllers;

import com.fullcycle.codeflix.subscription.application.subscription.CreateSubscription;
import com.fullcycle.codeflix.subscription.infrastructure.subscription.SubscriptionRestAPI;
import com.fullcycle.codeflix.subscription.infrastructure.subscription.commands.CreateSubscriptionCommand;
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
    public ResponseEntity<CreateSubscription.Output> createSubscription(@RequestBody @Valid CreateSubscriptionCommand cmd) {
        final var output = this.createSubscription.execute(cmd);
        return ResponseEntity.created(URI.create("/subscriptions/%s".formatted(output.subscriptionId()))).body(output);
    }
}
