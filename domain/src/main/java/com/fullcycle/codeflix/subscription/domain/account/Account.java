package com.fullcycle.codeflix.subscription.domain.account;

import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.person.Address;
import com.fullcycle.codeflix.subscription.domain.person.Document;
import com.fullcycle.codeflix.subscription.domain.person.Email;
import com.fullcycle.codeflix.subscription.domain.person.Name;

public class Account extends AggregateRoot<AccountId> {

    private static final int INIT_VERSION = 0;

    private int version;
    private Email email;
    private Name name;
    private Document document;
    private Address billingAddress;

    private Account(
            final AccountId accountId,
            final int version,
            final Email email,
            final Name name,
            final Document document,
            final Address billingAddress
    ) {
        super(accountId);
        setVersion(version);
        setEmail(email);
        setName(name);
        setDocument(document);
        setBillingAddress(billingAddress);
    }

    public static Account newAccount(final AccountId accountId, final Email email, final Name name, final Document document) {
        final var aNewAccount = new Account(accountId, INIT_VERSION, email, name, document, null);
        aNewAccount.registerEvent(new AccountCreated(aNewAccount));
        return aNewAccount;
    }

    public static Account with(
            final AccountId accountId,
            final int version,
            final Email email,
            final Name name,
            final Document document,
            final Address billingAddress
    ) {
        return new Account(accountId, version, email, name, document, billingAddress);
    }

    public Account execute(final AccountCommand... cmds) {
        for (final var cmd : cmds) {
            switch (cmd) {
                case AccountCommand.ChangeEmailCommand c -> apply(c);
                case AccountCommand.ChangeNameCommand c -> apply(c);
                case AccountCommand.ChangeDocumentCommand c -> apply(c);
                case AccountCommand.ChangeBillingAddressCommand c -> apply(c);
            }
        }
        return this;
    }

    public int version() {
        return version;
    }

    public Email email() {
        return email;
    }

    public Name name() {
        return name;
    }

    public Address billingAddress() {
        return billingAddress;
    }

    public Document document() {
        return document;
    }

    private Account apply(final AccountCommand.ChangeEmailCommand cmd) {
        this.setEmail(cmd.anEmail());
        return this;
    }

    private Account apply(final AccountCommand.ChangeNameCommand cmd) {
        this.setName(cmd.aName());
        return this;
    }

    private Account apply(final AccountCommand.ChangeDocumentCommand cmd) {
        this.setDocument(cmd.aDocument());
        return this;
    }

    private Account apply(final AccountCommand.ChangeBillingAddressCommand cmd) {
        this.setBillingAddress(cmd.anAddress());
        return this;
    }

    private void setVersion(final int version) {
        this.version = version;
    }

    private void setEmail(final Email email) {
        this.assertArgumentNotNull(email, "'email' should not be null");
        this.email = email;
    }

    private void setName(final Name name) {
        this.assertArgumentNotNull(name, "'name' should not be null");
        this.name = name;
    }

    private void setDocument(final Document document) {
        this.assertArgumentNotNull(document, "'document' should not be null");
        this.document = document;
    }

    private void setBillingAddress(final Address billingAddress) {
        this.billingAddress = billingAddress;
    }
}
