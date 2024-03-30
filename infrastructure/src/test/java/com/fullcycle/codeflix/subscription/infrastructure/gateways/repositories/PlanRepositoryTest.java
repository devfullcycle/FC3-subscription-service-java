package com.fullcycle.codeflix.subscription.infrastructure.gateways.repositories;

import com.fullcycle.codeflix.subscription.AbstractRepositoryTest;
import com.fullcycle.codeflix.subscription.domain.plan.Plan;
import com.fullcycle.codeflix.subscription.domain.plan.PlanId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PlanRepositoryTest extends AbstractRepositoryTest {

    @Test
    public void testCreatePlan() {
        // given
        Assertions.assertEquals(0, countPlans());

        final var expectedId = new PlanId("123");
        final var expectedVersion = 0;
        final var expectedName = "Free";
        final var expectedDescription = "Plan free";
        final var expectedGroupId = "231K";
        final var expectedActive = true;
        final var expectedCurrency = "USD";
        final var expectedPrice = 0.0;

        final var plan = Plan.with(
                expectedId, expectedVersion, expectedName, expectedDescription, expectedGroupId,
                expectedActive, expectedCurrency, expectedPrice
        );

        // when
        this.planRepository().save(plan);

        // then
        final var actualPlans = this.planRepository().allPlans();

        Assertions.assertEquals(1, actualPlans.size());

        final var actualPlan = actualPlans.iterator().next();
        Assertions.assertEquals(expectedId, actualPlan.id());
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(expectedGroupId, actualPlan.groupId());
        Assertions.assertEquals(expectedActive, actualPlan.active());
        Assertions.assertEquals(expectedCurrency, actualPlan.price().currency().getCurrencyCode());
        Assertions.assertEquals(expectedPrice, actualPlan.price().amount());
    }
}
