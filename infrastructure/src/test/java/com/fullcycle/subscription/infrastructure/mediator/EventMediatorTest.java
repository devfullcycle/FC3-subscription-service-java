package com.fullcycle.subscription.infrastructure.mediator;

import com.fullcycle.subscription.domain.Fixture;
import com.fullcycle.subscription.domain.UnitTest;
import com.fullcycle.subscription.domain.account.AccountCreated;
import com.fullcycle.subscription.infrastructure.gateway.repository.EventJdbcRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.Mockito.*;

class EventMediatorTest extends UnitTest {

    @Mock
    private EventJdbcRepository eventRepository;

    @InjectMocks
    private EventMediator eventMediator;

    @Test
    public void givenEvent_whenCallsMediate_shouldFinishOk() {
        // given
        var expectedEventId = 123L;

        when(eventRepository.eventOfId(expectedEventId))
                .thenReturn(Optional.of(new AccountCreated(Fixture.Accounts.john())));

        doNothing().when(eventRepository).markAsProcessed(expectedEventId);

        // when
        eventMediator.mediate(expectedEventId);

        // then
        verify(eventRepository, times(1)).markAsProcessed(expectedEventId);
    }
}