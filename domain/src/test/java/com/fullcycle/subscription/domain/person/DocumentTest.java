package com.fullcycle.subscription.domain.person;

import com.fullcycle.subscription.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentTest {

    @Test
    public void givenValidCpf_whenInstantiate_shouldReturnVO() {
        // given
        var expectedDocumentValue = "12312312332";
        var expectedDocumentType = "cpf";

        // when
        var actualDocument = Document.create(expectedDocumentValue, expectedDocumentType);

        // then
        assertEquals(expectedDocumentValue, actualDocument.value());
        assertEquals(expectedDocumentType, actualDocument.type());
        assertInstanceOf(Document.Cpf.class, actualDocument);
    }

    @Test
    public void givenInvalidCpfLength_whenInstantiate_shouldReturnError() {
        // given
        var expectedDocumentValue = "11";
        var expectedDocumentType = "cpf";
        var expectedError = "'cpf' is invalid";

        // when
        var actualError = assertThrows(DomainException.class, () -> Document.create(expectedDocumentValue, expectedDocumentType));

        // then
        assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    public void givenEmptyCpf_whenInstantiate_shouldReturnError() {
        // given
        var expectedDocumentValue = "";
        var expectedDocumentType = "cpf";
        var expectedError = "'cpf' should not be empty";

        // when
        var actualError = assertThrows(DomainException.class, () -> Document.create(expectedDocumentValue, expectedDocumentType));

        // then
        assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    public void givenNullCpf_whenInstantiate_shouldReturnError() {
        // given
        String expectedDocumentValue = null;
        var expectedDocumentType = "cpf";
        var expectedError = "'cpf' should not be empty";

        // when
        var actualError = assertThrows(DomainException.class, () -> Document.create(expectedDocumentValue, expectedDocumentType));

        // then
        assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    public void givenValidCnpj_whenInstantiate_shouldReturnVO() {
        // given
        var expectedDocumentValue = "12312312332000";
        var expectedDocumentType = "cnpj";

        // when
        var actualDocument = Document.create(expectedDocumentValue, expectedDocumentType);

        // then
        assertEquals(expectedDocumentValue, actualDocument.value());
        assertEquals(expectedDocumentType, actualDocument.type());
        assertInstanceOf(Document.Cnpj.class, actualDocument);
    }

    @Test
    public void givenInvalidCnpjLength_whenInstantiate_shouldReturnError() {
        // given
        var expectedDocumentValue = "11";
        var expectedDocumentType = "cnpj";
        var expectedError = "'cnpj' is invalid";

        // when
        var actualError = assertThrows(DomainException.class, () -> Document.create(expectedDocumentValue, expectedDocumentType));

        // then
        assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    public void givenEmptyCnpj_whenInstantiate_shouldReturnError() {
        // given
        var expectedDocumentValue = "";
        var expectedDocumentType = "cnpj";
        var expectedError = "'cnpj' should not be empty";

        // when
        var actualError = assertThrows(DomainException.class, () -> Document.create(expectedDocumentValue, expectedDocumentType));

        // then
        assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    public void givenNullCnpj_whenInstantiate_shouldReturnError() {
        // given
        String expectedDocumentValue = null;
        var expectedDocumentType = "cnpj";
        var expectedError = "'cnpj' should not be empty";

        // when
        var actualError = assertThrows(DomainException.class, () -> Document.create(expectedDocumentValue, expectedDocumentType));

        // then
        assertEquals(expectedError, actualError.getMessage());
    }
}