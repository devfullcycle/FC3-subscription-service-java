package com.fullcycle.codeflix.subscription.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(ValidationError anError);

    ValidationHandler append(String anError);

    ValidationHandler append(ValidationHandler anHandler);

    <T> T validate(Validation<T> aValidation);

    List<ValidationError> getErrors();

    default boolean hasError() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    default ValidationError firstError() {
        if (getErrors() != null && !getErrors().isEmpty()) {
            return getErrors().get(0);
        } else {
            return null;
        }
    }

    interface Validation<T> {
        T validate();
    }
}
