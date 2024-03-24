package com.fullcycle.codeflix.subscription.infrastructure.users;

import com.fullcycle.codeflix.subscription.application.iam.IamSignUp;
import com.fullcycle.codeflix.subscription.application.user.SignUp;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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

    public String signUp(final SignUpInput in) {
        final var iamUser = this.iamSignUp.execute(new IamSignUpInput(in));
        this.userSignUp.execute(new UserSignUpInput(in, iamUser));
        return iamUser.userId();
    }

    public record SignUpInput(
            @NotEmpty @Max(255) String firstname,
            @NotEmpty @Max(255) String lastname,
            @NotEmpty @Email String email,
            @NotEmpty @Min(12) @Max(20) String password,
            @NotEmpty String documentNumber,
            @NotEmpty String documentType
    ) {

    }

    public record IamSignUpInput(
            String firstname,
            String lastname,
            String email,
            String password
    ) implements IamSignUp.Input {
        public IamSignUpInput(SignUpInput in) {
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
        public UserSignUpInput(SignUpInput in, IamSignUp.Output out) {
            this(in.firstname(), in.lastname(), out.userId(), in.documentNumber(), in.documentType());
        }
    }

    public record Output(String userId) {
    }
}
