package com.fullcycle.subscription.application.plan.impl;

import com.fullcycle.subscription.application.plan.ChangePlan;
import com.fullcycle.subscription.domain.Fixture;
import com.fullcycle.subscription.domain.UnitTest;
import com.fullcycle.subscription.domain.money.Money;
import com.fullcycle.subscription.domain.plan.Plan;
import com.fullcycle.subscription.domain.plan.PlanGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DefaultChangePlanTest extends UnitTest {

    @Mock
    private PlanGateway planGateway;

    @InjectMocks
    private DefaultChangePlan target;

    @Captor
    private ArgumentCaptor<Plan> captor;

    @Test
    public void givenValidInput_whenCallsExecute_shouldChangePlan() {
        // given
        var plan = Fixture.Plans.plus();
        var expectedName = "Plan Master";
        var expectedDescription = "Master plan";
        var expectedPrice = 660d;
        var expectedCurrency = "USD";
        var expectedActive = true;
        var expectedPlanId = plan.id();

        when(planGateway.planOfId(any())).thenReturn(Optional.of(plan));
        when(planGateway.save(any())).thenAnswer(returnsFirstArg());

        // when
        final var actualOutput =
                this.target.execute(new ChangePlanTestInput(expectedPlanId.value(), expectedName, expectedDescription, expectedPrice, expectedCurrency, expectedActive));

        // then
        Assertions.assertEquals(expectedPlanId, actualOutput.planId());

        verify(planGateway, times(1)).save(captor.capture());

        var actualPlan = captor.getValue();
        Assertions.assertEquals(expectedPlanId, actualPlan.id());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(new Money(expectedCurrency, expectedPrice), actualPlan.price());
        Assertions.assertEquals(expectedActive, actualPlan.active());
    }

    record ChangePlanTestInput(
            Long planId,
            String name,
            String description,
            Double price,
            String currency,
            Boolean active
    ) implements ChangePlan.Input {

    }
}