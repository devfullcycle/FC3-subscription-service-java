package com.fullcycle.codeflix.subscription.application.user;

import com.fullcycle.codeflix.subscription.application.UseCase;
import com.fullcycle.codeflix.subscription.domain.iam.IdentityGateway;
import com.fullcycle.codeflix.subscription.domain.person.Email;
import com.fullcycle.codeflix.subscription.domain.user.EmailAlreadyRegistered;
import com.fullcycle.codeflix.subscription.domain.user.User;
import com.fullcycle.codeflix.subscription.domain.user.UserGateway;
import com.fullcycle.codeflix.subscription.domain.user.UserId;

import java.util.Objects;

public class SignUp extends UseCase<SignUp.Input, SignUp.Output> {

    private final UserGateway userGateway;

    public SignUp(final UserGateway userGateway) {
        this.userGateway = Objects.requireNonNull(userGateway);
    }

    @Override
    public Output execute(final Input in) {
        final var aUser = this.newUserWith(in);
        this.userGateway.create(aUser);
        return new Output(aUser);
    }

    private User newUserWith(final Input in) {
        return User.newUser(new UserId(in.iamUserId()), in.firstname(), in.lastname(), in.documentNumber(), in.documentType());
    }

    public interface Input {
        String iamUserId();
        String firstname();
        String lastname();
        String documentNumber();
        String documentType();
    }

    public record Output(String userId) {
        public Output(User user) {
            this(user.id().value());
        }
    }
}
