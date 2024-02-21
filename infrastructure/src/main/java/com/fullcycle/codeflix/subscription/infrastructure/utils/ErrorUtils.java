package com.fullcycle.codeflix.subscription.infrastructure.utils;

import com.fullcycle.codeflix.subscription.domain.validation.ValidationError;
import jakarta.validation.ConstraintViolation;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Set;

public final class ErrorUtils {
    private ErrorUtils() {
    }

    public static List<ValidationError> convert(final Set<ConstraintViolation<?>> violations) {
        return violations.stream()
                .map(it -> new ValidationError(it.getMessage()))
                .toList();
    }

    public static List<ValidationError> convert(final List<ObjectError> allErrors) {
        return allErrors.stream()
                .map(it -> new ValidationError(((FieldError) it).getField(), it.getDefaultMessage()))
                .toList();
    }
}
