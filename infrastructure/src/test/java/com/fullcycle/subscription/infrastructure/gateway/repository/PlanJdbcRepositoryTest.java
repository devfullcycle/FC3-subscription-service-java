package com.fullcycle.subscription.infrastructure.gateway.repository;

import com.fullcycle.subscription.AbstractRepositoryTest;
import com.fullcycle.subscription.domain.money.Money;
import com.fullcycle.subscription.domain.plan.PlanId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        var actualAccount = this.planRepository().planOfId(expectedId).get();

        // then
        assertEquals(expectedId, actualAccount.id());
        assertEquals(expectedVersion, actualAccount.version());
        assertEquals(expectedName, actualAccount.name());
        assertEquals(expectedDescription, actualAccount.description());
        assertEquals(expectedActive, actualAccount.active());
        assertEquals(expectedPrice, actualAccount.price());
        assertEquals(expectedCreatedAt, actualAccount.createdAt());
        assertEquals(expectedUpdatedAt, actualAccount.updatedAt());
        assertEquals(expectedDeletedAt, actualAccount.deletedAt());
    }
}