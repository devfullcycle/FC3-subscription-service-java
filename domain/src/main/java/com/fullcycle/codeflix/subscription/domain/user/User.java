package com.fullcycle.codeflix.subscription.domain.user;

import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.exceptions.NotificationException;
import com.fullcycle.codeflix.subscription.domain.utils.IdUtils;
import com.fullcycle.codeflix.subscription.domain.validation.Error;
import com.fullcycle.codeflix.subscription.domain.validation.ValidationHandler;
import com.fullcycle.codeflix.subscription.domain.validation.handler.Notification;

public class User extends AggregateRoot<UserId> {

    private String firstname;
    private String lastname;
    private Address address;
    private Document document;

    private User(
            final String userId,
            final String firstname,
            final String lastname,
            final String street,
            final String number,
            final String complement,
            final String neighborhood,
            final String zipcode,
            final String city,
            final String state,
            final String country,
            final String documentNumber,
            final String documentType
    ) {
        super(new UserId(userId));
        var n = Notification.create();
        setFirstname(firstname, n);
        setLastname(lastname, n);
        setAddress(street, number, complement, neighborhood, zipcode, city, state, country, n);
        setDocument(documentType, documentNumber, n);

        if (n.hasError()) {
            throw NotificationException.with("Invalid user", n);
        }
    }

    public static User newUser(
            final String firstname,
            final String lastname,
            final String street,
            final String number,
            final String complement,
            final String neighborhood,
            final String zipcode,
            final String city,
            final String state,
            final String country,
            final String documentNumber,
            final String documentType
    ) {
        return new User(
                IdUtils.uniqueId(),
                firstname,
                lastname,
                street, number, complement, neighborhood, zipcode, city, state, country,
                documentNumber, documentType
        );
    }

    public static User with(
            final String userId,
            final String firstname,
            final String lastname,
            final String street,
            final String number,
            final String complement,
            final String neighborhood,
            final String zipcode,
            final String city,
            final String state,
            final String country,
            final String documentNumber,
            final String documentType
    ) {
        return new User(
                userId,
                firstname,
                lastname,
                street, number, complement, neighborhood, zipcode, city, state, country,
                documentNumber, documentType
        );
    }

    public String firstname() {
        return firstname;
    }

    public String lastname() {
        return lastname;
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

    private void setFirstname(final String firstname, final ValidationHandler handler) {
        if (firstname == null || firstname.isBlank()) {
            handler.append(new Error("'firstname' should not be empty"));
        }
        this.firstname = firstname;
    }

    private void setLastname(final String lastname, final ValidationHandler handler) {
        if (lastname == null || lastname.isBlank()) {
            handler.append(new Error("'lastname' should not be empty"));
        }
        this.lastname = lastname;
    }

    private void setAddress(
            final String street,
            final String number,
            final String complement,
            final String neighborhood,
            final String zipcode,
            final String city,
            final String state,
            final String country,
            final ValidationHandler handler
    ) {
        this.address = handler.validate(() -> new Address(street, number, complement, neighborhood, zipcode, city, state, country));
    }

    private void setDocument(final String documentType, final String documentValue, final ValidationHandler handler) {
        this.document = handler.validate(() -> Document.create(documentType, documentValue));
    }
}
