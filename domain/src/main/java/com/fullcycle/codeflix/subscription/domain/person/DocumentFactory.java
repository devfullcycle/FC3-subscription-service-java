package com.fullcycle.codeflix.subscription.domain.person;

import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.validation.ValidationError;

public final class DocumentFactory {

    public static Document create(final String value, final String type) {
        return switch (type) {
            case Document.Cpf.TYPE -> new Document.Cpf(value);
            case Document.Cnpj.TYPE -> new Document.Cnpj(value);
            default -> throw DomainException.with(new ValidationError("Invalid document type"));
        };
    }
}
