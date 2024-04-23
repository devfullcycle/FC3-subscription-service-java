package com.fullcycle.subscription.infrastructure.rest.controllers;

import com.fullcycle.subscription.application.subscription.CreateSubscription;
import com.fullcycle.subscription.infrastructure.rest.SubscriptionRestApi;
import com.fullcycle.subscription.infrastructure.rest.models.req.CreateSubscriptionRequest;
import com.fullcycle.subscription.infrastructure.rest.models.res.CreateSubscriptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;
import java.util.Objects;

@RestController
public class SubscriptionRestController implements SubscriptionRestApi {

    private final CreateSubscription createSubscription;

    public SubscriptionRestController(final CreateSubscription createSubscription) {
        this.createSubscription = Objects.requireNonNull(createSubscription);
    }

    @Override
    public ResponseEntity<CreateSubscriptionResponse> createSubscription(final CreateSubscriptionRequest req, final Principal principal) {
        record CreateSubscriptionInput(Long planId, String accountId) implements CreateSubscription.Input {}
        final var res = this.createSubscription.execute(new CreateSubscriptionInput(req.planId(), ""), CreateSubscriptionResponse::new);
        return ResponseEntity.created(URI.create("/subscriptions/" + res.subscriptionId())).body(res);
    }
}
