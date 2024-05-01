package com.fullcycle.subscription.infrastructure.rest;

import com.fullcycle.subscription.ControllerTest;
import com.fullcycle.subscription.application.Presenter;
import com.fullcycle.subscription.application.subscription.CancelSubscription;
import com.fullcycle.subscription.application.subscription.ChargeSubscription;
import com.fullcycle.subscription.application.subscription.CreateSubscription;
import com.fullcycle.subscription.domain.payment.Transaction;
import com.fullcycle.subscription.domain.subscription.SubscriptionId;
import com.fullcycle.subscription.domain.subscription.status.CanceledSubscriptionStatus;
import com.fullcycle.subscription.infrastructure.rest.controllers.SubscriptionRestController;
import com.fullcycle.subscription.infrastructure.rest.models.res.CancelSubscriptionResponse;
import com.fullcycle.subscription.infrastructure.rest.models.res.ChargeSubscriptionResponse;
import com.fullcycle.subscription.infrastructure.rest.models.res.CreateSubscriptionResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.fullcycle.subscription.ApiTest.admin;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = SubscriptionRestController.class)
public class SubscriptionRestApiTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreateSubscription createSubscription;

    @MockBean
    private CancelSubscription cancelSubscription;

    @MockBean
    private ChargeSubscription chargeSubscription;

    @Captor
    private ArgumentCaptor<CreateSubscription.Input> createSubscriptionInputCaptor;

    @Captor
    private ArgumentCaptor<CancelSubscription.Input> cancelSubscriptionInputCaptor;

    @Captor
    private ArgumentCaptor<ChargeSubscription.Input> chargeSubscriptionInputCaptor;

    @Test
    public void givenValidInput_whenCreateSuccessfully_shouldReturnSubscriptionId() throws Exception {
        // given
        var expectedAccountId = "123";
        var expectedPlanId = 123L;
        var expectedSubscriptionId = new SubscriptionId("SUBS123");

        when(createSubscription.execute(any(), any())).thenAnswer(call -> {
            Presenter<CreateSubscription.Output, CreateSubscriptionResponse> p = call.getArgument(1);
            return p.apply(new CreateSubscriptionTestOutput(expectedSubscriptionId));
        });

        var json = """
                {
                    "plan_id": %s
                }
                """.formatted(expectedPlanId);

        // when
        var aRequest = post("/subscriptions")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json)
                .with(admin(expectedAccountId));

        var aResponse = this.mvc.perform(aRequest);

        // then
        aResponse
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/subscriptions/" + expectedSubscriptionId.value()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.subscription_id").value(equalTo(expectedSubscriptionId.value())));

        verify(createSubscription, times(1)).execute(createSubscriptionInputCaptor.capture(), any());

        var actualRequest = createSubscriptionInputCaptor.getValue();

        Assertions.assertEquals(expectedPlanId, actualRequest.planId());
        Assertions.assertEquals(expectedAccountId, actualRequest.accountId());
    }

    @Test
    public void givenValidAccountId_whenCanceledSuccessfully_shouldReturnNewSubscriptionStatus() throws Exception {
        // given
        var expectedAccountId = "123";
        var expectedStatus = CanceledSubscriptionStatus.CANCELED;
        var expectedSubscriptionId = new SubscriptionId("SUBS123");

        when(cancelSubscription.execute(any(), any())).thenAnswer(call -> {
            Presenter<CancelSubscription.Output, CancelSubscriptionResponse> p = call.getArgument(1);
            return p.apply(new CancelSubscriptionTestOutput(expectedSubscriptionId, expectedStatus));
        });

        // when
        var aRequest = put("/subscriptions/active/cancel")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(admin(expectedAccountId));

        var aResponse = this.mvc.perform(aRequest);

        // then
        aResponse
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.subscription_id").value(equalTo(expectedSubscriptionId.value())))
                .andExpect(jsonPath("$.subscription_status").value(equalTo(expectedStatus)));

        verify(cancelSubscription, times(1)).execute(cancelSubscriptionInputCaptor.capture(), any());

        var actualRequest = cancelSubscriptionInputCaptor.getValue();

        Assertions.assertEquals(expectedAccountId, actualRequest.accountId());
    }

    @Test
    public void givenValidAccountId_whenChargedSuccessfully_shouldReturnNewSubscriptionStatus() throws Exception {
        // given
        var expectedAccountId = "123";
        var expectedStatus = CanceledSubscriptionStatus.INCOMPLETE;
        var expectedSubscriptionId = new SubscriptionId("SUBS123");
        var expectedDueDate = LocalDate.now();
        var expectedTransactionId = "12312";
        var expectedTransactionError = "No fund";
        var expectedPaymentType = "credit_card";
        var expectedCreditCardToken = "123222";
        var expectedZipCode = "12312123";
        var expectedNumber = "1";
        var expectedCountry = "BR";
        var expectedComplement = "A";

        when(chargeSubscription.execute(any(), any())).thenAnswer(call -> {
            Presenter<ChargeSubscription.Output, ChargeSubscriptionResponse> p = call.getArgument(1);
            return p.apply(new ChargeSubscriptionTestOutput(expectedSubscriptionId, expectedStatus, expectedDueDate, Transaction.failure(expectedTransactionId, expectedTransactionError)));
        });

        var json = """
                {
                    "payment_type": "%s",
                    "credit_card_token": "%s",
                    "billing_address": {
                      "zipcode": "%s",
                      "number": "%s",
                      "complement": "%s",
                      "country": "%s"
                    }
                }
                """.formatted(expectedPaymentType, expectedCreditCardToken, expectedZipCode, expectedNumber, expectedComplement, expectedCountry);

        // when
        var aRequest = put("/subscriptions/active/charge")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json)
                .with(admin(expectedAccountId));

        var aResponse = this.mvc.perform(aRequest);

        // then
        aResponse
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.subscription_id").value(equalTo(expectedSubscriptionId.value())))
                .andExpect(jsonPath("$.subscription_status").value(equalTo(expectedStatus)))
                .andExpect(jsonPath("$.subscription_due_date").value(equalTo(expectedDueDate.toString())))
                .andExpect(jsonPath("$.payment_transaction_id").value(equalTo(expectedTransactionId)))
                .andExpect(jsonPath("$.payment_transaction_error").value(equalTo(expectedTransactionError)));

        verify(chargeSubscription, times(1)).execute(chargeSubscriptionInputCaptor.capture(), any());

        var actualRequest = chargeSubscriptionInputCaptor.getValue();

        Assertions.assertEquals(expectedAccountId, actualRequest.accountId());
        Assertions.assertEquals(expectedCreditCardToken, actualRequest.creditCardToken());
        Assertions.assertEquals(expectedPaymentType, actualRequest.paymentType());
        Assertions.assertEquals(expectedZipCode, actualRequest.billingAddress().zipcode());
        Assertions.assertEquals(expectedNumber, actualRequest.billingAddress().number());
        Assertions.assertEquals(expectedComplement, actualRequest.billingAddress().complement());
        Assertions.assertEquals(expectedCountry, actualRequest.billingAddress().country());
    }

    record CreateSubscriptionTestOutput(SubscriptionId subscriptionId) implements CreateSubscription.Output {

    }

    record CancelSubscriptionTestOutput(SubscriptionId subscriptionId, String subscriptionStatus)
            implements CancelSubscription.Output {}


    record ChargeSubscriptionTestOutput(
            SubscriptionId subscriptionId,
            String subscriptionStatus,
            LocalDate subscriptionDueDate,
            Transaction paymentTransaction
    )
            implements ChargeSubscription.Output {}
}
