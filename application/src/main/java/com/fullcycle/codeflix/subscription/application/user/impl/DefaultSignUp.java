package com.fullcycle.codeflix.subscription.application.user.impl;

import com.fullcycle.codeflix.subscription.application.user.SignUp;
import com.fullcycle.codeflix.subscription.domain.user.User;
import com.fullcycle.codeflix.subscription.domain.user.UserGateway;
import com.fullcycle.codeflix.subscription.domain.user.UserId;

import java.util.Objects;

public class DefaultSignUp extends SignUp {

    private final UserGateway userGateway;

    public DefaultSignUp(final UserGateway userGateway) {
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

    record StdOutput(String userId) implements Output {
        public StdOutput(User user) {
            this(user.id().value());
        }
    }
}
