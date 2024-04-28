package com.fullcycle.subscription.infrastructure.gateway.repository;

import com.fullcycle.subscription.AbstractRepositoryTest;
import com.fullcycle.subscription.domain.Fixture;
import com.fullcycle.subscription.domain.account.AccountId;
import com.fullcycle.subscription.domain.plan.PlanId;
import com.fullcycle.subscription.domain.subscription.*;
import com.fullcycle.subscription.domain.subscription.status.ActiveSubscriptionStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    @Sql({"classpath:/sql/subscriptions/seed-subscription-johndoe.sql"})
    public void givenPersistedSubscription_whenQueriesLatestSubscriptionOfAccountIdSuccessfully_shouldReturnIt() {
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
        var actualSubscription = this.subscriptionRepository().latestSubscriptionOfAccount(expectedAccountId).get();

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

    @Test
    public void givenEmptyTable_whenInsertsSuccessfully_shouldBePersisted() {
        // given
        Assertions.assertEquals(0, countSubscriptions());

        var plus = Fixture.Plans.plus();

        var expectedId = new SubscriptionId("5783bdbcbb2347eb8883e969f14d350c");
        var expectedVersion = 1;
        var expectedAccountId = new AccountId("033c7d9eb3cc4eb7840b942fa2194cab");
        var expectedPlanId = plus.id();
        var expectedStatus = ActiveSubscriptionStatus.TRAILING;
        var expectedDueDate = LocalDate.now().plusMonths(1);

        var newSubscription = Subscription.newSubscription(expectedId, expectedAccountId, plus);

        // when
        this.subscriptionRepository().save(newSubscription);

        assertEquals(expectedId, newSubscription.id());
        assertEquals(0, newSubscription.version());
        assertEquals(expectedAccountId, newSubscription.accountId());
        assertEquals(expectedPlanId, newSubscription.planId());
        assertEquals(expectedStatus, newSubscription.status().value());

        // then
        Assertions.assertEquals(1, countSubscriptions());

        var actualSubscription = this.subscriptionRepository().subscriptionOfId(expectedId).get();

        assertEquals(expectedId, actualSubscription.id());
        assertEquals(expectedVersion, actualSubscription.version());
        assertEquals(expectedAccountId, actualSubscription.accountId());
        assertEquals(expectedPlanId, actualSubscription.planId());
        assertEquals(expectedStatus, actualSubscription.status().value());
        assertNotNull(actualSubscription.createdAt());
        assertNotNull(actualSubscription.updatedAt());
        assertEquals(expectedDueDate, actualSubscription.dueDate());
        assertNull(actualSubscription.lastRenewDate());
        assertNull(actualSubscription.lastTransactionId());

        var actualEvents = this.eventRepository().allEventsOfAggregate(expectedId.value(), SubscriptionEvent.TYPE);
        assertEquals(1, actualEvents.size());

        var actualEvent = (SubscriptionCreated) actualEvents.getFirst();
        assertEquals(expectedId.value(), actualEvent.subscriptionId());
        assertEquals(expectedAccountId.value(), actualEvent.accountId());
        assertEquals(expectedPlanId.value(), actualEvent.planId());
        assertNotNull(actualEvent.occurredOn());
    }

    @Test
    @Sql({"classpath:/sql/subscriptions/seed-subscription-johndoe.sql"})
    public void givenPersistedSubscription_whenUpdateSuccessfully_shouldBePersisted() {
        // given
        Assertions.assertEquals(1, countSubscriptions());

        var expectedId = new SubscriptionId("5783bdbcbb2347eb8883e969f14d350c");
        var expectedVersion = 2;
        var expectedAccountId = new AccountId("033c7d9eb3cc4eb7840b942fa2194cab");
        var expectedPlanId = new PlanId(1L);
        var expectedStatus = ActiveSubscriptionStatus.INCOMPLETE;
        var expectedCreatedAt = Instant.parse("2024-04-28T10:58:11.111Z");
        var expectedDueDate = LocalDate.of(2024, 5, 27);
        var expectedLastTransactionId = "999f4e6a-79fa-473c-b7cb-a5b2bb4e6c8a";
        var expectedReason = "No funds";

        var persistedSubscription = this.subscriptionRepository().subscriptionOfId(expectedId).get();
        assertEquals(expectedId, persistedSubscription.id());
        assertEquals(1, persistedSubscription.version());
        assertEquals(expectedAccountId, persistedSubscription.accountId());
        assertEquals(expectedPlanId, persistedSubscription.planId());
        assertEquals(ActiveSubscriptionStatus.ACTIVE, persistedSubscription.status().value());
        assertEquals(expectedCreatedAt, persistedSubscription.createdAt());
        assertEquals(Instant.parse("2024-04-28T10:59:11.111Z"), persistedSubscription.updatedAt());
        assertEquals(expectedDueDate, persistedSubscription.dueDate());
        assertEquals(Instant.parse("2024-04-27T10:59:11.111Z"), persistedSubscription.lastRenewDate());
        assertEquals("560f4e6a-79fa-473c-b7cb-a5b2bb4e6c8a", persistedSubscription.lastTransactionId());


        persistedSubscription.execute(new SubscriptionCommand.IncompleteSubscription(expectedReason, expectedLastTransactionId));

        // when
        this.subscriptionRepository().save(persistedSubscription);

        // then
        var actualSubscription = this.subscriptionRepository().subscriptionOfId(expectedId).get();

        assertEquals(expectedId, actualSubscription.id());
        assertEquals(expectedVersion, actualSubscription.version());
        assertEquals(expectedAccountId, actualSubscription.accountId());
        assertEquals(expectedPlanId, actualSubscription.planId());
        assertEquals(expectedStatus, actualSubscription.status().value());
        assertNotNull(actualSubscription.createdAt());
        assertNotNull(actualSubscription.updatedAt());
        assertEquals(expectedDueDate, actualSubscription.dueDate());
        assertNotNull(actualSubscription.lastRenewDate());
        assertEquals(expectedLastTransactionId, actualSubscription.lastTransactionId());

        var actualEvents = this.eventRepository().allEventsOfAggregate(expectedId.value(), SubscriptionEvent.TYPE);

        var actualEvent = (SubscriptionIncomplete) actualEvents.getFirst();
        assertEquals(expectedId.value(), actualEvent.subscriptionId());
        assertEquals(expectedAccountId.value(), actualEvent.accountId());
        assertEquals(expectedPlanId.value(), actualEvent.planId());
        assertEquals(expectedReason, actualEvent.reason());
        assertEquals(expectedDueDate, actualEvent.dueDate());
        assertNotNull(actualEvent.occurredOn());
    }
}