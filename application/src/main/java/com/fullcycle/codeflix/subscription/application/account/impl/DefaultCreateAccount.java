package com.fullcycle.codeflix.subscription.application.account.impl;

import com.fullcycle.codeflix.subscription.application.account.CreateAccount;
import com.fullcycle.codeflix.subscription.domain.person.Document;
import com.fullcycle.codeflix.subscription.domain.person.Email;
import com.fullcycle.codeflix.subscription.domain.person.Name;
import com.fullcycle.codeflix.subscription.domain.account.Account;
import com.fullcycle.codeflix.subscription.domain.account.AccountGateway;

import java.util.Objects;

public class DefaultCreateAccount extends CreateAccount {

    private final AccountGateway accountGateway;

    public DefaultCreateAccount(final AccountGateway accountGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public Output execute(final Input in) {
        final var aUser = this.newUserWith(in);
        this.accountGateway.save(aUser);
        return new StdOutput(aUser);
    }

    private Account newUserWith(final Input in) {
        return Account.newAccount(
                this.accountGateway.nextId(),
                new Email(in.email()),
                new Name(in.firstname(), in.lastname()),
                Document.create(in.documentNumber(), in.documentType())
        );
    }
}
