package com.fullcycle.codeflix.subscription.application.account;

import com.fullcycle.codeflix.subscription.application.UseCase;
import com.fullcycle.codeflix.subscription.domain.account.Account;

public abstract class UpdateAccount extends UseCase<UpdateAccount.Input, UpdateAccount.Output> {

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
        public StdOutput(Account user) {
            this(user.id().value());
        }
    }
}
