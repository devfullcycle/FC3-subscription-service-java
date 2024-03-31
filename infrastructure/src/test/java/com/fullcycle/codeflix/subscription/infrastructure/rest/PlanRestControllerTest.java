package com.fullcycle.codeflix.subscription.infrastructure.rest;

import com.fullcycle.codeflix.subscription.ControllerTest;
import com.fullcycle.codeflix.subscription.application.plan.CreatePlan;
import com.fullcycle.codeflix.subscription.domain.utils.IdUtils;
import com.fullcycle.codeflix.subscription.infrastructure.rest.controllers.PlanRestController;
import com.fullcycle.codeflix.subscription.infrastructure.rest.models.res.CreatePlanResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.fullcycle.codeflix.subscription.ApiTest.user;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = PlanRestController.class)
public class PlanRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreatePlan createPlan;

    @Test
    void whenPayloadIsValidThenShouldCallCreatePlan() throws Exception {
        // given
        final var expectedId = IdUtils.uniqueId();
        final var expectedName = "Free";
        final var expectedDescription = "Most used";
        final var expectedPrice = 20d;
        final var expectedCurrency = "USD";
        final var expectedGroupId = UUID.randomUUID().toString();
        final var expectedActive = true;

        final var json = """
                {
                    "name": "%s",
                    "description": "%s",
                    "price": %s,
                    "currency": "%s",
                    "group_id": "%s",
                    "active": %s
                }
                """.formatted(expectedName, expectedDescription, expectedPrice, expectedCurrency, expectedGroupId, expectedActive);

        when(createPlan.execute(any(), any()))
                .thenReturn(new CreatePlanResponse(expectedId));

        // when
        final var aRequest = post("/plans")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(user());

        this.mvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/plans/" + expectedId))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.plan_id", equalTo(expectedId)));

        // then
        final var cmdCaptor = ArgumentCaptor.forClass(CreatePlan.Input.class);

        verify(createPlan).execute(cmdCaptor.capture(), any());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(expectedName, actualCmd.name());
        Assertions.assertEquals(expectedDescription, actualCmd.description());
        Assertions.assertEquals(expectedPrice, actualCmd.price());
        Assertions.assertEquals(expectedCurrency, actualCmd.currency());
        Assertions.assertEquals(expectedGroupId, actualCmd.groupId());
        Assertions.assertEquals(expectedActive, actualCmd.active());
    }
}
