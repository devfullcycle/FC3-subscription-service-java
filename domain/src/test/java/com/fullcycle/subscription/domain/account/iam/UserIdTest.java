package com.fullcycle.subscription.domain.account.iam;

import com.fullcycle.subscription.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserIdTest {

    @Test
    public void givenValidId_whenInstantiate_shouldReturnVO() {
        // given
        var expectedUserId = "123";

        // when
        var actualUserId = new UserId(expectedUserId);

        // then
        assertEquals(expectedUserId, actualUserId.value());
    }

    @Test
    public void givenEmptyUserId_whenInstantiate_shouldReturnError() {
        // given
        var expectedUserId = "";
        var expectedError = "'userId' should not be empty";

        // when
        var actualError = assertThrows(DomainException.class, () -> new UserId(expectedUserId));

        // then
        assertEquals(expectedError, actualError.getMessage());
    }
}