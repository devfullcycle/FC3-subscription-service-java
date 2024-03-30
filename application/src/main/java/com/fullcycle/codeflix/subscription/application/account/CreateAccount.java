package com.fullcycle.codeflix.subscription.application.account;

import com.fullcycle.codeflix.subscription.application.UseCase;
import com.fullcycle.codeflix.subscription.domain.account.Account;

public abstract class CreateAccount extends UseCase<CreateAccount.Input, CreateAccount.Output> {

    public interface Input {
        String email();
        String firstname();
        String lastname();
        String documentNumber();
        String documentType();
    }

    public interface Output {
        String accountId();
    }

    public record StdOutput(String accountId) implements Output {
        public StdOutput(Account user) {
            this(user.id().value());
        }
    }
}
