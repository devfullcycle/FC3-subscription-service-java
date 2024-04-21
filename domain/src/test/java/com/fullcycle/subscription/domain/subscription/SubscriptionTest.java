package com.fullcycle.subscription.domain.subscription;


import com.fullcycle.subscription.domain.Fixture;
import com.fullcycle.subscription.domain.account.AccountId;
import com.fullcycle.subscription.domain.plan.PlanId;
import com.fullcycle.subscription.domain.subscription.status.SubscriptionStatus;
import com.fullcycle.subscription.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * 1. Caminho feliz de um novo agregado
 * 2. Caminho feliz da restauração do agregado
 * 3. Caminho de validação
 */
public class SubscriptionTest {

    @Test
    public void givenValidParams_whenCallsNewSubscription_ShouldInstantiate() {
        // given
        var expectedId = new SubscriptionId("SUB123");
        var expectedVersion = 0;
        var expectedAccountId = new AccountId("ACC123");
        var expectedPlan = Fixture.Plans.plus();
        var expectedStatus = SubscriptionStatus.TRAILING;
        var expectedDueDate = LocalDate.now().plusMonths(1);
        Instant expectedLastRenewDate = null;
        String expectedLastTransactionId = null;
        var expectedEvents = 1;

        // when
        var actualSubscription =
                Subscription.newSubscription(expectedId, expectedAccountId, expectedPlan);

        // then
        Assertions.assertNotNull(actualSubscription);
        Assertions.assertEquals(expectedId, actualSubscription.id());
        Assertions.assertEquals(expectedVersion, actualSubscription.version());
        Assertions.assertEquals(expectedAccountId, actualSubscription.accountId());
        Assertions.assertEquals(expectedPlan.id(), actualSubscription.planId());
        Assertions.assertEquals(expectedDueDate, actualSubscription.dueDate());
        Assertions.assertEquals(expectedStatus, actualSubscription.status().value());
        Assertions.assertEquals(expectedLastRenewDate, actualSubscription.lastRenewDate());
        Assertions.assertEquals(expectedLastTransactionId, actualSubscription.lastTransactionId());
        Assertions.assertNotNull(actualSubscription.createdAt());
        Assertions.assertNotNull(actualSubscription.updatedAt());

        Assertions.assertEquals(expectedEvents, actualSubscription.domainEvents().size());
        Assertions.assertInstanceOf(SubscriptionCreated.class, actualSubscription.domainEvents().getFirst());
    }

    @Test
    public void givenValidParams_whenCallsWith_ShouldInstantiate() {
        // given
        var expectedId = new SubscriptionId("SUB123");
        var expectedVersion = 0;
        var expectedAccountId = new AccountId("ACC123");
        var expectedPlanId = new PlanId(123L);
        var expectedStatus = SubscriptionStatus.TRAILING;
        var expectedCreatedAt = InstantUtils.now();
        var expectedUpdatedAt = InstantUtils.now();
        var expectedDueDate = LocalDate.now().plusMonths(1);
        var expectedLastRenewDate = InstantUtils.now().minus(7, ChronoUnit.DAYS);
        var expectedLastTransactionId = UUID.randomUUID().toString();

        // when
        var actualSubscription = Subscription.with(
                expectedId,
                expectedVersion,
                expectedAccountId,
                expectedPlanId,
                expectedDueDate,
                expectedStatus,
                expectedLastRenewDate,
                expectedLastTransactionId,
                expectedCreatedAt,
                expectedUpdatedAt
        );

        // then
        Assertions.assertNotNull(actualSubscription);
        Assertions.assertEquals(expectedId, actualSubscription.id());
        Assertions.assertEquals(expectedVersion, actualSubscription.version());
        Assertions.assertEquals(expectedAccountId, actualSubscription.accountId());
        Assertions.assertEquals(expectedPlanId, actualSubscription.planId());
        Assertions.assertEquals(expectedDueDate, actualSubscription.dueDate());
        Assertions.assertEquals(expectedStatus, actualSubscription.status().value());
        Assertions.assertEquals(expectedLastRenewDate, actualSubscription.lastRenewDate());
        Assertions.assertEquals(expectedLastTransactionId, actualSubscription.lastTransactionId());
        Assertions.assertEquals(expectedCreatedAt, actualSubscription.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualSubscription.updatedAt());

        Assertions.assertTrue(actualSubscription.domainEvents().isEmpty());
    }

    @Test
    public void givenTrialingSubscription_whenExecuteIncompleteCommand_ShouldTransitToIncompleteState() {
        // given
        var expectedId = new SubscriptionId("SUB123");
        var expectedVersion = 0;
        var expectedAccountId = new AccountId("ACC123");
        var expectedPlanId = new PlanId(123L);
        var expectedStatus = SubscriptionStatus.INCOMPLETE;
        var expectedCreatedAt = InstantUtils.now();
        var expectedUpdatedAt = InstantUtils.now();
        var expectedDueDate = LocalDate.now();
        Instant expectedLastRenewDate = null;
        var expectedLastTransactionId = UUID.randomUUID().toString();
        var expectedReason = "Fail to charge creditcard";
        var expectedEvents = 1;

        var actualSubscription = Subscription.with(
                expectedId,
                expectedVersion,
                expectedAccountId,
                expectedPlanId,
                expectedDueDate,
                SubscriptionStatus.TRAILING,
                expectedLastRenewDate,
                null,
                expectedCreatedAt,
                expectedUpdatedAt
        );

        // when
        actualSubscription.execute(new SubscriptionCommand.IncompleteSubscription(expectedReason, expectedLastTransactionId));

        // then
        Assertions.assertNotNull(actualSubscription);
        Assertions.assertEquals(expectedId, actualSubscription.id());
        Assertions.assertEquals(expectedVersion, actualSubscription.version());
        Assertions.assertEquals(expectedAccountId, actualSubscription.accountId());
        Assertions.assertEquals(expectedPlanId, actualSubscription.planId());
        Assertions.assertEquals(expectedDueDate, actualSubscription.dueDate());
        Assertions.assertEquals(expectedStatus, actualSubscription.status().value());
        Assertions.assertEquals(expectedLastRenewDate, actualSubscription.lastRenewDate());
        Assertions.assertEquals(expectedLastTransactionId, actualSubscription.lastTransactionId());
        Assertions.assertEquals(expectedCreatedAt, actualSubscription.createdAt());
        Assertions.assertTrue(actualSubscription.updatedAt().isAfter(expectedUpdatedAt));

        Assertions.assertEquals(expectedEvents, actualSubscription.domainEvents().size());
        Assertions.assertInstanceOf(SubscriptionIncomplete.class, actualSubscription.domainEvents().getFirst());
    }

