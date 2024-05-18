package com.fullcycle.subscription.application.account;

import com.fullcycle.subscription.application.UseCase;
import com.fullcycle.subscription.domain.account.AccountId;

public abstract class CreateAccount extends UseCase<CreateAccount.Input, CreateAccount.Output> {

    public interface Input {
        String userId();
        String accountId();
        String email();
        String firstname();
        String lastname();
        String documentNumber();
        String documentType();
    }

    public interface Output {
        AccountId accountId();
    }
}
