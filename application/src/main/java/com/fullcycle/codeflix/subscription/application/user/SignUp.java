package com.fullcycle.codeflix.subscription.application.user;

import com.fullcycle.codeflix.subscription.application.UseCase;
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
        return new StdOutput(aUser);
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

    public interface Output {
        String userId();
    }

    record StdOutput(String userId) implements Output {
        public StdOutput(User user) {
            this(user.id().value());
        }
    }
}
