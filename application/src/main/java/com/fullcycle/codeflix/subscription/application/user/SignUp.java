package com.fullcycle.codeflix.subscription.application.user;

import com.fullcycle.codeflix.subscription.application.UseCase;
import com.fullcycle.codeflix.subscription.domain.user.User;

public abstract class SignUp extends UseCase<SignUp.Input, SignUp.Output> {

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
