package com.fullcycle.codeflix.subscription.application.plan.impl;

import com.fullcycle.codeflix.subscription.application.plan.CreatePlan;
import com.fullcycle.codeflix.subscription.domain.plan.Plan;
import com.fullcycle.codeflix.subscription.domain.plan.PlanGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultCreatePlanTest {

    @InjectMocks
    private DefaultCreatePlan useCase;

    @Mock
    private PlanGateway planGateway;

    @Captor
    private ArgumentCaptor<Plan> planCaptor;

    @Test
    public void test() {
        // given
        var expectedId = "PLAN-123";
        var expectedName = "Master";
        var expectedDescription = "Super description";
        var expectedPrice = 100d;
        var expectedCurrency = "USD";
        var expectedGroupId = "GROUP-129321IJ";
        var expectedActive = true;

        when(planGateway.save(any())).then(returnsFirstArg());

        // when
        var actualOutput = this.useCase.execute(new CreatePlanInput(
                expectedName,
                expectedDescription,
                expectedPrice,
                expectedCurrency,
                expectedGroupId,
                expectedActive
        ));

        // then
        Assertions.assertEquals(expectedId, actualOutput.planId());

        verify(planGateway, times(1)).save(planCaptor.capture());

        var actualPlan = planCaptor.getValue();
        Assertions.assertEquals(expectedId, actualPlan.id().value());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(expectedCurrency, actualPlan.price().currency().getCurrencyCode());
        Assertions.assertEquals(expectedPrice, actualPlan.price().amount());
        Assertions.assertEquals(expectedGroupId, actualPlan.groupId());
        Assertions.assertEquals(expectedActive, actualPlan.active());
    }

    record CreatePlanInput(
            String name,
            String description,
            Double price,
            String currency,
            String groupId,
            Boolean active
    ) implements CreatePlan.Input {

    }
}
