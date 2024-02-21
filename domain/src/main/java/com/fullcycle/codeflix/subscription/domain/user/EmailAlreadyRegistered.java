package com.fullcycle.codeflix.subscription.domain.user;

import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.validation.ValidationError;

import java.util.List;

public class EmailAlreadyRegistered extends DomainException {

    public EmailAlreadyRegistered() {
        super("E-mail already registered", List.of(new ValidationError("email", "E-mail already registered")));
    }
}
