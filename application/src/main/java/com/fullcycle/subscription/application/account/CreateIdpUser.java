package com.fullcycle.subscription.application.account;

import com.fullcycle.subscription.application.UseCase;
import com.fullcycle.subscription.domain.account.idp.UserId;

public abstract class CreateIdpUser extends UseCase<CreateIdpUser.Input, CreateIdpUser.Output> {

    public interface Input {
        String accountId();
        String firstname();
        String lastname();
        String email();
        String password();
    }

    public interface Output {
        UserId idpUserId();
    }
}
