package com.fullcycle.subscription.domain.account;

import com.fullcycle.subscription.domain.AggregateRoot;
import com.fullcycle.subscription.domain.account.iam.UserId;
import com.fullcycle.subscription.domain.person.Address;
import com.fullcycle.subscription.domain.person.Document;
import com.fullcycle.subscription.domain.person.Email;
import com.fullcycle.subscription.domain.person.Name;

public class Account extends AggregateRoot<AccountId> {

    private int version;
    private UserId userId;
    private Email email;
    private Name name;
    private Document document;
    private Address billingAddress;

    private Account(
            final AccountId anAccountId,
            final int version,
            final UserId anUserId,
            final Email anEmail,
            final Name aName,
            final Document aDocument,
            final Address billingAddress
    ) {
        super(anAccountId);
        this.setVersion(version);
        this.setUserId(anUserId);
        this.setEmail(anEmail);
        this.setName(aName);
        this.setDocument(aDocument);
        this.setBillingAddress(billingAddress);
    }

    public static Account newAccount(
            final AccountId anAccountId,
            final UserId anUserId,
            final Email anEmail,
            final Name aName,
            final Document aDocument
    ) {
        return new Account(anAccountId, 0, anUserId, anEmail, aName, aDocument, null);
    }

    public static Account with(
            final AccountId anAccountId,
            final int version,
            final UserId anUserId,
            final Email anEmail,
            final Name aName,
            final Document aDocument,
            final Address billingAddress
    ) {
        return new Account(anAccountId, version, anUserId, anEmail, aName, aDocument, billingAddress);
    }

    public int version() {
        return version;
    }

    public UserId userId() {
        return userId;
    }

    public Email email() {
        return email;
    }

    public Name name() {
        return name;
    }

    public Document document() {
        return document;
    }

    public Address billingAddress() {
        return billingAddress;
    }

    private void setVersion(int version) {
        this.version = version;
    }

    private void setUserId(final UserId userId) {
        this.userId = this.assertArgumentNotNull(userId, "'userId' should not be null");
    }

    private void setEmail(final Email email) {
        this.email = this.assertArgumentNotNull(email, "'email' should not be null");
    }

    private void setName(final Name name) {
        this.name = this.assertArgumentNotNull(name, "'name' should not be null");
    }

    private void setDocument(final Document document) {
        this.document = this.assertArgumentNotNull(document, "'document' should not be null");
    }

    private void setBillingAddress(final Address billingAddress) {
        this.billingAddress = billingAddress;
    }
}
