package com.fullcycle.codeflix.subscription.application.user;

import com.fullcycle.codeflix.subscription.application.UseCase;
import com.fullcycle.codeflix.subscription.domain.iam.IdentityGateway;
import com.fullcycle.codeflix.subscription.domain.person.Email;
import com.fullcycle.codeflix.subscription.domain.user.EmailAlreadyRegistered;
import com.fullcycle.codeflix.subscription.domain.user.User;
import com.fullcycle.codeflix.subscription.domain.user.UserGateway;

import java.util.Objects;

public class SignUp extends UseCase<SignUp.Command, SignUp.Output> {

    private final IdentityGateway identityGateway;
    private final UserGateway userGateway;

    public SignUp(final IdentityGateway identityGateway, final UserGateway userGateway) {
        this.identityGateway = Objects.requireNonNull(identityGateway);
        this.userGateway = Objects.requireNonNull(userGateway);
    }

    @Override
    public Output execute(final Command cmd) {
        if (this.userGateway.existsByEmail(new Email(cmd.email()))) {
            throw new EmailAlreadyRegistered();
        }

        final var aUser = this.newUserWith(cmd);
        this.userGateway.create(aUser);
        return new Output(aUser);
    }

    private User newUserWith(final Command cmd) {
        final var userId = this.userGateway.nextId();
        return User.newUser(userId, cmd.firstname(), cmd.lastname(), cmd.email(), cmd.documentNumber(), cmd.documentType());
    }

    public interface Command {
        String firstname();
        String lastname();
        String email();
        String password();
        String documentNumber();
        String documentType();
    }

    public record Output(String userId) {
        public Output(User user) {
            this(user.id().value());
        }
    }
}
