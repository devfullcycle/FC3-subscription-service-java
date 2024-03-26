package com.fullcycle.codeflix.subscription.application.iam;

import com.fullcycle.codeflix.subscription.application.UseCase;

public abstract class IamSignUp extends UseCase<IamSignUp.Input, IamSignUp.Output> {

    public interface Input {
        String firstname();
        String lastname();
        String email();
        String password();
    }

    public interface Output {
        String userId();
    }
}
