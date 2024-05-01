package com.fullcycle.subscription.infrastructure.rest.controllers;

import com.fullcycle.subscription.application.subscription.CancelSubscription;
import com.fullcycle.subscription.application.subscription.ChargeSubscription;
import com.fullcycle.subscription.application.subscription.CreateSubscription;
import com.fullcycle.subscription.infrastructure.authentication.principal.CodeflixUser;
import com.fullcycle.subscription.infrastructure.rest.SubscriptionRestApi;
import com.fullcycle.subscription.infrastructure.rest.models.req.ChargeSubscriptionRequest;
import com.fullcycle.subscription.infrastructure.rest.models.req.CreateSubscriptionRequest;
import com.fullcycle.subscription.infrastructure.rest.models.res.CancelSubscriptionResponse;
import com.fullcycle.subscription.infrastructure.rest.models.res.ChargeSubscriptionResponse;
import com.fullcycle.subscription.infrastructure.rest.models.res.CreateSubscriptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

/**
 * 1. Tornar o ID do IDP (Keycloak) o mesmo ID do usuário interno da aplicação (Account ID)
 * 2. Descobrir o Account ID com base no Subject do JWT
 * 3. Colocar o Account ID dentro dos claims do JWT
 */
@RestController
public class SubscriptionRestController implements SubscriptionRestApi {

    private final CreateSubscription createSubscription;
    private final CancelSubscription cancelSubscription;
    private final ChargeSubscription chargeSubscription;

    public SubscriptionRestController(
            final CreateSubscription createSubscription,
            final CancelSubscription cancelSubscription,
            final ChargeSubscription chargeSubscription
    ) {
        this.createSubscription = Objects.requireNonNull(createSubscription);
        this.cancelSubscription = Objects.requireNonNull(cancelSubscription);
        this.chargeSubscription = Objects.requireNonNull(chargeSubscription);
    }

    @Override
    public ResponseEntity<CreateSubscriptionResponse> createSubscription(final CreateSubscriptionRequest req, final CodeflixUser principal) {
        record CreateSubscriptionInput(Long planId, String accountId) implements CreateSubscription.Input {}
        final var res = this.createSubscription.execute(new CreateSubscriptionInput(req.planId(), principal.accountId()), CreateSubscriptionResponse::new);
        return ResponseEntity.created(URI.create("/subscriptions/" + res.subscriptionId())).body(res);
    }

    @Override
    public ResponseEntity<CancelSubscriptionResponse> cancelSubscription(final CodeflixUser principal) {
        record CancelSubscriptionInput(String accountId) implements CancelSubscription.Input {}
        final var res = this.cancelSubscription.execute(new CancelSubscriptionInput(principal.accountId()), CancelSubscriptionResponse::new);
        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<ChargeSubscriptionResponse> chargeActiveSubscription(final ChargeSubscriptionRequest req, final CodeflixUser principal) {
        record ChargeSubscriptionInput(String accountId, String paymentType, String creditCardToken, ChargeSubscriptionRequest.BillingAddress billingAddress)
                implements ChargeSubscription.Input {}

        final var res = this.chargeSubscription.execute(new ChargeSubscriptionInput(principal.accountId(), req.paymentType(), req.creditCardToken(), req.billingAddress()), ChargeSubscriptionResponse::new);
        return ResponseEntity.ok(res);
    }
}
