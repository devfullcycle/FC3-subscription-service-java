package com.fullcycle.codeflix.subscription.application.iam;

import com.fullcycle.codeflix.subscription.application.UseCase;
import com.fullcycle.codeflix.subscription.domain.iam.IAMUser;
import com.fullcycle.codeflix.subscription.domain.iam.IdentityGateway;

import java.util.Objects;

public class IamSignUp extends UseCase<IamSignUp.Input, IamSignUp.Output> {

    private final IdentityGateway identityGateway;

    public IamSignUp(final IdentityGateway identityGateway) {
        this.identityGateway = Objects.requireNonNull(identityGateway);
    }

    @Override
    public Output execute(final Input in) {
        final var aUser = this.identityGateway.create(this.newUserWith(in));
        return new Output(aUser.id());
    }

    private IAMUser newUserWith(final Input in) {
        return IAMUser.newUser(in.firstname(), in.lastname(), in.email(), in.password());
    }

    public interface Input {
        String firstname();
        String lastname();
        String email();
        String password();
    }

    public record Output(String userId) { }
}
