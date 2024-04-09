package com.fullcycle.subscription.domain.person;

import com.fullcycle.subscription.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NameTest {

    @Test
    public void givenValidNames_whenInstantiate_shouldReturnVO() {
        // given
        var expectedFirstname = "John";
        var expectedLastname = "Doe";

        // when
        var actualName = new Name(expectedFirstname, expectedLastname);

        // then
        assertEquals(expectedFirstname, actualName.firstname());
        assertEquals(expectedLastname, actualName.lastname());
    }

    @Test
    public void givenInvalidFirstname_whenInstantiate_shouldReturnError() {
        // given
        String expectedFirstname = null;
        var expectedError = "'firstname' should not be empty";
        var expectedLastname = "Doe";

        // when
        var actualError = assertThrows(DomainException.class, () -> new Name(expectedFirstname, expectedLastname));

        // then
        assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    public void givenInvalidLastname_whenInstantiate_shouldReturnError() {
        // given
        var expectedFirstname = "John";
        var expectedError = "'lastname' should not be empty";
        String expectedLastname = null;

        // when
        var actualError = assertThrows(DomainException.class, () -> new Name(expectedFirstname, expectedLastname));

        // then
        assertEquals(expectedError, actualError.getMessage());
    }
}