package com.fullcycle.codeflix.subscription.domain.user;

import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.validation.Error;

public sealed interface Document {

    static Document create(String documentType, String documentNumber) {
        return DocumentFactory.create(documentNumber, documentType);
    }

    record Cpf(String value) implements Document {
        public Cpf {
            if (value == null) {
                throw DomainException.with(new Error("'cpf' should not be empty"));
            }

            if (value.length() != 11) {
                throw DomainException.with(new Error("'cpf' should have 11 digits"));
            }
        }
    }

    record Cnpj(String value) implements Document {
        public Cnpj {
            if (value == null) {
                throw DomainException.with(new Error("'cnpj' should not be empty"));
            }

            if (value.length() != 14) {
                throw DomainException.with(new Error("'cnpj' should have 14 digits"));
            }
        }
    }
}
