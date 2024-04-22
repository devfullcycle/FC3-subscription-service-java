package com.fullcycle.subscription.infrastructure.mediator;

import com.fullcycle.subscription.application.account.CreateAccount;
import com.fullcycle.subscription.application.account.CreateIdpUser;
import com.fullcycle.subscription.infrastructure.rest.models.req.SignUpRequest;
import com.fullcycle.subscription.infrastructure.rest.models.res.SignUpResponse;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SignUpMediator {

    private final CreateAccount createAccount;
    private final CreateIdpUser createIdpUser;

    public SignUpMediator(final CreateAccount createAccount, final CreateIdpUser createIdpUser) {
        this.createAccount = Objects.requireNonNull(createAccount);
        this.createIdpUser = Objects.requireNonNull(createIdpUser);
    }

    public SignUpResponse signUp(final SignUpRequest req) {
        return this.createAccount.execute(this.createIdpUser.execute(req, req::with), SignUpResponse::new);
    }
}
