package com.fullcycle.codeflix.subscription.domain.user;

import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.person.Address;
import com.fullcycle.codeflix.subscription.domain.person.Document;
import com.fullcycle.codeflix.subscription.domain.person.Email;

public class User extends AggregateRoot<UserId> {

    private String firstname;
    private String lastname;
    private Email email;
    private Address address;
    private Document document;

    private User(
            final UserId userId,
            final String firstname,
            final String lastname,
            final String email,
            final String documentNumber,
            final String documentType
    ) {
        super(userId);
        setFirstname(firstname);
        setLastname(lastname);
        setEmail(email);
        setDocument(documentType, documentNumber);
    }

    private User(
            final UserId userId,
            final String firstname, final String lastname, final String email,
            final Address address,
            final String documentNumber, final String documentType
    ) {
        this(userId, firstname, lastname, email, documentNumber, documentType);
        setAddress(address);
    }

    public static User newUser(
            final UserId userId,
            final String firstname, final String lastname, final String email,
            final String documentNumber, final String documentType
    ) {
        return new User(userId, firstname, lastname, email, documentNumber, documentType);
    }

    public static User with(
            final UserId userId,
            final String firstname, final String lastname, final String email,
            final Address address,
            final String documentNumber, final String documentType
    ) {
        return new User(userId, firstname, lastname, email, address, documentNumber, documentType);
    }

    public String firstname() {
        return firstname;
    }

    public String lastname() {
        return lastname;
    }

    public Email email() {
        return email;
    }

    public Address address() {
        return address;
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

    public void setEmail(final String email) {
        this.email = new Email(email);
    }

    private void setAddress(final Address address) {
        this.assertArgumentNotNull(address, "'address' should not be null");
        this.address = address;
    }

    private void setDocument(final String documentType, final String documentValue) {
        this.document = Document.create(documentType, documentValue);
    }
}
