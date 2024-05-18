package com.fullcycle.subscription.application.account.impl;

import com.fullcycle.subscription.application.account.CreateAccount;
import com.fullcycle.subscription.domain.account.Account;
import com.fullcycle.subscription.domain.account.AccountGateway;
import com.fullcycle.subscription.domain.account.AccountId;
import com.fullcycle.subscription.domain.account.idp.UserId;
import com.fullcycle.subscription.domain.person.Document;
import com.fullcycle.subscription.domain.person.Email;
import com.fullcycle.subscription.domain.person.Name;

import java.util.Objects;

public class DefaultCreateAccount extends CreateAccount {

    private final AccountGateway accountGateway;

    public DefaultCreateAccount(final AccountGateway accountGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public Output execute(final Input in) {
        if (in == null) {
            throw new IllegalArgumentException("Input to DefaultCreateAccount cannot be null");
        }

        final var anUserAccount = this.newAccountWith(in);
        this.accountGateway.save(anUserAccount);
        return new StdOutput(anUserAccount.id());
    }

    private Account newAccountWith(final Input in) {
        return Account.newAccount(
                new AccountId(in.accountId()),
                new UserId(in.userId()),
                new Email(in.email()),
                new Name(in.firstname(), in.lastname()),
                Document.create(in.documentNumber(), in.documentType())
        );
    }

    record StdOutput(AccountId accountId) implements Output {

    }
}
