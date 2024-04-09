package com.fullcycle.subscription.domain.person;

import com.fullcycle.subscription.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailTest {

    @Test
    public void givenValidEmail_whenInstantiate_shouldReturnVO() {
        // given
        var expectedEmail = "john@gmail.com";

        // when
        var actualEmail = new Email(expectedEmail);

        // then
        assertEquals(expectedEmail, actualEmail.value());
    }

    @Test
    public void givenEmptyEmail_whenInstantiate_shouldReturnError() {
        // given
        var expectedEmail = "";
        var expectedError = "'email' should not be empty";

        // when
        var actualError = assertThrows(DomainException.class, () -> new Email(expectedEmail));

        // then
        assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    public void givenNullEmail_whenInstantiate_shouldReturnError() {
        // given
        String expectedEmail = null;
        var expectedError = "'email' should not be empty";

        // when
        var actualError = assertThrows(DomainException.class, () -> new Email(expectedEmail));

        // then
        assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    public void givenInvalidEmail_whenInstantiate_shouldReturnError() {
        // given
        var expectedEmail = "dsad";
        var expectedError = "'email' is invalid";

        // when
        var actualError = assertThrows(DomainException.class, () -> new Email(expectedEmail));

        // then
        assertEquals(expectedError, actualError.getMessage());
    }
}