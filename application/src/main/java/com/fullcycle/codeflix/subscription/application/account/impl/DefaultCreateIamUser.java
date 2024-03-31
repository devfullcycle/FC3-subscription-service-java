package com.fullcycle.codeflix.subscription.application.account.impl;

import com.fullcycle.codeflix.subscription.application.account.CreateIamUser;
import com.fullcycle.codeflix.subscription.domain.account.iam.User;
import com.fullcycle.codeflix.subscription.domain.account.iam.IdentityGateway;

import java.util.Objects;

public class DefaultCreateIamUser extends CreateIamUser {

    private final IdentityGateway identityGateway;

    public DefaultCreateIamUser(final IdentityGateway identityGateway) {
        this.identityGateway = Objects.requireNonNull(identityGateway);
    }

    @Override
    public Output execute(final Input in) {
        final var aUser = this.identityGateway.create(this.newUserWith(in));
        return new StdOutput(aUser.value());
    }

    private User newUserWith(final Input in) {
        return User.newUser(in.firstname(), in.lastname(), in.email(), in.password());
    }

    record StdOutput(String iamUserId) implements Output {
    }
}
