package com.fullcycle.subscription.domain.person;

import com.fullcycle.subscription.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    public void givenValidParams_whenInstantiate_shouldReturnVO() {
        // given
        var expectedZipcode = "12312321";
        var expectedNumber = "111";
        var expectedComplement = "01";
        var expectedCountry = "BR";

        // when
        var actualAddress = new Address(expectedZipcode, expectedNumber, expectedComplement, expectedCountry);

        // then
        assertEquals(expectedZipcode, actualAddress.zipcode());
        assertEquals(expectedNumber, actualAddress.number());
        assertEquals(expectedComplement, actualAddress.complement());
        assertEquals(expectedCountry, actualAddress.country());
    }

    @Test
    public void givenInvalidZipcode_whenInstantiate_shouldReturnError() {
        // given
        String expectedZipcode = null;
        var expectedNumber = "111";
        var expectedComplement = "01";
        var expectedCountry = "BR";
        var expectedError = "'zipcode' should not be empty";

        // when
        var actualError = assertThrows(DomainException.class, () -> new Address(expectedZipcode, expectedNumber, expectedComplement, expectedCountry));

        // then
        assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    public void givenInvalidNumber_whenInstantiate_shouldReturnError() {
        // given
        var expectedZipcode = "12312321";
        String expectedNumber = null;
        var expectedComplement = "01";
        var expectedCountry = "BR";
        var expectedError = "'number' should not be empty";

        // when
        var actualError = assertThrows(DomainException.class, () -> new Address(expectedZipcode, expectedNumber, expectedComplement, expectedCountry));

        // then
        assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    public void givenInvalidCountry_whenInstantiate_shouldReturnError() {
        // given
        var expectedZipcode = "12312321";
        var expectedNumber = "111";
        var expectedComplement = "01";
        String expectedCountry = null;
        var expectedError = "'country' should not be empty";

        // when
        var actualError = assertThrows(DomainException.class, () -> new Address(expectedZipcode, expectedNumber, expectedComplement, expectedCountry));

        // then
        assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    public void givenEmptyComplement_whenInstantiate_shouldReturnVO() {
        // given
        var expectedZipcode = "12312321";
        var expectedNumber = "111";
        String expectedComplement = null;
        var expectedCountry = "BR";
        var expectedError = "'zipcode' should not be empty";

        // when
        assertDoesNotThrow(() -> new Address(expectedZipcode, expectedNumber, expectedComplement, expectedCountry));
    }
}