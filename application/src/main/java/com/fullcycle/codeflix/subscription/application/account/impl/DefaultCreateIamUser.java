package com.fullcycle.codeflix.subscription.application.account.impl;

import com.fullcycle.codeflix.subscription.application.account.CreateIamUser;
import com.fullcycle.codeflix.subscription.domain.account.iam.IAMUser;
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
        return new StdOutput(aUser.id());
    }

    private IAMUser newUserWith(final Input in) {
        return IAMUser.newUser(in.firstname(), in.lastname(), in.email(), in.password());
    }

    record StdOutput(String iamUserId) implements Output {
    }
}
