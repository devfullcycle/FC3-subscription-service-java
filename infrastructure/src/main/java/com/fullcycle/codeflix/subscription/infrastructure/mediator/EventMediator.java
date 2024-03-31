package com.fullcycle.codeflix.subscription.infrastructure.mediator;

import com.fullcycle.codeflix.subscription.domain.DomainEvent;
import com.fullcycle.codeflix.subscription.infrastructure.gateways.repositories.EventRepository;
import com.fullcycle.codeflix.subscription.infrastructure.observer.Publisher;
import org.springframework.stereotype.Component;

@Component
public class EventMediator {

    private final EventRepository eventRepository;
    private final Publisher<DomainEvent> domainEventPublisher;

    public EventMediator(
            final EventRepository eventRepository,
            final Publisher<DomainEvent> domainEventPublisher
    ) {
        this.eventRepository = eventRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    public void execute(final String eventId) {
        this.eventRepository.eventOfId(eventId).ifPresent(event -> {
            this.domainEventPublisher.publish(event);
            this.eventRepository.saveAsProcessed(eventId);
        });
    }
}
