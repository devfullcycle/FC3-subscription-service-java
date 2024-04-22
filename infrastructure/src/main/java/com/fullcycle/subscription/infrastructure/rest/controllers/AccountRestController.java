package com.fullcycle.subscription.infrastructure.rest.controllers;

import com.fullcycle.subscription.infrastructure.mediator.SignUpMediator;
import com.fullcycle.subscription.infrastructure.rest.AccountRestApi;
import com.fullcycle.subscription.infrastructure.rest.models.req.SignUpRequest;
import com.fullcycle.subscription.infrastructure.rest.models.res.SignUpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class AccountRestController implements AccountRestApi {

    private final SignUpMediator signUpMediator;

    public AccountRestController(final SignUpMediator signUpMediator) {
        this.signUpMediator = Objects.requireNonNull(signUpMediator);
    }

    @Override
    public ResponseEntity<SignUpResponse> signUp(final SignUpRequest req) {
        final var res = this.signUpMediator.signUp(req);
        return ResponseEntity.created(URI.create("/accounts/%s".formatted(res.accountId()))).body(res);
    }
}
