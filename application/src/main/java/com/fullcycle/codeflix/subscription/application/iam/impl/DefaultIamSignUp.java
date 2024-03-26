package com.fullcycle.codeflix.subscription.application.iam.impl;

import com.fullcycle.codeflix.subscription.application.iam.IamSignUp;
import com.fullcycle.codeflix.subscription.domain.iam.IAMUser;
import com.fullcycle.codeflix.subscription.domain.iam.IdentityGateway;

import java.util.Objects;

public class DefaultIamSignUp extends IamSignUp {

    private final IdentityGateway identityGateway;

    public DefaultIamSignUp(final IdentityGateway identityGateway) {
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

    record StdOutput(String userId) implements Output { }
}
