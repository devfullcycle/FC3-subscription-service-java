package com.fullcycle.codeflix.subscription.domain.person;

import com.fullcycle.codeflix.subscription.domain.ValueObject;

public sealed interface Document extends ValueObject {

    static Document create(String documentType, String documentNumber) {
        return DocumentFactory.create(documentNumber, documentType);
    }

    record Cpf(String value) implements Document {
        public Cpf {
            this.assertArgumentNotNull(value, "CPF is required");
            this.assertArgumentLength(value, 11, "invalid CPF");
        }
    }

    record Cnpj(String value) implements Document {
        public Cnpj {
            this.assertArgumentNotNull(value, "CNPJ is required");
            this.assertArgumentLength(value, 14, "invalid CNPJ");
        }
    }
}
