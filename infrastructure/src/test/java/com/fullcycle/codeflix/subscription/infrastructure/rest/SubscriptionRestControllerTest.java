package com.fullcycle.codeflix.subscription.infrastructure.rest;

import com.fullcycle.codeflix.subscription.ControllerTest;
import com.fullcycle.codeflix.subscription.application.subscription.CreateSubscription;
import com.fullcycle.codeflix.subscription.domain.utils.IdUtils;
import com.fullcycle.codeflix.subscription.infrastructure.rest.controllers.SubscriptionRestController;
import com.fullcycle.codeflix.subscription.infrastructure.rest.models.res.CreateSubscriptionResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.fullcycle.codeflix.subscription.ApiTest.user;
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

    @MockBean
    private CreateSubscription createSubscription;

    @Test
    void whenPayloadIsValidThenShouldCallCreateSubscription() throws Exception {
        // given
        final var expectedUserId = IdUtils.uniqueId();
        final var expectedPlanId = IdUtils.uniqueId();
        final var expectedPrice = 20d;
        final var expectedSubscription = "1231";

        final var json = """
                {
                    "user_id": "%s",
                    "plan_id": "%s",
                    "price": %s
                }
                """.formatted(expectedUserId, expectedPlanId, expectedPrice);

        when(createSubscription.execute(any(), any()))
                .thenReturn(new CreateSubscriptionResponse(expectedSubscription));

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
        final var cmdCaptor = ArgumentCaptor.forClass(CreateSubscription.Input.class);

        verify(createSubscription).execute(cmdCaptor.capture(), any());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(expectedUserId, actualCmd.userId());
        Assertions.assertEquals(expectedPlanId, actualCmd.planId());
    }

    @Test
    void whenUserIdIsNullThenShouldObserveError() throws Exception {
        // given
        final var expectedPlanId = IdUtils.uniqueId();
        final var expectedPrice = 20d;

        final var json = """
                {
                    "user_id": null,
                    "plan_id": "%s",
                    "price": %s
                }
                """.formatted(expectedPlanId, expectedPrice);

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
        final var expectedPrice = 20d;

        final var json = """
                {
                    "user_id": "%s",
                    "plan_id": null,
                    "price": %s
                }
                """.formatted(expectedUserId, expectedPrice);

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
    void whenPriceIsNullThenShouldObserveError() throws Exception {
        // given
        final var expectedUserId = IdUtils.uniqueId();
        final var expectedPlanId = IdUtils.uniqueId();

        final var json = """
                {
                    "user_id": "%s",
                    "plan_id": "%s",
                    "price": null
                }
                """.formatted(expectedUserId, expectedPlanId);

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