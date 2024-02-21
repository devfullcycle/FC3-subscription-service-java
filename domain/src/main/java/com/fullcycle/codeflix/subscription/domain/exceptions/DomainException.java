package com.fullcycle.codeflix.subscription.domain.exceptions;

import com.fullcycle.codeflix.subscription.domain.validation.ValidationError;

import java.util.List;

public class DomainException extends NoStacktraceException {

    protected final List<ValidationError> errors;

    protected DomainException(final String aMessage, final List<ValidationError> anErrors) {
        super(aMessage);
        this.errors = anErrors;
    }

    public static DomainException with(final ValidationError anErrors) {
        return new DomainException(anErrors.message(), List.of(anErrors));
    }

    public static DomainException with(final List<ValidationError> anErrors) {
        return new DomainException("", anErrors);
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}
