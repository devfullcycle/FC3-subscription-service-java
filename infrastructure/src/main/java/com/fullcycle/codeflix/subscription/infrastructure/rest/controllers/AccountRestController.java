package com.fullcycle.codeflix.subscription.infrastructure.rest.controllers;

import com.fullcycle.codeflix.subscription.infrastructure.mediator.SignUpMediator;
import com.fullcycle.codeflix.subscription.infrastructure.rest.AccountRestAPI;
import com.fullcycle.codeflix.subscription.infrastructure.rest.models.req.SignUpRequest;
import com.fullcycle.codeflix.subscription.infrastructure.rest.models.res.SignUpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class AccountRestController implements AccountRestAPI {

    private final SignUpMediator signUpMediator;

    public AccountRestController(final SignUpMediator signUpMediator) {
        this.signUpMediator = Objects.requireNonNull(signUpMediator);
    }

    @Override
    public ResponseEntity<SignUpResponse> signUp(final SignUpRequest req) {
        final var output = this.signUpMediator.execute(req);
        return ResponseEntity.created(URI.create("/users/%s".formatted(output.userId()))).body(output);
    }
}
