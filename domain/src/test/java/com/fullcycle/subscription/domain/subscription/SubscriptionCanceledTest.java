package com.fullcycle.subscription.domain.subscription;

import com.fullcycle.subscription.domain.Fixture;
import com.fullcycle.subscription.domain.account.AccountId;
import com.fullcycle.subscription.domain.exceptions.DomainException;
import com.fullcycle.subscription.domain.utils.InstantUtils;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionCanceledTest {

    @Test
    public void givenValidSubscription_whenInstantiate_shouldReturnEvent() {
        // given
        var expectedSubscription = Subscription.newSubscription(new SubscriptionId("SUB"), new AccountId("ACC123"), Fixture.Plans.plus());
        expectedSubscription.status().cancel();

        // when
        var actualEvent = new SubscriptionCanceled(expectedSubscription);

        // then
        assertEquals(expectedSubscription.id().value(), actualEvent.subscriptionId());
        assertEquals(expectedSubscription.accountId().value(), actualEvent.accountId());
        assertEquals(expectedSubscription.planId().value(), actualEvent.planId());
        assertEquals(expectedSubscription.dueDate(), actualEvent.dueDate());
        assertNotNull(actualEvent.occurredOn());
    }

    @Test
    public void givenNullSubscriptionId_whenInstantiate_shouldReturnError() {
        // given
        String expectedSubscriptionId = null;
        var expectedAccountId = "ACC";
        var expectedPlanId = 12L;
        var expectedDueDate = LocalDate.now();
        var expectedOccurredOn = InstantUtils.now();
        var expectedError = "'subscriptionId' should not be empty";

        // when
        var actualError = assertThrows(
                DomainException.class,
                () -> new SubscriptionCanceled(expectedSubscriptionId, expectedAccountId, expectedPlanId, expectedDueDate, expectedOccurredOn)
        );

        // then
        assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    public void givenNullAccountId_whenInstantiate_shouldReturnError() {
        // given
        var expectedSubscriptionId = "123";
        String expectedAccountId = null;
        var expectedPlanId = 12L;
        var expectedDueDate = LocalDate.now();
        var expectedOccurredOn = InstantUtils.now();
        var expectedError = "'accountId' should not be empty";

        // when
        var actualError = assertThrows(
                DomainException.class,
                () -> new SubscriptionCanceled(expectedSubscriptionId, expectedAccountId, expectedPlanId, expectedDueDate, expectedOccurredOn)
        );

        // then
        assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    public void givenNullPlanId_whenInstantiate_shouldReturnError() {
        // given
        var expectedSubscriptionId = "123";
        var expectedAccountId = "9123";
        Long expectedPlanId = null;
        var expectedDueDate = LocalDate.now();
        var expectedOccurredOn = InstantUtils.now();
        var expectedError = "'planId' should not be null";

        // when
        var actualError = assertThrows(
                DomainException.class,
                () -> new SubscriptionCanceled(expectedSubscriptionId, expectedAccountId, expectedPlanId, expectedDueDate, expectedOccurredOn)
        );

        // then
        assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    public void givenNullDueDate_whenInstantiate_shouldReturnError() {
        // given
        var expectedSubscriptionId = "123";
        var expectedAccountId = "9123";
        var expectedPlanId = 12L;
        LocalDate expectedDueDate = null;
        var expectedOccurredOn = InstantUtils.now();
        var expectedError = "'dueDate' should not be null";

        // when
        var actualError = assertThrows(
                DomainException.class,
                () -> new SubscriptionCanceled(expectedSubscriptionId, expectedAccountId, expectedPlanId, expectedDueDate, expectedOccurredOn)
        );

        // then
        assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    public void givenNullOccurredOn_whenInstantiate_shouldReturnError() {
        // given
        var expectedSubscriptionId = "123";
        var expectedAccountId = "9123";
        var expectedPlanId = 12L;
        var expectedDueDate = LocalDate.now();
        Instant expectedOccurredOn = null;
        var expectedError = "'occurredOn' should not be null";

        // when
        var actualError = assertThrows(
                DomainException.class,
                () -> new SubscriptionCanceled(expectedSubscriptionId, expectedAccountId, expectedPlanId, expectedDueDate, expectedOccurredOn)
        );

        // then
        assertEquals(expectedError, actualError.getMessage());
    }
}