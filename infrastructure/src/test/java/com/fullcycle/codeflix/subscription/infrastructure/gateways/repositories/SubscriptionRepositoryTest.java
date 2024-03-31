package com.fullcycle.codeflix.subscription.infrastructure.gateways.repositories;

import com.fullcycle.codeflix.subscription.AbstractRepositoryTest;
import com.fullcycle.codeflix.subscription.domain.account.*;
import com.fullcycle.codeflix.subscription.domain.account.iam.UserId;
import com.fullcycle.codeflix.subscription.domain.person.Address;
import com.fullcycle.codeflix.subscription.domain.person.Document;
import com.fullcycle.codeflix.subscription.domain.person.Email;
import com.fullcycle.codeflix.subscription.domain.person.Name;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionId;
import com.fullcycle.codeflix.subscription.domain.subscription.status.ActiveSubscriptionStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.time.LocalDate;

public class SubscriptionRepositoryTest extends AbstractRepositoryTest {

    @Test
    @Sql({"classpath:/sql/subscriptions/seed-sub-123.sql"})
    public void testGetUser() {
        // given
        Assertions.assertEquals(1, countSubscriptions(), "Deveria começar com 1 subscription na base");

        final var expectedId = new SubscriptionId("123");
        final var expectedVersion = 1;
        final var expectedAccountId = new AccountId("12io31j");
        final var expectedStatus = ActiveSubscriptionStatus.ACTIVE;
        final var expectedCreatedAt = Instant.parse("2024-03-30T17:42:01.433Z");
        final var expectedUpdatedAt = Instant.parse("2024-03-30T17:42:01.444Z");
        final var expectedDueDate = LocalDate.parse("2024-04-30");
        final var expectedLastRenewDt = Instant.parse("2024-03-30T17:42:01.111Z");
        final var expectedLastTransactionId = "inv123123";

        // when
        final var actualSubscription = this.subscriptionRepository().subscriptionOfId(expectedId).get();

        // then
        Assertions.assertEquals(expectedVersion, actualSubscription.version());
        Assertions.assertEquals(expectedAccountId, actualSubscription.accountId());
        Assertions.assertEquals(expectedStatus, actualSubscription.status().value());
        Assertions.assertEquals(expectedCreatedAt, actualSubscription.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualSubscription.updatedAt());
        Assertions.assertEquals(expectedDueDate, actualSubscription.dueDate());
        Assertions.assertEquals(expectedLastRenewDt, actualSubscription.lastRenewDate());
        Assertions.assertEquals(expectedLastTransactionId, actualSubscription.lastTransactionId());
    }
}
