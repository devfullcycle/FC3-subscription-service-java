package com.fullcycle.subscription.domain.plan;

import com.fullcycle.subscription.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlanIdTest {

    @Test
    public void givenValue_whenInstantiate_shouldReturnVO() {
        // given
        var expectedID = 123L;

        // when
        var actualID = new PlanId(expectedID);

        // then
        assertEquals(expectedID, actualID.value());
    }

    @Test
    public void givenEmpty_whenCallsEmpty_shouldReturnOK() {
        // given
        Long expectedID = null;

        // when
        var actualID = PlanId.empty();

        // then
        assertEquals(expectedID, actualID.value());
    }

    @Test
    public void givenNullId_whenInstantiate_shouldReturnError() {
        // given
        Long expectedID = null;
        var expectedError = "'planId' should not be null";

        // when
        var actualError = assertThrows(DomainException.class, () -> new PlanId(expectedID));

        // then
        assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    public void givenEmpty_whenCallsEquals_shouldReturnTrue() {
        // when
        var actualOne = PlanId.empty();
        var actualTwo = PlanId.empty();

        // then
        assertTrue(actualOne.equals(actualTwo));
    }
}