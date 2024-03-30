package com.fullcycle.codeflix.subscription.infrastructure.rest.controllers;

import com.fullcycle.codeflix.subscription.application.account.CreateAccount;
import com.fullcycle.codeflix.subscription.infrastructure.rest.UserRestAPI;
import com.fullcycle.codeflix.subscription.infrastructure.rest.models.req.SignUpRequest;
import com.fullcycle.codeflix.subscription.infrastructure.rest.models.res.SignUpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class UserRestController implements UserRestAPI {

    private final CreateAccount createAccount;

    public UserRestController(final CreateAccount createAccount) {
        this.createAccount = Objects.requireNonNull(createAccount);
    }

    @Override
    public ResponseEntity<SignUpResponse> signUp(final SignUpRequest req) {
        final var output = this.createAccount.execute(req, SignUpResponse::new);
        return ResponseEntity.created(URI.create("/users/%s".formatted(output.userId()))).body(output);
    }
}
