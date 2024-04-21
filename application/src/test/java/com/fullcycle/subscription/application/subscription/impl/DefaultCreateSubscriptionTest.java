package com.fullcycle.subscription.application.subscription.impl;

import com.fullcycle.subscription.application.subscription.CreateSubscription;
import com.fullcycle.subscription.domain.Fixture;
import com.fullcycle.subscription.domain.UnitTest;
import com.fullcycle.subscription.domain.account.AccountGateway;
import com.fullcycle.subscription.domain.plan.PlanGateway;
import com.fullcycle.subscription.domain.subscription.Subscription;
import com.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import com.fullcycle.subscription.domain.subscription.SubscriptionId;
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

class DefaultCreateSubscriptionTest extends UnitTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private PlanGateway planGateway;

    @Mock
    private SubscriptionGateway subscriptionGateway;

    @InjectMocks
    private DefaultCreateSubscription target;

    @Captor
    private ArgumentCaptor<Subscription> captor;

    @Test
    public void givenValidAccountAndPlan_whenCallsCreateSubscription_shouldReturnNewSubscription() {
        // given
        var expectedPlan = Fixture.Plans.plus();
        var expectedAccount = Fixture.Accounts.john();
        var expectedSubscriptionId = new SubscriptionId("SUB123");

        when(this.subscriptionGateway.latestSubscriptionOfAccount(any())).thenReturn(Optional.empty());
        when(this.planGateway.planOfId(any())).thenReturn(Optional.of(expectedPlan));
        when(this.accountGateway.accountOfId(any())).thenReturn(Optional.of(expectedAccount));
        when(this.subscriptionGateway.nextId()).thenReturn(expectedSubscriptionId);
        when(this.subscriptionGateway.save(any())).thenAnswer(returnsFirstArg());

        // when
        final var actualOutput =
                this.target.execute(new CreateSubscriptionTestInput(expectedPlan.id().value(), expectedAccount.id().value()));

        // then
        Assertions.assertEquals(expectedSubscriptionId, actualOutput.subscriptionId());

        verify(subscriptionGateway, times(1)).save(captor.capture());

        final var actualSubscription = captor.getValue();
        Assertions.assertEquals(expectedSubscriptionId, actualSubscription.id());
        Assertions.assertEquals(expectedPlan.id(), actualSubscription.planId());
        Assertions.assertEquals(expectedAccount.id(), actualSubscription.accountId());
        Assertions.assertTrue(actualSubscription.isTrail());
        Assertions.assertNotNull(actualSubscription.dueDate());
    }

    record CreateSubscriptionTestInput(Long planId, String accountId) implements CreateSubscription.Input {

    }
}