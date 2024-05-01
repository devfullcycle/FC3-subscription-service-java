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
        final Function<SignUpRequest, SignUpRequest> nextAccountId = in -> in.with(this.accountGateway.nextId());
        final Function<SignUpRequest, SignUpRequest> createIdpUser = in -> in.with(this.createIdpUser.execute(in));
        final Function<SignUpRequest, SignUpResponse> createAccount = in -> this.createAccount.execute(in, SignUpResponse::new);
        return nextAccountId.andThen(createIdpUser).andThen(createAccount).apply(req);
    }
}
