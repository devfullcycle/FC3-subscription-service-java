package com.fullcycle.subscription.infrastructure.gateway.repository;

import com.fullcycle.subscription.AbstractRepositoryTest;
import com.fullcycle.subscription.domain.account.AccountId;
import com.fullcycle.subscription.domain.plan.PlanId;
import com.fullcycle.subscription.domain.subscription.SubscriptionId;
import com.fullcycle.subscription.domain.subscription.status.ActiveSubscriptionStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SubscriptionJdbcRepositoryTest extends AbstractRepositoryTest {

    @Test
    public void testAssertDependencies() {
        assertNotNull(subscriptionRepository());
    }

    @Test
    @Sql({"classpath:/sql/subscriptions/seed-subscription-johndoe.sql"})
    public void givenPersistedSubscription_whenQueriesSuccessfully_shouldReturnIt() {
        // given
        Assertions.assertEquals(1, countSubscriptions());

        var expectedId = new SubscriptionId("5783bdbcbb2347eb8883e969f14d350c");
        var expectedVersion = 1;
        var expectedAccountId = new AccountId("033c7d9eb3cc4eb7840b942fa2194cab");
        var expectedPlanId = new PlanId(1L);
        var expectedStatus = ActiveSubscriptionStatus.ACTIVE;
        var expectedCreatedAt = Instant.parse("2024-04-28T10:58:11.111Z");
        var expectedUpdatedAt = Instant.parse("2024-04-28T10:59:11.111Z");
        var expectedDueDate = LocalDate.of(2024, 5, 27);
        var expectedLastRenewDt = Instant.parse("2024-04-27T10:59:11.111Z");
        var expectedLastTransactionId = "560f4e6a-79fa-473c-b7cb-a5b2bb4e6c8a";


        // when
        var actualSubscription = this.subscriptionRepository().subscriptionOfId(expectedId).get();

        // then
        assertEquals(expectedId, actualSubscription.id());
        assertEquals(expectedVersion, actualSubscription.version());
        assertEquals(expectedAccountId, actualSubscription.accountId());
        assertEquals(expectedPlanId, actualSubscription.planId());
        assertEquals(expectedStatus, actualSubscription.status().value());
        assertEquals(expectedCreatedAt, actualSubscription.createdAt());
        assertEquals(expectedUpdatedAt, actualSubscription.updatedAt());
        assertEquals(expectedDueDate, actualSubscription.dueDate());
        assertEquals(expectedLastRenewDt, actualSubscription.lastRenewDate());
        assertEquals(expectedLastTransactionId, actualSubscription.lastTransactionId());
    }
}