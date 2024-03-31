package com.fullcycle.codeflix.subscription.infrastructure.mediator;

import com.fullcycle.codeflix.subscription.application.account.CreateAccount;
import com.fullcycle.codeflix.subscription.application.account.CreateIamUser;
import com.fullcycle.codeflix.subscription.infrastructure.rest.models.req.SignUpRequest;
import com.fullcycle.codeflix.subscription.infrastructure.rest.models.res.SignUpResponse;
import org.springframework.stereotype.Component;

@Component
public class SignUpMediator {

    private final CreateAccount createAccount;
    private final CreateIamUser createIamUser;

    public SignUpMediator(final CreateAccount createAccount, final CreateIamUser createIamUser) {
        this.createAccount = createAccount;
        this.createIamUser = createIamUser;
    }

    public SignUpResponse execute(final SignUpRequest req) {
        final var accInput = this.createIamUser.execute(new CreateIamUserInput(req), out -> new CreateAccountInput(req, out));
        return this.createAccount.execute(accInput, SignUpResponse::new);
    }

    record CreateIamUserInput(
            String firstname,
            String lastname,
            String email,
            String password
    ) implements CreateIamUser.Input {

        public CreateIamUserInput(final SignUpRequest req) {
            this(req.firstname(), req.lastname(), req.email(), req.password());
        }
    }

    record CreateAccountInput(
            String userId,
            String firstname,
            String lastname,
            String nickname,
            String email,
            String password,
            String documentNumber,
            String documentType
    ) implements CreateAccount.Input {

        public CreateAccountInput(final SignUpRequest req, final CreateIamUser.Output out) {
            this(
                    out.iamUserId(),
                    req.firstname(),
                    req.lastname(),
                    req.nickname(),
                    req.email(),
                    req.password(),
                    req.documentNumber(),
                    req.documentType()
            );
        }
    }
}
