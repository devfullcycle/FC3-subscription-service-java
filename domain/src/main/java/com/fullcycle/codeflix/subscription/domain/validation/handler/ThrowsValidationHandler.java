package com.fullcycle.codeflix.subscription.domain.validation.handler;

import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.validation.ValidationError;
import com.fullcycle.codeflix.subscription.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {

    @Override
    public ValidationHandler append(final ValidationError anError) {
        throw DomainException.with(anError);
    }

    @Override
    public ValidationHandler append(String anError) {
        return append(new ValidationError(anError));
    }

    @Override
    public ValidationHandler append(final ValidationHandler anHandler) {
        throw DomainException.with(anHandler.getErrors());
    }

    @Override
    public <T> T validate(final Validation<T> aValidation) {
        try {
            return aValidation.validate();
        } catch (final Exception ex) {
            throw DomainException.with(new ValidationError(ex.getMessage()));
        }
    }

    @Override
    public List<ValidationError> getErrors() {
        return List.of();
    }
}
