package com.fullcycle.codeflix.subscription.infrastructure.subscription;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.codeflix.subscription.ControllerTest;
import com.fullcycle.codeflix.subscription.application.subscription.ActivateSubscription;
import com.fullcycle.codeflix.subscription.domain.plan.BillingCycle;
import com.fullcycle.codeflix.subscription.domain.utils.IdUtils;
import com.fullcycle.codeflix.subscription.infrastructure.subscription.controllers.SubscriptionRestController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.fullcycle.codeflix.subscription.infrastructure.ApiTest.user;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = SubscriptionRestController.class)
class SubscriptionRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ActivateSubscription activateSubscription;

    @Test
    void whenPayloadIsValidThenShouldCallCreateSubscription() throws Exception {
        // given
        final var expectedUserId = IdUtils.uniqueId();
        final var expectedPlanId = IdUtils.uniqueId();
        final var expectedBillingCycle = BillingCycle.MONTHLY;
        final var expectedPrice = 20d;
        final var expectedSubscription = "1231";

        final var json = """
                {
                    "user_id": "%s",
                    "plan_id": "%s",
                    "billing_cycle": "%s",
                    "price": %s
                }
                """.formatted(expectedUserId, expectedPlanId, expectedBillingCycle.name(), expectedPrice);

        when(activateSubscription.execute(any()))
                .thenReturn(new ActivateSubscription.Output(expectedSubscription));

        // when
        final var aRequest = post("/subscriptions")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(user());

        this.mvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/subscriptions/" + expectedSubscription))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.subscription_id", equalTo(expectedSubscription)));

        // then
        final var cmdCaptor = ArgumentCaptor.forClass(ActivateSubscription.Input.class);

        verify(activateSubscription).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(expectedUserId, actualCmd.userId());
        Assertions.assertEquals(expectedPlanId, actualCmd.planId());
        Assertions.assertEquals(expectedBillingCycle, actualCmd.billingCycle());
        Assertions.assertEquals(expectedPrice, actualCmd.price());
    }

    @Test
    void whenUserIdIsNullThenShouldObserveError() throws Exception {
        // given
        final var expectedPlanId = IdUtils.uniqueId();
        final var expectedBillingCycle = BillingCycle.MONTHLY;
        final var expectedPrice = 20d;

        final var json = """
                {
                    "user_id": null,
                    "plan_id": "%s",
                    "billing_cycle": "%s",
                    "price": %s
                }
                """.formatted(expectedPlanId, expectedBillingCycle.name(), expectedPrice);

        // when
        final var aRequest = post("/subscriptions")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(user());

        final var result = this.mvc.perform(aRequest);

        // then
        result
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].property", equalTo("userId")))
                .andExpect(jsonPath("$.errors[0].message", equalTo("must not be empty")));
    }

    @Test
    void whenPLanIdIsNullThenShouldObserveError() throws Exception {
        // given
        final var expectedUserId = IdUtils.uniqueId();
        final var expectedBillingCycle = BillingCycle.MONTHLY;
        final var expectedPrice = 20d;

        final var json = """
                {
                    "user_id": "%s",
                    "plan_id": null,
                    "billing_cycle": "%s",
                    "price": %s
                }
                """.formatted(expectedUserId, expectedBillingCycle.name(), expectedPrice);

        // when
        final var aRequest = post("/subscriptions")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(user());

        final var result = this.mvc.perform(aRequest);

        // then
        result
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].property", equalTo("planId")))
                .andExpect(jsonPath("$.errors[0].message", equalTo("must not be empty")));
    }

    @Test
    void whenBillingCycleIsInvalidThenShouldObserveError() throws Exception {
        // given
        final var expectedUserId = IdUtils.uniqueId();
        final var expectedPlanId = IdUtils.uniqueId();
        final var expectedBillingCycle = "invalid";
        final var expectedPrice = 20d;

        final var json = """
                {
                    "user_id": "%s",
                    "plan_id": "%s",
                    "billing_cycle": "%s",
                    "price": %s
                }
                """.formatted(expectedUserId, expectedPlanId, expectedBillingCycle, expectedPrice);

        // when
        final var aRequest = post("/subscriptions")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(user());

        final var result = this.mvc.perform(aRequest);

        // then
        result
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].property", equalTo("billingCycle")))
                .andExpect(jsonPath("$.errors[0].message", equalTo("must not be null")));
    }

    @Test
    void whenPriceIsNullThenShouldObserveError() throws Exception {
        // given
        final var expectedUserId = IdUtils.uniqueId();
        final var expectedPlanId = IdUtils.uniqueId();
        final var expectedBillingCycle = BillingCycle.MONTHLY;

        final var json = """
                {
                    "user_id": "%s",
                    "plan_id": "%s",
                    "billing_cycle": "%s",
                    "price": null
                }
                """.formatted(expectedUserId, expectedPlanId, expectedBillingCycle);

        // when
        final var aRequest = post("/subscriptions")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(user());

        final var result = this.mvc.perform(aRequest);

        // then
        result
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].property", equalTo("price")))
                .andExpect(jsonPath("$.errors[0].message", equalTo("must not be null")));
    }
}