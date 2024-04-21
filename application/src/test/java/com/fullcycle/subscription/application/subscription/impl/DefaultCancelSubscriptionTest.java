package com.fullcycle.subscription.application.subscription.impl;

import com.fullcycle.subscription.application.subscription.CancelSubscription;
import com.fullcycle.subscription.domain.Fixture;
import com.fullcycle.subscription.domain.UnitTest;
import com.fullcycle.subscription.domain.account.AccountId;
import com.fullcycle.subscription.domain.plan.Plan;
import com.fullcycle.subscription.domain.subscription.Subscription;
import com.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import com.fullcycle.subscription.domain.subscription.SubscriptionId;
import com.fullcycle.subscription.domain.subscription.status.ActiveSubscriptionStatus;
import com.fullcycle.subscription.domain.subscription.status.CanceledSubscriptionStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DefaultCancelSubscriptionTest extends UnitTest {

    @Mock
    private SubscriptionGateway subscriptionGateway;

    @InjectMocks
    private DefaultCancelSubscription target;

    @Test
    public void givenActiveSubscription_whenCallsCancelSubscription_shouldCancelIt() {
        // given
        var expectedPlan = Fixture.Plans.plus();
        var expectedAccount = Fixture.Accounts.john();
        var expectedSubscription = newSubscriptionWith(expectedAccount.id(), expectedPlan, ActiveSubscriptionStatus.ACTIVE, LocalDateTime.now().minusDays(15));
        var expectedSubscriptionId = expectedSubscription.id();
        var expectedSubscriptionStatus = CanceledSubscriptionStatus.CANCELED;

        when(subscriptionGateway.subscriptionOfId(any())).thenReturn(Optional.of(expectedSubscription));
        when(subscriptionGateway.save(any())).thenAnswer(returnsFirstArg());

        // when
        var actualOutput =
                this.target.execute(new CancelSubscriptionTestInput(expectedSubscriptionId.value(), expectedAccount.id().value()));

        // then
        Assertions.assertEquals(expectedSubscriptionId, actualOutput.subscriptionId());
        Assertions.assertEquals(expectedSubscriptionStatus, actualOutput.subscriptionStatus());
    }

    private static Subscription newSubscriptionWith(AccountId expectedAccountId, Plan plus, String status, LocalDateTime date) {
        final var instant = date.toInstant(ZoneOffset.UTC);
        return Subscription.with(
                new SubscriptionId("SUB123"), 1, expectedAccountId, plus.id(),
                date.toLocalDate(), status,
                instant, "a123",
                instant, instant
        );
    }

    record CancelSubscriptionTestInput(String subscriptionId, String accountId) implements CancelSubscription.Input {

    }
}