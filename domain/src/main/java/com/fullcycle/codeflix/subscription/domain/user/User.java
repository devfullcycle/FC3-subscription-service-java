package com.fullcycle.codeflix.subscription.domain.user;

import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.person.Address;
import com.fullcycle.codeflix.subscription.domain.person.Document;

public class User extends AggregateRoot<UserId> {

    private String firstname;
    private String lastname;
    private Address billingAddress;
    private Document document;

    private User(
            final UserId userId,
            final String firstname,
            final String lastname,
            final String documentNumber,
            final String documentType
    ) {
        super(userId);
        setFirstname(firstname);
        setLastname(lastname);
        setDocument(documentType, documentNumber);
    }

    private User(
            final UserId userId,
            final String firstname, final String lastname,
            final Address address,
            final String documentNumber, final String documentType
    ) {
        this(userId, firstname, lastname, documentNumber, documentType);
        setBillingAddress(address);
    }

    public static User newUser(
            final UserId userId,
            final String firstname, final String lastname,
            final String documentNumber, final String documentType
    ) {
        return new User(userId, firstname, lastname, documentNumber, documentType);
    }

    public static User with(
            final UserId userId,
            final String firstname, final String lastname,
            final Address billingAddress,
            final String documentNumber, final String documentType
    ) {
        return new User(userId, firstname, lastname, billingAddress, documentNumber, documentType);
    }

    public String firstname() {
        return firstname;
    }

    public String lastname() {
        return lastname;
    }

    public Address billingAddress() {
        return billingAddress;
    }

    public Document document() {
        return document;
    }

    public String fullname() {
        return firstname.concat(" ").concat(lastname);
    }

    private void setFirstname(final String firstname) {
        this.assertArgumentNotEmpty(firstname, "'firstname' should not be empty");
        this.firstname = firstname;
    }

    private void setLastname(final String lastname) {
        this.assertArgumentNotEmpty(firstname, "'lastname' should not be empty");
        this.lastname = lastname;
    }

    private void setBillingAddress(final Address billingAddress) {
        this.assertArgumentNotNull(billingAddress, "'billingAddress' should not be null");
        this.billingAddress = billingAddress;
    }

    private void setDocument(final String documentType, final String documentValue) {
        this.document = Document.create(documentType, documentValue);
    }
}
