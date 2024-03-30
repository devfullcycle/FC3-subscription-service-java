package com.fullcycle.codeflix.subscription.domain.person;

import com.fullcycle.codeflix.subscription.domain.ValueObject;

public sealed interface Document extends ValueObject {

    String value();

    String type();

    static Document create(String documentNumber, String documentType) {
        return DocumentFactory.create(documentNumber, documentType);
    }

    record Cpf(String value) implements Document {

        public static final String TYPE = "cpf";

        public Cpf {
            this.assertArgumentNotNull(value, "CPF is required");
            this.assertArgumentLength(value, 11, "invalid CPF");
        }

        @Override
        public String type() {
            return TYPE;
        }
    }

    record Cnpj(String value) implements Document {

        public static final String TYPE = "cnpj";

        public Cnpj {
            this.assertArgumentNotNull(value, "CNPJ is required");
            this.assertArgumentLength(value, 14, "invalid CNPJ");
        }

        @Override
        public String type() {
            return TYPE;
        }
    }
}
