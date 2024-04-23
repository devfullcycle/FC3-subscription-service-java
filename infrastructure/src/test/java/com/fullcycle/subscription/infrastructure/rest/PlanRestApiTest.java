package com.fullcycle.subscription.infrastructure.rest;

import com.fullcycle.subscription.ControllerTest;
import com.fullcycle.subscription.application.Presenter;
import com.fullcycle.subscription.application.plan.ChangePlan;
import com.fullcycle.subscription.application.plan.CreatePlan;
import com.fullcycle.subscription.domain.plan.PlanId;
import com.fullcycle.subscription.infrastructure.rest.controllers.PlanRestController;
import com.fullcycle.subscription.infrastructure.rest.models.res.CreatePlanResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.fullcycle.subscription.ApiTest.admin;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = PlanRestController.class)
public class PlanRestApiTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreatePlan createPlan;

    @MockBean
    private ChangePlan changePlan;

    @Captor
    private ArgumentCaptor<CreatePlan.Input> createPlanInputCaptor;

    @Test
    public void givenValidInput_whenCreateSuccessfully_shouldReturnPlanId() throws Exception {
        // given
        var expectedName = "Plus";
        var expectedDescription = "O melhor plano";
        var expectedPrice = 20D;
        var expectedCurrency = "BRL";
        var expectedActive = true;
        var expectedPlanId = new PlanId(123L);

        when(createPlan.execute(any(), any())).thenAnswer(call -> {
            Presenter<CreatePlan.Output, CreatePlanResponse> p = call.getArgument(1);
            return p.apply(new CreatePlanTestOutput(expectedPlanId));
        });

        var json = """
                {
                    "name": "%s",
                    "description": "%s",
                    "price": %s,
                    "currency": "%s",
                    "active": %s
                }
                """.formatted(expectedName, expectedDescription, expectedPrice, expectedCurrency, expectedActive);

        // when
        var aRequest = post("/plans")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json)
                .with(admin());

        var aResponse = this.mvc.perform(aRequest);

        // then
        aResponse
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/plans/" + expectedPlanId.value()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.plan_id").value(equalTo(expectedPlanId.value()), Long.class));

        verify(createPlan, times(1)).execute(createPlanInputCaptor.capture(), any());

        var actualRequest = createPlanInputCaptor.getValue();

        Assertions.assertEquals(expectedName, actualRequest.name());
        Assertions.assertEquals(expectedDescription, actualRequest.description());
        Assertions.assertEquals(expectedPrice, actualRequest.price());
        Assertions.assertEquals(expectedCurrency, actualRequest.currency());
        Assertions.assertEquals(expectedActive, actualRequest.active());
    }

    @Test
    public void givenEmptyFirstname_shouldReturnError() throws Exception {
        // given
        var expectedErrorProperty = "name";
        var expectedErrorMessage = "must not be blank";
        var expectedName = " ";
        var expectedDescription = "O melhor plano";
        var expectedPrice = 20D;
        var expectedCurrency = "BRL";
        var expectedActive = true;

        var json = """
                {
                    "name": "%s",
                    "description": "%s",
                    "price": %s,
                    "currency": "%s",
                    "active": %s
                }
                """.formatted(expectedName, expectedDescription, expectedPrice, expectedCurrency, expectedActive);

        // when
        var aRequest = post("/plans")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json)
                .with(admin());

        var aResponse = this.mvc.perform(aRequest);

        // then
        aResponse
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].property", equalTo(expectedErrorProperty)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createPlan, times(0)).execute(any(), any());
    }

    record CreatePlanTestOutput(PlanId planId) implements CreatePlan.Output {}
}
