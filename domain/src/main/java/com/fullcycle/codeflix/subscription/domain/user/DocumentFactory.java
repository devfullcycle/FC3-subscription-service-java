package com.fullcycle.codeflix.subscription.domain.user;

import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.validation.Error;

public final class DocumentFactory {

    public static Document create(final String value, final String type) {
        return switch (type) {
            case "cpf" -> new Document.Cpf(value);
            case "cnpj" -> new Document.Cnpj(value);
            default -> throw DomainException.with(new Error("Invalid document type"));
        };
    }
}
