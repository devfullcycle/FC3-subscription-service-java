package com.fullcycle.subscription.infrastructure.gateway.repository;

import com.fullcycle.subscription.AbstractRepositoryTest;
import com.fullcycle.subscription.domain.money.Money;
import com.fullcycle.subscription.domain.plan.Plan;
import com.fullcycle.subscription.domain.plan.PlanId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class PlanJdbcRepositoryTest extends AbstractRepositoryTest {

    @Test
    public void testAssertDependencies() {
        assertNotNull(planRepository());
    }

    @Test
    @Sql({"classpath:/sql/plans/seed-plan-master.sql"})
    public void givenPersistedPlan_whenQueriesSuccessfully_shouldReturnIt() {
        // given
        Assertions.assertEquals(1, countPlans());

        var expectedId = new PlanId(1L);
        var expectedVersion = 1;
        var expectedName = "Master";
        var expectedDescription = "O plano mais custo benefício";
        var expectedActive = false;
        var expectedPrice = new Money("BRL", 20.0);
        var expectedCreatedAt = Instant.parse("2024-04-28T10:57:11.111Z");
        var expectedUpdatedAt = Instant.parse("2024-04-28T10:58:11.111Z");
        var expectedDeletedAt = Instant.parse("2024-04-28T10:59:11.111Z");

        // when
        var actualPlan = this.planRepository().planOfId(expectedId).get();

        // then
        assertEquals(expectedId, actualPlan.id());
        assertEquals(expectedVersion, actualPlan.version());
        assertEquals(expectedName, actualPlan.name());
        assertEquals(expectedDescription, actualPlan.description());
        assertEquals(expectedActive, actualPlan.active());
        assertEquals(expectedPrice, actualPlan.price());
        assertEquals(expectedCreatedAt, actualPlan.createdAt());
        assertEquals(expectedUpdatedAt, actualPlan.updatedAt());
        assertEquals(expectedDeletedAt, actualPlan.deletedAt());
    }

    @Test
    public void givenEmptyTable_whenInsertSuccessfully_shouldBePersisted() {
        // given
        Assertions.assertEquals(0, countPlans());

        var expectedId = new PlanId(1L);
        var expectedVersion = 1;
        var expectedName = "Master";
        var expectedDescription = "O plano mais custo benefício";
        var expectedActive = false;
        var expectedPrice = new Money("BRL", 20.0);
        var expectedCreatedAt = Instant.parse("2024-04-28T10:57:11.111Z");
        var expectedUpdatedAt = Instant.parse("2024-04-28T10:58:11.111Z");
        var expectedDeletedAt = Instant.parse("2024-04-28T10:59:11.111Z");

        var plan = Plan.with(expectedId, 0, expectedName, expectedDescription, expectedActive, expectedPrice, expectedCreatedAt, expectedUpdatedAt, expectedDeletedAt);

        // when
        var responsePlan = this.planRepository().save(plan);

        assertEquals(expectedId, responsePlan.id());
        assertEquals(0, responsePlan.version());
        assertEquals(expectedName, responsePlan.name());
        assertEquals(expectedDescription, responsePlan.description());
        assertEquals(expectedActive, responsePlan.active());
        assertEquals(expectedPrice, responsePlan.price());
        assertEquals(expectedCreatedAt, responsePlan.createdAt());
        assertEquals(expectedUpdatedAt, responsePlan.updatedAt());
        assertEquals(expectedDeletedAt, responsePlan.deletedAt());

        // then
        var actualPlan = this.planRepository().planOfId(expectedId).get();

        assertEquals(expectedId, actualPlan.id());
        assertEquals(expectedVersion, actualPlan.version());
        assertEquals(expectedName, actualPlan.name());
        assertEquals(expectedDescription, actualPlan.description());
        assertEquals(expectedActive, actualPlan.active());
        assertEquals(expectedPrice, actualPlan.price());
        assertEquals(expectedCreatedAt, actualPlan.createdAt());
        assertEquals(expectedUpdatedAt, actualPlan.updatedAt());
        assertEquals(expectedDeletedAt, actualPlan.deletedAt());
    }

    @Test
    @Sql({"classpath:/sql/plans/seed-plan-master.sql"})
    public void givenPersistedPlan_whenUpdateSuccessfully_shouldBePersisted() {
        // given
        Assertions.assertEquals(1, countPlans());

        var expectedId = new PlanId(1L);

        var persistedPlan = this.planRepository().planOfId(expectedId).get();
        assertEquals(1, persistedPlan.version());
        assertEquals("Master", persistedPlan.name());
        assertEquals("O plano mais custo benefício", persistedPlan.description());
        assertFalse(persistedPlan.active());
        assertEquals(new Money("BRL", 20.0), persistedPlan.price());
        assertEquals(Instant.parse("2024-04-28T10:57:11.111Z"), persistedPlan.createdAt());
        assertEquals(Instant.parse("2024-04-28T10:58:11.111Z"), persistedPlan.updatedAt());
        assertEquals(Instant.parse("2024-04-28T10:59:11.111Z"), persistedPlan.deletedAt());

        var expectedVersion = 2;
        var expectedName = "Plus";
        var expectedDescription = "O plano PLUS";
        var expectedActive = true;
        var expectedPrice = new Money("USD", 5.0);
        var expectedCreatedAt = Instant.parse("2024-04-27T10:57:11.111Z");
        var expectedUpdatedAt = Instant.parse("2024-04-27T10:58:11.111Z");
        var expectedDeletedAt = Instant.parse("2024-04-27T10:59:11.111Z");

        var plan = Plan.with(expectedId, 1, expectedName, expectedDescription, expectedActive, expectedPrice, expectedCreatedAt, expectedUpdatedAt, expectedDeletedAt);

        // when
        var responsePlan = this.planRepository().save(plan);

        assertEquals(expectedId, responsePlan.id());
        assertEquals(1, responsePlan.version());
        assertEquals(expectedName, responsePlan.name());
        assertEquals(expectedDescription, responsePlan.description());
        assertEquals(expectedActive, responsePlan.active());
        assertEquals(expectedPrice, responsePlan.price());
        assertEquals(expectedCreatedAt, responsePlan.createdAt());
        assertEquals(expectedUpdatedAt, responsePlan.updatedAt());
        assertEquals(expectedDeletedAt, responsePlan.deletedAt());

        // then
        var actualPlan = this.planRepository().planOfId(expectedId).get();

        assertEquals(expectedId, actualPlan.id());
        assertEquals(expectedVersion, actualPlan.version());
        assertEquals(expectedName, actualPlan.name());
        assertEquals(expectedDescription, actualPlan.description());
        assertEquals(expectedActive, actualPlan.active());
        assertEquals(expectedPrice, actualPlan.price());
        assertEquals(expectedCreatedAt, actualPlan.createdAt());
        assertEquals(expectedUpdatedAt, actualPlan.updatedAt());
        assertEquals(expectedDeletedAt, actualPlan.deletedAt());
    }
}