    @Test
    public void givenTrialingSubscription_whenExecuteRenewCommand_ShouldTransitToActiveState() {
        // given
        var expectedPlan = Fixture.Plans.plus();

        var expectedId = new SubscriptionId("SUB123");
        var expectedVersion = 0;
        var expectedAccountId = new AccountId("ACC123");
        var expectedPlanId = expectedPlan.id();
        var expectedStatus = SubscriptionStatus.ACTIVE;
        var expectedCreatedAt = InstantUtils.now();
        var expectedUpdatedAt = InstantUtils.now();
        var expectedDueDate = LocalDate.now().plusMonths(1);
        var expectedLastTransactionId = UUID.randomUUID().toString();
        var expectedEvents = 1;

        var actualSubscription = Subscription.with(
                expectedId,
                expectedVersion,
                expectedAccountId,
                expectedPlanId,
                LocalDate.now(),
                SubscriptionStatus.TRAILING,
                null,
                null,
                expectedCreatedAt,
                expectedUpdatedAt
        );

        // when
        actualSubscription.execute(new SubscriptionCommand.RenewSubscription(expectedPlan, expectedLastTransactionId));

        // then
        Assertions.assertNotNull(actualSubscription);
        Assertions.assertEquals(expectedId, actualSubscription.id());
        Assertions.assertEquals(expectedVersion, actualSubscription.version());
        Assertions.assertEquals(expectedAccountId, actualSubscription.accountId());
        Assertions.assertEquals(expectedPlanId, actualSubscription.planId());
        Assertions.assertEquals(expectedDueDate, actualSubscription.dueDate());
        Assertions.assertEquals(expectedStatus, actualSubscription.status().value());
        Assertions.assertNotNull(actualSubscription.lastRenewDate());
        Assertions.assertEquals(expectedLastTransactionId, actualSubscription.lastTransactionId());
        Assertions.assertEquals(expectedCreatedAt, actualSubscription.createdAt());
        Assertions.assertTrue(actualSubscription.updatedAt().isAfter(expectedUpdatedAt));

        Assertions.assertEquals(expectedEvents, actualSubscription.domainEvents().size());
        Assertions.assertInstanceOf(SubscriptionRenewed.class, actualSubscription.domainEvents().getFirst());
    }

    @Test
    public void givenTrialingSubscription_whenExecuteCancelCommand_ShouldTransitToCanceledState() throws InterruptedException {
        // given
        var expectedId = new SubscriptionId("SUB123");
        var expectedVersion = 0;
        var expectedAccountId = new AccountId("ACC123");
        var expectedPlanId = new PlanId(123L);
        var expectedStatus = SubscriptionStatus.CANCELED;
        var expectedCreatedAt = InstantUtils.now();
        var expectedUpdatedAt = InstantUtils.now();
        var expectedDueDate = LocalDate.now().plusMonths(1);
        var expectedLastRenewDate = InstantUtils.now();
        var expectedLastTransactionId = UUID.randomUUID().toString();
        var expectedEvents = 1;

        var actualSubscription = Subscription.with(
                expectedId,
                expectedVersion,
                expectedAccountId,
                expectedPlanId,
                expectedDueDate,
                SubscriptionStatus.TRAILING,
                expectedLastRenewDate,
                expectedLastTransactionId,
                expectedCreatedAt,
                expectedUpdatedAt
        );

        // when
        Thread.sleep(1);

        actualSubscription.execute(new SubscriptionCommand.CancelSubscription());

        // then
        Assertions.assertNotNull(actualSubscription);
        Assertions.assertEquals(expectedId, actualSubscription.id());
        Assertions.assertEquals(expectedVersion, actualSubscription.version());
        Assertions.assertEquals(expectedAccountId, actualSubscription.accountId());
        Assertions.assertEquals(expectedPlanId, actualSubscription.planId());
        Assertions.assertEquals(expectedDueDate, actualSubscription.dueDate());
        Assertions.assertEquals(expectedStatus, actualSubscription.status().value());
        Assertions.assertNotNull(actualSubscription.lastRenewDate());
        Assertions.assertEquals(expectedLastTransactionId, actualSubscription.lastTransactionId());
        Assertions.assertEquals(expectedCreatedAt, actualSubscription.createdAt());
        Assertions.assertTrue(actualSubscription.updatedAt().isAfter(expectedUpdatedAt));

        Assertions.assertEquals(expectedEvents, actualSubscription.domainEvents().size());
        Assertions.assertInstanceOf(SubscriptionCanceled.class, actualSubscription.domainEvents().getFirst());
    }
}