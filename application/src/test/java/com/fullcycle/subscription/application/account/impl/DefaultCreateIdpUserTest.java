package com.fullcycle.subscription.application.account.impl;

import com.fullcycle.subscription.application.account.CreateIdpUser;
import com.fullcycle.subscription.domain.UnitTest;
import com.fullcycle.subscription.domain.account.idp.IdentityProviderGateway;
import com.fullcycle.subscription.domain.account.idp.UserId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DefaultCreateIdpUserTest extends UnitTest {

    @Mock
    private IdentityProviderGateway identityProviderGateway;

    @InjectMocks
    private DefaultCreateIdpUser target;

    @Test
    public void givenValidInput_whenCallsExecute_shouldReturnUserID() {
        // given
        var expectedFirstname = "John";
        var expectedLastname = "Doe";
        var expectedEmail = "john@gmail.com";
        var expectedPassword = "123456";
        var expectedUserId = new UserId("123");

        when(identityProviderGateway.create(any())).thenReturn(expectedUserId);

        // when
        var actualOutput = this.target.execute(new CreateIdpUserTestInput(expectedFirstname, expectedLastname, expectedEmail, expectedPassword));

        // then
        Assertions.assertEquals(expectedUserId, actualOutput.idpUserId());
    }

    record CreateIdpUserTestInput(String firstname, String lastname, String email, String password) implements CreateIdpUser.Input {

    }
}