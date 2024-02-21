package com.fullcycle.codeflix.subscription.domain.validation;

public record ValidationError(String property, String message) {

    public ValidationError(String message) {
        this("", message);
    }
}
