package com.fullcycle.subscription.infrastructure.mediator;

import com.fullcycle.subscription.application.account.CreateAccount;
import com.fullcycle.subscription.application.account.CreateIdpUser;
import com.fullcycle.subscription.domain.account.AccountGateway;
import com.fullcycle.subscription.infrastructure.rest.models.req.SignUpRequest;
import com.fullcycle.subscription.infrastructure.rest.models.res.SignUpResponse;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Function;

@Component
public class SignUpMediator {

    private final AccountGateway accountGateway;
    private final CreateAccount createAccount;
    private final CreateIdpUser createIdpUser;

    public SignUpMediator(
            final AccountGateway accountGateway,
            final CreateAccount createAccount,
            final CreateIdpUser createIdpUser
    ) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.createAccount = Objects.requireNonNull(createAccount);
        this.createIdpUser = Objects.requireNonNull(createIdpUser);
    }

    public SignUpResponse signUp(final SignUpRequest req) {
        return nextAccountId().andThen(createIdpUser()).andThen(createAccount()).apply(req);
    }

    private Function<SignUpRequest, SignUpRequest> nextAccountId() {
        return req -> req.with(this.accountGateway.nextId());
    }

    private Function<SignUpRequest, SignUpRequest> createIdpUser() {
        return req -> this.createIdpUser.execute(req, req::with);
    }

    private Function<SignUpRequest, SignUpResponse> createAccount() {
        return req -> this.createAccount.execute(req, SignUpResponse::new);
    }
}
