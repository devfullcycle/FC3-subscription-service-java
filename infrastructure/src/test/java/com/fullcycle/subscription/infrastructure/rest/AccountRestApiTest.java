package com.fullcycle.subscription.infrastructure.rest;

import com.fullcycle.subscription.ControllerTest;
import com.fullcycle.subscription.domain.account.AccountId;
import com.fullcycle.subscription.domain.person.Document;
import com.fullcycle.subscription.infrastructure.mediator.SignUpMediator;
import com.fullcycle.subscription.infrastructure.rest.controllers.AccountRestController;
import com.fullcycle.subscription.infrastructure.rest.models.req.SignUpRequest;
import com.fullcycle.subscription.infrastructure.rest.models.res.SignUpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = AccountRestController.class)
public class AccountRestApiTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SignUpMediator signUpMediator;

    @Captor
    private ArgumentCaptor<SignUpRequest> signUpRequestCaptor;

    @Test
    public void givenValidInput_whenSignUpSuccessfully_shouldReturnAccountId() throws Exception {
        // given
        var expectedFirstname = "John";
        var expectedLastname = "Doe";
        var expectedEmail = "john@gmail.com";
        var expectedDocumentType = Document.Cpf.TYPE;
        var expectedDocumentNumber = "12312312323";
        var expectedPassword = "12312312323";
        var expectedAccountId = new AccountId("ACC-123");

        when(signUpMediator.signUp(any())).thenReturn(new SignUpResponse(expectedAccountId.value()));

        var json = """
                {
                    "firstname": "%s",
                    "lastname": "%s",
                    "email": "%s",
                    "document_type": "%s",
                    "document_number": "%s",
                    "password": "%s"
                }
                """.formatted(expectedFirstname, expectedLastname, expectedEmail, expectedDocumentType, expectedDocumentNumber, expectedPassword);

        // when
        var aRequest = post("/accounts/sign-up")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json);

        var aResponse = this.mvc.perform(aRequest);

        // then
        aResponse
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/accounts/" + expectedAccountId.value()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.account_id").value(equalTo(expectedAccountId.value())));

        verify(signUpMediator, times(1)).signUp(signUpRequestCaptor.capture());

        var actualRequest = signUpRequestCaptor.getValue();

        Assertions.assertEquals(expectedFirstname, actualRequest.firstname());
        Assertions.assertEquals(expectedLastname, actualRequest.lastname());
        Assertions.assertEquals(expectedEmail, actualRequest.email());
        Assertions.assertEquals(expectedDocumentType, actualRequest.documentType());
        Assertions.assertEquals(expectedDocumentNumber, actualRequest.documentNumber());
        Assertions.assertEquals(expectedPassword, actualRequest.password());
    }

    @Test
    public void givenEmptyFirstname_shouldReturnError() throws Exception {
        // given
        var expectedFirstname = " ";
        var expectedLastname = "Doe";
        var expectedEmail = "john@gmail.com";
        var expectedDocumentType = Document.Cpf.TYPE;
        var expectedDocumentNumber = "12312312323";
        var expectedPassword = "12312312323";
        var expectedErrorProperty = "firstname";
        var expectedErrorMessage = "must not be blank";

        var json = """
                {
                    "firstname": "%s",
                    "lastname": "%s",
                    "email": "%s",
                    "document_type": "%s",
                    "document_number": "%s",
                    "password": "%s"
                }
                """.formatted(expectedFirstname, expectedLastname, expectedEmail, expectedDocumentType, expectedDocumentNumber, expectedPassword);

        // when
        var aRequest = post("/accounts/sign-up")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json);

        var aResponse = this.mvc.perform(aRequest);

        // then
        aResponse
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].property", equalTo(expectedErrorProperty)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(signUpMediator, times(0)).signUp(any());
    }
}
