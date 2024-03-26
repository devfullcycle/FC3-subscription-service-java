package com.fullcycle.codeflix.subscription.infrastructure.mediator;

import com.fullcycle.codeflix.subscription.application.iam.IamSignUp;
import com.fullcycle.codeflix.subscription.application.user.SignUp;
import com.fullcycle.codeflix.subscription.infrastructure.rest.models.req.SignUpRequest;
import com.fullcycle.codeflix.subscription.infrastructure.rest.models.res.SignUpResponse;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SignUpMediator {

    private final IamSignUp iamSignUp;
    private final SignUp userSignUp;

    public SignUpMediator(final IamSignUp iamSignUp, final SignUp userSignUp) {
        this.iamSignUp = Objects.requireNonNull(iamSignUp);
        this.userSignUp = Objects.requireNonNull(userSignUp);
    }

    public SignUpResponse signUp(final SignUpRequest in) {
        final var iamUser = this.iamSignUp.execute(new IamSignUpInput(in));
        this.userSignUp.execute(new UserSignUpInput(in, iamUser));
        return new SignUpResponse(iamUser.userId());
    }

    public record IamSignUpInput(
            String firstname,
            String lastname,
            String email,
            String password
    ) implements IamSignUp.Input {
        public IamSignUpInput(SignUpRequest in) {
            this(in.firstname(), in.lastname(), in.email(), in.password());
        }
    }

    public record UserSignUpInput(
            String firstname,
            String lastname,
            String iamUserId,
            String documentNumber,
            String documentType
    ) implements SignUp.Input {
        public UserSignUpInput(SignUpRequest in, IamSignUp.Output out) {
            this(in.firstname(), in.lastname(), out.userId(), in.documentNumber(), in.documentType());
        }
    }
}
