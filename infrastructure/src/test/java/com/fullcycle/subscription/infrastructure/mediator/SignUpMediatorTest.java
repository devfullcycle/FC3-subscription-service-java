package com.fullcycle.subscription.infrastructure.mediator;

import com.fullcycle.subscription.application.account.CreateAccount;
import com.fullcycle.subscription.application.account.CreateIdpUser;
import com.fullcycle.subscription.domain.UnitTest;
import com.fullcycle.subscription.domain.account.AccountGateway;
import com.fullcycle.subscription.domain.account.AccountId;
import com.fullcycle.subscription.domain.account.idp.UserId;
import com.fullcycle.subscription.domain.person.Document;
import com.fullcycle.subscription.infrastructure.rest.models.req.SignUpRequest;
import com.fullcycle.subscription.infrastructure.rest.models.res.SignUpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SignUpMediatorTest extends UnitTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private CreateAccount createAccount;

    @Mock
    private CreateIdpUser createIdpUser;

    @InjectMocks
    private SignUpMediator signUpMediator;

    @Captor
    private ArgumentCaptor<CreateAccount.Input> createAccountInputCaptor;

    @Test
    void givenValidRequest_whenSignUpSuccessfully_shouldReturnAccountId() {
        // given
        var expectedFirstname = "John";
        var expectedLastname = "Doe";
        var expectedEmail = "john@gmail.com";
        var expectedDocumentType = Document.Cpf.TYPE;
        var expectedDocumentNumber = "12312312323";
        var expectedPassword = "123";
        var expectedUserId = new UserId("123");
        var expectedAccountId = new AccountId("ACC-123");

        var req = new SignUpRequest(expectedDocumentNumber, expectedDocumentType, expectedPassword, expectedEmail, expectedLastname, expectedFirstname);

        when(accountGateway.nextId()).thenReturn(expectedAccountId);
        when(createIdpUser.execute(any())).thenReturn(() -> expectedUserId);
        when(createAccount.execute(any(), any())).thenReturn(new SignUpResponse(expectedAccountId.value()));

        // when
        var actualOutput = this.signUpMediator.signUp(req);

        // then
        Assertions.assertEquals(expectedAccountId.value(), actualOutput.accountId());

        verify(createAccount, times(1)).execute(createAccountInputCaptor.capture(), any());

        var actualInput = createAccountInputCaptor.getValue();
        Assertions.assertEquals(expectedUserId.value(), actualInput.userId());
    }
}