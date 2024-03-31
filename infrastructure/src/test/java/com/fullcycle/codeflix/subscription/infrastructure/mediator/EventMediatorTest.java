package com.fullcycle.codeflix.subscription.infrastructure.mediator;

import com.fullcycle.codeflix.subscription.domain.DomainEvent;
import com.fullcycle.codeflix.subscription.domain.account.Account;
import com.fullcycle.codeflix.subscription.domain.account.AccountCreated;
import com.fullcycle.codeflix.subscription.domain.account.AccountId;
import com.fullcycle.codeflix.subscription.domain.account.iam.UserId;
import com.fullcycle.codeflix.subscription.domain.person.Document;
import com.fullcycle.codeflix.subscription.domain.person.Email;
import com.fullcycle.codeflix.subscription.domain.person.Name;
import com.fullcycle.codeflix.subscription.infrastructure.gateways.repositories.EventRepository;
import com.fullcycle.codeflix.subscription.infrastructure.observer.Publisher;
import com.fullcycle.codeflix.subscription.infrastructure.observer.Subscriber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EventMediatorTest {

    @Test
    public void givenEventWhenCallsMediatorShouldRetrieveThenProcessThenMarkAsProcessed() {
        final var expectedEventId = "123123";

        final var john = Account.newAccount(
                new AccountId("123"),
                new UserId("456"),
                new Email("john@email.com"),
                new Name("John", "Doe"),
                Document.create("12332112322", Document.Cpf.TYPE)
        );

        final var expectedEvent = new AccountCreated(john);
        final var actualEvent = new AtomicReference<DomainEvent>();
        final var eventRepository = Mockito.mock(EventRepository.class);

        final var publisher = new Publisher<DomainEvent>();
        publisher.register(new GenericSubscriber<>(ev -> ev instanceof AccountCreated, actualEvent::set));

        Mockito.when(eventRepository.eventOfId(expectedEventId)).thenReturn(Optional.of(expectedEvent));
        Mockito.doNothing().when(eventRepository).saveAsProcessed(expectedEventId);

        final var mediator = new EventMediator(eventRepository, publisher);
        mediator.execute(expectedEventId);

        Assertions.assertEquals(expectedEvent, actualEvent.get());
        Mockito.verify(eventRepository, Mockito.times(1)).saveAsProcessed(expectedEventId);
    }

    record GenericSubscriber<T>(Predicate<T> predicate, Consumer<T> consumer) implements Subscriber<T> {

        @Override
        public boolean test(T ev) {
            return this.predicate.test(ev);
        }

        @Override
        public void onEvent(T ev) {
            this.consumer.accept(ev);
        }
    }
}
