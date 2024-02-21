package com.fullcycle.codeflix.subscription.domain.validation.handler;

import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.exceptions.NotificationException;
import com.fullcycle.codeflix.subscription.domain.validation.Error;
import com.fullcycle.codeflix.subscription.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private final List<Error> errors;

    private Notification(final List<Error> errors) {
        this.errors = errors;
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Throwable t) {
        return create(new Error(t.getMessage()));
    }

    public static Notification create(final Error anError) {
        return new Notification(new ArrayList<>()).append(anError);
    }

    @Override
    public Notification append(final Error anError) {
        this.errors.add(anError);
        return this;
    }

    @Override
    public ValidationHandler append(final String anError) {
        return append(new Error(anError));
    }

    @Override
    public Notification append(final ValidationHandler anHandler) {
        this.errors.addAll(anHandler.getErrors());
        return this;
    }

    @Override
    public <T> T validate(final Validation<T> aValidation) {
        try {
            return aValidation.validate();
        } catch (final DomainException ex) {
            this.errors.addAll(ex.getErrors());
        } catch (final Throwable t) {
            this.errors.add(new Error(t.getMessage()));
        }
        return null;
    }

    @Override
    public List<Error> getErrors() {
        return this.errors;
    }

    public void get(final String message) {
        if (hasError()) {
            throw NotificationException.with(message, this);
        }
    }
}
