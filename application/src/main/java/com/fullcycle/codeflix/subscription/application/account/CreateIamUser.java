package com.fullcycle.codeflix.subscription.application.account;

import com.fullcycle.codeflix.subscription.application.UseCase;

public abstract class CreateIamUser extends UseCase<CreateIamUser.Input, CreateIamUser.Output> {

    public interface Input {
        String firstname();
        String lastname();
        String email();
        String password();
    }

    public interface Output {
        String iamUserId();
    }
}
